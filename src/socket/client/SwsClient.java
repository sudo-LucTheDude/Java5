package socket.client;

import JavaFX.Login;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


//Diese Klass Enthält fast die gliechen Methoden wie die SwsServerActions nur das sie nicht static sind
public class SwsClient {

        private DatagramSocket socket;
        private InetAddress address;
        private int port;
        private boolean running;

    public SwsClient(String name, String address, int port){
            try {
                this.address = InetAddress.getByName(address); //Convertiert String-->InetAddress
                this.port = port;
                socket = new DatagramSocket();
                running = true;
                listen();
                //Zeigt allen Benutzer das man nun Online ist
                send("\\con:"+name);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        //Nachricht Senden
        public void send(String message){
            try{
                message += "\\e";
                byte[] data = message.getBytes(); //konvertiert String zu Byte[]
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                System.out.println("versucht Nachricht an "+ address + ":"+ port +" gesendet");
                socket.send(packet);
                System.out.println("Nachricht an "+ address.getHostAddress() + ":"+port+" gesendet"); //dis=true
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
                        DatagramPacket packet = new DatagramPacket(data, data.length);
                        socket.receive(packet);
                        String message = new String(data);
                        message = message.substring(0, message.indexOf("\\e")); //\\e markiert die Letzte !Null stelle des dataArrays
                        if(!srvCommand(message, packet)){
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
        else if(message.startsWith("\\dis:")){
            Login.printConsole(message);
            return true;
        }
        else if(message.startsWith("\\clear:")){
            return true;
        }
        else if(message.startsWith("\\priv:")){
            System.out.println("Hier ist Perfekt und sollte allein sein");
            Login.printConsole("Privat!; " + message);
            return true;
        }
        return false;
    }

    public void stop(){
    running = false;
    }
}
