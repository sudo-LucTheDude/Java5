package socket.server;

import java.net.Inet4Address;
import java.net.InetAddress;

//Diese Klasse Seichert alle relevanten ClientInfos
public class ClientInfos {

    //Variabeldeklaration
    private InetAddress address;
    private int port;
    private String name;
    private int id;

    //Konstruktor
    public ClientInfos (String name, int id, InetAddress address, int port){
        this.name = name;
        this.id = id;
        this.address = address;
        this.port = port;
    }


    // Getter und Setter
    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

}
