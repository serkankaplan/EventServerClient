package com.serkankaplan.userclient;

import com.google.gson.Gson;
import com.serkankaplan.common.messagepojo.Event;
import com.serkankaplan.common.messagepojo.SocketType;
import com.serkankaplan.common.messagepojo.User;
import com.serkankaplan.common.util.Validator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

/**
 * UserClient app register itself to server and waits for events related to it
 * Created by serkan on 08/09/16.
 */
public class UserClient {

    public static void main(String[] args) throws Exception {


        //Checks Parameters
        if (args.length < 3) {
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


        String username = args[2];

        //Create socket connection
        try (Socket socket = new Socket(host, port)) {

            //Create reader and writer
            try (BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                //send client type to server
                out.println(SocketType.USER);

                Gson gson = new Gson();

                //Register username to server
                User user = new User(username);
                out.println(gson.toJson(user));

                while (true) {
                    //wait for event related to itself
                    try {
                        Event event = gson.fromJson(in.readLine(), Event.class);
                        if (event == null) {
                            System.err.println("Lost connection with server.");

                            return;
                        }
                        System.out.println("Event Received : " + event.getContent());
                    } catch (Exception e) {
                        System.err.println("Error occurred while reading message : " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error occurred: " + e.getMessage());
            }
        } catch (ConnectException e) {
            System.err.println("Error to connect the server : "+ e.getMessage());
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
    }

    private static final void printUsage() {
        System.out.println("Usage : {host} {port} {username}");
    }
}
