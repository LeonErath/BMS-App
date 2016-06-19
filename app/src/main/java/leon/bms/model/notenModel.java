package leon.bms.model;

import java.util.List;

import leon.bms.realm.dbKurs;
import leon.bms.realm.dbNote;


/**
 * Created by Leon E on 24.05.2016.
 */
public class notenModel {
    List<dbNote> schriftlicheNoten;
    List<dbNote> mündlicheNoten;
    dbKurs kurs;
    double durchschnitt;

    public notenModel() {
    }

    public notenModel(List<dbNote> schriftlicheNoten, List<dbNote> mündlicheNoten, dbKurs kurs, double durchschnitt) {
        this.schriftlicheNoten = schriftlicheNoten;
        this.mündlicheNoten = mündlicheNoten;
        this.kurs = kurs;
        this.durchschnitt = durchschnitt;
    }

    public List<dbNote> getSchriftlicheNoten() {
        return schriftlicheNoten;
    }

    public void setSchriftlicheNoten(List<dbNote> schriftlicheNoten) {
        this.schriftlicheNoten = schriftlicheNoten;
    }

    public List<dbNote> getMündlicheNoten() {
        return mündlicheNoten;
    }

    public void setMündlicheNoten(List<dbNote> mündlicheNoten) {
        this.mündlicheNoten = mündlicheNoten;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }

    public double getDurchschnitt() {
        return durchschnitt;
    }

    public void setDurchschnitt(double durchschnitt) {
        this.durchschnitt = durchschnitt;
    }
}
