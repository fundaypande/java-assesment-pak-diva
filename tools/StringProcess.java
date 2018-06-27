package tools;

import javafx.scene.control.PasswordField;
import table.TResult;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 25/01/2018.
 */
public class StringProcess {
    public static String[] removeSparator(String str, String chr){
        String[] partOf = str.split(chr);
        return partOf;
    }

    public static String hashMD5(PasswordField fieldPassword){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        md.update(fieldPassword.getText().getBytes(), 0, fieldPassword.getLength());
        return new BigInteger(1, md.digest()).toString(16);
    }


}
