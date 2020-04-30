package socket.client;



import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


//Diese Klass Enthält fast die gliechen Methoden wie die SwsServerActions nur das sie nicht static sind
public class ClientBackEnd {

        private DatagramSocket socket;
        private InetAddress address;
        private int port;
        private boolean running;

    /**
     * Erstellt einen neuen User im Backend
     * @param name
     * @param address
     * @param port
     */
    public ClientBackEnd(String name, String address, int port){
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

    /**
     * Nachricht Senden
     * @param message
     */
        public void send(String message){
            try{
                message += "\\e";
                byte[] data = message.getBytes(); //konvertiert String zu Byte[]
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                socket.send(packet);
                System.out.println("Nachricht an "+ address.getHostAddress() + ":"+port+" gesendet"); //dis=true
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    /**
     * Listen Thread der ständig horcht ob neue Befehle oder nachrichten kommen
     */
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
                            ClientFrontEnd.printConsole(message);
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }; listenThread.start();
    }

    /**
     * Überprüft ob eine Nachricht ein Befehl ist. Wen ja ist return true, sonst false.
     * @param message
     * @param packet
     * @return
     */
    private boolean srvCommand(String message, DatagramPacket packet){
        if(message.startsWith("\\con:")){
        return true;
        }
        else if(message.startsWith("\\dis:")){
            ClientFrontEnd.printConsole(message);
            return true;
        }
        else if(message.startsWith("\\priv:")){
            ClientFrontEnd.printConsole("Privat!; " + message);
            return true;
        }
        return false;
    }

    /**
     * Stopt den Thread
     */
    public void stop(){
    running = false;
    }
}
