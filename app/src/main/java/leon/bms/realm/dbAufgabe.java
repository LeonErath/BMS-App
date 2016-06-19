package leon.bms.realm;

import android.content.Context;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * @dbAufgabe ist eine Tabelle der Datenbank für die Aufgaben
 * Sie verwaltet alle Einträge der Aufgaben und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbAufgabe extends RealmObject {
    @PrimaryKey
    private int id;
    private String abgabeDatum;
    private String erinnerungDate;
    private String erstelltAm;
    private String zuletztAktualisiert;
    private Boolean erledigt;
    private String beschreibung;
    private String notizen;
    private String titel;
    private Integer priorität;

    // defining a relationship
    private dbKurs kurs;
    private dbMediaFile mediaFile;
    private dbUser user;

    //Auto Increment ID
    public dbAufgabe(Realm realm) {
        int primaryKeyValue;
        if (realm.where(dbAufgabe.class).max("id") != null){
            primaryKeyValue = realm.where(dbAufgabe.class).max("id").intValue()+1;
        }else {
            primaryKeyValue = 0;
        }
        id = primaryKeyValue;
        Log.d("dbAufgabe",id+"");
    }

    public dbAufgabe() {


    }

    public int getId() {
        return id;
    }

    public String getAbgabeDatum() {
        return abgabeDatum;
    }

    public void setAbgabeDatum(String abgabeDatum) {
        this.abgabeDatum = abgabeDatum;
    }

    public String getErinnerungDate() {
        return erinnerungDate;
    }

    public void setErinnerungDate(String erinnerungDate) {
        this.erinnerungDate = erinnerungDate;
    }

    public String getErstelltAm() {
        return erstelltAm;
    }

    public void setErstelltAm(String erstelltAm) {
        this.erstelltAm = erstelltAm;
    }

    public String getZuletztAktualisiert() {
        return zuletztAktualisiert;
    }

    public void setZuletztAktualisiert(String zuletztAktualisiert) {
        this.zuletztAktualisiert = zuletztAktualisiert;
    }

    public Boolean getErledigt() {
        return erledigt;
    }

    public void setErledigt(Boolean erledigt) {
        this.erledigt = erledigt;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getNotizen() {
        return notizen;
    }

    public void setNotizen(String notizen) {
        this.notizen = notizen;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public Integer getPriorität() {
        return priorität;
    }

    public void setPriorität(Integer priorität) {
        this.priorität = priorität;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }

    public dbMediaFile getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(dbMediaFile mediaFile) {
        this.mediaFile = mediaFile;
    }

    public dbUser getUser() {
        return user;
    }

    public void setUser(dbUser user) {
        this.user = user;
    }
}
