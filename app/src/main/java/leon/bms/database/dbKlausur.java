package leon.bms.database;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 19.11.2015.
 */

/**
 * @dbKlausur ist eine Tabelle der Datenbank für die Klausuren
 * Sie verwaltet alle Einträge der Klausur und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbKlausur extends SugarRecord {

    //Datensätze der Klausur
    public String beginn;
    public String datum;
    public String ende;
    public String nachschreibertermin;
    public String notizen;
    public String name;
    public int serverid;
    public Boolean restkursFrei;

    // defining a relationship
    public dbKlausuraufsicht aufsicht;
    public dbKurs kurs;
    public dbNote note;
    public dbRaum raum;
    public dbFehler fehler;
    public dbKlausurinhalt klausurinhalt;


    public dbKlausur() {
        //empty Constructor needed!
    }

    public dbKlausur(String beginn, String datum, String ende, String nachschreibertermin, String notizen, String name, int serverid, Boolean restkursFrei, dbKlausuraufsicht aufsicht, dbKurs kurs, dbNote note, dbRaum raum, dbFehler fehler, dbKlausurinhalt klausurinhalt) {
        this.beginn = beginn;
        this.datum = datum;
        this.ende = ende;
        this.nachschreibertermin = nachschreibertermin;
        this.notizen = notizen;
        this.name = name;
        this.serverid = serverid;
        this.restkursFrei = restkursFrei;
        this.aufsicht = aufsicht;
        this.kurs = kurs;
        this.note = note;
        this.raum = raum;
        this.fehler = fehler;
        this.klausurinhalt = klausurinhalt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public String getBeginn() {
        return beginn;
    }

    public void setBeginn(String beginn) {
        this.beginn = beginn;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getEnde() {
        return ende;
    }

    public void setEnde(String ende) {
        this.ende = ende;
    }

    public String getNachschreibertermin() {
        return nachschreibertermin;
    }

    public void setNachschreibertermin(String nachschreibertermin) {
        this.nachschreibertermin = nachschreibertermin;
    }

    public String getNotizen() {
        return notizen;
    }

    public void setNotizen(String notizen) {
        this.notizen = notizen;
    }

    public Boolean getRestkursFrei() {
        return restkursFrei;
    }

    public void setRestkursFrei(Boolean restkursFrei) {
        this.restkursFrei = restkursFrei;
    }

    public dbKlausuraufsicht getAufsicht() {
        return aufsicht;
    }

    public void setAufsicht(dbKlausuraufsicht aufsicht) {
        this.aufsicht = aufsicht;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }

    public dbNote getNote() {
        return note;
    }

    public void setNote(dbNote note) {
        this.note = note;
    }

    public dbRaum getRaum() {
        return raum;
    }

    public void setRaum(dbRaum raum) {
        this.raum = raum;
    }

    public dbFehler getFehler() {
        return fehler;
    }

    public void setFehler(dbFehler fehler) {
        this.fehler = fehler;
    }

    public dbKlausurinhalt getKlausurinhalt() {
        return klausurinhalt;
    }

    public void setKlausurinhalt(dbKlausurinhalt klausurinhalt) {
        this.klausurinhalt = klausurinhalt;
    }
}
