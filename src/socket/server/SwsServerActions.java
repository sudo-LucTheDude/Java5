package socket.server;

import Database.DBconnection;
import JavaFX.Login;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;

import static JavaFX.Login.printConsole;
import static JavaFX.Login.printUsers;

public class SwsServerActions{

    //Static Variablen weil wegen nur einem Server
    public static DatagramSocket socketDatagram; //(UDP-Socket)
    private static int port;
    private static boolean runningBoolean;
    public static ArrayList<ClientInfos> clientsArrayList = new ArrayList<>();
    private static int clientID = 0;

    public static void start(int port){
    try{
        //InetAddress adresse = InetAddress.getLocalHost();
        socketDatagram = new DatagramSocket(port); //(UDP-Socket)
        System.out.println("Server startet auf Port: " + port);
        runningBoolean = true;
        listen();
    }catch(Exception e){
        e.printStackTrace();
        }
    }
    //Nachricht wird an jeden User geschickt
    public static void broadcast(String message){
        System.out.println("Boadcast");
        for(ClientInfos info : clientsArrayList){
            send(message, info.getAddress(), info.getPort());
        }
    }

    public static void send(String message, InetAddress address, int port){
        try{
            //Der Nachricht wird ein \\e hinzugefügt das man den Schluss der Nachricht im ByteArray erkennt
            message += "\\e";
            byte[] data = message.getBytes(); //konvertiert String zu Byte[]
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            //Datagrampacket wird versendet
            socketDatagram.send(packet);
            System.out.println("Nachricht " + message + " an "+ address.getHostAddress() + ":"+port+" gesendet");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void privateSend(String message){
        try{
            String id = message.substring(6,7);
            System.out.print(id);
            int destId = Integer.parseInt(id);

            for(ClientInfos info : clientsArrayList){
                int i = 0;
                if(destId == info.getId()) {
                    message = message.substring(9);
                    message = "Privat: " + message;
                    send(message, info.getAddress(), info.getPort());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void listen(){
        Thread listenThread = new Thread("Einkommende Nachrichten") // Neuer Thread, ansonsten würde die Methode die Applikation nicht weiterlaufen lassen, da immer auf neue Nachrichten gewartet wird (While)
        {
            //run() wird für den Thread benötigt
            public void run(){
                try{
                    while(runningBoolean){
                        byte[] data = new byte[1024]; //Byte Array wegen Datagram-Socket/Packet, in jedem Byte wird ein Char gespeichert
                        DatagramPacket packet = new DatagramPacket(data, data.length); //Die Struktur für einkommende DatagramPackets wird definiert
                        socketDatagram.receive(packet);
                        String message = new String(data);
                        message = message.substring(0, message.indexOf("\\e")); //\\e markiert die Letzte !Null stelle des dataArrays
                        //Nur normale nachrichten --> broadcasten, Server Befehle werden im srvCommand weiterverarbeitet
                        if(!srvCommand(message, packet)){
                            //if(!(message.startsWith("\\priv:"))){
                            broadcast(message);
                            //}
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
    }; listenThread.start();
    }

    //ServerCommand Kürzel steht immer vor der Nachricht , \\dis: = verbindung getrennt, \\con: online
    //Die Commands \\clear & \\user werden erst im Login.printConsole interpretiert
    private static boolean srvCommand(String message, DatagramPacket packet){
        if(message.startsWith("\\con:")){
            //Name wird aus dem Commando gelesen
            String name = message.substring(message.indexOf(":")+1); //lucas
            //Neues ClientObjekt erstellen
            clientsArrayList.add(new ClientInfos(name, clientID++, packet.getAddress(), packet.getPort()));
            broadcast("Benutzer " + name + " ist online");
            //Aktualisiert Onlineliste im GUI
            broadcast("\\clear");
            for (ClientInfos user: clientsArrayList) {
                broadcast("\\user:" + user.getName() + " erreichbar über ID: " + user.getId());
            }
            return true;
        }
        else if(message.startsWith("\\dis:")){
            String name = message.substring(message.indexOf(":")+1); //lucas
            //Enfernt Logout Benutzer aus der ClientListe
            for(ClientInfos info : clientsArrayList){
                if(name.equals(info.getName())){
                    broadcast("Benutzer " + info.getName() + " ist offline");
                    clientsArrayList.remove(info);
                    //Aktualisiert Onlineliste im GUI
                    broadcast("\\clear");
                    for (ClientInfos user: clientsArrayList) {
                        broadcast("\\user:" + user.getName() + " erreichbar über ID: " + user.getId());
                    }
                    return true;
                }
            }
        }
        else if(message.startsWith("\\priv:")){
            privateSend(message);
            return true;
        }
        return false;
    }

    public static void stop(){
        runningBoolean = false;
    }


}

