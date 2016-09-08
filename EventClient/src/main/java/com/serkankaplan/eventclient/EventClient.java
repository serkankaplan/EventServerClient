package com.serkankaplan.eventclient;

import com.google.gson.Gson;
import com.serkankaplan.common.messagepojo.Event;
import com.serkankaplan.common.messagepojo.SocketType;
import com.serkankaplan.common.util.Validator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

/**
 * UserClient app
 * Created by serkan on 08/09/16.
 */
public class EventClient {


    public static void main(String[] args) throws Exception {
        
        //Validating the arguments
        if (args.length < 2) {
            printUsage();
            return;
        }

        String host = null;
        Integer port = null;

        try {
            host = Validator.hostValidator(args[0]);
            port = Validator.portValidator(args[1]);
        }catch (IllegalArgumentException ex) {
            printUsage();
            System.out.println(ex.getMessage());
            return;
        }

        //Creating a socket connection
        try (Socket socket = new Socket(host, port)) {


            //Creating reader and writer
            try (BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                //Sending Client Type to server
                out.println(SocketType.EVENT_PUBLISHER);


                Scanner scanner = new Scanner(System.in);
                Gson gson = new Gson();

                //Starts reading events from console
                while (true) {
                    System.out.println("Enter Event User Name :");
                    String userName = scanner.nextLine();

                    System.out.println("Enter Event Content :");
                    String content = scanner.nextLine();

                    Event event = new Event(userName, content);

                    out.println(gson.toJson(event));
                }
            }
        } catch (ConnectException e) {
            System.err.println("Error to connect the server : "+ e.getMessage());
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
    }

    private static final void printUsage() {
        System.out.println("Usage : {host} {port}");
    }
}
