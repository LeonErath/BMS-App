package leon.bms;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 21.05.2016.
 */
public class dbUnterrichtszeit extends SugarRecord {
    String beginn;
    String beschreibung;
    String ende;
    Boolean pause;
    int stundenindex;

    dbSchule schule;

    public dbUnterrichtszeit() {
    }

    public dbUnterrichtszeit(String beginn, String beschreibung, String ende, Boolean pause, int stundenindex, dbSchule schule) {
        this.beginn = beginn;
        this.beschreibung = beschreibung;
        this.ende = ende;
        this.pause = pause;
        this.stundenindex = stundenindex;
        this.schule = schule;
    }

    public dbSchule getSchule() {
        return schule;
    }

    public void setSchule(dbSchule schule) {
        this.schule = schule;
    }

    public String getBeginn() {
        return beginn;
    }

    public void setBeginn(String beginn) {
        this.beginn = beginn;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getEnde() {
        return ende;
    }

    public void setEnde(String ende) {
        this.ende = ende;
    }

    public Boolean getPause() {
        return pause;
    }

    public void setPause(Boolean pause) {
        this.pause = pause;
    }

    public int getStundenindex() {
        return stundenindex;
    }

    public void setStundenindex(int stundenindex) {
        this.stundenindex = stundenindex;
    }
}
