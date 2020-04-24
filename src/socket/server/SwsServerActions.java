package socket.server;

import Database.DBconnection;
import JavaFX.Login;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;

import static JavaFX.Login.printUsers;

public class SwsServerActions{
    //Static Variablen weil wegen nur einem Server

    private static DatagramSocket socketDatagram; //(UDP-Socket)
    private static int port;
    private static boolean runningBoolean;
    public static ArrayList<ClientInfos> clientsArrayList = new ArrayList<>();
    private static int clientID = 0;

    public static void start(int port){
    try{
        //InetAddress adresse = InetAddress.getLocalHost();
        socketDatagram = new DatagramSocket(port); //(UDP-Socket)
        System.out.println("Server startet auf Port: " + port);
        InetAddress adr = socketDatagram.getInetAddress();
        System.out.println("adresse ist " + adr);
        runningBoolean = true;
        listen();
    }catch(Exception e){
        e.printStackTrace();
        }
    }

    public static void broadcast(String message){
        for(ClientInfos info : clientsArrayList){
            send(message, info.getAddress(), info.getPort());
        }
    }

    public static void send(String message, InetAddress address, int port){
        try{
            message += "\\e";
            byte[] data = message.getBytes(); //konvertiert String zu Byte[]
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socketDatagram.send(packet);
            System.out.println("Nachricht " + message + " an "+ address.getHostAddress() + ":"+port+" gesendet");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void listen(){
        Thread listenThread = new Thread("Einkommende Nachrichten") // Neuer Thread, ansonsten würde die Methode die Applikation nicht weiterlaufen lassen
        {
            //run() wird für den Thread benötigt
            public void run(){
                try{
                    while(runningBoolean){
                        byte[] data = new byte[1024]; //Byte Array wegen Datagram-Socket/Packet, in jedem Byte wird ein Char gespeichert
                        DatagramPacket packet = new DatagramPacket(data, data.length); //DatagramPacket enhält viele MetaDaten, bspw. Absender usw.
                        socketDatagram.receive(packet);
                        String message = new String(data);
                        message = message.substring(0, message.indexOf("\\e")); //\\e markiert die Letzte !Null stelle des dataArrays
                        //Nur normale nachrichten --> broadcasten
                        if(!srvCommand(message, packet)){
                            System.out.print(message);
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
        runningBoolean = false;
    }

    private static boolean srvCommand(String message, DatagramPacket packet){
        if(message.startsWith("\\con:")){
            //Name wird aus dem Commando gelesen
            String name = message.substring(message.indexOf(":")+1); //lucas
            //Neues ClientObjekt erstellen
            clientsArrayList.add(new ClientInfos(name, clientID++, packet.getAddress(), packet.getPort()));
            broadcast("Benutzer " + name + " ist online");
            broadcast("\\clear");
            for (ClientInfos user: clientsArrayList) {
                broadcast("\\user:" + user.getName());
            }
            return true;
        }
        else if(message.startsWith("\\dis:")){
            String name = message.substring(message.indexOf(":")+1); //lucas
            for(ClientInfos info : clientsArrayList){
                if(name.equals(info.getName())){
                    broadcast("Benutzer " + info.getName() + " ist offline");
                    clientsArrayList.remove(info);
                    clientsArrayList.remove(info);
                    broadcast("\\clear");
                    for (ClientInfos user: clientsArrayList) {
                        broadcast("\\user:" + user.getName());
                    }
                    return true;
                }
            }
        }
        return false;
    }

}
