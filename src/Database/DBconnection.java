package Database;
import java.sql.*;

public class DBconnection {

    private String username = "test";
    private String password = "1234";

    private String url = "jdbc:mysql://localhost:3306/SWSMessenger?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin";
    private String userDB = "root";
    private String passwordDB = "1234";
    private String dbanswer;

    public boolean passWordValidator(String username, String password) {
        String uname = username;
        String upw = password;

        //es wird versucht eine DB Verbindung herzustellen, falls dies nicht möglich ist wird dies gecatcht.
        try (Connection conn = DriverManager.getConnection(url, userDB, passwordDB)) {
            System.out.println("Erfolgreich mit Datenbank verbunden!"); //Ausgeben
            String query = "Select password from user where username ='" + uname + "'";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            //Das Result set wird in einen String gespeichert
            while (result.next()) {
                dbanswer = result.getString("password");
            }
            //falls Username existiert und ein passwort von der DB zurück kommt wird dieses im "try" verglichen.
            //falls Username nicht existiert haben wir keine antwort, dies würde in einer nullPointerException resultieren, welche gecatcht wird.
            try {
                if (dbanswer.equals(upw)) {
                    conn.close();
                    return true;
                } else {
                    return false;
                }
            } catch (java.lang.NullPointerException exception) {
                return false;
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }

    //es wird ein neuer Account in der Datenbank angelegt
    public boolean accountCreate(String username, String password) {
        String newusername = username;
        String newpassword = password;

        //es wird versucht eine DB verbindung aufzubauen
        try (Connection conn = DriverManager.getConnection(url, userDB, passwordDB)) {
            System.out.println("Erfolgreich mit Datenbank verbunden!"); //Ausgeben
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("insert into user (username, password) values ('" + newusername + "', '" + newpassword + "')");
            conn.close();
            return true;

            //falls die verbindung nicht aufgebaut werden kann wird dies gecatcht
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return false;
    }

}
