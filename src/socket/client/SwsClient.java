package socket.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SwsClient {
private DatagramSocket socket;
private InetAddress address;
private int port;

public SwsClient(String name, String address, int port){
    try {
        this.address = InetAddress.getByName(address); //Convertiert String-->InetAddress
        this.port = port;
        socket = new DatagramSocket();

    }catch(Exception e){
        e.printStackTrace();
    }
    send("\\con:"+);
}

public void send(String message){
    try{
        message += "\\e";
        byte[] data = message.getBytes(); //konvertiert String zu Byte[]
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        socket.send(packet);
        System.out.println("Nachricht an "+ address.getHostAddress() + ":"+port+" gesendet");
    }catch(Exception e){
        e.printStackTrace();
    }


}

}
