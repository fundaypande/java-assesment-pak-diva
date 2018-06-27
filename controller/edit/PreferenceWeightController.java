package controller.edit;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.DefaultStringConverter;
import table.TAspect;
import table.TAverage;
import table.TInputInstrument;
import table.TPreference;
import tools.CRUD;
import tools.GetData;

import javax.swing.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static controller.LoginController.username;
import static tools.ScreenWidth.screenWidth.allWidth;
import static tools.StringProcess.removeSparator;

/**
 * Created by funday on 26/01/2018.
 */
public class PreferenceWeightController implements Initializable {

    @FXML private VBox vBox;
    @FXML private Label labelWarning;
    @FXML private TableView<TPreference> table;
    @FXML private TableColumn<TPreference, String> tId;
    @FXML private TableColumn<TPreference, String> tAspect;
    @FXML private TableColumn<TPreference, String> tRating;
    @FXML private Button btnProses;
    @FXML private ListView<String> listView;

    @FXML private HBox hBoxMaster;
    @FXML private VBox vBox1;
    @FXML private VBox vBox2;
    @FXML private VBox vBox3;

    @FXML private TableView<TAverage> tableAverage;
    @FXML private TableColumn<TAverage, String> tAverage;


    CRUD crud = new CRUD();
    GetData getData = new GetData();
    private String idT, aspectT, ratingT;
    private int sumRating = 0;

    HashMap<String, String> hashMapRating = new HashMap<>();

    private ObservableList<TPreference> data;
    private ObservableList<TAverage> dataAverage;

    private ObservableList<String> masterData3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        masterData3 = FXCollections.observableArrayList();

        vBox.setMinWidth(allWidth()-20);
        hBoxMaster.setMinWidth(allWidth()-40);
        vBox1.setMinWidth((allWidth()*0.6)-5);
        vBox2.setMinWidth((allWidth()*0.10)-5);
        vBox3.setMinWidth((allWidth()*0.25)-5);
        setComboRating();
        processClicked();
        getTable();
        show();
        getRating();
        tRating.setText("Rating ( "+String.valueOf(getSumRating())+" )");
    }

    private void show(){

        data = FXCollections.observableArrayList();
        String SQL = "SELECT id, aspect, rating FROM aspect ORDER BY id ASC";
        ResultSet rs = crud.simpleShow(SQL);
        int i = 0;
        try {
            while(rs.next()){
                data.add(new TPreference(
                        rs.getString("id"),
                        rs.getString("aspect"),
                        rs.getString("rating")
                ));
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //set Cell Table Vactory
        tId.setCellValueFactory(new PropertyValueFactory<>("satu"));
        tAspect.setCellValueFactory(new PropertyValueFactory<>("dua"));
        tRating.setCellValueFactory(new PropertyValueFactory<>("tiga"));

        table.setItems(null);
        table.setItems(data);

    }

    private void getRating(){
        table.setEditable(true);

        tRating.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), masterData3));

        tRating.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TPreference, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TPreference, String> event) {
                getTable();

                String[] rating = removeSparator(event.getNewValue(), "-");
                String dataSQL = "UPDATE aspect SET rating = '"+rating[0]+"' WHERE id = '"+idT+"'";
                if (crud.simpleUpdate(dataSQL)) {
                    show();
                    tRating.setText("Rating ( "+String.valueOf(getSumRating())+" )");
                }
            }
        });
    }
    private void setComboRating(){
        String SQL = "SELECT score, interest FROM rating ORDER BY score";
        ResultSet rs = crud.simpleShow(SQL);
        try {
            while(rs.next()){
                masterData3.add(rs.getString("score")+"- "+rs.getString("interest"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void getTable(){
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TPreference>() {
            @Override
            public void changed(ObservableValue<? extends TPreference> observable, TPreference oldValue, TPreference newValue) {
                if(table.getSelectionModel().getSelectedItems() != null){
                    idT = table.getSelectionModel().getSelectedItem().getSatu();
                    aspectT = table.getSelectionModel().getSelectedItem().getDua();
                    ratingT = table.getSelectionModel().getSelectedItem().getTiga();
                }
            }
        });
    }

    //what we do
    //1. get all data rating to arraylist

    private void getRatingToList(){

        String SQL = "SELECT id, rating FROM aspect ORDER BY id ASC";
        ResultSet rs = crud.simpleShow(SQL);
        double getRating = 0;
        double sum = getSumRating();
        try {
            while(rs.next()){
                //do average
                getRating = rs.getInt("rating");
                double average = getRating/sum;
                String dataSQL = "UPDATE aspect SET average = '"+average+"' WHERE id = '"+rs.getString("id")+"'";
                hashMapRating.put(rs.getString("id"), dataSQL);
                System.out.println("data average : " + average);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //jangan gunakan arraylist, tapi gunakan Hasmap
    private void processClicked(){
        btnProses.setOnAction(event -> {
            //data
            System.out.println("SUM : "+ getSumRating());
            System.out.println("Jumlah Array : "+ hashMapRating.size());
            double getRating = 5;
            double sum = 20;
            double setup = getRating/sum;
            System.out.println("Percobaan pembagian : "+ setup);


            getRatingToList();
            System.out.println("Ini hasilnyaaa :");
            for(Map.Entry map : hashMapRating.entrySet()){
                if(crud.simpleStore((String) map.getValue())){
                    System.out.println("Berhasil");
                }
            }
            JOptionPane.showMessageDialog(null, "Data Succesfully Inputted");
            averageShow();
            tAverage.setText("Preference Weight ( 1 )");
        });
    }

    private Integer getSumRating(){
        String SQL = "SELECT SUM(rating) AS 'sum' FROM aspect ORDER BY id ASC";
        int sumRating = 0;
        ResultSet rs = crud.simpleShow(SQL);
        int i = 0;
        try {
            if(rs.next()){
                sumRating = rs.getInt("sum");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sumRating;
    }

    private void averageShow(){

        dataAverage = FXCollections.observableArrayList();
        String SQL = "SELECT average FROM aspect ORDER BY id ASC";
        ResultSet rs = crud.simpleShow(SQL);
        int i = 0;
        try {
            while(rs.next()){
                dataAverage.add(new TAverage(
                        rs.getString("average")
                ));
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //set Cell Table Vactory
        tAverage.setCellValueFactory(new PropertyValueFactory<>("satu"));

        tableAverage.setItems(null);
        tableAverage.setItems(dataAverage);

    }
}
