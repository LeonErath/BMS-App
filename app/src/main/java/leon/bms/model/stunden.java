package leon.bms.model;

import java.io.Serializable;

import leon.bms.realm.dbSchulstunde;
import leon.bms.realm.dbVertretung;


/**
 * Created by Leon E on 10.02.2016.
 */

/**
 * @stunden  klasse die alle Daten für die Stundenplan Anzeige bietet. Constructor sowie Getter/Setter
 * Methode sind implementiert.
 */
public class stunden implements Serializable {
    public boolean active;
    public int stunde;
    public dbSchulstunde schulstunde;
    public dbVertretung vertretung;

    public stunden() {
    }

    public stunden(boolean active, int stunde, dbSchulstunde schulstunde, dbVertretung vertretung) {
        this.active = active;
        this.stunde = stunde;
        this.schulstunde = schulstunde;
        this.vertretung = vertretung;
    }

    public int getStunde() {
        return stunde;
    }

    public void setStunde(int stunde) {
        this.stunde = stunde;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public dbSchulstunde getSchulstunde() {
        return schulstunde;
    }

    public void setSchulstunde(dbSchulstunde schulstunde) {
        this.schulstunde = schulstunde;
    }

    public dbVertretung getVertretung() {
        return vertretung;
    }

    public void setVertretung(dbVertretung vertretung) {
        this.vertretung = vertretung;
    }
}
