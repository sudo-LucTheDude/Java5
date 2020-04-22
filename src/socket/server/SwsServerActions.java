package socket.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SwsServerActions{
    //Static Variablen weil wegen nur einem Server

    private static DatagramSocket socket; //(UDP-Socket)
    private static int port;
    private static boolean running;

    public static void start(int port){
    try{
        socket = new DatagramSocket(port); //(UDP-Socket)
        System.out.println("Server startet auf Port: " + port);
        running = true;
        listen();
    }catch(Exception e){
        e.printStackTrace();
        }
    }

    public static void broadcast(String message){

    }

    public static void send(){

    }

    public static void listen(){
        Thread listenThread = new Thread("Einkommende Nachrichten") // Neuer Thread, ansonsten würde die Methode die Applikation nicht weiterlaufen lassen
        {
            //run() wird für den Thread benötigt
            public void run(){
                try{
                    while(running){
                        byte[] data = new byte[1024]; //Byte Array wegen Datagram-Socket/Packet, in jedem Byte wird ein Char gespeichert
                        DatagramPacket packet = new DatagramPacket(data, data.length); //DatagramPacket enhält viele MetaDaten, bspw. Absender usw.
                        socket.receive(packet);
                        String message = new String(data);
                        message = message.substring(0, message.indexOf("\\e")); //\\e markiert die Letzte !Null stelle des dataArrays

                        broadcast(message);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
    }; listenThread.start();
    }

    public static void stop(){

    }

}
