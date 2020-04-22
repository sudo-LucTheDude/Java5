package socket.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SwsServerActions{
    //Static Variablen weil wegen nur einem Server

    private static DatagramSocket socket; //(UDP-Socket)
    private static int port;

    public static void start(int port){
    try{
        socket = new DatagramSocket(port); //(UDP-Socket)
        System.out.println("Server startet auf Port: " + port);
    }catch(Exception e){
        e.printStackTrace();
        }
    }

    public static void broadcast(){

    }

    public static void send(){

    }

    public static void listen(){

    }

    public static void stop(){

    }

}
