package com.rudichain.rudichain.network;

import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;

import com.rudichain.rudichain.backend.blockchain;

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

    blockchain chain;
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
                System.out.println("Message received from: " + message.getChannel()
                                    + "  Message: " + message.getMessage());

                JsonObject jsonObject = message.getMessage().getAsJsonObject();
                Gson gson= new Gson();
                blockchain obj = gson.fromJson(jsonObject.toString(),blockchain.class);
                chain.replaceChain(obj);
                
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
        this.pubnub.publish().message(message).channel(channels.BLOCKCHAIN.name());
    }

    //to implement broadcastTransactions() method

}