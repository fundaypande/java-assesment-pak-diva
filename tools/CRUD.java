package tools;

import connection.Connection;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by User on 25/01/2018.
 */
public class CRUD {
    private Connection db = Connection.getDbCon();
    public Boolean simpleDelete(String SQL, String id){
        System.out.println("SQL Delete Data : "+SQL);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Data");
        alert.setContentText("Are You Sure to Delete "+ id +"");
        alert.setHeaderText(null);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            //Delete From Database
            try {
                int eq = db.insert(SQL);
                JOptionPane.showMessageDialog(null, "Successfully Delete  "+ id +"  ");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Failed to Delete");
            return false;
        }
        return true;
    }

    public Boolean simpleStore(String SQL){
        System.out.println("SQL Input Data : "+SQL);
        try {
            int eq = db.insert(SQL);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to Input Data on Table Structure");
            return false;
        }
        return true;
    }

    public Boolean simpleUpdate(String SQL){
        System.out.println("SQL Update Data : "+SQL);
        try {
            int eq = db.insert(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to Input Data on Table Structure");
            return false;
        }
        return true;
    }

    public ResultSet simpleShow(String SQL){
        System.out.println("SQL Show Data : "+SQL);
        ResultSet rs = null;
        try {
            rs = db.query(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }


}
