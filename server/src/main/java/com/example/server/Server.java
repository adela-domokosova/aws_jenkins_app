package com.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.server.ClientHandler.clientSet;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket server) {
        this.serverSocket = server;
    }

    public void serverStart(){
        try{
            while(!serverSocket.isClosed()){
                System.out.println(clientSet);
                //when client accepts Socket object is returned
                Socket socket = serverSocket.accept();
                System.out.println("a client connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch (IOException e){
            closeServerSocket();
        }
    }

    public void closeServerSocket(){
        try{
            if(serverSocket !=null){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
