package leon.bms.database;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Leon E on 19.11.2015.
 */

/**
 * @dbRaum ist eine Tabelle der Datenbank für die Raum
 * Sie verwaltet alle Einträge der Räume und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbRaum extends SugarRecord {
    //Datensätze des Raumes
    public int serverid;
    public Boolean beamer;
    public String beschreibung;
    public Boolean computer;
    public String funktion;
    public Integer kapazitaet;
    public Boolean lautsprecher;
    public String nummer;
    // defining a relationship
    public dbKlausur klausur;
    public  dbSchulstunde stunden;
    public dbVertretung vertretung;

    public dbRaum() {
        //empty Contructor needed!
    }

    public dbRaum(int serverId, Boolean beamer, String beschreibung, Boolean computer, String funktion, Integer kapazitaet, Boolean lautsprecher, String nummer, dbKlausur klausur, dbSchulstunde stunden, dbVertretung vertretung) {
        this.serverid = serverId;
        this.beamer = beamer;
        this.beschreibung = beschreibung;
        this.computer = computer;
        this.funktion = funktion;
        this.kapazitaet = kapazitaet;
        this.lautsprecher = lautsprecher;
        this.nummer = nummer;
        this.klausur = klausur;
        this.stunden = stunden;
        this.vertretung = vertretung;
    }

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public dbKlausur getKlausur() {
        return klausur;
    }

    public void setKlausur(dbKlausur klausur) {
        this.klausur = klausur;
    }

    public dbSchulstunde getStunden() {
        return stunden;
    }

    public void setStunden(dbSchulstunde stunden) {
        this.stunden = stunden;
    }

    public dbVertretung getVertretung() {
        return vertretung;
    }

    public void setVertretung(dbVertretung vertretung) {
        this.vertretung = vertretung;
    }

    public Boolean getBeamer() {
        return beamer;
    }

    public void setBeamer(Boolean beamer) {
        this.beamer = beamer;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Boolean getComputer() {
        return computer;
    }

    public void setComputer(Boolean computer) {
        this.computer = computer;
    }

    public String getFunktion() {
        return funktion;
    }

    public void setFunktion(String funktion) {
        this.funktion = funktion;
    }

    public Integer getKapazitaet() {
        return kapazitaet;
    }

    public void setKapazitaet(Integer kapazitaet) {
        this.kapazitaet = kapazitaet;
    }

    public Boolean getLautsprecher() {
        return lautsprecher;
    }

    public void setLautsprecher(Boolean lautsprecher) {
        this.lautsprecher = lautsprecher;
    }

    public String getNummer() {
        return nummer;
    }

    public void setNummer(String nummer) {
        this.nummer = nummer;
    }

    public dbRaum getRaumWithId(int id) {
        List<dbRaum> dbRaumList = dbRaum.find(dbRaum.class, "serverid = ?", String.valueOf(id));
        if (dbRaumList != null && dbRaumList.size()== 1){
            return dbRaumList.get(0);
        }
        return null;
    }

    public int countRaum() {
        List<dbRaum> dbRaumList = dbRaum.listAll(dbRaum.class);
        return dbRaumList.size();
    }
}
