package socket.server;

import java.net.InetAddress;

//Diese Klasse speichert alle relevanten ClientInfos
public class ServerClientInfos {

    //Variabeldeklaration
    private InetAddress address;
    private int port;
    private String name;
    private int id;

    /**
     * Konstruktor
     * @param name
     * @param id
     * @param address
     * @param port
     */
    public ServerClientInfos(String name, int id, InetAddress address, int port){
        this.name = name;
        this.id = id;
        this.address = address;
        this.port = port;
    }

    /**
     * Gibt die adresse zurück
     * @return
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     * Gibt den Port zurück
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     * Gibt den Namen zurück
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die ID zurück
     * @return
     */
    public int getId() {
        return id;
    }

}
