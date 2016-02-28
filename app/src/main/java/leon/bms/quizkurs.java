package leon.bms;

import java.util.List;

/**
 * Created by Leon E on 28.02.2016.
 */
public class quizkurs {
    String kursFach;
    String kursId;
    String lehrer;
    int fragen;
    String datum;



    public quizkurs() {
    }

    public quizkurs(String kursFach, String kursId, String lehrer, int fragen, String datum) {
        this.kursFach = kursFach;
        this.kursId = kursId;
        this.lehrer = lehrer;
        this.fragen = fragen;
        this.datum = datum;
    }

    public String getKursFach() {
        return kursFach;
    }

    public void setKursFach(String kursFach) {
        this.kursFach = kursFach;
    }

    public String getKursId() {
        return kursId;
    }

    public void setKursId(String kursId) {
        this.kursId = kursId;
    }

    public String getLehrer() {
        return lehrer;
    }

    public void setLehrer(String lehrer) {
        this.lehrer = lehrer;
    }

    public int getFragen() {
        return fragen;
    }

    public void setFragen(int fragen) {
        this.fragen = fragen;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }
}
