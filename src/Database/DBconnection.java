package Database;
import JavaFX.Login;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBconnection {

    private String username;
    private String password = "1234";

    public void pw(){
        String url ="jdbc:mysql://localhost:3306/login?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin";
        String user = "root";
        String password = "1234";

        Login lg = new Login();

        String uname = getUsername();

        try(Connection conn = DriverManager.getConnection(url,user,password)){
            System.out.println("Erfolgreich mit Datenbank verbunden!");

            //Ausgeben
            String query = "Select password from data where username ='" + uname +"'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            int col = rs.getMetaData().getColumnCount();

            while(rs.next()){
                for(int i = 1; i <= col; i++){
                    setPassword(String.format(rs.getString(i)));
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
