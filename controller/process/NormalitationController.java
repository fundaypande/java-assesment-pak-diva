package controller.process;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import table.TAspect;
import table.TInputInstrument;
import table.TNormFirst;
import table.TNormValue;
import tools.CRUD;
import tools.GetData;

import javax.swing.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by User on 28/01/2018.
 */
public class NormalitationController implements Initializable {

    @FXML private VBox vBox;
    @FXML private Label labelWarning;
    @FXML private HBox hBoxMaster;
    @FXML private VBox vBox1;
    @FXML private TableView<TNormFirst> tableFirst;
    @FXML private TableColumn<TNormFirst, String> tWeight;
    @FXML private TableColumn<TNormFirst, String> tId;
    @FXML private TableColumn<TNormFirst, String> tAssesment;
    @FXML private TableColumn<TNormFirst, String> tPlanning;
    @FXML private TableColumn<TNormFirst, String> tImplementation;
    @FXML private TableColumn<TNormFirst, String> tImprove;
    @FXML private TableColumn<TNormFirst, String> tCertification;
    @FXML private VBox vBox2;
    @FXML private Button btnProses;
    @FXML private VBox vBox3;
    @FXML private TableView<TNormValue> tableOne;
    @FXML private TableColumn<TNormValue, String> tAssesment1;
    @FXML private TableColumn<TNormValue, String> tPlanning1;
    @FXML private TableColumn<TNormValue, String> tImplementation1;
    @FXML private TableColumn<TNormValue, String> tImprove1;
    @FXML private TableColumn<TNormValue, String> tCertification1;
    @FXML private TextField pAssesment;
    @FXML private TextField pPlanning;
    @FXML private TextField pImplementation;
    @FXML private TextField pImprovement;
    @FXML private TextField pCertification;
    @FXML private TextField vAssesment;
    @FXML private TextField vPlanning;
    @FXML private TextField vImplementation;
    @FXML private TextField vImprovement;
    @FXML private TextField vCertification;
    @FXML private Button btnSave;
    @FXML private TextField pTotal;


    HashMap<String, Double> hashAssesment = new HashMap<>();
    HashMap<String, Double> hashCertification = new HashMap<>();
    HashMap<String, Double> hashImplementation = new HashMap<>();
    HashMap<String, Double> hashPlanning = new HashMap<>();
    HashMap<String, Double> hashImprovement = new HashMap<>();

    HashMap<String, Double> hasilAssesment = new HashMap<>();
    HashMap<String, Double> hasilCertification = new HashMap<>();
    HashMap<String, Double> hasilImplementation = new HashMap<>();
    HashMap<String, Double> hasilPlanning = new HashMap<>();
    HashMap<String, Double> hasilImprovement = new HashMap<>();

    HashMap<String, Double> hashWeight = new HashMap<>();

    CRUD crud = new CRUD();
    GetData getData = new GetData();
    private ObservableList<TNormFirst> data;
    private ObservableList<TNormValue> dataValue;

    double vekAssesment = 0;
    double vekPlanning = 0;
    double vekImplementation = 0;
    double vekImprovement = 0;
    double vekCertification = 0;

    private double pAssesmentData = 1, pPlanningData = 1, pImplementationData = 1, pImprovementData = 1, pCertificationData = 1;
    private double total = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getDataToArray();
        show();
        processClicked();
        vektorSave();

        for(Map.Entry map : hashWeight.entrySet()){
            System.out.println("Ini datanya WEIGHT : "+ map.getKey() +" = "+ map.getValue());
        }
    }

    private void show(){
        getDataToArray();
        data = FXCollections.observableArrayList();
        for(Map.Entry map : hashWeight.entrySet()){
            data.add(new TNormFirst(
                    Double.toString((Double) map.getValue()),
                    (String) map.getKey(),
                    Double.toString(hashAssesment.get((String) map.getKey())),
                    Double.toString(hashCertification.get((String) map.getKey())),
                    Double.toString(hashImplementation.get((String) map.getKey())),
                    Double.toString(hashPlanning.get((String) map.getKey())),
                    Double.toString(hashImprovement.get((String) map.getKey()))
                    ));
        }

        tWeight.setCellValueFactory(new PropertyValueFactory<>("satu"));
        tId.setCellValueFactory(new PropertyValueFactory<>("dua"));
        tAssesment.setCellValueFactory(new PropertyValueFactory<>("tiga"));
        tCertification.setCellValueFactory(new PropertyValueFactory<>("empat"));
        tImplementation.setCellValueFactory(new PropertyValueFactory<>("lima"));
        tPlanning.setCellValueFactory(new PropertyValueFactory<>("enam"));
        tImprove.setCellValueFactory(new PropertyValueFactory<>("tujuh"));

        tableFirst.setItems(null);
        tableFirst.setItems(data);
    }

    private void showValue(){
        dataValue = FXCollections.observableArrayList();
        for(Map.Entry map : hashWeight.entrySet()){
            dataValue.add(new TNormValue(
                    Double.toString(hasilAssesment.get((String) map.getKey())),
                    Double.toString(hasilPlanning.get((String) map.getKey())),
                    Double.toString(hasilImplementation.get((String) map.getKey())),
                    Double.toString(hasilImprovement.get((String) map.getKey())),
                    Double.toString(hasilCertification.get((String) map.getKey()))
            ));
            pAssesmentData = pAssesmentData * hasilAssesment.get((String) map.getKey());
            pPlanningData = pPlanningData * hasilPlanning.get((String) map.getKey());
            pImplementationData = pImplementationData * hasilImplementation.get((String) map.getKey());
            pImprovementData = pImprovementData * hasilImprovement.get((String) map.getKey());
            pCertificationData = pCertificationData * hasilCertification.get((String) map.getKey());
        }

        tAssesment1.setCellValueFactory(new PropertyValueFactory<>("satu"));
        tCertification1.setCellValueFactory(new PropertyValueFactory<>("dua"));
        tImplementation1.setCellValueFactory(new PropertyValueFactory<>("tiga"));
        tPlanning1.setCellValueFactory(new PropertyValueFactory<>("empat"));
        tImprove1.setCellValueFactory(new PropertyValueFactory<>("lima"));

        tableOne.setItems(null);
        tableOne.setItems(dataValue);
    }

    private void getDataToArray(){
        String SQL = "SELECT average, id, evaluation, average_instrumen FROM aspect\n" +
                "ORDER BY id ASC";
        ResultSet rs = crud.simpleShow(SQL);
        int i = 0;
        double totalKoresponden = getData.getCorrespondent();
        try {
            while(rs.next()){

                //array fro
                hashWeight.put(rs.getString("id"), rs.getDouble("average"));


                //1 array for system assesment
                if(rs.getString("evaluation").equals("System Assessment")){
                    //System.out.println("masuk ke sini harusnya satu si "+ rs.getDouble("average_instrumen"));
                    hashAssesment.put(rs.getString("id"), rs.getDouble("average_instrumen"));
                } else {
                    hashAssesment.put(rs.getString("id"), totalKoresponden);
                    //System.out.println("ini si evaluation "+ rs.getString("evaluation"));
                }

                //2 array for Program Certification
                if(rs.getString("evaluation").equals("Program Certification")){
                    hashCertification.put(rs.getString("id"), rs.getDouble("average_instrumen"));
                } else {
                    hashCertification.put(rs.getString("id"), totalKoresponden);
                }

                //3 array for Program Implementation
                if(rs.getString("evaluation").equals("Program Implementation")){
                    hashImplementation.put(rs.getString("id"), rs.getDouble("average_instrumen"));
                } else {
                    hashImplementation.put(rs.getString("id"), totalKoresponden);
                }

                //4 array for Program Planning
                if(rs.getString("evaluation").equals("Program Planning")){
                    hashPlanning.put(rs.getString("id"), rs.getDouble("average_instrumen"));
                } else {
                    hashPlanning.put(rs.getString("id"), totalKoresponden);
                }

                //5 array for Program Improvment
                if(rs.getString("evaluation").equals("Program Improvement")){
                    hashImprovement.put(rs.getString("id"), rs.getDouble("average_instrumen"));
                } else {
                    hashImprovement.put(rs.getString("id"), totalKoresponden);
                }

                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void processClicked(){
        btnProses.setOnAction(event -> {
            if(vAssesment.getText().equals("")){
                if(getData.getCorrespondent() != 0){
                    process();
                    showValue();
                    pAssesment.setText(String.valueOf(pAssesmentData));
                    pPlanning.setText(String.valueOf(pPlanningData));
                    pImplementation.setText(String.valueOf(pImplementationData));
                    pImprovement.setText(String.valueOf(pImprovementData));
                    pCertification.setText(String.valueOf(pCertificationData));
                    total = pAssesmentData+pPlanningData+pImplementationData+pImprovementData+pCertificationData;
                    pTotal.setText(String.valueOf(total));

                    double vekAssesment = total/pAssesmentData;
                    double vekPlanning = total/pPlanningData;
                    double vekImplementation = total/pImplementationData;
                    double vekImprovement = total/pImprovementData;
                    double vekCertification = total/pCertificationData;

                    vAssesment.setText(String.valueOf(vekAssesment));
                    vPlanning.setText(String.valueOf(vekPlanning));
                    vImplementation.setText(String.valueOf(vekImplementation));
                    vImprovement.setText(String.valueOf(vekImprovement));
                    vCertification.setText(String.valueOf(vekCertification));
                } else JOptionPane.showMessageDialog(null, "No Correspondent Data yet");

            } else {
                JOptionPane.showMessageDialog(null, "Vektor V Have Been Created");
            }
        });
    }

    private void process(){
        for(Map.Entry map : hashWeight.entrySet()){
            hasilAssesment.put((String) map.getKey(), Math.pow(hashAssesment.get((String) map.getKey()), (Double) map.getValue()));
            hasilPlanning.put((String) map.getKey(), Math.pow(hashPlanning.get((String) map.getKey()), (Double) map.getValue()));
            hasilImplementation.put((String) map.getKey(), Math.pow(hashImplementation.get((String) map.getKey()), (Double) map.getValue()));
            hasilImprovement.put((String) map.getKey(), Math.pow(hashImprovement.get((String) map.getKey()), (Double) map.getValue()));
            hasilCertification.put((String) map.getKey(), Math.pow(hashCertification.get((String) map.getKey()), (Double) map.getValue()));
        }
    }

    private void vektorSave(){
        btnSave.setOnAction(event -> {
            storeDataEvaluation();
        });
    }

    private void storeDataEvaluation() {

        if (validation()) {
            //do store data

            String SQL = "UPDATE vektor_v SET vektor = '"+ vAssesment.getText() +"' WHERE id = 'System Assessment'";
            String SQL2 = "UPDATE vektor_v SET vektor = '"+ vPlanning.getText() +"' WHERE id = 'Program Planning'";
            String SQL3 = "UPDATE vektor_v SET vektor = '"+ vImplementation.getText() +"' WHERE id = 'Program Implementation'";
            String SQL4 = "UPDATE vektor_v SET vektor = '"+ vImprovement.getText() +"' WHERE id = 'Program Improvement'";
            String SQL5 = "UPDATE vektor_v SET vektor = '"+ vCertification.getText() +"' WHERE id = 'Program Certification'";

            if (crud.simpleUpdate(SQL) &&
                    crud.simpleUpdate(SQL2) &&
                    crud.simpleUpdate(SQL3) &&
                    crud.simpleUpdate(SQL4) &&
                    crud.simpleUpdate(SQL5)
                    ) {
                JOptionPane.showMessageDialog(null, "Successfuly Input Data");
            }

        } else {
            JOptionPane.showMessageDialog(null, "All Fields Must be Filled");
        }
    }

    private Boolean validation(){
        if(!pAssesment.getText().equals("") &&
                !pCertification.getText().equals("") &&
                !pImprovement.getText().equals("") &&
                !pPlanning.getText().equals("") &&
                !pImplementation.getText().equals("")

                ){
            return true;
        }

        return false;
    }
}
