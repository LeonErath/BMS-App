package leon.bms.database;

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
    public Double dauer;
    public Integer beginnzeit;
    public Integer endZeit;
    public String zuletztAktualisiert;
    public String kursID;
    public Integer wochentag;
    public int blocknummer;
    public int serverId;
    // defining a relationship
    public dbKurs kurs;
    public dbLehrer lehrer;
    public dbVertretung vertretung;
    public dbRaum raum;

    public dbSchulstunde() {
        // empty Constructor needed!
    }

    public dbSchulstunde(Double dauer, Integer beginnzeit, Integer endZeit, String zuletztAktualisiert, String kursID, Integer wochentag, int blocknummer, int serverId, dbKurs kurs, dbLehrer lehrer, dbVertretung vertretung, dbRaum raum) {
        this.dauer = dauer;
        this.beginnzeit = beginnzeit;
        this.endZeit = endZeit;
        this.zuletztAktualisiert = zuletztAktualisiert;
        this.kursID = kursID;
        this.wochentag = wochentag;
        this.blocknummer = blocknummer;
        this.serverId = serverId;
        this.kurs = kurs;
        this.lehrer = lehrer;
        this.vertretung = vertretung;
        this.raum = raum;
    }



    public String getZuletztAktualisiert() {
        return zuletztAktualisiert;
    }

    public void setZuletztAktualisiert(String zuletztAktualisiert) {
        this.zuletztAktualisiert = zuletztAktualisiert;
    }

    public dbVertretung getVertretung() {
        return vertretung;
    }

    public void setVertretung(dbVertretung vertretung) {
        this.vertretung = vertretung;
    }

    public void setRaum(dbRaum raum) {
        this.raum = raum;
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

    public dbVertretung getVertretung(long id) {
        List<dbVertretung> vertretungs = dbVertretung.find(dbVertretung.class, "schulstunde = ?",String.valueOf(id));
        if (vertretungs.size() != 0) {
            return vertretungs.get(0);
        } else {
            return null;
        }
    }


}
