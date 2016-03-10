package leon.bms;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 19.11.2015.
 */

/**
 * @dbRaum ist eine Tabelle der Datenbank für die Raum
 * Sie verwaltet alle Einträge der Räume und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbRaum extends SugarRecord<dbRaum> {
    //Datensätze des Raumes
    Boolean beamer;
    String beschreibung;
    Boolean computer;
    String funktion;
    Integer kapazitaet;
    Boolean lautsprecher;
    String nummer;
    // defining a relationship
    dbKlausur klausur;
    dbSchulstunde stunden;

    public dbRaum() {
        //empty Contructor needed!
    }

    public dbRaum(Boolean beamer, String beschreibung, Boolean computer, String funktion, Integer kapazitaet, Boolean lautsprecher, String nummer) {

        this.beamer = beamer;
        this.beschreibung = beschreibung;
        this.computer = computer;
        this.funktion = funktion;
        this.kapazitaet = kapazitaet;
        this.lautsprecher = lautsprecher;
        this.nummer = nummer;
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


}
