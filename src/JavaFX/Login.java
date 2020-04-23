package JavaFX;

import Database.DBconnection;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import socket.client.SwsClient;
import socket.server.ClientInfos;

import java.awt.*;
import java.net.DatagramPacket;

public class Login extends Application  {


    //layout1
    Button btnLogin1;
    Label lblTitle1;
    Label lblUsername1;
    Label lblPassword1;
    Label lblFail1;
    PasswordField txtPassword1;
    TextField txtUsername1;
    //layout2
    Button btnSend;
    Button btnRegister1;
    Label lblTitle2;
    Stage window;
    Scene scene1,scene2, scene3;
    String tmp;
    String userName;
    Button userLogout;
    static TextArea outputArea;

    private SwsClient client;

    /*public Login(){
        client = new SwsClient("HalloWelt","localhost", 2345);
    }
*/

    @Override
    public void start(Stage PrimaryStage) throws Exception {

        DBconnection db = new DBconnection();

        //Allgemeines FX
        window = PrimaryStage;

        GridPane layout1 = new GridPane();
        GridPane layout2 = new GridPane();
        GridPane layout3 = new GridPane();
        layout1.setPadding(new Insets(10));
        layout2.setPadding(new Insets(10));
        layout3.setPadding(new Insets(10));
        scene1 = new Scene(layout1, 300,300);
        scene2 = new Scene(layout2, 500,600);

        //Layout1
        btnLogin1 = new Button("Login");
        btnLogin1.setFont(new Font(15));
        btnLogin1.setOnAction(e -> {

            db.setUsername(txtUsername1.getText());
            String username = txtUsername1.getText();
            String password = txtPassword1.getText();
            Boolean dbCkeck = db.pw(username, password);
            if(dbCkeck) {
                userName = username;
                setUserName(username);
                client = new SwsClient(userName, "localhost", 1312);
                lblTitle2 = new Label("Hallo " + userName +", schreibe eine Nachricht");
                lblTitle2.setFont(new Font(20));
                lblTitle2.setPadding(new Insets(10));
                layout2.add(lblTitle2,1,1);
                window.setScene(scene2);

            }else{
                layout1.add(lblFail1, 1,4);
            }
        });

        lblFail1 = new Label("Falsches Passwort");
        lblFail1.setFont(new Font(10));
        lblFail1.setStyle("-fx-background-color: red");
        lblTitle1 = new Label("Loginformular");
        lblTitle1.setFont(new Font(20));
        lblTitle1.setPadding(new Insets(10));

        lblUsername1 = new Label("Username");
        lblUsername1.setPadding(new Insets(10));

        lblPassword1 = new Label("Password");
        lblPassword1.setPadding(new Insets(10));

        txtUsername1 = new TextField();
        txtUsername1.setPromptText("Username...");
        txtUsername1.setPadding(new Insets(10));

        txtPassword1 = new PasswordField();
        txtPassword1.setPromptText("Password...");
        txtPassword1.setPadding(new Insets(10));



        layout1.setPadding(new Insets(10));
        layout2.setPadding(new Insets(10));
        btnRegister1 = new Button("Registrieren");
        btnRegister1.setFont(new Font(15));
        layout1.add(btnRegister1, 1,4);

        layout1.add(lblTitle1 ,0,0, 2,1);
        layout1.add(lblUsername1,0,1);
        layout1.add(txtUsername1,1,1);
        layout1.add(lblPassword1,0,2);
        layout1.add(txtPassword1,1,2);
        layout1.add(btnLogin1,1,3);
        //layout1.add(lblfail,1,4);

        //Scene 2
        TextField inputName = new TextField();
        TextField userInput = new TextField();
        userInput.setPromptText("Nachricht eingeben");
        userLogout = new Button("Logout");
        userLogout.setOnAction(e->{
            client.send("\\dis:"+ userName);
        });
        outputArea = new TextArea();



        outputArea.setEditable(false);
        btnSend = new Button("Send");
        btnSend.setOnAction(e->{
            tmp = userInput.getText();
            tmp = userName + "; " + tmp;
            client.send(tmp);
            userInput.setText("");
        });
        layout2.add(outputArea, 1,2);
        layout2.add(userInput, 1, 3);
        layout2.add(btnSend,1,4);
        layout2.add(userLogout,1,6);


        window.setScene(scene1);
        window.setTitle("SWS Messenger");
        window.show();

        scene3 = new Scene(layout3, 300,300);

    }

    public static void printConsole(String message){
       try{
           outputArea.setText(outputArea.getText()+message+"\n");
       }catch (Exception e){e.printStackTrace();}

    }

    public static void main(String[] args) {
        launch(args);
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
