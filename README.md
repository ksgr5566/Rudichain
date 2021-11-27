# Rudichain

The goal of Rudichain is to simulate how transactions occur in real time on a blockchain based system, and by no means a
“production ready” implementation of a cryptocurrency, but rather tries to show that the basic principles in a cryptocurrency 
can be implemented in a concise way using pure `Java`. <br />

**Note: *This a real time application, and can be used just like any other cyrptocurrency wallet. Just that this a very basic version and is not scaled for large amount of users and data.***


# Installation

1. If you have JRE and Maven installed, then download zip and:
```
cd Rudichain/rudichain
mvn exec:java
```

2. For Windows user only: If you don't have the above mentioned software installed, then 
   download the zip [here](https://drive.google.com/file/d/1tq16-CS3IyDvvEfvmQcDUi2hUYleW7ce/view?usp=sharing), then: 
- Extract RudiWallet and go to rudichain sub-folder then, double click or run the `run` windows batch file.

# Backend only Rudichain Wallet

Visit our backend version of Rudichain [here](https://github.com/ksgr5566/Rudichain/tree/complete_backend). This utilizes `SpringBoot` for directing requests to mappings in localhost and `Postman` software is required for sending and receiving `GET` and `POST` requests.


# Navigate
```
├───main
│   └───java
│       └───com
│           └───rudichain
│               │   constants.java
│               │   RudichainApplication.java
│               │
│               ├───backend
│               │       block.java
│               │       blockchain.java
│               │
│               ├───cryptography
│               │       ECDSA.java
│               │       Hash.java
│               │
│               ├───frontend
│               │   │   BlockChainPage.java
│               │   │   chainPageHelper.java
│               │   │   helper2.java
│               │   │   HomePage.java
│               │   │   LoginFrame.java
│               │   │   mine.java
│               │   │   TransactionsFrame.java
│               │   │
│               │   └───images
│               │           image1.png
│               │           image2.png
│               │           logo.PNG
│               │
│               ├───network
│               │       pubsub.java
│               │
│               └───wallet
│                       Input.java
│                       InvalidTransaction.java
│                       Transaction.java
│                       TransactionMiner.java
│                       TransactionPool.java
│                       Wallet.java
│
└───test
    └───java
        └───com
            └───rudichain
                    RudichainApplicationTest.java
```

# Project Demo

- The first pop up would be the `Login` window, which enables you to login if you already have the public/private key pair, or you could generate new keys for yourself.

![loginPageRudichain](https://user-images.githubusercontent.com/74421758/143677403-43496406-8adb-490a-adb0-a1386341ec95.PNG)

<br />

- You would then be navigated to the `Home` window where you could view your keys, balance and send transactions to others using their public key:

 ![HomePageRudichain](https://user-images.githubusercontent.com/74421758/143677457-851349fd-118f-4b11-bc5d-c85dc0aadc78.PNG)
 
 <br />
 
 - You can mine the transactions available in the transaction pool by clicking `mine` button in `Transaction Pool` window, and note that it may take time, and a reward amount will be given on successful mining of a new block.
 
 ![TransactionPoolRudichain](https://user-images.githubusercontent.com/74421758/143677769-9c051966-0a21-4533-b325-5ccfb92cf4b7.PNG)

<br />

- You can view the blockchain in `Rudichain` window, which consists of all the blocks that are added to the chain since the Genesis block:

![BlockchainRudichain](https://user-images.githubusercontent.com/74421758/143677865-26191bca-30e0-4e0b-a276-d4b5673a04db.PNG)

<br />

- You can view your transactions in `My Transactions` window:

![MyTransactionsRudichain](https://user-images.githubusercontent.com/74421758/143677960-06eb3259-9198-495a-8bdd-7304cea15e42.PNG)

<br />

# Java API's and Dependencies used

- `com.google.code.gson` to format Objects into Json Strings and vice-versa.
- `com.pubnub` to implement Publisher-Subscriber architecture model in Java for the blokchain network. To know more about PubNub, visit 
 [here](https://www.pubnub.com/docs/sdks/java).
- `org.web3j` to use elliptic key curve cyptography for hashing and generating keys. To know more ECC, visit [here](https://avinetworks.com/glossary/elliptic-curve-cryptography/#:~:text=Elliptic%20Curve%20Cryptography%20(ECC)%20is,and%20encryption%20of%20web%20traffic.&text=RSA%20achieves%20one%2Dway%20encryption,and%20software%20using%20prime%20factorization.).
- `com.google.bitcoin.core` for conversion to/from Base58.
<br />

- `Swing` is used for frontend interface.
- `Maven` is used for managing dependencies and build purposes.







