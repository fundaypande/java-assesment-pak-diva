package controller.result;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import table.TWelcomeResult;
import tools.CRUD;
import tools.GetData;

import javax.swing.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static controller.LoginController.level;
import static tools.ScreenWidth.screenWidth.allWidth;

/**
 * Created by Funday on 08/02/2018.
 */
public class ResultController implements Initializable {

    @FXML
    private TableView<TWelcomeResult> table;
    @FXML private TableColumn<TWelcomeResult, String> tPosition;
    @FXML private TableColumn<TWelcomeResult, String> tDate;
    @FXML private TableColumn<TWelcomeResult, String> tComponent;
    @FXML private TableColumn<TWelcomeResult, String> tConstraint;
    @FXML private TableColumn<TWelcomeResult, String> tRecomendation;
    @FXML private Button btnKiri;
    @FXML private Button btnKanan;
    @FXML private Button btnReset;
    @FXML private AnchorPane anchor;

    CRUD crud = new CRUD();
    GetData getData = new GetData();
    private ObservableList<TWelcomeResult> data;

    private int offset = 0;
    private int getCountResult = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anchor.setPrefWidth(allWidth()-20);
        setNext();
        setBack();
        show();
        getCountResult = getData.sumResult();
        resetData();
        if(level().equals("1"))
            btnReset.setVisible(true);
        else btnReset.setVisible(false);
    }

    private void resetData(){
        btnReset.setOnAction(event -> {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Data");
            alert.setContentText("All Data Result Will Be Deleted, Are You Sure to Continue?");
            alert.setHeaderText(null);

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                //Delete From Database
                String SQL = "DELETE FROM evaluation";

                if (crud.simpleDelete(SQL, "All")) {
                    System.out.println("Data Evaluasi Berhasil di Kosongkan");
                    show();
                }

            } else {
                JOptionPane.showMessageDialog(null, "Failed to Delete");
            }
        });
    }

    private void show(){
        data = FXCollections.observableArrayList();
        String SQL = "SELECT * FROM evaluation ORDER BY date DESC LIMIT 5 OFFSET " + offset;
        ResultSet rs = crud.simpleShow(SQL);
        int i = 1;
        try {
            while(rs.next()){
                data.add(new TWelcomeResult(
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
            } else System.out.println("Data Out");
        });
    }
}
