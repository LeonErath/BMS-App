package leon.bms;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 06.03.2016.
 */
public class dbWebsiteTag extends SugarRecord {
    String hinzugefuegtAm;
    int relevanz;
    String websitetag;
    int vorkommen;

    dbKurs kurs;
    dbUser user;
    dbKursTagConnect dbKursTagConnect;

    public dbWebsiteTag() {
        this.vorkommen =0;
        this.relevanz =0;
    }

    public dbWebsiteTag(String hinzugefuegtAm, int relevanz, String websitetag, int vorkommen, dbKurs kurs, dbUser user, leon.bms.dbKursTagConnect dbKursTagConnect) {
        this.hinzugefuegtAm = hinzugefuegtAm;
        this.relevanz = relevanz;
        this.websitetag = websitetag;
        this.vorkommen = vorkommen;
        this.kurs = kurs;
        this.user = user;
        this.dbKursTagConnect = dbKursTagConnect;
    }

    public leon.bms.dbKursTagConnect getDbKursTagConnect() {
        return dbKursTagConnect;
    }

    public void setDbKursTagConnect(leon.bms.dbKursTagConnect dbKursTagConnect) {
        this.dbKursTagConnect = dbKursTagConnect;
    }

    public int getVorkommen() {
        return vorkommen;
    }

    public void setVorkommen(int vorkommen) {
        this.vorkommen = vorkommen;
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
    public boolean tagVorhanden(String tag){
        if (dbWebsiteTag.find(dbWebsiteTag.class,"websitetag = ?",tag).size() >0){
            return true;
        }else {
            return false;
        }
    }
    public dbWebsiteTag getWebsiteTag(String tag) {
        if (dbWebsiteTag.find(dbWebsiteTag.class, "websitetag = ?", tag).size() == 1) {
            dbWebsiteTag websiteTag = dbWebsiteTag.find(dbWebsiteTag.class, "websitetag = ?", tag).get(0);
            return websiteTag;
        } else {
            return null;
        }
    }
}
