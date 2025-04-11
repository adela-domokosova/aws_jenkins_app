package com.example.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client{
    public String name;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Socket socket;

    public Client(Socket socket,String name){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            this.name = name;
            bufferedWriter.write(name);
            System.out.println("construct");
        } catch (IOException e){
            System.out.println("opss");
        }
    }

    //poslouchat než dojde zpráva -> block operation
    //udělat listener, nebude se implementovat runnable
    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msg;
                while(!socket.isClosed()){
                    try{
                        msg = bufferedReader.readLine();
                        System.out.println(msg);
                    }catch(IOException e){

                    }
                }
            }
        }).start();
    }

    public void sendMessage(){
        try {
            System.out.println("run");
            bufferedWriter.write(name);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                String msg = scanner.nextLine();
                bufferedWriter.write(name + " " + msg);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            //blocking operation -> čeká na daném vlákně
        } catch (IOException e) {


        }
    }
}
