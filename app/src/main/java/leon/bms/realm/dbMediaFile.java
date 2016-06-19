package leon.bms.realm;

import io.realm.RealmObject;


/**
 * Created by Leon E on 18.06.2016.
 */
public class dbMediaFile extends RealmObject {
    private String path;
    private dbAufgabe aufgabe;

    public dbMediaFile() {
        //empty Constructor needed!
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public dbAufgabe getAufgaben() {
        return aufgabe;
    }

    public void setAufgaben(dbAufgabe aufgaben) {
        this.aufgabe = aufgaben;
    }
}
