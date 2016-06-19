package leon.bms.realm;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Leon E on 18.06.2016.
 */
public class dbTermin extends RealmObject {
    @PrimaryKey @Required
    private Integer terminID;
    private Boolean beschaeftigt;
    private Date date;
    private Date erinnerungDate;
    private Date hinzugefuegtAm;
    private String quelle;
    private Boolean schulfrei;
    private Date zuletztAktualisiert;

    public dbTermin() {
        // empty Constructor needed!
    }

    public Integer getTerminID() {
        return terminID;
    }

    public void setTerminID(Integer terminID) {
        this.terminID = terminID;
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

    public Date getZuletztAktualisiert() {
        return zuletztAktualisiert;
    }

    public void setZuletztAktualisiert(Date zuletztAktualisiert) {
        this.zuletztAktualisiert = zuletztAktualisiert;
    }
}
