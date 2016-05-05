package leon.bms;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by Leon E on 19.11.2015.
 */

/**
 * @dbTermin ist eine Tabelle der Datenbank für die Termin
 * Sie verwaltet alle Einträge der Termin und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 */
public class dbTermin extends SugarRecord {
    //Datensätze des Termines
    Boolean beschaeftigt;
    Date date;
    Date erinnerungDate;
    Date hinzugefuegtAm;
    String quelle;
    Boolean schulfrei;
    Integer terminID;
    Date zuletztAktualisiert;

    public dbTermin() {
        // empty Constructor needed!
    }

    public dbTermin(Boolean beschaeftigt, Date date, Date erinnerungDate, Date hinzugefuegtAm, String quelle, Boolean schulfrei, Integer terminID, Date zuletztAktualisiert) {

        this.beschaeftigt = beschaeftigt;
        this.date = date;
        this.erinnerungDate = erinnerungDate;
        this.hinzugefuegtAm = hinzugefuegtAm;
        this.quelle = quelle;
        this.schulfrei = schulfrei;
        this.terminID = terminID;
        this.zuletztAktualisiert = zuletztAktualisiert;
    }

    public Boolean getBeschaeftigt() {
        return beschaeftigt;
    }

    public void setBeschaeftigt(Boolean beschaeftigt) {
        this.beschaeftigt = beschaeftigt;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getErinnerungDate() {
        return erinnerungDate;
    }

    public void setErinnerungDate(Date erinnerungDate) {
        this.erinnerungDate = erinnerungDate;
    }

    public Date getHinzugefuegtAm() {
        return hinzugefuegtAm;
    }

    public void setHinzugefuegtAm(Date hinzugefuegtAm) {
        this.hinzugefuegtAm = hinzugefuegtAm;
    }

    public String getQuelle() {
        return quelle;
    }

    public void setQuelle(String quelle) {
        this.quelle = quelle;
    }

    public Boolean getSchulfrei() {
        return schulfrei;
    }

    public void setSchulfrei(Boolean schulfrei) {
        this.schulfrei = schulfrei;
    }

    public Integer getTerminID() {
        return terminID;
    }

    public void setTerminID(Integer terminID) {
        this.terminID = terminID;
    }

    public Date getZuletztAktualisiert() {
        return zuletztAktualisiert;
    }

    public void setZuletztAktualisiert(Date zuletztAktualisiert) {
        this.zuletztAktualisiert = zuletztAktualisiert;
    }


}
