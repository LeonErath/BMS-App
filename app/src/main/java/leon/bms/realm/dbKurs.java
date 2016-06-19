package leon.bms.realm;

/**
 * Created by Leon E on 16.06.2016.
 */

import com.orm.SugarRecord;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import java.util.List;

/**
 * Created by Leon E on 19.11.2015.
 */

/**
 * @dbKurs ist eine Tabelle der Datenbank für die Kurse
 * Sie verwaltet alle Einträge der Kurse und beinhaltet Filter für die weitere
 * Verarbeitung der Datensätze. Constructor und Getter/Setter Methoden sind implentiert.
 * Die Kurse Tabelle ist der Mittelpunkt der App den alles dreht sich um die Kurse. Daraus resultiert
 * auch , dass die Kurse Tabelle die größte und aktivste Tabelle ist.
 */
public class dbKurs extends RealmObject {
    @PrimaryKey @Required
    private Integer int_id;
    @Required
    private String name;
    @Required
    private String id;
    @Index
    private Boolean aktiv;
    private Boolean schriftlich;
    private String geandertAm;
    private String hinzugefuegtAm;
    private int blocknummer;
    private String iconPath;
    private Boolean archiviert;

    // defining a relationship
    private dbAufgabe aufgabe;
    private dbKlausur klausur;
    private dbNote note;
    private dbFach fach;
    private dbUser user;
    private dbLehrer lehrer;
    private dbSchulstunde stunde;
    private dbSchuljahr schuljahr;
    private dbKursart kursart;
    RealmList<dbWebsiteTag> websiteTags;


    public dbKurs() {
        //empty Constructor needed!
    }

    public Integer getInt_id() {
        return int_id;
    }

    public void setInt_id(Integer int_id) {
        this.int_id = int_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getAktiv() {
        return aktiv;
    }

    public void setAktiv(Boolean aktiv) {
        this.aktiv = aktiv;
    }

    public Boolean getSchriftlich() {
        return schriftlich;
    }

    public void setSchriftlich(Boolean schriftlich) {
        this.schriftlich = schriftlich;
    }

    public String getGeandertAm() {
        return geandertAm;
    }

    public void setGeandertAm(String geandertAm) {
        this.geandertAm = geandertAm;
    }

    public String getHinzugefuegtAm() {
        return hinzugefuegtAm;
    }

    public void setHinzugefuegtAm(String hinzugefuegtAm) {
        this.hinzugefuegtAm = hinzugefuegtAm;
    }

    public int getBlocknummer() {
        return blocknummer;
    }

    public void setBlocknummer(int blocknummer) {
        this.blocknummer = blocknummer;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public Boolean getArchiviert() {
        return archiviert;
    }

    public void setArchiviert(Boolean archiviert) {
        this.archiviert = archiviert;
    }

    public dbAufgabe getAufgabe() {
        return aufgabe;
    }

    public void setAufgabe(dbAufgabe aufgabe) {
        this.aufgabe = aufgabe;
    }

    public dbKlausur getKlausur() {
        return klausur;
    }

    public void setKlausur(dbKlausur klausur) {
        this.klausur = klausur;
    }

    public dbNote getNote() {
        return note;
    }

    public void setNote(dbNote note) {
        this.note = note;
    }

    public dbFach getFach() {
        return fach;
    }

    public void setFach(dbFach fach) {
        this.fach = fach;
    }

    public dbUser getUser() {
        return user;
    }

    public void setUser(dbUser user) {
        this.user = user;
    }

    public dbLehrer getLehrer() {
        return lehrer;
    }

    public void setLehrer(dbLehrer lehrer) {
        this.lehrer = lehrer;
    }

    public dbSchulstunde getStunde() {
        return stunde;
    }

    public void setStunde(dbSchulstunde stunde) {
        this.stunde = stunde;
    }

    public dbSchuljahr getSchuljahr() {
        return schuljahr;
    }

    public void setSchuljahr(dbSchuljahr schuljahr) {
        this.schuljahr = schuljahr;
    }

    public dbKursart getKursart() {
        return kursart;
    }

    public void setKursart(dbKursart kursart) {
        this.kursart = kursart;
    }

    public RealmList<dbWebsiteTag> getWebsiteTags() {
        return websiteTags;
    }

    public void setWebsiteTags(RealmList<dbWebsiteTag> websiteTags) {
        this.websiteTags = websiteTags;
    }
}
