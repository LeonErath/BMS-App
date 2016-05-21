package leon.bms.database;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Leon E on 19.11.2015.
 */

/**
 * @dbAufgabe ist eine Tabelle der Datenbank für die Aufgaben
 * Sie verwaltet alle Einträge der Aufgaben und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbAufgabe extends SugarRecord {

    //Datensätze der Aufgabe
    public String abgabeDatum;
    public String erinnerungDate;
    public String erstelltAm;
    public String zuletztAktualisiert;
    public Boolean erledigt;
    public String beschreibung;
    public String notizen;
    public String titel;
    public Integer priorität;

    // defining a relationship
    public dbKurs kurs;
    public dbMediaFile mediaFile;
    public dbUser user;


    public dbAufgabe() {

}

    public dbAufgabe(String abgabeDatum, String erinnerungDate, String erstelltAm, String zuletztAktualisiert, Boolean erledigt, String beschreibung, String notizen, String titel, Integer priorität, dbKurs kurs, dbMediaFile mediaFile, dbUser user) {
        this.abgabeDatum = abgabeDatum;
        this.erinnerungDate = erinnerungDate;
        this.erstelltAm = erstelltAm;
        this.zuletztAktualisiert = zuletztAktualisiert;
        this.erledigt = erledigt;
        this.beschreibung = beschreibung;
        this.notizen = notizen;
        this.titel = titel;
        this.priorität = priorität;
        this.kurs = kurs;
        this.mediaFile = mediaFile;
        this.user = user;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public dbUser getUser() {
        return user;
    }

    public void setUser(dbUser user) {
        this.user = user;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
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

    public dbMediaFile getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(dbMediaFile mediaFile) {
        this.mediaFile = mediaFile;
    }

    public String getNotizen() {
        return notizen;
    }

    public void setNotizen(String notizen) {
        this.notizen = notizen;
    }

    public Integer getPriorität() {
        return priorität;
    }

    public void setPriorität(Integer priorität) {
        this.priorität = priorität;
    }

    /**
     * @getUnerledigtAufgabe gibt alle Aufgabe zurürck die den Boolean "erledigt" auf false stehen
     * haben
     */
    public List<dbAufgabe> getUnerledigtAufgabe() {
        List<dbAufgabe> dbAufgabeList = dbAufgabe.find(dbAufgabe.class, "erledigt = ?", "0");
        return dbAufgabeList;
    }

    /**
     * @getErledigtAufgabe gibt alle Aufgabe zurürck die den Boolean "erledigt" auf true stehen
     * haben
     */
    public List<dbAufgabe> getErledigtAufgabe() {
        List<dbAufgabe> dbAufgabeList = dbAufgabe.find(dbAufgabe.class, "erledigt = ?", "1");
        return dbAufgabeList;
    }

    /**
     * @vorhandeneAufgabe prüft ob entwder die erledigten oder unerledigten Aufgaben vorhanden sind
     * dieser Filter wird gebraucht für das Anzeigen der Aufgaben. Er gibt true zurück wenn die Aufgaben
     * vorhanden sind / false wenn sie es nicht sind
     */
    public Boolean vorhandeneAufgaben(int erledigt) {
        List<dbAufgabe> dbAufgabeList = dbAufgabe.find(dbAufgabe.class, "erledigt = ?", String.valueOf(erledigt));
        return (dbAufgabeList.size() != 0) ? true : false;
    }

    /**
     * @param aufgabe übergibt eine Aufgabe über deren serverid überprüft wird ob sie erledigt ist
     * @checkifErledigtAufgabe überprüft ob bestimmte Aufgabe ist erledigt oder nicht . Wichtig für das
     * richtig Anzeige der Aufgaben in Fragment_aufgabeÜbersicht
     */
    public Boolean checkIfErledigtAufgabe(dbAufgabe aufgabe) {
        List<dbAufgabe> dbAufgabeList = dbAufgabe.find(dbAufgabe.class, "serverid = ?", String.valueOf(aufgabe.getId()));
        return dbAufgabeList.get(0).erledigt;
    }

    /**
     * @param id ist die ID der Aufgabe die rausgesucht werden soll
     * @return gibt die Aufgabe zurück
     * @getAufgabe gibt Aufgabe einer bestimmer ID zurück
     */
    public dbAufgabe getAufgabe(long id) {
        List<dbAufgabe> dbAufgabeList = dbAufgabe.find(dbAufgabe.class, "serverid = ?", String.valueOf(id));
        return dbAufgabeList.get(0);
    }

    /**
     * @param id ist die ID der Aufgabe von der die Bilder geladen werden soll
     * @return gibt wenn Bilder vorhanden eine Liste mit alle Bildern zurück
     * @getMediaFile gibt alle Bilder(MediaFiles) zurück die eine Aufgabe besitzt
     */
    public List<dbMediaFile> getMediaFile(long id) {
        List<dbMediaFile> dbMediaFileList = dbMediaFile.find(dbMediaFile.class, "aufgaben = ?", String.valueOf(id));
        Log.d("MEDIAFILE", dbMediaFileList.size() + "");
        return dbMediaFileList;
    }


}
