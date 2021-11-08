package com.rudichain.rudichain.network;

import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    TEST, BLOCKCHAIN;
}

public class pubsub{

    public blockchain chain;
    private PubNub pubnub;

    public pubsub(blockchain chain){
        this.chain = chain;

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(credentials.SUBSCRIBE_KEY.getCredentials());
        pnConfiguration.setPublishKey(credentials.PUBLISH_KEY.getCredentials());
        pnConfiguration.setUuid(credentials.SECRET_KEY.getCredentials());

        this.pubnub = new PubNub(pnConfiguration);

        this.pubnub.subscribe().channels(Arrays.asList(channels.TEST.name(), channels.BLOCKCHAIN.name())).execute();

        this.pubnub.addListener(new SubscribeCallback() {
            public void message(PubNub pubnub, PNMessageResult message){
                JsonElement receivedMessageObject = message.getMessage();
                System.out.println("Message received from: " + message.getChannel()
                                    + "  Message: " + receivedMessageObject);

                String messageOb = receivedMessageObject.toString();
                String temp = messageOb.replaceAll("\\"+"\\","");
                String withoutLastCharacter = temp.substring(0, temp.length() - 1);
                String messageObj = withoutLastCharacter.substring(1);

                System.out.println(messageObj);

                JsonObject jsonElement = new Gson().fromJson(messageObj, JsonObject.class);

                

                String blockArray = jsonElement.get("chain").toString();

                JsonElement jsonBlockArray = JsonParser.parseString(blockArray);

                JsonArray jsonArray = jsonBlockArray.getAsJsonArray();

                blockchain toReplace = new blockchain();
                toReplace.chain.clear();

                for(int i=0; i<=jsonArray.size()-1; i++){

                    String foo = jsonArray.get(i).toString();
                    JsonObject jsonFoo = new Gson().fromJson(foo, JsonObject.class);

                    String lastHash, hash, data;
                    long timestamp, nonce;
                    int difficulty;
                    
                    lastHash = jsonFoo.get("lastHash").toString();
                    hash = jsonFoo.get("hash").toString();
                    data = jsonFoo.get("data").toString();

                    lastHash = lastHash.replace("\"", "");
                    hash = hash.replace("\"", "");
                    data = data.replace("\"", "");

                    timestamp = Long.parseLong(jsonFoo.get("timestamp").toString());
                    nonce = Long.parseLong(jsonFoo.get("nonce").toString());
                    difficulty = Integer.parseInt(jsonFoo.get("difficulty").toString());

                    block receivedBlock = new block(lastHash, hash, data, timestamp, nonce, difficulty);

                    toReplace.chain.add(receivedBlock);

                }
                

                chain.replaceChain(toReplace);

                // System.out.println("ToReplace: " + gson.toJson(obj));
                 
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
        //JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        this.pubnub.publish().message(message).channel(channels.BLOCKCHAIN.name()).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                // handle publish result, status always present, result if successful
                // status.isError() to see if error happened
                if(!status.isError()) {
                    System.out.println("pub timetoken: " + result.getTimetoken());
                }
                System.out.println("pub status code: " + status.getStatusCode());
            }
        });
        System.out.println("broadcasting to " + channels.BLOCKCHAIN.name());
    }

    //to implement broadcastTransactions() method

}