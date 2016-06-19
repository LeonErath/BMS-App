package leon.bms.realm;

import io.realm.RealmObject;


/**
 * Created by Leon E on 18.06.2016.
 */
public class dbKlausuraufsicht extends RealmObject{
    //Datensätze der Klausuraufsicht
    private String start;
    private String end;

    // defining a relationship
    private dbKlausur klausur;
    private dbLehrer lehrer;

    public dbKlausuraufsicht() {
        //empty Constructor needed!
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public dbKlausur getKlausur() {
        return klausur;
    }

    public void setKlausur(dbKlausur klausur) {
        this.klausur = klausur;
    }

    public dbLehrer getLehrer() {
        return lehrer;
    }

    public void setLehrer(dbLehrer lehrer) {
        this.lehrer = lehrer;
    }
}
