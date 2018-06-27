package table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by User on 28/01/2018.
 */
public class TNormValue {
        private final StringProperty satu;
        private final StringProperty dua;
        private final StringProperty tiga;
        private final StringProperty empat;
        private final StringProperty lima;



        public TNormValue(
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

        //public getter
        /*public String getIdNasabah(){
            return idNasabah.get();
        }
        public String getNo(){
            return no.get();
        }
        public String getNama(){
            return nama.get();
        }
        public String getNoKredit(){
            return noKredit.get();
        }
        public String getTanggalKredit(){
            return tanggalKredit.get();
        }
        public String getJaminan(){
            return jaminan.get();
        }
        public String getBunga(){
            return bunga.get();
        }
        public String getJangkaBulan(){
            return jangkaBulan.get();
        }
        public String getJenis(){
            return jenis.get();
        }
        public String getTanggalTempo(){
            return tanggalTempo.get();
        }
        public String getSisaKredit(){
            return sisaKredit.get();
        }
        public String getStatus(){
            return status.get();
        }*/


        //public setter
        /*public void setIdNasabah(String value){
            idNasabah.set(value);
        }
        public void setNo(String value){
            no.set(value);
        }
        public void setNama(String value){
            nama.set(value);
        }
        public void setNoKredit(String value){
            noKredit.set(value);
        }
        public void setTanggalKredit(String value){
            tanggalKredit.set(value);
        }
        public void setJaminan(String value){
            jaminan.set(value);
        }
        public void setBunga(String value){
            bunga.set(value);
        }
        public void setJangkaBulan(String value){
            jangkaBulan.set(value);
        }
        public void setJenis(String value){
            jenis.set(value);
        }
        public void setTanggalTempo(String value){
            tanggalTempo.set(value);
        }
        public void setSisaKredit(String value){
            sisaKredit.set(value);
        }
        public void setStatus(String value){
            status.set(value);
        }*/


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

