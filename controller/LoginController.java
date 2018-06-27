package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.paint.Color;
import connection.Connection;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static tools.ScreenWidth.screenWidth.allHeight;
import static tools.ScreenWidth.screenWidth.allWidth;

public class LoginController implements Initializable {


    //private TextField fieldUsername;
    //private PasswordField fieldPassword;
    @FXML private Button btnLogin;
    @FXML private Label warning;
    @FXML private JFXTextField username_field;
    @FXML private JFXPasswordField password_field;
    
    @FXML
    void click(ActionEvent event) {
        authentic();
    }

    private String userDb = "";
    private String passDb = "";
    private String levelDb = "";
    private String nameDb = "";

    private String password = "";
    private String user = "";

    static String username = "";
    static String level = "";
    static String name = "";

    private Connection db = Connection.getDbCon();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public static String username(){
        return username;
    }
    public static String level(){ return level; }
    public static String name(){ return name; }

    public void authentic(){
        user = username_field.getText();
        password = password_field.getText();
        try {
            //hash to MD5 algorithm
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes(), 0, password_field.getLength());
            password = new BigInteger(1, md.digest()).toString(16);
            System.out.println("Here's the Password : " + password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //language=GenericSQL
        String passSQL = "SELECT username, password, level, name FROM users WHERE username = '"+ user + "'";
        try {
            ResultSet rs = db.query(passSQL);
            if(rs.next()){
                passDb = (rs.getString(2));
                userDb = (rs.getString(1));
                levelDb = (rs.getString(3));
                nameDb = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(password);

        if((passDb.equals(password)) && (userDb.equals(user))){
            username = user;
            level = levelDb;
            name = nameDb;
            Stage stage = (Stage) btnLogin.getScene().getWindow();

            System.out.println("Login Successfull");

            PauseTransition delay8 = new PauseTransition(Duration.millis(1000));
            delay8.setOnFinished( event -> stage.close() );
            delay8.play();
            /*
             * Open main class*/
            PauseTransition delay9 = new PauseTransition(Duration.millis(500));
            delay9.setOnFinished( event -> {
                Stage primaryStage = new Stage();
                primaryStage.initStyle(StageStyle.UNDECORATED);

                try {
                    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/main.fxml"));
                    primaryStage.setTitle("Main Menu");
                    primaryStage.setScene(new Scene(root, allWidth(), allHeight(), Color.BLUE));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //primaryStage.setMaximized(true);

                primaryStage.show();
            } );
            delay9.play();
        } else {
            warning.setVisible(true);
        }
    }
}
