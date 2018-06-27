package table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by User on 18/07/2017.
 */
public class TWelcomeResult {
    private final StringProperty satu;
    private final StringProperty dua;
    private final StringProperty tiga;
    private final StringProperty empat;
    private final StringProperty lima;

    public TWelcomeResult(
            //this is parameter
            String satu,
            String dua,
            String tiga,
            String empat,
            String lima

    ) {
        this.satu = new SimpleStringProperty(satu);
        this.dua = new SimpleStringProperty(dua);
        this.tiga = new SimpleStringProperty(tiga);
        this.empat = new SimpleStringProperty(empat);
        this.lima = new SimpleStringProperty(lima);

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
    public String getEmpat(){
        return empat.get();
    }
    public String getLima(){
        return lima.get();
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
    public StringProperty empatProperty(){
        return empat;
    }
    public StringProperty limaProperty(){
        return lima;
    }

}
