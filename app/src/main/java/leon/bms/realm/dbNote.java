package leon.bms.realm;


import io.realm.RealmObject;
import io.realm.annotations.Required;


/**
 * Created by Leon E on 17.06.2016.
 */
public class dbNote extends RealmObject {
    @Required
    private Integer punkte;
    private String beschreibung;
    private String hinzugefuegtAm;
    private Double klassendurchschnitt;
    private Boolean schriftlich;
    // defining a relationship
    private  dbKlausur klausur;
    private dbKurs kurs;

    public dbNote() {
        //empty Constructor needed!
    }

    public Integer getPunkte() {
        return punkte;
    }

    public void setPunkte(Integer punkte) {
        this.punkte = punkte;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getHinzugefuegtAm() {
        return hinzugefuegtAm;
    }

    public void setHinzugefuegtAm(String hinzugefuegtAm) {
        this.hinzugefuegtAm = hinzugefuegtAm;
    }

    public Double getKlassendurchschnitt() {
        return klassendurchschnitt;
    }

    public void setKlassendurchschnitt(Double klassendurchschnitt) {
        this.klassendurchschnitt = klassendurchschnitt;
    }

    public Boolean getSchriftlich() {
        return schriftlich;
    }

    public void setSchriftlich(Boolean schriftlich) {
        this.schriftlich = schriftlich;
    }

    public dbKlausur getKlausur() {
        return klausur;
    }

    public void setKlausur(dbKlausur klausur) {
        this.klausur = klausur;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }
}
