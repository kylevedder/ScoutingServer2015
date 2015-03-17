/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author kyle
 */
public class SyncFilesServerThread implements Runnable
{

    private volatile boolean isRunning = true;

    ServerSocket serverSocket = null;
    private int port;

    public SyncFilesServerThread(int port)
    {
        this.port = port;
    }

    /**
     * Initializes the server to accept connections.
     */
    private void init()
    {
        try
        {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException ex)
        {
            System.err.println("Server unable to open socket on port: " + port);
        }
    }

    @Override
    public void run()
    {
        System.out.println("Starting SyncFilesServer...");
        init();
        while (isRunning)
        {
            mainLoop();
        }
    }

    /**
     * Hook for the primary loop through the code.
     */
    private void mainLoop()
    {
        try
        {
            //block for new connection
            Socket socket = serverSocket.accept();
            System.out.println("Connection Recieved!");

            //setup in and out
            InputStream ins = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            StringBuilder out = new StringBuilder();
            
            //read from the client
            String line;
            while ((line = reader.readLine()) != null)
            {
                out.append(line);
                out.append("\n");
            }
            
            //construct final string
            String recievedString = out.toString();
            
            //split into individual JSON objects
            String[] jsonStrings = recievedString.split("\n");
            JSONObject[] jsonObjs = new JSONObject[jsonStrings.length];
            
            //add each JSON string as JSONObject to array
            for (int i = 0; i < jsonStrings.length; i++)
            {
                String jsonString = jsonStrings[i];
                jsonObjs[i] = new JSONObject(jsonString);
            }                            
            
            System.out.println(recievedString);   //Prints the string content read from input stream
            
            
        }
        catch (IOException ex)
        {
            Logger.getLogger(SyncFilesServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Gracefully stops the thread.
     */
    public void kill()
    {
        this.isRunning = false;
    }

}
