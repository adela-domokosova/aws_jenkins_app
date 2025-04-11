package com.example.server;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

//runnable nutné pro vlastní vlákno pro každého klienta -> run() co se děje na vlákně
public class ClientHandler implements Runnable {
    public static Set<ClientHandler> clientSet = new HashSet<>();
    public String name;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ClientHandler(Socket socket) throws IOException {
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            this.name = bufferedReader.readLine();
            clientSet.add(this);
            //notify others, user has just connected
            broadcastMessage("Server: " + name + " has just joined the chat");

        } catch (IOException e){
            System.out.println("ops constructor");
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    //leaving/disconnectiong -> closing connection
    private void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) throws IOException {
        removeClientHandler();
        //není potřeba explicitně zavřít outputstreamwriter a inputstreamreader
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void broadcastMessage(String message) throws IOException {
        for (ClientHandler client:clientSet){
            //vynechat toho, kdo ji posílá
            try{
              if(!Objects.equals(client.name, this.name)){
                  client.bufferedWriter.write(message);
                  client.bufferedWriter.newLine();
                  client.bufferedWriter.flush();
              }
            }catch(IOException e){
                closeEverything(socket,bufferedWriter,bufferedReader);
            }
        }
    }

    @Override
    public void run() {
        String message;
        //co se dějě na tom vlákně
        //čeká na input stream od užiavtele
        while(!socket.isClosed()){
            try {
                //blocking operation -> čeká na daném vlákně
                message = bufferedReader.readLine();
                System.out.println(message);
                broadcastMessage(message);
            } catch (IOException e) {
                try {
                    closeEverything(socket, bufferedWriter, bufferedReader);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                //break out of while loop
                break;
            }
        }


    }
    public void sendMessage(String message){

    }

    public void handleMessage(){
        //print to server terminal
        //brodcast to other users
    }
    public void removeClientHandler() throws IOException {
        clientSet.remove(this);
        broadcastMessage("user " + name +" has left");
    }
}
