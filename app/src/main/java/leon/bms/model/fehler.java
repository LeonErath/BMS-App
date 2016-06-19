package leon.bms.model;


import leon.bms.realm.dbFehler;

/**
 * Created by Leon E on 24.05.2016.
 */
public class fehler {
    dbFehler fehler;
    boolean status;

    public fehler() {
    }

    public dbFehler getFehler() {
        return fehler;
    }

    public void setFehler(dbFehler fehler) {
        this.fehler = fehler;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
