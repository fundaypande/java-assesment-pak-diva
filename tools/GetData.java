package tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static controller.LoginController.username;

/**
 * Created by User on 26/01/2018.
 */
public class GetData {

    CRUD crud = new CRUD();

    public Integer getCountInstrument(){
        String SQL = "SELECT COUNT(id) AS  'countID' FROM instrumen";
        ResultSet rs = crud.simpleShow(SQL);
        try {
            if(rs.next()){
                return rs.getInt("countID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Integer getCorrespondent(){
        String SQL = "SELECT DISTINCT id_user FROM instrument_user";
        ResultSet rs = crud.simpleShow(SQL);
        int i = 0;
        try {
            while(rs.next()){
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public String getStartDate(){
        String SQL = "SELECT min(tanggal) AS 'date' FROM instrument_user";
        ResultSet rs = crud.simpleShow(SQL);
        String date = "";
        try {
            if(rs.next()){
                date = rs.getString("date");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return date;
    }


    public HashMap topAverage(){
        HashMap<String, String> hashMap = new HashMap<>();
        String SQL = "SELECT id_aspect , SUM(instrument_user.ratting)\n" +
                "FROM instrument_user\n" +
                "  INNER JOIN instrumen ON instrumen.id = instrument_user.id_instrument\n" +
                "  INNER JOIN aspect ON aspect.id = instrumen.id_aspect\n" +
                "GROUP BY aspect.aspect\n" +
                "ORDER BY id_aspect ASC";
        ResultSet rs = crud.simpleShow(SQL);
        try {
            while(rs.next()){
                hashMap.put(rs.getString("id_aspect"), rs.getString("SUM(instrument_user.ratting)"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hashMap;
    }

    public int sumResult(){
        String SQL = "SELECT count(*) FROM evaluation";
        ResultSet rs = crud.simpleShow(SQL);
        int count = 0;
        try {
            if(rs.next()){
                count = Integer.parseInt(rs.getString("count(*)"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }
}
