package leon.bms.realm;

import io.realm.RealmObject;


/**
 * Created by Leon E on 18.06.2016.
 */
public class dbWebsiteTag extends RealmObject {

    private String hinzugefuegtAm;
    private int relevanz;
    private String websitetag;
    private int vorkommen;

    private dbKurs kurs;
    private dbUser user;


    public dbWebsiteTag() {
        //empty Constructor needed!
        vorkommen = 0;
        relevanz = 0;
    }

    public String getHinzugefuegtAm() {
        return hinzugefuegtAm;
    }

    public void setHinzugefuegtAm(String hinzugefuegtAm) {
        this.hinzugefuegtAm = hinzugefuegtAm;
    }

    public int getRelevanz() {
        return relevanz;
    }

    public void setRelevanz(int relevanz) {
        this.relevanz = relevanz;
    }

    public String getWebsitetag() {
        return websitetag;
    }

    public void setWebsitetag(String websitetag) {
        this.websitetag = websitetag;
    }

    public int getVorkommen() {
        return vorkommen;
    }

    public void setVorkommen(int vorkommen) {
        this.vorkommen = vorkommen;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }

    public dbUser getUser() {
        return user;
    }

    public void setUser(dbUser user) {
        this.user = user;
    }
}
