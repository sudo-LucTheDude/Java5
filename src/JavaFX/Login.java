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
    Button btnLogin1;
    Label lblTitle1;
    Label lblUsername1;
    Label lblPassword1;
    Label lblFail1;
    PasswordField txtPassword1;
    TextField txtUsername1;

    Button btnSend;
    Label lblTitle2;
    Stage window;
    Scene scene1,scene2;
    String tmp;
    String userName;
    Button btnApplyName;
    static TextArea outputArea;

    private SwsClient client;

    /*public Login(){
        client = new SwsClient("HalloWelt","localhost", 2345);
    }
*/

    @Override
    public void start(Stage PrimaryStage) throws Exception {

        DBconnection db = new DBconnection();
        window = PrimaryStage;

        GridPane layout1 = new GridPane();
        GridPane layout2 = new GridPane();

        btnLogin1 = new Button("Login");
        btnLogin1.setFont(new Font(15));

        btnLogin1.setOnAction(e -> {

            db.setUsername(txtUsername1.getText());
            String username = txtUsername1.getText();
            String password = txtPassword1.getText();
            Boolean dbCkeck = db.pw(username, password);
            if(dbCkeck == true) {
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
        scene1 = new Scene(layout1, 300,200);


        layout1.setPadding(new Insets(10));
        layout2.setPadding(new Insets(10));

        //btnSend.setOnAction(e -> window.setScene(scene1));

        layout1.add(lblTitle1 ,0,0, 2,1);
        layout1.add(lblUsername1,0,1);
        layout1.add(txtUsername1,1,1);
        layout1.add(lblPassword1,0,2);
        layout1.add(txtPassword1,1,2);
        layout1.add(btnLogin1,1,3);
        //layout1.add(lblfail,1,4);

        TextField inputName = new TextField();
        TextField userInput = new TextField();
        Label output = new Label();
        outputArea = new TextArea();
        lblTitle2 = new Label("Schreibe eine Nachricht");
        lblTitle2.setFont(new Font(20));
        lblTitle2.setPadding(new Insets(10));
        scene2 = new Scene(layout2, 500,800);
        btnApplyName = new Button("Apply");
        btnApplyName.setOnAction(e -> {
            userName = inputName.getText();
            client = new SwsClient(userName, "localhost", 1312);
        });

        btnSend = new Button("Send");
        btnSend.setOnAction(e->{
            tmp = userInput.getText();
            tmp = userName + "; " + tmp;
            client.send(tmp);
            userInput.setText("");
        });
        layout2.add(lblTitle2,1,1);
        layout2.add(outputArea, 1,2);
        layout2.add(userInput, 1, 3);
        layout2.add(btnSend,1,8);
        layout2.add(inputName, 1, 9);
        layout2.add(btnApplyName,1,10);
        layout2.add(output,1,11, 4,4);


        window.setScene(scene1);
        window.setTitle("SWS Messenger");
        window.show();

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
