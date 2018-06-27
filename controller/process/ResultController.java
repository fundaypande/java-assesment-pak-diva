package controller.process;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import table.TAspect;
import table.TResult;
import tools.CRUD;
import tools.GetData;

import javax.swing.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static tools.ScreenWidth.screenWidth.allWidth;

/**
 * Created by User on 31/01/2018.
 */
public class ResultController implements Initializable {

    @FXML private VBox vBox;
    @FXML private Label labelWarning;
    @FXML private HBox hBoxMaster;
    @FXML private VBox vBox1;
    @FXML private TextField vSatu;
    @FXML private TextField vDua;
    @FXML private TextField vTiga;
    @FXML private TextField vEmpat;
    @FXML private TextField vLima;
    @FXML private TextField comp1;
    @FXML private TextField comp2;
    @FXML private TextField comp3;
    @FXML private TextField comp4;
    @FXML private TextField comp5;
    @FXML private TextArea cons1;
    @FXML private TextArea cons2;
    @FXML private TextArea cons3;
    @FXML private TextArea cons4;
    @FXML private TextArea cons5;
    @FXML private TextArea Rec1;
    @FXML private TextArea Rec2;
    @FXML private TextArea Rec3;
    @FXML private TextArea Rec4;
    @FXML private TextArea Rec5;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private TableView<TResult> table;
    @FXML private TableColumn<TResult, String> tPosition;
    @FXML private TableColumn<TResult, String> tDate;
    @FXML private TableColumn<TResult, String> tComponent;
    @FXML private TableColumn<TResult, String> tConstraint;
    @FXML private TableColumn<TResult, String> tRecomendation;
    @FXML private GridPane grid;
    @FXML private AnchorPane anchor;
    @FXML private Button btnKiri;
    @FXML private Button btnKanan;

    CRUD crud = new CRUD();
    GetData getData = new GetData();
    ArrayList<String> vektorV = new ArrayList<String>();
    ArrayList<String> evaluation = new ArrayList<String>();
    private ObservableList<TResult> data;

    private int offset = 0;
    private int getCountResult = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        settComponent();
        anchor.setPrefWidth(allWidth()-20);
        getDataFromUser();
        showContraint();
        show();
        setNext();
        setBack();
        getCountResult = getData.sumResult();
    }

    private void getVektor(){
        String SQL = "SELECT * FROM vektor_v ORDER BY vektor DESC";
        ResultSet rs = crud.simpleShow(SQL);
        try {
            while(rs.next()){
                vektorV.add(rs.getString("vektor"));
                evaluation.add(rs.getString("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void settComponent(){
        getVektor();
        vSatu.setText(vektorV.get(0));
        vDua.setText(vektorV.get(1));
        vTiga.setText(vektorV.get(2));
        vEmpat.setText(vektorV.get(3));
        vLima.setText(vektorV.get(4));

        comp1.setText(evaluation.get(0));
        comp2.setText(evaluation.get(1));
        comp3.setText(evaluation.get(2));
        comp4.setText(evaluation.get(3));
        comp5.setText(evaluation.get(4));
    }

    private void getDataFromUser(){
        btnSave.setOnAction(event -> {
            String SQL1 = "INSERT INTO evaluation (id, vektor_v, contraints, recomendations, evaluation, date) VALUES " +
                    "(null,'"+vSatu.getText()+"', '"+cons1.getText()+"', '"+Rec1.getText()+"', '"+comp1.getText()+"' , DATETIME('now', 'localtime'))";

            String SQL2 = "INSERT INTO evaluation (id, vektor_v, contraints, recomendations, evaluation, date) VALUES " +
                    "(null,'"+vDua.getText()+"', '"+cons2.getText()+"', '"+Rec2.getText()+"', '"+comp2.getText()+"' , DATETIME('now', 'localtime'))";

            String SQL3 = "INSERT INTO evaluation (id, vektor_v, contraints, recomendations, evaluation, date) VALUES " +
                    "(null,'"+vTiga.getText()+"', '"+cons3.getText()+"', '"+Rec3.getText()+"', '"+comp3.getText()+"' , DATETIME('now', 'localtime'))";

            String SQL4 = "INSERT INTO evaluation (id, vektor_v, contraints, recomendations, evaluation, date) VALUES " +
                    "(null,'"+vEmpat.getText()+"', '"+cons4.getText()+"', '"+Rec4.getText()+"', '"+comp4.getText()+"' , DATETIME('now', 'localtime'))";

            String SQL5 = "INSERT INTO evaluation (id, vektor_v, contraints, recomendations, evaluation, date) VALUES " +
                    "(null,'"+vLima.getText()+"', '"+cons5.getText()+"', '"+Rec5.getText()+"', '"+comp5.getText()+"' , DATETIME('now', 'localtime'))";

            if(validation()){
                if(crud.simpleStore(SQL1) &&
                        crud.simpleStore(SQL2) &&
                        crud.simpleStore(SQL3) &&
                        crud.simpleStore(SQL4) &&
                        crud.simpleStore(SQL5) ){
                    setEmpty();
                    JOptionPane.showMessageDialog(null, "Successfully Uploaded");
                    offset = 0;
                    show();
                    getCountResult = getData.sumResult();
                    //do remove data
                    setEmptyVektor();
                    setEmptyInsturmenUser();
                }
            } else {
                JOptionPane.showMessageDialog(null, "All Field Are Required");
            }
            System.out.println(Rec1.getText());
        });
    }

    private Boolean validation(){
        if(!vSatu.getText().equals("") &&
                !vDua.getText().equals("") &&
                !vTiga.getText().equals("") &&
                !vEmpat.getText().equals("") &&
                !vLima.getText().equals("") &&
                !comp1.getText().equals("") &&
                !comp2.getText().equals("") &&
                !comp3.getText().equals("") &&
                !comp4.getText().equals("") &&
                !comp5.getText().equals("") &&
                !cons1.getText().equals("") &&
                !cons2.getText().equals("") &&
                !cons3.getText().equals("") &&
                !cons4.getText().equals("") &&
                !cons5.getText().equals("") &&
                !Rec1.getText().equals("") &&
                !Rec2.getText().equals("") &&
                !Rec3.getText().equals("") &&
                !Rec4.getText().equals("") &&
                !Rec5.getText().equals("")
                ){
            return true;
        }

        return false;
    }

    private void setEmpty(){
        vSatu.setText("");
        vDua.setText("");
        vTiga.setText("");
        vEmpat.setText("");
        vLima.setText("");

        cons1.setText("");
        cons2.setText("");
        cons3.setText("");
        cons4.setText("");
        cons5.setText("");

        Rec1.setText("");
        Rec2.setText("");
        Rec3.setText("");
        Rec4.setText("");
        Rec5.setText("");

        comp1.setText("");
        comp2.setText("");
        comp3.setText("");
        comp4.setText("");
        comp5.setText("");
    }

    private ResultSet getMinAspect(String evaluation){
        String SQL = "SELECT instrumen.instrument, aspect.aspect, aspect.average_instrumen\n" +
                "FROM instrumen\n" +
                "INNER JOIN aspect ON aspect.id = instrumen.id_aspect\n" +
                "WHERE instrumen.id_aspect = (\n" +
                "  SELECT aspect.id FROM aspect\n" +
                "  WHERE aspect.average_instrumen = (\n" +
                "    SELECT MIN(aspect.average_instrumen)\n" +
                "    FROM aspect\n" +
                "    WHERE aspect.evaluation = '"+ evaluation +"'\n" +
                "  ) AND aspect.evaluation = '"+ evaluation +"'\n" +
                ")";
        return crud.simpleShow(SQL);
    }

    private void settConstraint(TextArea cons, TextField evaluation){
        ArrayList<String> instrument = new ArrayList<String>();

        ResultSet rs = getMinAspect(evaluation.getText());
        String aspek = "";
        String average = "";
        try {
            while(rs.next()){
                aspek = rs.getString("aspect");
                average = rs.getString("average_instrumen");
                instrument.add(rs.getString("instrument"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cons.appendText("Yang Memiliki Aspect Terendah Adalah : " + aspek + " Dengan nilai Average : " + average + " Dengan Instrument : ");
        for (int i = 0; i < instrument.size(); i++){
            cons.appendText(instrument.get(i)+ ", ");
        }
    }
    private void showContraint(){
        settConstraint(cons1, comp1);
        settConstraint(cons2, comp2);
        settConstraint(cons3, comp3);
        settConstraint(cons4, comp4);
        settConstraint(cons5, comp5);
    }

    private void show(){
        data = FXCollections.observableArrayList();
        String SQL = "SELECT * FROM evaluation ORDER BY date DESC LIMIT 5 OFFSET " + offset;
        ResultSet rs = crud.simpleShow(SQL);
        int i = 1;
        try {
            while(rs.next()){
                data.add(new TResult(
                        Integer.toString(i),
                        rs.getString("date"),
                        rs.getString("evaluation"),
                        rs.getString("contraints"),
                        rs.getString("recomendations")
                ));
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //set Cell Table Vactory
        tPosition.setCellValueFactory(new PropertyValueFactory<>("satu"));
        tDate.setCellValueFactory(new PropertyValueFactory<>("dua"));
        tComponent.setCellValueFactory(new PropertyValueFactory<>("tiga"));
        tConstraint.setCellValueFactory(new PropertyValueFactory<>("empat"));
        tRecomendation.setCellValueFactory(new PropertyValueFactory<>("lima"));

        table.setItems(null);
        table.setItems(data);
    }

    private void setNext(){
        btnKanan.setOnAction(event -> {
            if(offset < getCountResult){
                offset = offset + 5;
                show();
            } else System.out.println("Data Habis");
        });
    }

    private void setBack(){
        btnKiri.setOnAction(event -> {
            if(offset > 0){
                offset = offset - 5;
                show();
            } else System.out.println("Data Habis");
        });
    }

    private void setEmptyVektor(){
        String SQL = "UPDATE vektor_v SET vektor = null WHERE id = 'System Assessment'";
        String SQL1 = "UPDATE vektor_v SET vektor = null WHERE id = 'Program Planning'";
        String SQL2 = "UPDATE vektor_v SET vektor = null WHERE id = 'Program Implementation'";
        String SQL3 = "UPDATE vektor_v SET vektor = null WHERE id = 'Program Improvement'";
        String SQL4 = "UPDATE vektor_v SET vektor = null WHERE id = 'Program Certification'";

        if (crud.simpleUpdate(SQL) &&
                crud.simpleUpdate(SQL1) &&
                crud.simpleUpdate(SQL2) &&
                crud.simpleUpdate(SQL3) &&
                crud.simpleUpdate(SQL4)) {
            System.out.println("Data Vektor Berhasil di Kosongkan");
        }


    }

    private void setEmptyInsturmenUser(){
        String SQL = "DELETE FROM instrument_user";

        if (crud.simpleUpdate(SQL)) {
            System.out.println("Data Instrumen User Berhasil di Kosongkan");
        }
    }
}
