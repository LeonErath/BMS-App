package leon.bms.database;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 19.11.2015.
 */

/**
 * @dbKlausuraufsicht ist eine Tabelle der Datenbank für die Klausuraufsicht
 * Sie verwaltet alle Einträge der Klausuraufsicht und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbKlausuraufsicht extends SugarRecord {

    //Datensätze der Klausuraufsicht
    public String start;
    public String end;

    // defining a relationship
    public dbKlausur klausur;
    public dbLehrer lehrer;

    public dbKlausuraufsicht() {
        //empty Constructor needed!
    }

    public dbKlausuraufsicht(String start, String end, dbKlausur klausur, dbLehrer lehrer) {
        this.start = start;
        this.end = end;
        this.klausur = klausur;
        this.lehrer = lehrer;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public dbKlausur getKlausur() {
        return klausur;
    }

    public void setKlausur(dbKlausur klausur) {
        this.klausur = klausur;
    }

    public dbLehrer getLehrer() {
        return lehrer;
    }

    public void setLehrer(dbLehrer lehrer) {
        this.lehrer = lehrer;
    }
}
