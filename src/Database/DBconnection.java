package Database;
import JavaFX.Login;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBconnection {

    private String username = "test";
    private String password = "1234";

    private String url ="jdbc:mysql://localhost:3306/desicionmaker?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin";
    private String userDB = "root";
    private String passwordDB = "1234";
    private String dbanswer;

    public boolean pw(String username, String password){

        Login lg = new Login();
        String uname = username;
        String upw = password;

        try(Connection conn = DriverManager.getConnection(url,userDB,passwordDB)){
            System.out.println("Erfolgreich mit Datenbank verbunden!");

            //Ausgeben
            String query = "Select password from user where username ='" + uname +"'";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            String[] arr = null;
            while (result.next()) {
                String em = result.getString("password");
                dbanswer = result.getString("password");
            }
            System.out.println(dbanswer);
            if (dbanswer.equals(upw)){
                return true;
            }

        }catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        return false;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
