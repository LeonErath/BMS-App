package leon.bms.realm;

import io.realm.RealmObject;


/**
 * Created by Leon E on 18.06.2016.
 */
public class dbFehler extends RealmObject {
    private Boolean bearbeitet;
    private String beschreibung;
    private String erstellungsDatum;

    private dbKlausur klausur;

    public dbFehler() {
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
