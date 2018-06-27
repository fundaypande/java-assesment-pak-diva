package controller.file;

import connection.Connection;
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
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.converter.DefaultStringConverter;
import table.AddUser;
import table.TInputInstrument;
import table.TInstrument;
import tools.CRUD;
import tools.GetData;

import javax.swing.*;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static controller.LoginController.username;
import static tools.ScreenWidth.screenWidth.allWidth;
import static tools.StringProcess.removeSparator;

public class InputRatingController implements Initializable {

    @FXML private VBox vBox;
    @FXML private TableView<TInputInstrument> table;
    @FXML private TableColumn<TInputInstrument, String> tId;
    @FXML private TableColumn<TInputInstrument, String> tInstrument;
    @FXML private TableColumn<TInputInstrument, String> tRating;
    @FXML private TableColumn<TInputInstrument, String> tEvaluation;
    @FXML private TableColumn<TInputInstrument, String> tAspect;
    @FXML private Button btnProses;
    @FXML private Label labelWarning;

    CRUD crud = new CRUD();
    GetData getData = new GetData();

    private ObservableList<TInputInstrument> data;
    private String idT, instrumentT, ratingT;

    private ObservableList<String> masterData3;

    HashMap<String, String> hashMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        masterData3 = FXCollections.observableArrayList();
        setComboRating();
        checkUser();
        show();
        getTable();
        store();

        getRating();
        vBox.setMinWidth(allWidth()-40);

    }

    //what we do
    //1. cek apakah user itu sudah sempat mengisi form untuk periode bulan ini? (SOLVED)
    //2. memastikan bahwa user mengisi semua jawaban (SOLVER)
    //3. Notif jika user sudah pernah mingisi angket untuk periode ini
//    ArrayList<String> combo = new ArrayList<String>();


    private void show(){
        int counter = 0;
        data = FXCollections.observableArrayList();
        String SQL = "SELECT instrumen.id, aspect.evaluation, aspect.aspect, instrumen.instrument\n" +
                "FROM instrumen\n" +
                "  INNER JOIN aspect ON aspect.id = instrumen.id_aspect\n" +
                "ORDER BY evaluation";
        ResultSet rs = crud.simpleShow(SQL);
        int i = 0;
        try {
            while(rs.next()){
                data.add(new TInputInstrument(
                        rs.getString("id"),
                        rs.getString("instrument"),
                        "",
                        rs.getString("evaluation"),
                        rs.getString("aspect")
                ));
                i++;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.setMinHeight(i*34);
        System.out.println("Tingginya : " + i*34);
        //set Cell Table Vactory
        tId.setCellValueFactory(new PropertyValueFactory<>("satu"));
        tInstrument.setCellValueFactory(new PropertyValueFactory<>("dua"));
        tRating.setCellValueFactory(new PropertyValueFactory<>("tiga"));
        tEvaluation.setCellValueFactory(new PropertyValueFactory<>("empat"));
        tAspect.setCellValueFactory(new PropertyValueFactory<>("lima"));


        table.setItems(null);
        table.setItems(data);

    }

    private void store(){
        btnProses.setOnAction(event -> {
            if(isAllFiled()){
                System.out.println("Ini hasilnyaaa :");
                for(Map.Entry map : hashMap.entrySet()){
                    if(crud.simpleStore((String) map.getValue())){
                        System.out.println("Berhasil");
                    }
                }
                JOptionPane.showMessageDialog(null, "Data Successfuly in Input");
                checkUser();
            } else JOptionPane.showMessageDialog(null, "All Data must be Filled. What's New in Content : "+hashMap.size()+" - Data instrument : "+getData.getCountInstrument());
        });
    }


    private void checkUser(){
        if(alreadyPushData())
            alreadyMode();
        else normalMode();
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
    private void getRating(){
        table.setEditable(true);

        tRating.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), masterData3));

        tRating.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TInputInstrument, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TInputInstrument, String> event) {
                getTable();

                String[] rating = removeSparator(event.getNewValue(), "-");
                String dataSQL = "INSERT INTO instrument_user VALUES\n" +
                        "  (null,'"+idT+"', '"+username()+"','"+ rating[0] +"',DATETIME('now', 'localtime'))";
                hashMap.put(idT, dataSQL);
                //test();
            }
        });
    }
    private void getTable(){
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TInputInstrument>() {
            @Override
            public void changed(ObservableValue<? extends TInputInstrument> observable, TInputInstrument oldValue, TInputInstrument newValue) {
                if(table.getSelectionModel().getSelectedItems() != null){
                    idT = table.getSelectionModel().getSelectedItem().getSatu();
                    instrumentT = table.getSelectionModel().getSelectedItem().getDua();
                    ratingT = table.getSelectionModel().getSelectedItem().getTiga();
                }
            }
        });
    }
    private void normalMode(){
        table.setDisable(false);
        btnProses.setDisable(false);
        labelWarning.setVisible(false);
    }
    private void alreadyMode(){
        table.setDisable(true);
        btnProses.setDisable(true);
        labelWarning.setVisible(true);
    }
    private Boolean isAllFiled(){
        if(hashMap.size() == getData.getCountInstrument())
            return true;
        return false;
    }
    private Boolean alreadyPushData(){
        String SQL = "SELECT DISTINCT id_user FROM instrument_user WHERE id_user = '"+username()+"'";
        ResultSet rs = crud.simpleShow(SQL);
        try {
            if(rs.next()){
                System.out.println("TRUE Sudah pernah input data");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
