package leon.bms.realm;

import io.realm.RealmObject;



/**
 * Created by Leon E on 18.06.2016.
 */
public class dbSchule extends RealmObject {
    private String abkuerzung;
    private String adressen;
    private String appURL;
    private String bundesland;
    private int globalID;
    private String last_change;
    private Boolean lehrerVersionVerfuegbar;
    private String loginText;
    private String logoPath;
    private String schulname;
    private String stadt;
    private int telefonnummer;
    private int typ;
    private String webURL;

    private dbUnterrichtszeit unterrichtszeit;
    private dbUser user;

    public dbSchule() {
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

    public dbUnterrichtszeit getUnterrichtszeit() {
        return unterrichtszeit;
    }

    public void setUnterrichtszeit(dbUnterrichtszeit unterrichtszeit) {
        this.unterrichtszeit = unterrichtszeit;
    }

    public dbUser getUser() {
        return user;
    }

    public void setUser(dbUser user) {
        this.user = user;
    }
}
