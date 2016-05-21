package leon.bms;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 21.05.2016.
 */
public class dbSchule extends SugarRecord {
    String abkuerzung;
    String adressen;
    String appURL;
    String bundesland;
    int globalID;
    String last_change;
    Boolean lehrerVersionVerfuegbar;
    String loginText;
    String logoPath;
    String schulname;
    String stadt;
    int telefonnummer;
    int typ;
    String webURL;

    dbUnterrichtszeit unterrichtszeit;
    dbUser user;




    public dbSchule() {
    }

    public dbSchule(String abkuerzung, String adressen, String appURL, String bundesland, int globalID, String last_change, Boolean lehrerVersionVerfuegbar, String loginText, String logoPath, String schulname, String stadt, int telefonnummer, int typ, String webURL, dbUnterrichtszeit unterrichtszeit) {
        this.abkuerzung = abkuerzung;
        this.adressen = adressen;
        this.appURL = appURL;
        this.bundesland = bundesland;
        this.globalID = globalID;
        this.last_change = last_change;
        this.lehrerVersionVerfuegbar = lehrerVersionVerfuegbar;
        this.loginText = loginText;
        this.logoPath = logoPath;
        this.schulname = schulname;
        this.stadt = stadt;
        this.telefonnummer = telefonnummer;
        this.typ = typ;
        this.webURL = webURL;
        this.unterrichtszeit = unterrichtszeit;
    }

    public dbUnterrichtszeit getUnterrichtszeit() {
        return unterrichtszeit;
    }

    public void setUnterrichtszeit(dbUnterrichtszeit unterrichtszeit) {
        this.unterrichtszeit = unterrichtszeit;
    }

    public String getAbkuerzung() {
        return abkuerzung;
    }

    public void setAbkuerzung(String abkuerzung) {
        this.abkuerzung = abkuerzung;
    }

    public String getAdressen() {
        return adressen;
    }

    public void setAdressen(String adressen) {
        this.adressen = adressen;
    }

    public String getAppURL() {
        return appURL;
    }

    public void setAppURL(String appURL) {
        this.appURL = appURL;
    }

    public String getBundesland() {
        return bundesland;
    }

    public void setBundesland(String bundesland) {
        this.bundesland = bundesland;
    }

    public int getGlobalID() {
        return globalID;
    }

    public void setGlobalID(int globalID) {
        this.globalID = globalID;
    }

    public String getLast_change() {
        return last_change;
    }

    public void setLast_change(String last_change) {
        this.last_change = last_change;
    }

    public Boolean getLehrerVersionVerfuegbar() {
        return lehrerVersionVerfuegbar;
    }

    public void setLehrerVersionVerfuegbar(Boolean lehrerVersionVerfuegbar) {
        this.lehrerVersionVerfuegbar = lehrerVersionVerfuegbar;
    }

    public String getLoginText() {
        return loginText;
    }

    public void setLoginText(String loginText) {
        this.loginText = loginText;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getSchulname() {
        return schulname;
    }

    public void setSchulname(String schulname) {
        this.schulname = schulname;
    }

    public String getStadt() {
        return stadt;
    }

    public void setStadt(String stadt) {
        this.stadt = stadt;
    }

    public int getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(int telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public int getTyp() {
        return typ;
    }

    public void setTyp(int typ) {
        this.typ = typ;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }
}
