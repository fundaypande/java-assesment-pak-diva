package controller.edit;

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
import table.TAspect;
import table.TRating;
import tools.CRUD;
import tools.Evaluation5;

import javax.swing.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static tools.Evaluation5.evaluation5;
import static tools.ScreenWidth.screenWidth.allWidth;

/**
 * Created by funday on 25/01/2018.
 */
public class AspectController implements Initializable {

    @FXML private ComboBox<String> comboEvaluation;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Button btnUpdate;
    @FXML private TextField fieldAspect;
    @FXML private TextField fieldId;
    @FXML private VBox vbox;
    @FXML private TableView<TAspect> table;
    @FXML private TableColumn<TAspect, String> tId;
    @FXML private TableColumn<TAspect, String> tAspect;
    @FXML private TableColumn<TAspect, String> tEvaluation;

    private ObservableList<TAspect> data;

    private String idT, aspectT, evaluationT;

    CRUD crud = new CRUD();

    private ContextMenu contextMenu = new ContextMenu();
    private MenuItem menuUpdate = new MenuItem("Update Aspect          ");
    private MenuItem menuDelete = new MenuItem("Delete Aspect ");


    @Override public void initialize(URL location, ResourceBundle resources) {
        setComboEvaluation();
        vbox.setMinWidth(allWidth()-20);
        show();
        store();
        update();
        setRightClick();
        cancelClick();
        delete();
        
        menuDelete.setStyle("-fx-text-fill: #282828");
        menuUpdate.setStyle("-fx-text-fill: #282828");
    }

    private void show(){
        data = FXCollections.observableArrayList();
        String SQL = "SELECT * FROM aspect ORDER BY id ASC";
        ResultSet rs = crud.simpleShow(SQL);
        try {
            while(rs.next()){
                data.add(new TAspect(
                        rs.getString("id"),
                        rs.getString("aspect"),
                        rs.getString("evaluation")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //set Cell Table Vactory
        tId.setCellValueFactory(new PropertyValueFactory<>("satu"));
        tAspect.setCellValueFactory(new PropertyValueFactory<>("dua"));
        tEvaluation.setCellValueFactory(new PropertyValueFactory<>("tiga"));

        table.setItems(null);
        table.setItems(data);
    }

    private void store(){
        btnSave.setOnAction(event -> {
            if(validation()){
                //do store data
                if(idExist(fieldId.getText())){
                    String SQL = "INSERT INTO aspect (id, aspect, evaluation) VALUES ('"+fieldId.getText()+"','"+fieldAspect.getText()+"', '"+comboEvaluation.getValue()+"')";
                    if(crud.simpleStore(SQL)){
                        setEmpty();
                        show();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "All field must be filled");
            }
        });
    }

    private void update(){
        getTable();
        menuUpdate.setOnAction(event -> {
            fieldAspect.setText(aspectT);
            comboEvaluation.setValue(evaluationT);
            fieldId.setText(idT);
            editMode();
        });
        btnUpdate.setOnAction(event -> {
            String SQL = "UPDATE aspect SET aspect = '"+ fieldAspect.getText() +"', evaluation = '"+ comboEvaluation.getValue() +"'" +
                    " WHERE" +
                    " id = '"+ idT +"'";
            if(validation()) {
                if (crud.simpleUpdate(SQL)) {
                    show();
                    normalMode();
                    setEmpty();
                }
            } else
                JOptionPane.showMessageDialog(null, "All field must be filled");

        });

    }

    private void delete(){
        getTable();
        menuDelete.setOnAction(event -> {
            String SQL = "DELETE FROM aspect WHERE id = '"+ idT +"'";
            if(crud.simpleDelete(SQL, idT))
                show();
        });
    }

    private void getTable(){
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TAspect>() {
            @Override
            public void changed(ObservableValue<? extends TAspect> observable, TAspect oldValue, TAspect newValue) {
                if(table.getSelectionModel().getSelectedItems() != null){
                    idT = table.getSelectionModel().getSelectedItem().getSatu();
                    aspectT = table.getSelectionModel().getSelectedItem().getDua();
                    evaluationT = table.getSelectionModel().getSelectedItem().getTiga();
                }
            }
        });
    }
    private void setComboEvaluation(){
        ArrayList<String> combo = new ArrayList<String>();
        ObservableList<String> limit = FXCollections.observableArrayList(
                evaluation5()
        );
        comboEvaluation.getItems().addAll(limit);
    }
    private Boolean validation(){
        if(!fieldAspect.getText().equals("") &&
                !comboEvaluation.getValue().equals("")
                ){
            return true;
        }

        return false;
    }
    private void setEmpty(){
        fieldAspect.setText("");
        comboEvaluation.setValue("");
        fieldId.setText("");
    }
    private void editMode(){
        btnUpdate.setVisible(true);
        btnSave.setVisible(false);
        fieldId.setDisable(true);
    }
    private void normalMode(){
        btnUpdate.setVisible(false);
        btnSave.setVisible(true);
        fieldId.setDisable(false);
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
    private void cancelClick(){
        btnCancel.setOnAction(event -> {
            normalMode();
            setEmpty();
        });
    }
    private Boolean idExist(String id){
        String SQL = "SELECT COUNT(id) FROM aspect WHERE id = '"+id+"'";
        ResultSet rs = crud.simpleShow(SQL);
        String count = "";
        try {
            if(rs.next()){
                count = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Value count : "+ count);
        if(count.equals("0"))
            return true;

        JOptionPane.showMessageDialog(null, "Sorry! ID Already used");
        return false;
    }
}
