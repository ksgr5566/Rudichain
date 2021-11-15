package com.rudichain.rudichain;

import java.io.IOException;
import java.net.Socket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RudichainApplication implements constants{

	private static boolean available(int port) {
		try (Socket ignored = new Socket("localhost", port)) {
			return false;
		} catch (IOException ignored) {
			return true;
		}
	}

	public static void main(String[] args){
		
		if(available(DEFAULT_PORT)){
			System.setProperty("server.port", String.valueOf(DEFAULT_PORT));
		}
		SpringApplication.run(RudichainApplication.class, args);
	}

}
