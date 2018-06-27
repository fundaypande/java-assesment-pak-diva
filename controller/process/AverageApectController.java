package controller.process;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import table.TAspect;
import table.TAverageAspect;
import tools.CRUD;
import tools.GetData;

import javax.swing.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static tools.ScreenWidth.screenWidth.allWidth;

/**
 * Created by User on 27/01/2018.
 */
public class AverageApectController implements Initializable {

    @FXML private VBox vBox;
    @FXML private Label labelWarning;
    @FXML private Label label_totalKoresponden;
    @FXML private Label label_tanggal;
    @FXML private Button btnProses;
    @FXML private HBox hBoxMaster;
    @FXML private VBox vBox1;
    @FXML private TableView<TAverageAspect> table;
    @FXML private TableColumn<TAverageAspect, String> tId;
    @FXML private TableColumn<TAverageAspect, String> tAspect;
    @FXML private TableColumn<TAverageAspect, String> tAverage;

    private ObservableList<TAverageAspect> data;

    private String idT, aspectT, evaluationT;
    HashMap<String, Double> hashMapBawah = new HashMap<>();
    HashMap<String, String> hasMapAtas = new HashMap<>();

    CRUD crud = new CRUD();
    GetData getData = new GetData();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vBox.setMinWidth(allWidth()-20);
        vBox1.setMinWidth(allWidth()-20);

        processData();

        update();
        label_totalKoresponden.setText("Total Correspondent : "+ getData.getCorrespondent());
        label_tanggal.setText("Questionnaire Start From : " + getData.getStartDate() + " Until Now");
    }

    private void show(){
        data = FXCollections.observableArrayList();
        String SQL = "SELECT id, aspect, average_instrumen FROM aspect ORDER BY id ASC";
        ResultSet rs = crud.simpleShow(SQL);
        try {
            while(rs.next()){
                data.add(new TAverageAspect(
                        rs.getString("id"),
                        rs.getString("aspect"),
                        rs.getString("average_instrumen")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //set Cell Table Vactory
        tId.setCellValueFactory(new PropertyValueFactory<>("satu"));
        tAspect.setCellValueFactory(new PropertyValueFactory<>("dua"));
        tAverage.setCellValueFactory(new PropertyValueFactory<>("tiga"));

        table.setItems(null);
        table.setItems(data);
    }

    private void update(){
        btnProses.setOnAction((ActionEvent event) -> {
            processData();
                System.out.println("Ini hasilnyaaa :");
                if(getData.getCorrespondent() == 0){
                    System.out.println("Harusnya disini " +getData.getCorrespondent());
                    JOptionPane.showMessageDialog(null, "No Correspondent Data yet");
                } else {
                    for(Map.Entry map : hasMapAtas.entrySet()){
                        if(crud.simpleStore((String) map.getValue())){
                            System.out.println("Berhasil");
                        }
                    }
                    System.out.println(getData.getCorrespondent());
                    JOptionPane.showMessageDialog(null, "Data Successfully in Input");
                    show();
                }
        });
    }

    //what we do
    //1. ambil data jumlah aspect dibagi dengan total koresponden lalu tampilkan

    public void processData(){

        //get pembagi bawah
        String SQL = "SELECT id_aspect ,count(id_aspect) FROM instrumen\n" +
                "GROUP BY id_aspect\n" +
                "ORDER BY id_aspect ASC\n";
        ResultSet rs = crud.simpleShow(SQL);
        try {
            while(rs.next()){
                hashMapBawah.put(rs.getString("id_aspect"), rs.getDouble("count(id_aspect)"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

            //get pembagi atas
            String SQL2 = "SELECT id_aspect , SUM(instrument_user.ratting)\n" +
                    "FROM instrument_user\n" +
                    "  INNER JOIN instrumen ON instrumen.id = instrument_user.id_instrument\n" +
                    "  INNER JOIN aspect ON aspect.id = instrumen.id_aspect\n" +
                    "GROUP BY aspect.aspect\n" +
                    "ORDER BY id_aspect ASC";
            ResultSet rs2 = crud.simpleShow(SQL2);
            try {
                while(rs2.next()){
                    double value = rs2.getDouble("SUM(instrument_user.ratting)")/hashMapBawah.get(rs2.getString("id_aspect"));
                    String id = rs2.getString("id_aspect");
                    hasMapAtas.put(rs2.getString("id_aspect"), "UPDATE aspect SET average_instrumen = '"+value+"' WHERE aspect.id = '"+id+"'\n");

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        for(Map.Entry map : hasMapAtas.entrySet()){
            System.out.println("Ini adalah hasilnya : "+ map.getValue());
        }
    }


}
