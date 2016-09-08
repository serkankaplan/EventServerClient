package com.serkankaplan.server;

import com.google.gson.Gson;
import com.serkankaplan.common.messagepojo.Event;
import com.serkankaplan.common.messagepojo.SocketType;
import com.serkankaplan.common.messagepojo.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handler thread for socket connection.
 * If connection is a EventClient connection thread should be keeped alive
 * and wait for the events send from the EventClient and send them through correct writes
 * If connection is a UserClient thread will be ended after storing Socket and writer to the main thread.
 * <p>
 * Created by serkan on 08/09/16.
 */
public class SocketHandler extends Thread {

    private SocketType socketType;
    private Socket socket;
    Map<String, List<PrintWriter>> userWriterMap;


    public SocketHandler(Socket socket, Map<String, List<PrintWriter>> userWriterMapMap) {
        this.socket = socket;
        this.userWriterMap = userWriterMapMap;
    }

    @Override
    public void run() {

        BufferedReader in = null;
        PrintWriter out = null;
        try {

            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            // Create character streams for the socket.


            String socketTypeMessage = in.readLine();

            try {
                this.socketType = SocketType.valueOf(socketTypeMessage);
            } catch (IllegalArgumentException e) {

                out.println("Invalid SocketType");

                socket.close();
                return;
            }

            System.out.println("Client Connected : " + this.socketType.name());

            Gson gson = new Gson();
            switch (this.socketType) {
                case EVENT_PUBLISHER:
                    //Connected event publisher keep thread alive and wait for event to read
                    while (true) {

                        Event event = gson.fromJson(in.readLine(), Event.class);

                        if (event == null) {
                            return;
                        }

                        if (userWriterMap.get(event.getUsername()) != null) {
                            userWriterMap.get(event.getUsername()).forEach((writer) -> {
                                    writer.println(gson.toJson(event));
                            });
                        }
                    }

                case USER:

                    //UserClient connection received keep writer and end thread
                    try {
                        User user = gson.fromJson(in.readLine(), User.class);

                        if (user == null) {
                            return;
                        }

                        //Synchronize userWriterMap username adding to avoid race condition
                        synchronized (userWriterMap) {

                            if (userWriterMap.get(user.getName()) == null) {
                                userWriterMap.put(user.getName(), new ArrayList<>());
                            }
                        }
                        userWriterMap.get(user.getName()).add(out);
                    } catch (Exception e) {

                        System.err.println("Error in userClient connect : " + e.getMessage());
                    }

                    break;
            }

        } catch (IOException e) {

            System.err.println("Error on SocketHandler : " + e.getMessage());
        }
    }


}
