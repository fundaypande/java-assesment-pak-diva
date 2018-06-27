package controller.edit;

import connection.Connection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.VBox;
import table.AddUser;
import table.TRating;
import tools.CRUD;

import javax.swing.*;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static tools.ScreenWidth.screenWidth.allWidth;

/**
 * Created by User on 25/01/2018.
 */
public class RatingController implements Initializable {

    @FXML private TextField fieldScore;
    @FXML private TextField fieldRating;

    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Button btnUpdate;
    @FXML private VBox vbox;
    @FXML private TableView<TRating> table;
    @FXML private TableColumn<TRating, String> tId;
    @FXML private TableColumn<TRating, String> tRating;
    @FXML private TableColumn<TRating, String> tScore;

    private ObservableList<TRating> data;

    private String interestT, scoreT;

    CRUD crud = new CRUD();

    private ContextMenu contextMenu = new ContextMenu();
    private MenuItem menuUpdate = new MenuItem("Update Rating          ");
    private MenuItem menuDelete = new MenuItem("Delete Rating ");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vbox.setMinWidth(allWidth()-20);
        show();
        store();
        setRightClick();
        delete();
        update();
        cancelClick();
        menuDelete.setStyle("-fx-text-fill: #282828");
        menuUpdate.setStyle("-fx-text-fill: #282828");
    }

    private void show(){
        data = FXCollections.observableArrayList();
        String SQL = "SELECT * FROM rating ORDER BY score ASC";
        ResultSet rs = crud.simpleShow(SQL);

        try {
            while(rs.next()){
                data.add(new TRating(
                        rs.getString("interest"),
                        rs.getString("score")
                    ));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //set Cell Table Vactory
        tRating.setCellValueFactory(new PropertyValueFactory<>("satu"));
        tScore.setCellValueFactory(new PropertyValueFactory<>("dua"));

        table.setItems(null);
        table.setItems(data);
    }

    private void store(){
        btnSave.setOnAction(event -> {
            if(validation()){
                    //do store data
                    String SQL = "INSERT INTO rating VALUES ('"+fieldRating.getText()+"','"+fieldScore.getText()+"')";
                    if(crud.simpleStore(SQL)){
                        setEmpty();
                        show();
                    }
            } else {
                JOptionPane.showMessageDialog(null, "All fields are required");
            }
        });
    }

    private void delete(){
        getTable();
        menuDelete.setOnAction(event -> {
            String SQL = "DELETE FROM rating WHERE interest = '"+ interestT +"'";
            if(crud.simpleDelete(SQL, interestT))
                show();
        });
    }

    private void update(){
        getTable();
        menuUpdate.setOnAction(event -> {
            fieldScore.setText(scoreT);
            fieldRating.setText(interestT);
            editMode();
        });
        btnUpdate.setOnAction(event -> {
            String SQL = "UPDATE rating SET score = '"+ fieldScore.getText() +"' WHERE" +
                    " interest = '"+ fieldRating.getText() +"'";
            if(crud.simpleUpdate(SQL)){
                show();
                normalMode();
                setEmpty();
            }
        });

    }

    private void getTable(){
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TRating>() {
            @Override
            public void changed(ObservableValue<? extends TRating> observable, TRating oldValue, TRating newValue) {
                if(table.getSelectionModel().getSelectedItems() != null){
                    interestT = table.getSelectionModel().getSelectedItem().getSatu();
                    scoreT = table.getSelectionModel().getSelectedItem().getDua();
                }
            }
        });
    }

    private Boolean validation(){
        if(!fieldScore.getText().equals("") &&
                !fieldRating.getText().equals("")
                ){
            return true;
        }

        return false;
    }
    private void setEmpty(){
        fieldScore.setText("");
        fieldRating.setText("");
    }
    private void setRightClick(){
        table.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

            @Override
            public void handle(ContextMenuEvent event) {
                contextMenu.show(table, event.getScreenX(), event.getScreenY());
            }
        });
        contextMenu.getItems().addAll(menuUpdate, menuDelete);
    }
    private void editMode(){
        btnUpdate.setVisible(true);
        btnSave.setVisible(false);
        fieldRating.setDisable(true);
    }
    private void normalMode(){
        btnUpdate.setVisible(false);
        btnSave.setVisible(true);
        fieldRating.setDisable(false);
    }
    private void cancelClick(){
        btnCancel.setOnAction(event -> {
            normalMode();
            setEmpty();
        });
    }
}
