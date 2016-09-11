package com.project;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ben on 9/4/2016.
 */
public class ArduinoServer {
    ServerSocket serverSocket;
    Socket clientSocket;
    BlockingQueue<Action> queue;

    public ArduinoServer(BlockingQueue<Action> queue){
        this.queue = queue;
        try {
            serverSocket = new ServerSocket(3029);
            clientSocket = serverSocket.accept();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void startTransmitting(){
        TransmiterThread transmiter = new TransmiterThread();
        transmiter.start();

        try{
            transmiter.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private class TransmiterThread extends Thread{
        Action action;
        BufferedWriter outputWriter;

        @Override
        public void start(){
            try{
                outputWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            } catch (IOException e){
                System.out.println("Could not write to Arduino");
                return;
            }

            while(true){
                try{
                    action = queue.poll(5, TimeUnit.SECONDS);
                    switch(action){
                        case CLOSE:{
                            outputWriter.write("close\n");
                            outputWriter.flush();
                        }
                        case OPEN:{
                            outputWriter.write("open\n");
                            outputWriter.flush();
                        }
                    }
                } catch (InterruptedException e){
                    break;
                } catch (IOException e){
                    System.out.println("Could not write to Arduino");
                    return;
                }
            }
        }
    }

}
