package socket.server;

import java.net.Inet4Address;

//Diese Klasse Seichert alle relevanten ClientInfos
public class ClientInfos {

    //Variabeldeklaration
    private Inet4Address address;
    private int port;
    private String name;
    private int id;

    //Konstruktor
    public ClientInfos (String name, int id, Inet4Address address, int port){
        this.name = name;
        this.id = id;
        this.address = address;
        this.port = port;
    }


    // Getter und Setter
    public Inet4Address getAddress() {
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
