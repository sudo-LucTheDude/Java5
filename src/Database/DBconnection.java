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
    private String user = "root";
    private String dbpassword = "1234";

    public void pw(){

        Login lg = new Login();
        String uname = getUsername();

        try(Connection conn = DriverManager.getConnection(url,user,password)){
            System.out.println("Erfolgreich mit Datenbank verbunden!");

            //Ausgeben
            String query = "Select password from user where username ='" + uname +"'";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            int col = result.getMetaData().getColumnCount();

            while(result.next()){
                for(int i = 1; i <= col; i++){
                    setPassword(result.getString(i));
                }
            }

        }catch(SQLException ex){
            System.err.println(ex.getMessage());
        }

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
