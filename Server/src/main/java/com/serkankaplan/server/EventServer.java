package com.serkankaplan.server;

import com.serkankaplan.common.util.Validator;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Event collecting and dispatching server
 * Created by serkan on 07/09/16.
 */
public class EventServer {

    private int port;



     //Storing PrintWriters for UserClients a username could be used by more than one client
    private Map<String,List<PrintWriter>> userWriterMap = new ConcurrentHashMap<>();

    //sockets opened stored to close at finally
    private Set<Socket> sockets = new HashSet<>();


    public EventServer(int port) {
        this.port = port;


    }

    public static void main(String[] args) {

        //Validating arguments
        if (args.length < 1 ) {
            System.out.println("Numeric port parameter is required");
            printUsage();
            return;
        }

        Integer port = null;

        try {
            port = Validator.portValidator(args[0]);
        }catch (IllegalArgumentException ex) {
            printUsage();
            System.out.println(ex.getMessage());
            return;
        }

        //Creating a server instance
        EventServer eventServer = new EventServer(port);

        //Start listening
        eventServer.start();
    }

    /**
     *
     */
    public void start() {

        //Create Listener
        try (ServerSocket listener = new ServerSocket(this.getPort())){

            try {
                while (true) {
                    //Starts accepting sockets
                    Socket socket = listener.accept();

                    //create thread for every socket
                    new SocketHandler(socket,userWriterMap).start();
                    sockets.add(socket);
                }
            } catch (IOException e) {
                System.out.println("Server closed : " + e.getMessage());
                return;
            }
        } catch (IOException e) {

            System.out.println("Server can not started : " + e.getMessage());
            return;
        } finally {

            //closing opened writers and sockets
            userWriterMap.forEach((k, printWriters) -> {
                if (printWriters != null) {
                    printWriters.forEach((printWriter -> {
                        printWriter.close();
                    }));
                }
            });

            sockets.forEach((socket -> {
                try {
                    socket.close();
                } catch (Exception e) {

                }
            }));

        }



    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private static final void printUsage() {
        System.out.println("Usage : {port}");
    }
}
