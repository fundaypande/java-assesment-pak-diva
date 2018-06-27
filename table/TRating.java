package table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by User on 18/07/2017.
 */
public class TRating {
    private final StringProperty satu;
    private final StringProperty dua;

    public TRating(
            //this is parameter
            String satu,
            String dua
    ) {
        this.satu = new SimpleStringProperty(satu);
        this.dua = new SimpleStringProperty(dua);
    }

    public String getSatu(){
        return satu.get();
    }
    public String getDua(){
        return dua.get();
    }


    //property value
    public StringProperty satuProperty(){
        return satu;
    }
    public StringProperty duaProperty(){
        return dua;
    }
}
