package leon.bms.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Leon E on 18.06.2016.
 */
public class dbVertretung extends RealmObject {
    @PrimaryKey
    private int serverId;
    private int art;
    private int artBitfields;
    private String datum;
    private Boolean eva;
    private String hinzugefuegtAm;
    private String letzteAenderung;
    private  String notiz;


    private dbSchulstunde schulstunde;
    private dbLehrer lehrer;
    private dbRaum raum;

    public dbVertretung() {
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
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
