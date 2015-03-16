/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.main;

import server.frames.MainFrame;
import server.networking.SyncFilesServerThread;

/**
 *
 * @author kyle
 */
public class Main
{
    public static MainFrame frame = null;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        frame = new MainFrame();
        Thread syncFilesServerThread = new Thread(new SyncFilesServerThread(8080));
        syncFilesServerThread.start();
    }    
}
