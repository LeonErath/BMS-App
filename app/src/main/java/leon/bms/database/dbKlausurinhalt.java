package leon.bms.database;

/**
 * Created by Leon E on 21.05.2016.
 */
public class dbKlausurinhalt {
    public String beschreibung;
    public int inhaltIndex;

    public dbKlausur klausur;

    public dbKlausurinhalt() {
    }

    public dbKlausurinhalt(String beschreibung, int inhaltIndex, dbKlausur klausur) {
        this.beschreibung = beschreibung;
        this.inhaltIndex = inhaltIndex;
        this.klausur = klausur;
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
