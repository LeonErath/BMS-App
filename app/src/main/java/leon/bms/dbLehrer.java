package leon.bms;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Leon E on 19.11.2015.
 */

/**
 * @dbLehrer ist eine Tabelle der Datenbank für die Lehrer
 * Sie verwaltet alle Einträge der Lehrer und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbLehrer extends SugarRecord<dbLehrer> {
    //Datensätze der Lehrer
    public String titel;
    String faecher;
    public String email;
    public String kuerzel;
    public String Vorname;
    public String name;
    // defining a relationship
    dbKlausuraufsicht aufsicht;
    dbKurs kurs;
    dbSchulstunde schulstunde;

    public dbLehrer() {
        //empty Constructor needed!
        titel = "";
        name = "";
    }

    public dbLehrer(String titel, String faecher, String email, String kuerzel, String vorname, String name, dbKlausuraufsicht aufsicht, dbKurs kurs, dbSchulstunde schulstunde) {
        this.titel = titel;
        this.faecher = faecher;
        this.email = email;
        this.kuerzel = kuerzel;
        Vorname = vorname;
        this.name = name;
        this.aufsicht = aufsicht;
        this.kurs = kurs;
        this.schulstunde = schulstunde;
    }

    /**
     * @param suchKategorie ist die Kategorie mit der man den Lehrer raussuchen möchte
     * @param suchWort      ist das Wort womit der Lehrer in der entsprechenden Kategorie gepseichert ist
     * @return gibt den gesucht Lehrer zurück
     * @getLehrer abstrakte Methode um einen Lehrer nach bestimmen Kategorien rauszusuchen
     */
    public dbLehrer getLehrer(String suchKategorie, String suchWort) {
        List<dbLehrer> lehrer = dbLehrer.find(dbLehrer.class, suchKategorie + " = ?", suchWort);
        dbLehrer lehrer1 = lehrer.get(0);
        return lehrer1;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getFaecher() {
        return faecher;
    }

    public void setFaecher(String faecher) {
        this.faecher = faecher;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKuerzel() {
        return kuerzel;
    }

    public void setKuerzel(String kuerzel) {
        this.kuerzel = kuerzel;
    }

    public String getVorname() {
        return Vorname;
    }

    public void setVorname(String vorname) {
        Vorname = vorname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public dbSchulstunde getSchulstunde() {
        return schulstunde;
    }

    public void setSchulstunde(dbSchulstunde schulstunde) {
        this.schulstunde = schulstunde;
    }

    /**
     * @sucheLehrer sucht einen Lehrer nach seinem Lehrerkürzel raus
     */
    public dbLehrer sucheLehrer(String LKZ) {
        List<dbLehrer> dbLehrerList = dbLehrer.find(dbLehrer.class, "kuerzel = ?", LKZ);
        return dbLehrerList.get(0);
    }

    /**
     * @sucheLehrer gibt die Anzahl alle Lehrer zurück
     */
    public int countLehrer() {
        List<dbLehrer> dbLehrerList = dbLehrer.listAll(dbLehrer.class);
        return dbLehrerList.size();
    }


}
