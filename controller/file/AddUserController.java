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
import javafx.util.converter.DefaultStringConverter;
import table.AddUser;
import table.TInputInstrument;
import tools.CRUD;
import tools.ScreenWidth;

import javax.swing.*;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static controller.LoginController.username;
import static tools.ScreenWidth.screenWidth.allWidth;
import static tools.StringProcess.removeSparator;

public class AddUserController implements Initializable {

    @FXML private TextField fieldUsername;
    @FXML private TextField fieldInstitution;
    @FXML private PasswordField fieldPassword;
    @FXML private TextField fieldPhone;
    @FXML private TextField fieldEmail;
    @FXML private ComboBox<String> comboPosition;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private TextField fieldName;
    @FXML private TableView<AddUser> table;
    @FXML private TableColumn<AddUser, String> tUsername;
    @FXML private TableColumn<AddUser, String> tName;
    @FXML private TableColumn<AddUser, String> tPosition;
    @FXML private TableColumn<AddUser, String> tInstitution;
    @FXML private TableColumn<AddUser, String> tPhone;
    @FXML private TableColumn<AddUser, String> tEmail;
    @FXML private TableColumn<AddUser, String> tLevel;
    @FXML private VBox vbox;

    private ObservableList<AddUser> data;
    private Connection db = Connection.getDbCon();
    CRUD crud = new CRUD();

    private ContextMenu contextMenu = new ContextMenu();
    private MenuItem menuDelete = new MenuItem("Delete User       ");

    private String userNameT, passwordT, nameT, positionT, phoneT, emailT, institutionT;
    private ObservableList<String> masterData3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        masterData3 = FXCollections.observableArrayList();
        show();
        vbox.setMinWidth(allWidth()-20);
        store();
        setComboPosition();
        setEmpty();
        cancelClick();
        setRightClick();
        delete();
        updateLevel();
        setComboLevel();
        menuDelete.setStyle("-fx-text-fill: #282828");
    }

    private void setComboLevel(){
        masterData3.add("1- Admin");
        masterData3.add("0- Member");
    }

    private void updateLevel(){
        getTable();
        table.setEditable(true);

        tLevel.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), masterData3));

        tLevel.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<AddUser, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<AddUser, String> event) {
                getTable();
                String[] rating = removeSparator(event.getNewValue(), "-");
                String dataSQL = "UPDATE users SET level = '"+ rating[0] +"'" +
                        " WHERE" +
                        " username = '"+ userNameT +"'";
                //do validation
                //cek apakah admin ada 1? jika satu jangan hapus admin jadi member lagi
                if(countAdmin() == 1 && Integer.parseInt(rating[0]) == 0){
                    JOptionPane.showMessageDialog(null, "The number of admins can not be empty");
                } else {
                    if (crud.simpleUpdate(dataSQL)) {
                        show();
                        setEmpty();
                        JOptionPane.showMessageDialog(null, "Successfully Edited Level Data");
                    }
                }
            }
        });
    }

    private int countAdmin() {
        String SQL = "SELECT count(username) FROM users WHERE level = 1";
        ResultSet rs = crud.simpleShow(SQL);
        int count = 0;
        try {
            if(rs.next()){
                count = Integer.parseInt(rs.getString("count(username)"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    private void show(){
        data = FXCollections.observableArrayList();
        String SQL = "SELECT * FROM users ORDER BY username ASC";
        ResultSet rs = null;
        try {
            rs = db.query(SQL);
            while(rs.next()){
                data.add(new AddUser(
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getString("posision"),
                        rs.getString("institution"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("level")
                ));

                //list.add(rs.getString("user"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //set Cell Table Vactory
        tUsername.setCellValueFactory(new PropertyValueFactory<>("satu"));
        tName.setCellValueFactory(new PropertyValueFactory<>("dua"));
        tPosition.setCellValueFactory(new PropertyValueFactory<>("tiga"));
        tInstitution.setCellValueFactory(new PropertyValueFactory<>("empat"));
        tPhone.setCellValueFactory(new PropertyValueFactory<>("lima"));
        tEmail.setCellValueFactory(new PropertyValueFactory<>("enam"));
        tLevel.setCellValueFactory(new PropertyValueFactory<>("tujuh"));

        table.setItems(null);
        table.setItems(data);
    }

    private void store(){
        btnSave.setOnAction(event -> {
            if(validation()){
                if(usernameExist(fieldUsername.getText())){
                    //do store data
                    MessageDigest md = null;
                    try {
                        md = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e1) {
                        e1.printStackTrace();
                    }
                    md.update(fieldPassword.getText().getBytes(), 0, fieldPassword.getLength());
                    String password = new BigInteger(1, md.digest()).toString(16);

                    String SQL = "INSERT INTO users VALUES ('"+fieldUsername.getText()+"','"+password+"','"+fieldName.getText()+"'," +
                            "'"+comboPosition.getValue()+"','"+fieldPhone.getText()+"','"+fieldEmail.getText()+"', '0', '"+fieldInstitution.getText()+"')";
                    System.out.println("SQL Input Data User : "+SQL);
                    try {
                        int eq = db.insert(SQL);
                        show();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    setEmpty();
                } else JOptionPane.showMessageDialog(null, "Username is Already in Use");
            } else {
                JOptionPane.showMessageDialog(null, "All Fields are Required");
            }
        });
    }

    private void delete(){
        getTable();
        menuDelete.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Hapus Data User");
            alert.setContentText("Are you sure you want to Remove "+ nameT +" From te User");
            alert.setHeaderText(null);

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                //Delete From Database
                String SQL = "DELETE FROM users WHERE username = '"+ userNameT +"'";
                try {
                    int eq = db.insert(SQL);
                    JOptionPane.showMessageDialog(null, "Successfuly Delete User");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                show();
            } else {
                JOptionPane.showMessageDialog(null, "Gagal Hapus User");
            }
        });
    }

    private void getTable(){
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AddUser>() {
            @Override
            public void changed(ObservableValue<? extends AddUser> observable, AddUser oldValue, AddUser newValue) {
                if(table.getSelectionModel().getSelectedItems() != null){
                    userNameT = table.getSelectionModel().getSelectedItem().getSatu();
                    nameT = table.getSelectionModel().getSelectedItem().getDua();
                    positionT = table.getSelectionModel().getSelectedItem().getTiga();
                    institutionT = table.getSelectionModel().getSelectedItem().getEmpat();
                    phoneT = table.getSelectionModel().getSelectedItem().getLima();
                    emailT = table.getSelectionModel().getSelectedItem().getEnam();
                }
            }
        });
    }

    private void cancelClick(){
        btnCancel.setOnAction(event -> {
            setEmpty();
        });
    }

    private Boolean validation(){
        if(!fieldEmail.getText().equals("") &&
                !fieldInstitution.getText().equals("") &&
                !fieldName.getText().equals("") &&
                !fieldPassword.getText().equals("") &&
                !fieldPhone.getText().equals("") &&
                !fieldUsername.getText().equals("")){
            return true;
        }

        return false;
    }
    private Boolean usernameExist(String username){
        String SQL = "SELECT COUNT(username) FROM users WHERE username = '"+username+"'";
        System.out.println(SQL);
        String count = "";
        try {
            ResultSet rs = db.query(SQL);
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
    private void setComboPosition(){
        comboPosition.setValue("Mahasiswa");
        ObservableList<String> limit = FXCollections.observableArrayList(
            "Dosen","Mahasiswa","Umum"
        );
        comboPosition.getItems().addAll(limit);
    }
    private void setEmpty(){
        fieldInstitution.setText("");
        fieldEmail.setText("");
        fieldPhone.setText("");
        fieldName.setText("");
        fieldInstitution.setText("");
        fieldPassword.setText("");
        fieldUsername.setText("");
        comboPosition.setValue("Mahasiswa");
    }

    private void setRightClick(){
        table.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

            @Override
            public void handle(ContextMenuEvent event) {
                contextMenu.show(table, event.getScreenX(), event.getScreenY());
            }
        });
        contextMenu.getItems().addAll(menuDelete);
    }
}
