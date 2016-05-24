package leon.bms.database;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 21.05.2016.
 */
public class dbKlausurinhalt extends SugarRecord {
    public String beschreibung;
    public int inhaltIndex;
    public boolean erledigt;

    public dbKlausur klausur;

    public dbKlausurinhalt() {
    }

    public dbKlausurinhalt(String beschreibung, int inhaltIndex, boolean erledigt, dbKlausur klausur) {
        this.beschreibung = beschreibung;
        this.inhaltIndex = inhaltIndex;
        this.erledigt = erledigt;
        this.klausur = klausur;
    }

    public boolean isErledigt() {
        return erledigt;
    }

    public void setErledigt(boolean erledigt) {
        this.erledigt = erledigt;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getInhaltIndex() {
        return inhaltIndex;
    }

    public void setInhaltIndex(int inhaltIndex) {
        this.inhaltIndex = inhaltIndex;
    }

    public dbKlausur getKlausur() {
        return klausur;
    }

    public void setKlausur(dbKlausur klausur) {
        this.klausur = klausur;
    }
}
