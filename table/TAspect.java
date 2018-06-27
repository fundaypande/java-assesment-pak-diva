package table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by User on 18/07/2017.
 */
public class TAspect {
    private final StringProperty satu;
    private final StringProperty dua;
    private final StringProperty tiga;

    public TAspect(
            //this is parameter
            String satu,
            String dua,
            String tiga
    ) {
        this.satu = new SimpleStringProperty(satu);
        this.dua = new SimpleStringProperty(dua);
        this.tiga = new SimpleStringProperty(tiga);
    }

    public String getSatu(){
        return satu.get();
    }
    public String getDua(){
        return dua.get();
    }
    public String getTiga(){
        return tiga.get();
    }


    //property value
    public StringProperty satuProperty(){
        return satu;
    }
    public StringProperty duaProperty(){
        return dua;
    }
    public StringProperty tigaProperty(){
        return tiga;
    }
}
