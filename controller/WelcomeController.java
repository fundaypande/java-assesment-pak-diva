package controller;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import table.TResult;
import table.TWelcomeResult;
import tools.CRUD;
import tools.GetData;
import tools.Logout;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static controller.LoginController.level;
import static controller.LoginController.name;
import static controller.LoginController.username;
import static tools.ScreenWidth.screenWidth.allWidth;

public class WelcomeController implements Initializable {

    @FXML private Label lblAlert;
    @FXML private Button btnOut;

    @FXML private TableView<TWelcomeResult> table;
    @FXML private TableColumn<TWelcomeResult, String> tPosition;
    @FXML private TableColumn<TWelcomeResult, String> tDate;
    @FXML private TableColumn<TWelcomeResult, String> tComponent;
    @FXML private TableColumn<TWelcomeResult, String> tConstraint;
    @FXML private TableColumn<TWelcomeResult, String> tRecomendation;
    @FXML private Button btnKiri;
    @FXML private Button btnKanan;
    @FXML private AnchorPane anchor;

    Logout logout = new Logout();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnOut.setOnAction(event -> logout.logout(btnOut));
        anchor.setPrefWidth(allWidth()-20);
        //level 1 = admin
        lblAlert.setText("Welcome " + name() + ", you signed in as ''" + cluster()+"''");
    }

    private String cluster(){
        if(level().equals("1")) return "Admin";
        return "Member";
    }

}
