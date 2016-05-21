package leon.bms;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 21.05.2016.
 */
public class dbVertretung extends SugarRecord {
    int art;
    int artBitfields;
    String datum;
    Boolean eva;
    String hinzugefuegtAm;
    String letzteAenderung;
    String notiz;

    dbSchulstunde schulstunde;
    dbLehrer lehrer;
    dbRaum raum;

    public dbVertretung() {
    }

    public dbVertretung(int art, int artBitfields, String datum, Boolean eva, String hinzugefuegtAm, String letzteAenderung, String notiz, dbSchulstunde schulstunde, dbLehrer lehrer, dbRaum raum) {
        this.art = art;
        this.artBitfields = artBitfields;
        this.datum = datum;
        this.eva = eva;
        this.hinzugefuegtAm = hinzugefuegtAm;
        this.letzteAenderung = letzteAenderung;
        this.notiz = notiz;
        this.schulstunde = schulstunde;
        this.lehrer = lehrer;
        this.raum = raum;
    }

    public int getArt() {
        return art;
    }

    public void setArt(int art) {
        this.art = art;
    }

    public int getArtBitfields() {
        return artBitfields;
    }

    public void setArtBitfields(int artBitfields) {
        this.artBitfields = artBitfields;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public Boolean getEva() {
        return eva;
    }

    public void setEva(Boolean eva) {
        this.eva = eva;
    }

    public String getHinzugefuegtAm() {
        return hinzugefuegtAm;
    }

    public void setHinzugefuegtAm(String hinzugefuegtAm) {
        this.hinzugefuegtAm = hinzugefuegtAm;
    }

    public String getLetzteAenderung() {
        return letzteAenderung;
    }

    public void setLetzteAenderung(String letzteAenderung) {
        this.letzteAenderung = letzteAenderung;
    }

    public String getNotiz() {
        return notiz;
    }

    public void setNotiz(String notiz) {
        this.notiz = notiz;
    }

    public dbSchulstunde getSchulstunde() {
        return schulstunde;
    }

    public void setSchulstunde(dbSchulstunde schulstunde) {
        this.schulstunde = schulstunde;
    }

    public dbLehrer getLehrer() {
        return lehrer;
    }

    public void setLehrer(dbLehrer lehrer) {
        this.lehrer = lehrer;
    }

    public dbRaum getRaum() {
        return raum;
    }

    public void setRaum(dbRaum raum) {
        this.raum = raum;
    }
}
