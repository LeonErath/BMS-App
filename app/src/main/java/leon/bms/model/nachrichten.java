package leon.bms.model;

/**
 * Created by Leon E on 13.03.2016.
 */
public class nachrichten {
    public int serverid;
    public String titel;
    public String nachricht;
    public  String hinzugefuegtAm;
    public  String geandertAm;
    public  String dateString;
    public  int geloescht;

    public nachrichten() {
    }

    public nachrichten(int serverid, String titel, String nachricht, String hinzugefuegtAm, String geandertAm, int geloescht) {
        this.serverid = serverid;
        this.titel = titel;
        this.nachricht = nachricht;
        this.hinzugefuegtAm = hinzugefuegtAm;
        this.geandertAm = geandertAm;
        this.geloescht = geloescht;
    }

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getNachricht() {
        return nachricht;
    }

    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }

    public String getHinzugefuegtAm() {
        return hinzugefuegtAm;
    }

    public void setHinzugefuegtAm(String hinzugefuegtAm) {
        this.hinzugefuegtAm = hinzugefuegtAm;
    }

    public String getGeandertAm() {
        return geandertAm;
    }

    public void setGeandertAm(String geandertAm) {
        this.geandertAm = geandertAm;
    }

    public int getGeloescht() {
        return geloescht;
    }

    public void setGeloescht(int geloescht) {
        this.geloescht = geloescht;
    }
}
