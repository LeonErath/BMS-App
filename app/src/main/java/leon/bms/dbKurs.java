package leon.bms;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Leon E on 19.11.2015.
 */

/**
 * @dbKurs ist eine Tabelle der Datenbank für die Kurse
 * Sie verwaltet alle Einträge der Kurse und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 * Die Kurse Tabelle ist der Mittelpunkt der App den alles dreht sich um die Kurse. Daraus resultiert
 * auch , dass die Kurse Tabelle die größte und aktivste Tabelle ist.
 */
public class dbKurs extends SugarRecord {

    //Datensätze der Kurse
    public String name;
    // kursart 0 LK 1 GK 2 AG 3 PK
    public int kursart;
    /**
     * @aktiv zeigt ob ein Kurs vom User ausgewählt worden ist. Wichtig für die Erstellung des
     * Stundenplans
     * @schriftlich nacher wichtig für das laden des Klausurplans
     */
    public Boolean aktiv;
    public String fach;
    public Boolean schriftlich;
    String geandertAm;
    String hinzugefuegtAm;
    int icon;
    int schuljahr;
    Boolean archiviert;

    // defining a relationship
    dbAufgabe aufgabe;
    dbKlausur klausur;
    dbNote note;
    dbUser user;
    public dbLehrer lehrer;
    dbSchulstunde stunde;
    dbKursTagConnect dbKursTagConnect;

    public dbKurs() {
        //empty Constructor needed!
    }

    public dbKurs(String name, int kursart, Boolean aktiv, String fach, Boolean schriftlich, String geandertAm, String hinzugefuegtAm, int icon, int schuljahr, Boolean archiviert, dbAufgabe aufgabe, dbKlausur klausur, dbNote note, dbUser user, dbLehrer lehrer, dbSchulstunde stunde, leon.bms.dbKursTagConnect dbKursTagConnect) {
        this.name = name;
        this.kursart = kursart;
        this.aktiv = aktiv;
        this.fach = fach;
        this.schriftlich = schriftlich;
        this.geandertAm = geandertAm;
        this.hinzugefuegtAm = hinzugefuegtAm;
        this.icon = icon;
        this.schuljahr = schuljahr;
        this.archiviert = archiviert;
        this.aufgabe = aufgabe;
        this.klausur = klausur;
        this.note = note;
        this.user = user;
        this.lehrer = lehrer;
        this.stunde = stunde;
        this.dbKursTagConnect = dbKursTagConnect;
    }

    public leon.bms.dbKursTagConnect getDbKursTagConnect() {
        return dbKursTagConnect;
    }

    public void setDbKursTagConnect(leon.bms.dbKursTagConnect dbKursTagConnect) {
        this.dbKursTagConnect = dbKursTagConnect;
    }

    public String getGeandertAm() {
        return geandertAm;
    }

    public void setGeandertAm(String geandertAm) {
        this.geandertAm = geandertAm;
    }

    public String getHinzugefuegtAm() {
        return hinzugefuegtAm;
    }

    public void setHinzugefuegtAm(String hinzugefuegtAm) {
        this.hinzugefuegtAm = hinzugefuegtAm;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getSchuljahr() {
        return schuljahr;
    }

    public void setSchuljahr(int schuljahr) {
        this.schuljahr = schuljahr;
    }

    public Boolean getArchiviert() {
        return archiviert;
    }

    public void setArchiviert(Boolean archiviert) {
        this.archiviert = archiviert;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKursart() {
        return kursart;
    }

    public void setKursart(int kursart) {
        this.kursart = kursart;
    }

    public Boolean getAktiv() {
        return aktiv;
    }

    public void setAktiv(Boolean aktiv) {
        this.aktiv = aktiv;
    }


    public String getFach() {
        return fach;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }

    public Boolean getSchriftlich() {
        return schriftlich;
    }

    public void setSchriftlich(Boolean schriftlich) {
        this.schriftlich = schriftlich;
    }

    public dbAufgabe getAufgabe() {
        return aufgabe;
    }

    public void setAufgabe(dbAufgabe aufgabe) {
        this.aufgabe = aufgabe;
    }

    public dbKlausur getKlausur() {
        return klausur;
    }

    public void setKlausur(dbKlausur klausur) {
        this.klausur = klausur;
    }

    public dbNote getNote() {
        return note;
    }

    public void setNote(dbNote note) {
        this.note = note;
    }

    public dbUser getUser() {
        return user;
    }

    public void setUser(dbUser user) {
        this.user = user;
    }

    public dbLehrer getLehrer() {
        return lehrer;
    }

    public void setLehrer(dbLehrer lehrer) {
        this.lehrer = lehrer;
    }

    public dbSchulstunde getStunde() {
        return stunde;
    }

    public void setStunde(dbSchulstunde stunde) {
        this.stunde = stunde;
    }


    /**
     * @kursVorhanden gibt an ob der übergebene Kurs vorhanden ist.
     * Dazu wird geguckt ob der Name des Kurses in der Kurstabelle vorkommt.
     * Wenn ja und die Kursart der beiden Kurse größer als 1 sind ist der Kurs nicht vorhanden
     * -> dies liegt daran das AGs und PK den gleichen Namen haben können also wird geguckt ob der andere
     * kurs ein PK oder AG ist (kursart > 1)
     * Wenn ja und die Kursart gleich oder unter 1 ist dann ist der Kurs vorhanden
     * Wenn nein ist der Kurs nicht vorhanden
     */
    public Boolean kursVorhanden(dbKurs kurs) {

        if (dbKurs.find(dbKurs.class, "name = ?", kurs.name).size() > 0) {
            dbKurs kurs2 = dbKurs.find(dbKurs.class, "name = ?", kurs.name).get(0);
            if (kurs.kursart != kurs2.kursart) {
                if (kurs.kursart > 1 && kurs2.kursart > 1) {
                    return false;
                }
            }
            Log.d("TAG", "KURS VORHANDEN!");
            return true;
        } else {
            return false;
        }
    }

    /**
     * @countKurse ist eine einfache Methode um die Anzahl alle Kurse herauszubekommen
     */
    public int countKurse() {
        List<dbKurs> dbKursList = dbKurs.listAll(dbKurs.class);
        return dbKursList.size();
    }

    /**
     * @kursartListe gibt alle Kurse einer bestimmten kursart wieder
     */
    public List<dbKurs> kursartListe(int kursart) {
        List<dbKurs> dbKursList = dbKurs.find(dbKurs.class, "kursart = ?", String.valueOf(kursart));
        return dbKursList;
    }

    /**
     * @getSchulStunden gibt alle Schulstunden des Kurses zurück // Hier kommen die Relationships
     * ins Spiel
     */
    public List<dbSchulstunde> getSchulStunden(long id) {
        return dbSchulstunde.find(dbSchulstunde.class, "kurs = ?", String.valueOf(id));
    }

    /**
     * @getActiveKurse gibt alle Kurse einer Kursart zurück die aktiv sind also bei der Kursauswahl
     * ausgwählt worden sind
     */
    public List<dbKurs> getActiveKurse(int kursart) {
        List<dbKurs> dbKursList = dbKurs.find(dbKurs.class, "kursart = ? and aktiv = ?", String.valueOf(kursart), "1");
        return dbKursList;
    }

    /**
     * @getAllActiveKurse gibt alle Kurse zurück die aktiv sind also bei der Kursauswahl
     * ausgwählt worden sind
     */
    public List<dbKurs> getAllActiveKurse() {
        List<dbKurs> dbKursList = dbKurs.find(dbKurs.class, "aktiv = ?", "1");
        return dbKursList;
    }

    /**
     * @getSchulStunden gibt alle Aufgaben des Kurses zurück // Hier kommen die Relationships
     * ins Spiel
     */
    public List<dbAufgabe> getAufgaben(long id) {
        return find(dbAufgabe.class, "kurs = ?", String.valueOf(id));
    }

    /**
     * @getKursWithFach gibt den Kurs zurück des fach
     * Ist bei der AufgabenActivity von Relevanz
     */
    public dbKurs getKursWithFach(String fach) {
        List<dbKurs> dbKursList = dbKurs.find(dbKurs.class, "fach = ?", fach);
        return dbKursList.get(0);
    }

    /**
     * @param id ist die ID des Kurses durch den die Themenbereich herausgesucht werden
     * @return gibt wenn Themenbereich vorhanden diesen zurück sonst null
     * @getThemenbereiche gibt alle themenbereich eines Kurses zurück. Wichtig fü die erstellung des
     * Quizes
     */
    public List<dbThemenbereich> getThemenbereiche(long id) {
        if (find(dbThemenbereich.class, "kurs = ?", String.valueOf(id)).size() != 0) {
            return find(dbThemenbereich.class, "kurs = ?", String.valueOf(id));
        } else {
            return null;
        }
    }

    /**
     * @param kursid ist die Kursid eines bestimmtes Kurses z.B.: M L1
     * @return gibt den Kurs zurück wenn er existiret sonst null
     * @getKursWitchKursid giibt ein Kurs zurürck durch die kursid
     */
    public dbKurs getKursWithKursid(String kursid) {
        List<dbKurs> dbKursList = dbKurs.find(dbKurs.class, "name = ?", kursid);
        if (dbKursList.size() == 1) {
            return dbKursList.get(0);
        } else {
            return null;
        }
    }

    /**
     * @param kursid damit werden die Schulstunden rausgesucht
     * @param week   ist der spezielle Wochentag
     * @return gibt die Schulstunden zurück wenn sie exisitieren sonst null
     * @getSchulstundeWithWeekAndKurs gibt alle Schulstune eines bestimmten Kurses an einem bestimmten
     * wochentag. Wichtig für das erstellen und anzeigen des Stundenplan des Users
     */
    public List<dbSchulstunde> getSchulstundeWithWeekAndKurs(long kursid, int week) {
        List<dbSchulstunde> dbSchulstundeList = dbSchulstunde.find(dbSchulstunde.class, "kurs = ? and wochentag = ?", String.valueOf(kursid), String.valueOf(week));
        if (dbSchulstundeList.size() > 0) {
            return dbSchulstundeList;
        } else {
            return null;
        }
    }

    /**
     * @param id ist die ID des Kurses von dem die Tags rausgesucht werden soll
     * @return gibt eine Liste der Tags zurück falls der Kurs welchen besitzt sonst null
     * @getWebsiteTags gibt alle Tags des Kurses zurück. Wichtig für das personalisierte Darstellen
     * der Website Artikel
     */
    public List<dbWebsiteTag> getWebsiteTags(Long id) {
        List<dbWebsiteTag> dbWebsiteTagList = dbWebsiteTag.find(dbWebsiteTag.class, "kurs = ?", String.valueOf(id));
        if (dbWebsiteTagList.size() > 0) {
            return dbWebsiteTagList;
        } else {
            return null;
        }
    }

    /**
     * @param kursid ist die Kursid mit der der Kurs in der Datenbank gespiechert ist
     * @return wenn der Kurs in der Datenbank vorhanden ist gibt er diesen zurück sonst null
     * @gerKursWithID gibt einen Kurs zurück über die kursid der Datenbank
     */
    public dbKurs getKursWithID(long kursid) {
        List<dbKurs> kursList = dbKurs.find(dbKurs.class, "id = ?", String.valueOf(kursid));
        if (kursList.size() == 1) {
            return kursList.get(0);
        } else {
            return null;
        }
    }


}
