package leon.bms.database;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 21.05.2016.
 */
public class dbMitteilung extends SugarRecord {
    public String datum;
    public int serverid;
    public String letzteLokaleAenderung;
    public String lokalHinzugefuegtAm;
    public String nachricht;
    public String stufen;
    public String titel;
    public dbUser user;

    public dbMitteilung() {
    }

    public dbMitteilung(String datum, int serverid, String letzteLokaleAenderung, String lokalHinzugefuegtAm, String nachricht, String stufen, String titel, dbUser user) {
        this.datum = datum;
        this.serverid = serverid;
        this.letzteLokaleAenderung = letzteLokaleAenderung;
        this.lokalHinzugefuegtAm = lokalHinzugefuegtAm;
        this.nachricht = nachricht;
        this.stufen = stufen;
        this.titel = titel;
        this.user = user;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public String getLetzteLokaleAenderung() {
        return letzteLokaleAenderung;
    }

    public void setLetzteLokaleAenderung(String letzteLokaleAenderung) {
        this.letzteLokaleAenderung = letzteLokaleAenderung;
    }

    public String getLokalHinzugefuegtAm() {
        return lokalHinzugefuegtAm;
    }

    public void setLokalHinzugefuegtAm(String lokalHinzugefuegtAm) {
        this.lokalHinzugefuegtAm = lokalHinzugefuegtAm;
    }

    public String getNachricht() {
        return nachricht;
    }

    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }

    public String getStufen() {
        return stufen;
    }

    public void setStufen(String stufen) {
        this.stufen = stufen;
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
}
