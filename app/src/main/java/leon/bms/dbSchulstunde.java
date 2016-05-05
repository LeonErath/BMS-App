package leon.bms;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Leon E on 19.11.2015.
 */

/**
 * @dbSchulstunde ist eine Tabelle der Datenbank für die Schulstunde
 * Sie verwaltet alle Einträge der Schulstunde und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbSchulstunde extends SugarRecord {
    //Datensätze des Schulstunde
    Double dauer;
    public Integer beginnzeit;
    Integer endZeit;
    String zuletztAktualisiert;
    public String kursID;
    public String raum;
    public Integer wochentag;
    // defining a relationship
    public dbKurs kurs;
    public dbLehrer lehrer;
    //dbRaum raum;

    public dbSchulstunde() {
        // empty Constructor needed!
    }

    public dbSchulstunde(Double dauer, Integer beginnZeit, Integer endZeit, String kursID, String raum, Integer wochentag, dbKurs kurs, dbLehrer lehrer) {
        this.dauer = dauer;
        this.beginnzeit = beginnZeit;
        this.endZeit = endZeit;
        this.kursID = kursID;
        this.raum = raum;
        this.wochentag = wochentag;
        this.kurs = kurs;
        this.lehrer = lehrer;
    }

    public Double getDauer() {
        return dauer;
    }

    public void setDauer(Double dauer) {
        this.dauer = dauer;
    }

    public Integer getBeginnzeit() {
        return beginnzeit;
    }

    public void setBeginnzeit(Integer beginnzeit) {
        this.beginnzeit = beginnzeit;
    }

    public Integer getEndZeit() {
        return endZeit;
    }

    public void setEndZeit(Integer endZeit) {
        this.endZeit = endZeit;
    }

    public String getKursID() {
        return kursID;
    }

    public void setKursID(String kursID) {
        this.kursID = kursID;
    }

    public Integer getWochentag() {
        return wochentag;
    }

    public void setWochentag(Integer wochentag) {
        this.wochentag = wochentag;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }

    public dbLehrer getLehrer() {
        return lehrer;
    }

    public void setLehrer(dbLehrer lehrer) {
        this.lehrer = lehrer;
    }

    public String getRaum() {
        return raum;
    }

    public void setRaum(String raum) {
        this.raum = raum;
    }

    /**
     * @param week ist der Wochentag der gesuchten Schulstunde
     * @param time ist die Zeit dieser Schulstunde
     * @return gibt diese Schulstunde zurück wenn sie vorhanden ist
     * @getSchulstundenByWeekAndTime gibt eine Schulstunde in einer Woche zu einer bestimmte Zeit zurück
     */
    public dbSchulstunde getSchulstundeByWeekAndTime(int week, int time) {
        List<dbSchulstunde> schulstundeList = dbSchulstunde.find(dbSchulstunde.class, "wochentag = ? and beginnzeit = ?", String.valueOf(week), String.valueOf(time));
        if (schulstundeList.size() != 0) {
            return schulstundeList.get(0);
        } else {
            return null;
        }
    }


}
