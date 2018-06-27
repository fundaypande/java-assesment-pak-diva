package table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by User on 18/07/2017.
 */
public class TAverage {
    private final StringProperty satu;

    public TAverage(
            //this is parameter
            String satu

    ) {
        this.satu = new SimpleStringProperty(satu);

    }

    public String getSatu(){
        return satu.get();
    }



    //property value
    public StringProperty satuProperty(){
        return satu;
    }

}
