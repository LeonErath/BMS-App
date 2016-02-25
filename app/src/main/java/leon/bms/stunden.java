package leon.bms;

import java.io.Serializable;

/**
 * Created by Leon E on 10.02.2016.
 */
public class stunden implements Serializable {
    public String stundenname;
    public String lehrer;
    public boolean active;
    public String raum;
    public int stunde;

    public stunden(String stundenname, String lehrer, boolean active, String raum, int stunde) {
        this.stundenname = stundenname;
        this.lehrer = lehrer;
        this.active = active;
        this.raum = raum;
        this.stunde = stunde;
    }

    public stunden() {

    }

    public String getStundenname() {
        return stundenname;
    }

    public void setStundenname(String stundenname) {
        this.stundenname = stundenname;
    }

    public String getLehrer() {
        return lehrer;
    }

    public void setLehrer(String lehrer) {
        this.lehrer = lehrer;
    }

    public String getRaum() {
        return raum;
    }

    public void setRaum(String raum) {
        this.raum = raum;
    }

    public String getStunde() {
        return String.valueOf(stunde);
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
}
