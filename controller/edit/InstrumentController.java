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
import table.TInstrument;
import tools.CRUD;

import javax.swing.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static tools.Evaluation5.evaluation5;
import static tools.ScreenWidth.screenWidth.allWidth;
import static tools.StringProcess.removeSparator;

/**
 * Created by User on 25/01/2018.
 */
public class InstrumentController implements Initializable {

    @FXML private ComboBox<String> comboAspect;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Button btnUpdate;
    @FXML private TextArea textInstrument;
    @FXML private VBox vbox;
    @FXML private TextField fieldId;
    @FXML private TableView<TInstrument> table;
    @FXML private TableColumn<TInstrument, String> tId;
    @FXML private TableColumn<TInstrument, String> tInstrument;
    @FXML private TableColumn<TInstrument, String> tAspek;


    private ObservableList<TInstrument> data;

    private String idT, instrumentT, aspectT;

    CRUD crud = new CRUD();

    private ContextMenu contextMenu = new ContextMenu();
    private MenuItem menuUpdate = new MenuItem("Update Aspect          ");
    private MenuItem menuDelete = new MenuItem("Delete Aspect ");


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vbox.setMinWidth(allWidth()-40);
        setComboAspect();
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
        String SQL = "SELECT instrumen.id, instrument, aspect.id AS 'aspek_id', aspect.aspect FROM instrumen\n" +
                "INNER JOIN aspect on aspect.id = instrumen.id_aspect\n" +
                "ORDER BY instrumen.id ASC";
        ResultSet rs = crud.simpleShow(SQL);
        try {
            while(rs.next()){
                data.add(new TInstrument(
                        rs.getString("id"),
                        rs.getString("instrument"),
                        rs.getString("aspek_id")+"- "+rs.getString("aspect")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //set Cell Table Vactory
        tId.setCellValueFactory(new PropertyValueFactory<>("satu"));
        tInstrument.setCellValueFactory(new PropertyValueFactory<>("dua"));
        tAspek.setCellValueFactory(new PropertyValueFactory<>("tiga"));

        table.setItems(null);
        table.setItems(data);
    }

    private void store(){
        btnSave.setOnAction(event -> {
            if(validation()){
                //do store data
                //get ID aspect for foreign key
                if(idExist(fieldId.getText())){
                    String[] idAspect = removeSparator(comboAspect.getValue(), "-");

                    String SQL = "INSERT INTO instrumen VALUES ('"+fieldId.getText()+"','"+textInstrument.getText()+"', '"+idAspect[0]+"')";
                    if(crud.simpleStore(SQL)){
                        setEmpty();
                        show();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Sorry! Id Already Registered");
                    fieldId.setFocusTraversable(true);
                }

            } else {
                JOptionPane.showMessageDialog(null, "All field must be filled");
            }
        });
    }

    private void update(){
        getTable();
        menuUpdate.setOnAction(event -> {
            textInstrument.setText(instrumentT);
            comboAspect.setValue(aspectT);
            fieldId.setText(idT);
            editMode();
        });
        btnUpdate.setOnAction(event -> {
            String[] idAspect = removeSparator(comboAspect.getValue(), "-");
            String SQL = "UPDATE instrumen SET instrument = '"+ textInstrument.getText() +"', id_aspect = '"+ idAspect[0] +"'" +
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
            String SQL = "DELETE FROM instrumen WHERE id = '"+ idT +"'";
            if(crud.simpleDelete(SQL, idT))
                show();
        });
    }

    private void getTable(){
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TInstrument>() {
            @Override
            public void changed(ObservableValue<? extends TInstrument> observable, TInstrument oldValue, TInstrument newValue) {
                if(table.getSelectionModel().getSelectedItems() != null){
                    idT = table.getSelectionModel().getSelectedItem().getSatu();
                    instrumentT = table.getSelectionModel().getSelectedItem().getDua();
                    aspectT = table.getSelectionModel().getSelectedItem().getTiga();
                }
            }
        });
    }
    private Boolean validation(){
        if(!textInstrument.getText().equals("") &&
                !fieldId.getText().equals("") &&
                !comboAspect.getValue().equals("")
                ){
            return true;
        }
        return false;
    }
    private void setEmpty(){
        textInstrument.setText("");
        comboAspect.setValue("");
        fieldId.setText("");
    }
    private void setComboAspect(){
        ArrayList<String> combo = new ArrayList<String>();
        String SQL = "SELECT id, aspect FROM aspect ORDER BY id ASC";
        ResultSet rs = crud.simpleShow(SQL);
        try {
            while(rs.next()){
                combo.add(rs.getString("id")+"- "+rs.getString("aspect"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ObservableList<String> limit = FXCollections.observableArrayList(
                combo
        );
        comboAspect.getItems().addAll(limit);
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
        String SQL = "SELECT COUNT(id) FROM instrumen WHERE id = '"+id+"'";
        ResultSet rs = crud.simpleShow(SQL);
        String count = "";
        try {
            if(rs.next()){
                count = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("nilai count : "+ count);
        if(count.equals("0"))
            return true;

        return false;
    }

}
