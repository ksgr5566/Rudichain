package com.rudichain.rudichain.network;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;

import com.rudichain.rudichain.backend.*;
import com.rudichain.rudichain.wallet.*;
import com.rudichain.rudichain.index;

import org.web3j.crypto.Sign;

enum credentials{
    PUBLISH_KEY("pub-c-5aafc894-23fd-41a1-9acf-dbea80bbd8fa"),
    SUBSCRIBE_KEY("sub-c-087ef208-3e4d-11ec-b2c1-a25c7fcd9558"),
    SECRET_KEY("sec-c-ZmU2OGFhMDYtYmZkYS00NTU3LWE4NzAtNjE1MjdhYzc4NGZl");
    private String value;
    credentials(String value){
        this.value = value;
    }
    public String getCredentials(){
        return value;
    }
}

enum channels{
    TEST, BLOCKCHAIN, TRANSACTION, SYNCPOOL, SYNCCHAIN, INTRO, INTRO2;
}

public class pubsub{

    public blockchain chain;
    public PubNub pubnub;
    public TransactionPool pool;
    Wallet wallet;

    public pubsub(blockchain chain, TransactionPool pool, Wallet wallet){
        this.chain = chain;
        this.pool = pool;
        this.wallet = wallet;

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(credentials.SUBSCRIBE_KEY.getCredentials());
        pnConfiguration.setPublishKey(credentials.PUBLISH_KEY.getCredentials());
        pnConfiguration.setUuid(credentials.SECRET_KEY.getCredentials());

        this.pubnub = new PubNub(pnConfiguration);

        this.pubnub.subscribe().channels(Arrays.asList(channels.TEST.name(),
                                                       channels.BLOCKCHAIN.name(),
                                                       channels.TRANSACTION.name(),
                                                       channels.SYNCPOOL.name(),
                                                       channels.SYNCCHAIN.name(),
                                                       channels.INTRO.name(),
                                                       channels.INTRO2.name())).execute();

        this.pubnub.addListener(new SubscribeCallback() {
            public void message(PubNub pubnub, PNMessageResult message){
                JsonElement receivedMessageObject = message.getMessage();
                System.out.println("Message received from: " + message.getChannel()
                                    + "  Message: " + receivedMessageObject);

                String messageOb = receivedMessageObject.toString();
                String temp = messageOb.replaceAll("\\"+"\\","");
                String withoutLastCharacter = temp.substring(0, temp.length() - 1);
                String messageObj = withoutLastCharacter.substring(1);


                if(message.getChannel().equals("BLOCKCHAIN")){

                    blockchain toReplace = handleJsonChain(messageObj);
                    if(chain.replaceChain(toReplace)){
                        pool.clearBlockchainTransactions(toReplace);
                    }
                }

                if(message.getChannel().equals("TRANSACTION")){

                    Transaction transaction = handleJsonTransaction(messageObj);

                    // if(pool.exists(wallet.publicKey)==null){
                    //     pool.setTransaction(transaction);
                    // }

                    pool.setTransaction(transaction);

                }

                if(message.getChannel().equals("SYNCPOOL")){
                    if(!index.synced){

                        JsonObject jsonElement = new Gson().fromJson(messageObj, JsonObject.class);
                        String poolObj = jsonElement.get("transactionMap").toString();
                        
                        JsonElement poolele = JsonParser.parseString(poolObj);
                        JsonObject poolJson = poolele.getAsJsonObject();

                        Map<String, Object> mapObj = new Gson().fromJson(poolObj, new TypeToken<HashMap<String, Object>>() {}.getType());

                        Map<String,Transaction> tempMap = new HashMap<>();
                        
                        for (Map.Entry<String,Object> entry : mapObj.entrySet()){

                            String barray = poolJson.get(entry.getKey()).toString();
                            Transaction transaction = handleJsonTransaction(barray);
                            tempMap.put(entry.getKey(), transaction);
                        }

                        pool.transactionMap = tempMap;
                        index.synced = true;
                    
                    }
                }

                if(message.getChannel().equals("SYNCCHAIN")){
                    if(!index.synced2){
                        blockchain toReplace = handleJsonChain(messageObj);
                        chain.replaceChain(toReplace);
                        index.synced2 = true;
                    }
                }

                if(message.getChannel().equals("INTRO")){
                    if(index.synced){
                        String str = new Gson().toJson(pool);
                        pubnub.publish().message(str).channel(channels.SYNCPOOL.name()).async(new PNCallback<PNPublishResult>() {
                            @Override
                            public void onResponse(PNPublishResult result, PNStatus status) {
                                if(!status.isError()) {
                                    System.out.println("pub timetoken: " + result.getTimetoken());
                                }
                                System.out.println("pub status code: " + status.getStatusCode());
                            }
                        });
                        System.out.println("broadcasting to " + channels.SYNCPOOL.name());
                    }else{
                        System.out.println("Please wait while the pool is being synced!!");
                    }
                }

                if(message.getChannel().equals("INTRO2")){
                    if(index.synced2){
                        String str = new Gson().toJson(chain);
                        pubnub.publish().message(str).channel(channels.SYNCCHAIN.name()).async(new PNCallback<PNPublishResult>() {
                            @Override
                            public void onResponse(PNPublishResult result, PNStatus status) {
                                if(!status.isError()) {
                                    System.out.println("pub timetoken: " + result.getTimetoken());
                                }
                                System.out.println("pub status code: " + status.getStatusCode());
                            }
                        });
                        System.out.println("broadcasting to " + channels.SYNCCHAIN.name());
                    }else{
                        System.out.println("Please wait while the chain is being synced!!");
                    }
                }

            }

            public void status(PubNub pubnub, PNStatus status) {}
            public void signal(PubNub pubnub, PNSignalResult pnSignalResult) {}
            public void membership(PubNub pubnub, PNMembershipResult pnMembershipResult) {}
            public void messageAction(PubNub pubnub, PNMessageActionResult pnMessageActionResult) {}
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {}
            public void file(PubNub pubnub, PNFileEventResult pnFileEventResult) {}
            public void channel(PubNub pubnub, PNChannelMetadataResult pnChannelMetadataResult) {}
            public void uuid(PubNub pubnub, PNUUIDMetadataResult pnUUIDMetadataResult) {}

        });

    }

    public void broadcastChain(){
        String message = new Gson().toJson(this.chain);
        this.pubnub.publish().message(message).channel(channels.BLOCKCHAIN.name()).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                if(!status.isError()) {
                    System.out.println("pub timetoken: " + result.getTimetoken());
                }
                System.out.println("pub status code: " + status.getStatusCode());
            }
        });
        System.out.println("broadcasting to " + channels.BLOCKCHAIN.name());
    }

    public void broadcastTransactions(Transaction transaction){
        String message = new Gson().toJson(transaction);
        this.pubnub.publish().message(message).channel(channels.TRANSACTION.name()).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                if(!status.isError()) {
                    System.out.println("pub timetoken: " + result.getTimetoken());
                }
                System.out.println("pub status code: " + status.getStatusCode());
            }
        });
        System.out.println("broadcasting to " + channels.TRANSACTION.name());
        System.out.println(message);
    }

    public void introduceSelf(){
        String message = "I am here for the pool";
        this.pubnub.publish().message(message).channel(channels.INTRO.name()).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                if(!status.isError()) {
                    System.out.println("pub timetoken: " + result.getTimetoken());
                }
                System.out.println("pub status code: " + status.getStatusCode());
            }
        });
        System.out.println("broadcasting to " + channels.INTRO.name());
        System.out.println(message);
    }

    public void introduceSelf2(){
        String message = "I am here for the chain";
        this.pubnub.publish().message(message).channel(channels.INTRO2.name()).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                if(!status.isError()) {
                    System.out.println("pub timetoken: " + result.getTimetoken());
                }
                System.out.println("pub status code: " + status.getStatusCode());
            }
        });
        System.out.println("broadcasting to " + channels.INTRO2.name());
        System.out.println(message);
    }

    Transaction handleJsonTransaction(String messageObj){
        // System.out.println();
        // System.out.println();
        // System.out.println();
        System.out.println();
        System.out.println(messageObj);


        JsonObject jsonTransaction = new Gson().fromJson(messageObj, JsonObject.class);
        String outMap = jsonTransaction.get("OutputMap").toString();
        Map<String, Double> mapObj = new Gson().fromJson(outMap, new TypeToken<HashMap<String, Double>>() {}.getType());

        Transaction transaction = new Transaction();

        transaction.OutputMap = new HashMap<String, Double>(mapObj);

        if(jsonTransaction.get("input")!=null){
            String inp = jsonTransaction.get("input").toString();
            JsonObject jsonInput = new Gson().fromJson(inp, JsonObject.class);
            String c = jsonInput.get("timestamp").toString();
            String d = jsonInput.get("amount").toString();
            String e = jsonInput.get("address").toString();
            Long timestamp = Long.parseLong(c);
            Double amount = Double.parseDouble(d);
            BigInteger address = new BigInteger(e);

            Input input = new Input();

            input.timestamp = timestamp;
            input.amount = amount;
            input.address = address;

            String f = jsonInput.get("signature").toString();
            JsonObject jsonSign = new Gson().fromJson(f, JsonObject.class);
            byte[] r = new byte[32];
            byte[] s = new byte[32];
            String g = jsonSign.get("r").toString();
            String h = jsonSign.get("s").toString();
            String z = jsonSign.get("v").toString();
            byte v = Byte.parseByte(z);
            JsonElement jsonR = JsonParser.parseString(g);
            JsonElement jsonS = JsonParser.parseString(h);
            JsonArray jsonArrayR = jsonR.getAsJsonArray();
            JsonArray jsonArrayS = jsonS.getAsJsonArray();
            for(int i=0; i<=jsonArrayR.size()-1; i++){
                String foo = jsonArrayR.get(i).toString();
                r[i] = Byte.parseByte(foo);
            }
            for(int i=0; i<=jsonArrayS.size()-1; i++){
                String foo = jsonArrayS.get(i).toString();
                s[i] = Byte.parseByte(foo);
            }
            Sign.SignatureData sigData = new Sign.SignatureData(v, r, s);

            input.signature = sigData;

            transaction.input = input;
        }else{
            transaction.input = null;
        }

        String uuid = jsonTransaction.get("id").toString();
        StringBuilder sb = new StringBuilder(uuid);
        sb.deleteCharAt(uuid.length()-1);
        sb.deleteCharAt(0);
        String id = sb.toString();

        transaction.id = id;

        return transaction;
    }

    blockchain handleJsonChain(String messageObj){
        JsonObject jsonElement = new Gson().fromJson(messageObj, JsonObject.class);
        String blockArray = jsonElement.get("chain").toString();

        JsonElement jsonBlockArray = JsonParser.parseString(blockArray);

        JsonArray jsonArray = jsonBlockArray.getAsJsonArray();

        blockchain toReplace = new blockchain();
        toReplace.chain.clear();

        for(int i=0; i<=jsonArray.size()-1; i++){

            String foo = jsonArray.get(i).toString();
            JsonObject jsonFoo = new Gson().fromJson(foo, JsonObject.class);

            String lastHash, hash;
            long timestamp, nonce;
            int difficulty;

            lastHash = jsonFoo.get("lastHash").toString();
            hash = jsonFoo.get("hash").toString();

            ArrayList<Transaction> data = new ArrayList<>();
            if(jsonFoo.get("data") != null){
                String dataStr = jsonFoo.get("data").toString();
                JsonElement dataele = JsonParser.parseString(dataStr);
                JsonArray jsonArr = dataele.getAsJsonArray();
                for(int j=0; j<jsonArr.size(); j++){
                    Transaction a = handleJsonTransaction(jsonArr.get(j).toString());
                    data.add(a);
                }
            }else{
                data = null;
            }
    
            lastHash = lastHash.replace("\"", "");
            hash = hash.replace("\"", "");
            //data = data.replace("\"", "");

            timestamp = Long.parseLong(jsonFoo.get("timestamp").toString());
            nonce = Long.parseLong(jsonFoo.get("nonce").toString());
            difficulty = Integer.parseInt(jsonFoo.get("difficulty").toString());

            block receivedBlock = new block(lastHash, hash, data, timestamp, nonce, difficulty);

            toReplace.chain.add(receivedBlock);

        }
        return toReplace;
    } 
}