package controller.profile;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import table.AddUser;
import table.TAspect;
import tools.CRUD;
import tools.Logout;

import javax.swing.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static controller.LoginController.username;
import static tools.StringProcess.hashMD5;

/**
 * Created by User on 02/02/2018.
 */
public class EditProfileController implements Initializable {

    @FXML
    private TextField fieldUsername;

    @FXML
    private TextField fieldInstitution;

    @FXML
    private PasswordField fieldPassword;

    @FXML private TextField fieldPhone;
    @FXML private TextField fieldEmail;
    @FXML private ComboBox<String> comboPosition;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private TextField fieldName;
    @FXML private VBox vbox;
    @FXML private PasswordField passOld;
    @FXML private PasswordField passNew;
    @FXML private Button btnSavePass;
    @FXML private Button btnCancelPass;

    CRUD crud = new CRUD();
    private String passwordDb = "";
    Logout logout = new Logout();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setComboPosition();
        show();
        update();
        updatePassword();
    }

    private void update(){
        btnSave.setOnAction(event -> {
            String SQL = "UPDATE users SET name = '"+ fieldName.getText() +"', posision = '"+ comboPosition.getValue() +"'," +
                    "institution = '"+ fieldInstitution.getText() +"', phone = '"+ fieldPhone.getText() +"',"+
                    "email = '"+ fieldEmail.getText() +"'"+
                    " WHERE" +
                    " username = '"+ fieldUsername.getText() +"'";
            if(validation()) {
                if (crud.simpleUpdate(SQL)) {
                    show();
                    JOptionPane.showMessageDialog(null, "Data was updated Successfully");
                }
            } else
                JOptionPane.showMessageDialog(null, "All Fields are Required");

        });
    }

    private void updatePassword(){
        btnSavePass.setOnAction(event -> {
            show();
            if(passwordDb.equals(hashMD5(passOld))){
                String SQL = "UPDATE users SET password = '"+ hashMD5(passNew) +"'" +
                        " WHERE" +
                        " username = '"+ username() +"'";
                if(crud.simpleUpdate(SQL)){
                    JOptionPane.showMessageDialog(null, "Password Successfully Edited");
                    resetPasswordField();
                    logout.logout(btnSavePass);
                }
            } else JOptionPane.showMessageDialog(null, "Enter the Correct Old Password");
        });
    }

    private void show(){
        String SQL = "SELECT * FROM users WHERE username = '" + username() + "'";
        ResultSet rs = crud.simpleShow(SQL);
        try {
            if(rs.next()){
                fieldUsername.setText(rs.getString("username"));
                fieldEmail.setText(rs.getString("email"));
                fieldInstitution.setText(rs.getString("institution"));
                fieldName.setText(rs.getString("name"));
                fieldPhone.setText(rs.getString("phone"));
                passwordDb = rs.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Boolean validation(){
        if(!fieldEmail.getText().equals("") &&
                !fieldInstitution.getText().equals("") &&
                !fieldName.getText().equals("") &&
                !fieldPhone.getText().equals("") &&
                !fieldUsername.getText().equals("")){
            return true;
        }

        return false;
    }
    private void setComboPosition(){
        comboPosition.setValue("Mahasiswa");
        ObservableList<String> limit = FXCollections.observableArrayList(
                "Dosen","Mahasiswa","Umum"
        );
        comboPosition.getItems().addAll(limit);
    }
    private void resetPasswordField(){
        passNew.setText("");
        passOld.setText("");
    }
}
