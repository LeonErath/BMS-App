package leon.bms.database;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 19.11.2015.
 */

/**
 * @dbNote ist eine Tabelle der Datenbank für die Note
 * Sie verwaltet alle Einträge der Note und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbNote extends SugarRecord {
    //Datensätze für die Note
    public String beschreibung;
    public String hinzugefuegtAm;
    public Double klassendurchschnitt;
    public Integer punkte;
    public Boolean schriftlich;
    // defining a relationship
    public dbKlausur klausur;
    public dbKurs kurs;

    public dbNote() {
        //empty Constructor needed!
    }

    public dbNote(String beschreibung, String hinzugefuegtAm, Double klassendurchschnitt, Integer punkte, Boolean schriftlich, dbKlausur klausur, dbKurs kurs) {
        this.beschreibung = beschreibung;
        this.hinzugefuegtAm = hinzugefuegtAm;
        this.klassendurchschnitt = klassendurchschnitt;
        this.punkte = punkte;
        this.schriftlich = schriftlich;
        this.klausur = klausur;
        this.kurs = kurs;
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

    public Double getKlassendurchschnitt() {
        return klassendurchschnitt;
    }

    public void setKlassendurchschnitt(Double klassendurchschnitt) {
        this.klassendurchschnitt = klassendurchschnitt;
    }

    public Integer getPunkte() {
        return punkte;
    }

    public void setPunkte(Integer punkte) {
        this.punkte = punkte;
    }

    public Boolean getSchriftlich() {
        return schriftlich;
    }

    public void setSchriftlich(Boolean schriftlich) {
        this.schriftlich = schriftlich;
    }


}
