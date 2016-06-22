package leon.bms.realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;



import android.util.Log;

import java.util.List;

/**
 * Created by Leon E on 22.12.2015.
 */

/**
 * @dbUserist eine Tabelle der Datenbank für die User
 * Sie verwaltet alle Einträge der User und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbUser extends RealmObject {
    // Datensätze der User
    @PrimaryKey @Required
    private String session_hash;
    private String grade_string;
    private String benutzername;
    private Boolean loggedIn;
    private String last_name;
    private Boolean eingerichtet;
    private Boolean punktesystem;
    private Boolean validData;
    private String first_name;
    private String schulklasse;
    private String geburtstag;
    private String lastAppOpen;
    private String lastDataUpdate;
    private String lastServerConnection;
    private String email;
    private String sex;
    private Boolean blocked;
    private int quizPunkte;
    private String quizLastUpdate;
    private Boolean isLehrer;
    // defining a relationship
    private dbSchule schule;
    private dbSchuljahr schuljahr;
    private dbAufgabe aufgabe;
    private  dbMitteilung mitteilung;
    private  dbKurs kurs;


    public dbUser() {
        //empty Constructor needed!

    }

    public String getSession_hash() {
        return session_hash;
    }

    public void setSession_hash(String session_hash) {
        this.session_hash = session_hash;
    }

    public String getGrade_string() {
        return grade_string;
    }

    public void setGrade_string(String grade_string) {
        this.grade_string = grade_string;
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

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public Boolean getValidData() {
        return validData;
    }

    public void setValidData(Boolean validData) {
        this.validData = validData;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSchulklasse() {
        return schulklasse;
    }

    public void setSchulklasse(String schulklasse) {
        this.schulklasse = schulklasse;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
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

    public Boolean getLehrer() {
        return isLehrer;
    }

    public void setLehrer(Boolean lehrer) {
        isLehrer = lehrer;
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

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }
}
