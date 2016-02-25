package leon.bms;

import android.graphics.Bitmap;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 17.02.2016.
 */
public class dbMediaFile extends SugarRecord<dbMediaFile> {
    String path;

    dbAufgabe aufgaben;

    public dbMediaFile() {
    }

    public dbMediaFile(String path,  dbAufgabe aufgaben) {
        this.path = path;

        this.aufgaben = aufgaben;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public dbAufgabe getAufgabe() {
        return aufgaben;
    }

    public void setAufgabe(dbAufgabe aufgaben) {
        this.aufgaben = aufgaben;
    }
}
