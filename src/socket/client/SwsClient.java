package socket.client;

import JavaFX.Login;
import socket.server.ClientInfos;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static socket.server.SwsServerActions.broadcast;
import static socket.server.SwsServerActions.clients;

public class SwsClient {
private DatagramSocket socket;
private InetAddress address;
private int port;
private boolean running;
//Name Eingeben
    //Client
public SwsClient(String name, String address, int port){
    try {
        this.address = InetAddress.getByName(address); //Convertiert String-->InetAddress
        this.port = port;
        socket = new DatagramSocket();
        running = true;
        listen();
        System.out.println("\\con:"+name);
        send("\\con:"+name);

    }catch(Exception e){
        e.printStackTrace();
    }

}

public void send(String message){
    try{
        message += "\\e";
        System.out.println(message);
        byte[] data = message.getBytes(); //konvertiert String zu Byte[]
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        socket.send(packet);
        System.out.println("Nachricht an "+ address.getHostAddress() + ":"+port+" gesendet");
    }catch(Exception e){
        e.printStackTrace();
    }
}
    private void listen(){
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
                        System.out.println(message);
                        message = message.substring(0, message.indexOf("\\e")); //\\e markiert die Letzte !Null stelle des dataArrays
                        System.out.println(message);
                        if(!srvCommand(message, packet)){
                            System.out.print("Test");
                            Login.printConsole(message);
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }; listenThread.start();
    }
    private boolean srvCommand(String message, DatagramPacket packet){
        if(message.startsWith("\\con:")){
        return true;
        }
        return false;
    }

}
