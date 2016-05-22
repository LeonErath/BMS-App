package leon.bms.model;

import leon.bms.database.dbKurs;

/**
 * Created by Leon E on 22.05.2016.
 */
public class kursauswahl {
    public boolean headlineOrKurs;
    public dbKurs kurs;
    public String headline;

    public kursauswahl() {

    }

    public kursauswahl(boolean headlineOrKurs, dbKurs kurs, String headline) {
        this.headlineOrKurs = headlineOrKurs;
        this.kurs = kurs;
        this.headline = headline;
    }

    public boolean isHeadlineOrKurs() {
        return headlineOrKurs;
    }

    public void setHeadlineOrKurs(boolean headlineOrKurs) {
        this.headlineOrKurs = headlineOrKurs;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }
}
