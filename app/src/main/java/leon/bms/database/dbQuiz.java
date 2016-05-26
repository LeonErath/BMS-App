package leon.bms.database;

import com.orm.SugarRecord;

/**
 * Created by Leon E on 26.05.2016.
 */
public class dbQuiz extends SugarRecord {
    public int anzahlDerSpiele;
    public String beschreibung;
    public int durchschnittPunktzahl;
    public String erstelltAm;
    public String geandertAm;
    public int gesamtePunkte;
    public int globaleid;
    public int intergerid;
    public boolean katalogQuiz;
    public String lastServerSync;
    public String name;
    public int relevanz;
    public int schwierigkeitsgrad;
    public int stufe;

    public dbFach fach;
    public dbFragen fragen;
    public dbKlausur klausur;
    public dbKurs kurs;

    public dbQuiz() {
    }

    public dbQuiz(int anzahlDerSpiele, String beschreibung, int durchschnittPunktzahl, String erstelltAm, String geandertAm, int gesamtePunkte, int globaleid, int intergerid, boolean katalogQuiz, String lastServerSync, String name, int relevanz, int schwierigkeitsgrad, int stufe, dbFach fach, dbFragen fragen, dbKlausur klausur, dbKurs kurs) {
        this.anzahlDerSpiele = anzahlDerSpiele;
        this.beschreibung = beschreibung;
        this.durchschnittPunktzahl = durchschnittPunktzahl;
        this.erstelltAm = erstelltAm;
        this.geandertAm = geandertAm;
        this.gesamtePunkte = gesamtePunkte;
        this.globaleid = globaleid;
        this.intergerid = intergerid;
        this.katalogQuiz = katalogQuiz;
        this.lastServerSync = lastServerSync;
        this.name = name;
        this.relevanz = relevanz;
        this.schwierigkeitsgrad = schwierigkeitsgrad;
        this.stufe = stufe;
        this.fach = fach;
        this.fragen = fragen;
        this.klausur = klausur;
        this.kurs = kurs;
    }

    public int getAnzahlDerSpiele() {
        return anzahlDerSpiele;
    }

    public void setAnzahlDerSpiele(int anzahlDerSpiele) {
        this.anzahlDerSpiele = anzahlDerSpiele;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getDurchschnittPunktzahl() {
        return durchschnittPunktzahl;
    }

    public void setDurchschnittPunktzahl(int durchschnittPunktzahl) {
        this.durchschnittPunktzahl = durchschnittPunktzahl;
    }

    public String getErstelltAm() {
        return erstelltAm;
    }

    public void setErstelltAm(String erstelltAm) {
        this.erstelltAm = erstelltAm;
    }

    public String getGeandertAm() {
        return geandertAm;
    }

    public void setGeandertAm(String geandertAm) {
        this.geandertAm = geandertAm;
    }

    public int getGesamtePunkte() {
        return gesamtePunkte;
    }

    public void setGesamtePunkte(int gesamtePunkte) {
        this.gesamtePunkte = gesamtePunkte;
    }

    public int getGlobaleid() {
        return globaleid;
    }

    public void setGlobaleid(int globaleid) {
        this.globaleid = globaleid;
    }

    public int getIntergerid() {
        return intergerid;
    }

    public void setIntergerid(int intergerid) {
        this.intergerid = intergerid;
    }

    public boolean isKatalogQuiz() {
        return katalogQuiz;
    }

    public void setKatalogQuiz(boolean katalogQuiz) {
        this.katalogQuiz = katalogQuiz;
    }

    public String getLastServerSync() {
        return lastServerSync;
    }

    public void setLastServerSync(String lastServerSync) {
        this.lastServerSync = lastServerSync;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRelevanz() {
        return relevanz;
    }

    public void setRelevanz(int relevanz) {
        this.relevanz = relevanz;
    }

    public int getSchwierigkeitsgrad() {
        return schwierigkeitsgrad;
    }

    public void setSchwierigkeitsgrad(int schwierigkeitsgrad) {
        this.schwierigkeitsgrad = schwierigkeitsgrad;
    }

    public int getStufe() {
        return stufe;
    }

    public void setStufe(int stufe) {
        this.stufe = stufe;
    }

    public dbFach getFach() {
        return fach;
    }

    public void setFach(dbFach fach) {
        this.fach = fach;
    }

    public dbFragen getFragen() {
        return fragen;
    }

    public void setFragen(dbFragen fragen) {
        this.fragen = fragen;
    }

    public dbKlausur getKlausur() {
        return klausur;
    }

    public void setKlausur(dbKlausur klausur) {
        this.klausur = klausur;
    }

    public dbKurs getKurs() {
        return kurs;
    }

    public void setKurs(dbKurs kurs) {
        this.kurs = kurs;
    }
}
