package leon.bms.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * Created by Leon E on 18.06.2016.
 */
public class dbUnterrichtszeit extends RealmObject {
    @PrimaryKey
    private int stundenindex;
    private String beginn;
    private String beschreibung;
    private String ende;
    private Boolean pause;

    private dbSchule schule;

    public dbUnterrichtszeit() {
    }

    public int getStundenindex() {
        return stundenindex;
    }

    public void setStundenindex(int stundenindex) {
        this.stundenindex = stundenindex;
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

    public dbSchule getSchule() {
        return schule;
    }

    public void setSchule(dbSchule schule) {
        this.schule = schule;
    }
}
