package leon.bms;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 19.11.2015.
 */

/** @dbNote ist eine Tabelle der Datenbank für die Note
 *  Sie verwaltet alle Einträge der Note und beinhaltet Filter für die weitere
 *  Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbNote extends SugarRecord<dbNote> {
    //Datensätze für die Note
    Double klassendurchschnitt;
    Integer punkte;
    Boolean schriftlich;
    // defining a relationship
    dbKlausur klausur;
    dbKurs kurs;
    public dbNote() {
    }

    public dbNote(Double klassendurchschnitt, Integer punkte, Boolean schriftlich, dbKlausur klausur, dbKurs kurs) {
        this.klassendurchschnitt = klassendurchschnitt;
        this.punkte = punkte;
        this.schriftlich = schriftlich;
        this.klausur = klausur;
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
