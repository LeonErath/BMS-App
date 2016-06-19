package leon.bms.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


/**
 * Created by Leon E on 18.06.2016.
 */
public class dbMitteilung extends RealmObject {
    @PrimaryKey
    private int serverid;
    @Required
    private String nachricht;
    @Required
    private String titel;
    private String datum;
    private String letzteLokaleAenderung;
    private String lokalHinzugefuegtAm;
    private String stufen;

    private dbUser user;

    public dbMitteilung() {
    }

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public String getNachricht() {
        return nachricht;
    }

    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
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

    public String getStufen() {
        return stufen;
    }

    public void setStufen(String stufen) {
        this.stufen = stufen;
    }

    public dbUser getUser() {
        return user;
    }

    public void setUser(dbUser user) {
        this.user = user;
    }
}
