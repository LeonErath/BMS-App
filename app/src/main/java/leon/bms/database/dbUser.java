package leon.bms.database;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Leon E on 22.12.2015.
 */

/**
 * @dbUserist eine Tabelle der Datenbank für die User
 * Sie verwaltet alle Einträge der User und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbUser extends SugarRecord {
    // Datensätze der User
    public String benutzername;
    public String geburtstag;
    public String lastAppOpen;
    public String lastDataUpdate;
    public String lastServerConnection;
    public Boolean loggedIn;
    public String nachname;
    public String stufe;
    public String vorname;
    public String email;
    public String geschlecht;
    public Boolean blocked;
    public int quizPunkte;
    public String quizLastUpdate;
    public String schulklasse;
    public Boolean isLehrer;
    public Boolean eingerichtet;
    public Boolean punktesystem;
    public Boolean validData;
    // defining a relationship
    public dbSchule schule;
    public dbSchuljahr schuljahr;
    public dbAufgabe aufgabe;
    public  dbMitteilung mitteilung;
    public  dbKurs kurs;

    public dbUser() {
        //empty Constructor needed!

    }

    public dbUser(String benutzername, String geburtstag, String lastAppOpen, String lastDataUpdate, String lastServerConnection, Boolean loggedIn, String nachname, String stufe, String vorname, String email, String geschlecht, Boolean blocked, int quizPunkte, String quizLastUpdate, String schulklasse, Boolean isLehrer, Boolean eingerichtet, Boolean punktesystem, Boolean validData, dbSchule schule, dbSchuljahr schuljahr, dbAufgabe aufgabe, dbMitteilung mitteilung, dbKurs kurs) {
        this.benutzername = benutzername;
        this.geburtstag = geburtstag;
        this.lastAppOpen = lastAppOpen;
        this.lastDataUpdate = lastDataUpdate;
        this.lastServerConnection = lastServerConnection;
        this.loggedIn = loggedIn;
        this.nachname = nachname;
        this.stufe = stufe;
        this.vorname = vorname;
        this.email = email;
        this.geschlecht = geschlecht;
        this.blocked = blocked;
        this.quizPunkte = quizPunkte;
        this.quizLastUpdate = quizLastUpdate;
        this.schulklasse = schulklasse;
        this.isLehrer = isLehrer;
        this.eingerichtet = eingerichtet;
        this.punktesystem = punktesystem;
        this.validData = validData;
        this.schule = schule;
        this.schuljahr = schuljahr;
        this.aufgabe = aufgabe;
        this.mitteilung = mitteilung;
        this.kurs = kurs;
    }

    public int getQuizPunkte() {
        return quizPunkte;
    }

    public void setQuizPunkte(int quizPunkte) {
        this.quizPunkte = quizPunkte;
    }

    public String getQuizLastUpdate() {
        return quizLastUpdate;
    }

    public void setQuizLastUpdate(String quizLastUpdate) {
        this.quizLastUpdate = quizLastUpdate;
    }

    public String getSchulklasse() {
        return schulklasse;
    }

    public void setSchulklasse(String schulklasse) {
        this.schulklasse = schulklasse;
    }

    public Boolean getLehrer() {
        return isLehrer;
    }

    public void setLehrer(Boolean lehrer) {
        isLehrer = lehrer;
    }

    public Boolean getEingerichtet() {
        return eingerichtet;
    }

    public void setEingerichtet(Boolean eingerichtet) {
        this.eingerichtet = eingerichtet;
    }

    public Boolean getPunktesystem() {
        return punktesystem;
    }

    public void setPunktesystem(Boolean punktesystem) {
        this.punktesystem = punktesystem;
    }

    public dbSchule getSchule() {
        return schule;
    }

    public void setSchule(dbSchule schule) {
        this.schule = schule;
    }

    public dbSchuljahr getSchuljahr() {
        return schuljahr;
    }

    public void setSchuljahr(dbSchuljahr schuljahr) {
        this.schuljahr = schuljahr;
    }

    public dbAufgabe getAufgabe() {
        return aufgabe;
    }

    public void setAufgabe(dbAufgabe aufgabe) {
        this.aufgabe = aufgabe;
    }

    public dbMitteilung getMitteilung() {
        return mitteilung;
    }

    public void setMitteilung(dbMitteilung mitteilung) {
        this.mitteilung = mitteilung;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Boolean getValidData() {
        return validData;
    }

    public void setValidData(Boolean validData) {
        this.validData = validData;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(String geschlecht) {
        this.geschlecht = geschlecht;
    }

    public String getGeburtstag() {
        return geburtstag;
    }

    public void setGeburtstag(String geburtstag) {
        this.geburtstag = geburtstag;
    }

    public String getLastAppOpen() {
        return lastAppOpen;
    }

    public void setLastAppOpen(String lastAppOpen) {
        this.lastAppOpen = lastAppOpen;
    }

    public String getLastDataUpdate() {
        return lastDataUpdate;
    }

    public void setLastDataUpdate(String lastDataUpdate) {
        this.lastDataUpdate = lastDataUpdate;
    }

    public String getLastServerConnection() {
        return lastServerConnection;
    }

    public void setLastServerConnection(String lastServerConnection) {
        this.lastServerConnection = lastServerConnection;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getStufe() {
        return stufe;
    }

    public void setStufe(String stufe) {
        this.stufe = stufe;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    /**
     * @getUser gibt den aktuelle User zurück
     */
    public dbUser getUser() {
        List<dbUser> dbUserList = dbUser.listAll(dbUser.class);
        Log.d("dbUSer", dbUserList.size() + "");
        return dbUserList.get(0);
    }


    /**
     * @checkUser guckt ob ein User vorhanden ist und Daten beinhaltet
     */
    public Boolean checkUser() {
        if (dbUser.listAll(dbUser.class).size() != 0) {
            return true;
        }
        return false;
    }


}
