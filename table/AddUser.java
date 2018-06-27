package table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by User on 18/07/2017.
 */
public class AddUser {
    private final StringProperty satu;
    private final StringProperty dua;
    private final StringProperty tiga;
    private final StringProperty empat;
    private final StringProperty lima;
    private final StringProperty enam;
    private final StringProperty tujuh;

    public AddUser(
            //this is parameter
            String satu,
            String dua,
            String tiga,
            String empat,
            String lima,
            String enam,
            String tujuh
    ) {
        this.satu = new SimpleStringProperty(satu);
        this.dua = new SimpleStringProperty(dua);
        this.tiga = new SimpleStringProperty(tiga);
        this.empat = new SimpleStringProperty(empat);
        this.lima = new SimpleStringProperty(lima);
        this.enam = new SimpleStringProperty(enam);
        this.tujuh = new SimpleStringProperty(tujuh);
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
    public String getEnam(){
        return enam.get();
    }
    public String getTujuh(){
        return tujuh.get();
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
    public StringProperty enamProperty(){
        return enam;
    }
    public StringProperty tujuhProperty(){
        return tujuh;
    }
}
