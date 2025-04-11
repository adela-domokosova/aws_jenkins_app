package com.example.client;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class ClientApplication {
    public static void main(String[] args) {
    try {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter your user name: ");
        String name = scanner.nextLine();
        System.out.println("start");
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket, name);
        client.listenForMessage();
        client.sendMessage();
    }catch (IOException e){

    }
    }

}
