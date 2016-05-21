package leon.bms;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 21.05.2016.
 */
public class dbFehler extends SugarRecord {
    Boolean bearbeitet;
    String beschreibung;
    String erstellungsDatum;

    dbKlausur klausur;

    public dbFehler() {
    }

    public dbFehler(Boolean bearbeitet, String beschreibung, String erstellungsDatum, dbKlausur klausur) {
        this.bearbeitet = bearbeitet;
        this.beschreibung = beschreibung;
        this.erstellungsDatum = erstellungsDatum;
        this.klausur = klausur;
    }

    public Boolean getBearbeitet() {
        return bearbeitet;
    }

    public void setBearbeitet(Boolean bearbeitet) {
        this.bearbeitet = bearbeitet;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getErstellungsDatum() {
        return erstellungsDatum;
    }

    public void setErstellungsDatum(String erstellungsDatum) {
        this.erstellungsDatum = erstellungsDatum;
    }

    public dbKlausur getKlausur() {
        return klausur;
    }

    public void setKlausur(dbKlausur klausur) {
        this.klausur = klausur;
    }
}
