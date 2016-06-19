package leon.bms.model;


import leon.bms.realm.dbNote;

/**
 * Created by Leon E on 24.05.2016.
 */
public class mündlicheNoten {
    dbNote note;
    boolean keinNote;

    public mündlicheNoten() {
    }

    public mündlicheNoten(dbNote note, boolean keinNote) {
        this.note = note;
        this.keinNote = keinNote;
    }

    public dbNote getNote() {
        return note;
    }

    public void setNote(dbNote note) {
        this.note = note;
    }

    public boolean isKeinNote() {
        return keinNote;
    }

    public void setKeinNote(boolean keinNote) {
        this.keinNote = keinNote;
    }
}
