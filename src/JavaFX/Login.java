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
import javafx.stage.Stage;
import socket.client.SwsClient;

public class Login extends Application  {

    //Alle Layout elemente
    Button btnLogin1, btnBack3, btnRegister1, btnSend2, btnRegister3, btnUserLogout2 ;
    Label lblTitle1, lblTitle2, lblTitle3, lblPassword1, lblPassword31, lblPassword32, lblUsername1, lblUsername3, lblFail1;
    PasswordField txtPassword1, txtPassword31, txtPassword32;
    TextField txtUsername1, txtUsername3;
    Stage window;
    Scene scene1,scene2, scene3;
    String tmp, userName;
    static TextArea outputArea2;
    private SwsClient client;

    @Override
    public void start(Stage PrimaryStage) throws Exception {
        DBconnection db = new DBconnection();

        //Mainstage
        window = PrimaryStage;

        //Scene 1 (Login Fenster)
        GridPane layout1 = new GridPane();
        scene1 = new Scene(layout1, 400,400);
        layout1.setPadding(new Insets(10));
        lblFail1 = new Label("Username oder Passwort falsch");
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

        btnLogin1 = new Button("Login");
        btnLogin1.setFont(new Font(15));

        btnRegister1 = new Button("Registrieren");
        btnRegister1.setFont(new Font(15));

        //Elemente dem Layout 1 (Loginscreen) hinzufügen
        layout1.add(btnRegister1, 1,4);
        layout1.add(lblTitle1 ,0,0, 2,1);
        layout1.add(lblUsername1,0,1);
        layout1.add(txtUsername1,1,1);
        layout1.add(lblPassword1,0,2);
        layout1.add(txtPassword1,1,2);
        layout1.add(btnLogin1,1,3);


        //Scene 2 (Messenger Fenster)
        GridPane layout2 = new GridPane();
        layout2.setPadding(new Insets(10));
        scene2 = new Scene(layout2, 500,600);
        TextField userInput2 = new TextField();
        userInput2.setPromptText("Nachricht eingeben");

        btnUserLogout2 = new Button("Logout");
        btnUserLogout2.setFont(new Font(15));
        btnUserLogout2.setPadding(new Insets(10));

        outputArea2 = new TextArea();
        outputArea2.setEditable(false);

        btnSend2 = new Button("Send");
        btnSend2.setPadding(new Insets(10));
        btnSend2.setFont(new Font(15));

        //Elemente dem Layout 2 (MessengerScreen) hinzufügen
        layout2.add(outputArea2, 1,2);
        layout2.add(userInput2, 1, 3);
        layout2.add(btnSend2,1,4);
        layout2.add(btnUserLogout2,1,6);



        //Scene 3 (Registrierungs Fenster)
        GridPane layout3 = new GridPane();
        layout3.setPadding(new Insets(10));
        scene3 = new Scene(layout3, 400,400);

        lblTitle3 = new Label("Registrierung");
        lblTitle3.setFont(new Font(20));
        lblTitle3.setPadding(new Insets(10));

        lblUsername3 = new Label("Neuer Username");
        lblUsername3.setPadding(new Insets(10));

        lblPassword31 = new Label("Password ");
        lblPassword31.setPadding(new Insets(10));

        lblPassword32 = new Label("Nochmals Password");
        lblPassword32.setPadding(new Insets(10));

        btnBack3 = new Button("Zurück");
        btnBack3.setFont(new Font(15));

        btnRegister3 = new Button("Account erstellen");
        btnRegister3.setFont(new Font(15));

        txtUsername3 = new TextField();
        txtUsername3.setPromptText("Username...");
        txtUsername3.setPadding(new Insets(10));

        txtPassword31 = new PasswordField();
        txtPassword31.setPromptText("Password...");
        txtPassword31.setPadding(new Insets(10));

        txtPassword32 = new PasswordField();
        txtPassword32.setPromptText("Password...");
        txtPassword32.setPadding(new Insets(10));

        //Elemente dem Layout 3 (Registration) hinzufügen
        layout3.add(lblTitle3 ,0,0, 2,1);
        layout3.add(lblUsername3,0,1);
        layout3.add(txtUsername3,1,1);
        layout3.add(lblPassword31,0,2);
        layout3.add(txtPassword31,1,2);
        layout3.add(lblPassword32,0,3);
        layout3.add(txtPassword32,1,3);
        layout3.add(btnBack3,1,5);
        layout3.add(btnRegister3, 1,4);

        window.setScene(scene1);
        window.setTitle("SWS Messenger");
        window.show();

        //Diverse Button Action

        //Loginbutton zum Einloggen (DB abfrage wird durchgeführt)
        btnLogin1.setOnAction(e -> {
            //db.setUsername(txtUsername1.getText());
            String username = txtUsername1.getText();
            String password = txtPassword1.getText();
            Boolean dbCkeck = db.pw(username, password);
            if(dbCkeck) {
                setUserName(username);
                client = new SwsClient(username, "localhost", 1312);
                lblTitle2 = new Label("Hallo " + username +", schreibe eine Nachricht");
                lblTitle2.setFont(new Font(20));
                lblTitle2.setPadding(new Insets(10));
                layout2.add(lblTitle2,1,1);
                window.setScene(scene2);
            }else{
                layout1.add(lblFail1, 1,5);
            }
        });

        //Registrierungsfenster wird geöffent
        btnRegister1.setOnAction(e -> window.setScene(scene3));

        //Nachricht wird Abgeschickt
        btnSend2.setOnAction(e->{
            tmp = userInput2.getText();
            tmp = userName + "; " + tmp;
            client.send(tmp);
            userInput2.setText("");
        });

        //Logout von Messenger, weiterleitung auf LoginScreen, gespeichertes Passwort wird gelöscht
        btnUserLogout2.setOnAction(e->{
            client.send("\\dis:"+ userName);
            System.out.println("\\dis:"+ userName);
            window.setScene(scene1);
            txtPassword1.setText("");
            txtPassword1.setPromptText("Password...");
        });

        //Kehrt vom Registrierungsfenster zurück zum Login Screen
        btnBack3.setOnAction(e -> window.setScene(scene1));

        //User wird in der DB registriert und zu Messenger weitergeleitet
        btnRegister3.setOnAction(e -> {
            String username3 = txtUsername3.getText();
            String password31 = txtPassword31.getText();
            String password32 = txtPassword32.getText();
            if(password31.equals(password32)){
                db.accountCreate(username3, password31);
                client = new SwsClient(username3, "localhost", 1312);
                lblTitle2 = new Label("Hallo " + username3 +", schreibe eine Nachricht");
                lblTitle2.setFont(new Font(20));
                lblTitle2.setPadding(new Insets(10));
                layout2.add(lblTitle2,1,1);
                window.setScene(scene2);
            }
            else {
                System.out.println("Passwörter stimmen nicht überein");
            }
        });
    }

    //Nachricht wird im Messenger ausgegeben
    public static void printConsole(String message){
       try{
           outputArea2.setText(outputArea2.getText()+message+"\n");
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
