package socket.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;

public class SwsServerActions{
    //Static Variablen weil wegen nur einem Server

    private static DatagramSocket socket; //(UDP-Socket)
    private static int port;
    private static boolean running;
    public static ArrayList<ClientInfos> clients = new ArrayList<ClientInfos>();
    private static int clientID = 0;

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
        for(ClientInfos info : clients){
            send(message, info.getAddress(), info.getPort());
        }

    }

    public static void send(String message, InetAddress address, int port){
        try{
            message += "\\e";
            byte[] data = message.getBytes(); //konvertiert String zu Byte[]
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            System.out.println("Nachricht an"+ address.getHostAddress() + ":"+port+" gesendet");
        }catch(Exception e){
            e.printStackTrace();
        }
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

                        if(!srvCommand(message, packet)){
                            broadcast(message);
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
    }; listenThread.start();
    }

    public static void stop(){
        running = false;
    }

    private static boolean srvCommand(String message, DatagramPacket packet){
        if(message.startsWith("\\con:")){
            //Name wird aus dem Commando gelesen
            String name = message.substring(message.indexOf(":")+1);

            //Neues ClientObjekt erstellen
            clients.add(new ClientInfos(name, clientID++, packet.getAddress(), packet.getPort()));
            broadcast("Benutzer " + name + " ist online");
            return true;
        }
//noch nicht fertig!
        if(message.startsWith("\\dis:")){
            return true;
        }
        return false;
    }

}
