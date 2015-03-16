/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.networking;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            loop();
        }
    }

    /**
     * Hook for the primary loop through the code.
     */
    private void loop()
    {
        try
        {
            Socket socket = serverSocket.accept();
            System.out.println("Connection Recieved!");
            InputStream ins = socket.getInputStream();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(ins)))
            {
                StringBuilder out = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                {
                    out.append(line);
                }
                System.out.println(out.toString());   //Prints the string content read from input stream
            }

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
