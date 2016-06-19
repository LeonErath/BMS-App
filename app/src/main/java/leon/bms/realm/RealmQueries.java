package leon.bms.realm;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * Created by Leon E on 18.06.2016.
 */
public class RealmQueries {
    Realm realm;
    RealmConfiguration realmConfig;
    public RealmQueries(Context context) {
        realmConfig= new RealmConfiguration.Builder(context).build();
        Realm.setDefaultConfiguration(realmConfig);
        realm = Realm.getDefaultInstance();
    }

    public RealmResults<dbKlausur> getKlausurFromKurs(dbKurs kurs){
        RealmResults<dbKlausur> klausurRealmQuery = realm.where(dbKlausur.class)
                .equalTo("kurs.int_id",kurs.getInt_id())
                .findAll();
        return  checkResults(klausurRealmQuery);
    }

    public RealmResults<dbNote> getNoteFromKurs(dbKurs kurs,boolean schriftlich){
        RealmResults<dbNote> noteRealmResults = realm.where(dbNote.class)
                .equalTo("kurs.int_id",kurs.getInt_id())
                .equalTo("schriftlich",schriftlich)
                .findAll();
        return  checkResults(noteRealmResults);
    }

    public RealmResults<dbSchulstunde> getStundenFromDay(int day){
        RealmResults<dbSchulstunde> schulstundeRealmResults = realm.where(dbSchulstunde.class)
                .equalTo("kurs.aktiv",true)
                .equalTo("day",day)
                .findAll();
        return  checkResults(schulstundeRealmResults);
    }

    public RealmResults<dbKurs> getKurseFromFach(dbFach fach){
        RealmResults<dbKurs> schulstundeRealmResults = realm.where(dbKurs.class)
                .equalTo("fach.id",fach.getId())
                .findAll();
        return  checkResults(schulstundeRealmResults);
    }


    public boolean isWebsiteTagVorhanden(String tag){
        RealmResults<dbWebsiteTag> websiteTagRealmResults = realm.where(dbWebsiteTag.class)
                .equalTo("websitetag",tag)
                .findAll();
        if (websiteTagRealmResults.size() > 0){
            return  true;
        }else {
            return false;
        }
    }

    public dbWebsiteTag getWebsiteTagFromTag(String tag){
        RealmResults<dbWebsiteTag> websiteTagRealmResults = realm.where(dbWebsiteTag.class)
                .equalTo("websitetag",tag)
                .findAll();
        if (websiteTagRealmResults.size() > 0){
            return  websiteTagRealmResults.get(0);
        }else {
            return null;
        }
    }

    public RealmResults<dbWebsiteTag> getWebsiteTagFromKurs(dbKurs kurs){
        RealmResults<dbWebsiteTag> websiteTagRealmResults = realm.where(dbWebsiteTag.class)
                .equalTo("kurs.int_id",kurs.getInt_id())
                .findAll();
        return  checkResults(websiteTagRealmResults);
    }

    public dbNote getNoteFromKlausur(dbKlausur klausur){
        RealmResults<dbNote> noteRealmResults = realm.where(dbNote.class)
                .equalTo("klausur.serverid",klausur.getServerid())
                .findAll();
        if (noteRealmResults.size() > 0){
            return  noteRealmResults.get(0);
        }else {
            return null;
        }
    }

    public RealmResults<dbFehler> getFehlerFromKlausur(dbKlausur klausur){
        RealmResults<dbFehler> fehlerRealmResults = realm.where(dbFehler.class)
                .equalTo("klausur.serverid",klausur.getServerid())
                .findAll();
        return  checkResults(fehlerRealmResults);
    }

    public RealmResults<dbKlausurinhalt> getInhaltFromKlausur(dbKlausur klausur){
        RealmResults<dbKlausurinhalt> klausurinhalts = realm.where(dbKlausurinhalt.class)
                .equalTo("klausur.serverid",klausur.getServerid())
                .findAll();
        return  checkResults(klausurinhalts);
    }
    public RealmResults<dbKlausuraufsicht> getAufsichtFromKlausur(dbKlausur klausur){
        RealmResults<dbKlausuraufsicht> dbKlausuraufsichtRealmResults = realm.where(dbKlausuraufsicht.class)
                .equalTo("klausur.serverid",klausur.getServerid())
                .findAll();
        return  checkResults(dbKlausuraufsichtRealmResults);
    }

    public RealmResults<dbKlausur> getKlausuren(){
        RealmResults<dbKlausur> klausurRealmResults = realm.where(dbKlausur.class)
                .findAll();
        return  checkResults(klausurRealmResults);
    }

    public RealmResults<dbKurs> getAktiveKurse(){
        RealmResults<dbKurs> kursRealmResults = realm.where(dbKurs.class)
                .equalTo("aktiv",true)
                .findAll();
        return  checkResults(kursRealmResults);
    }

    public RealmResults<dbKurs> getSchriftlicheKurse(){
        RealmResults<dbKurs> kursRealmResults = realm.where(dbKurs.class)
                .equalTo("aktiv",true)
                .equalTo("schriftlich",true)
                .findAll();
        return  checkResults(kursRealmResults);
    }


    public dbAufgabe getAufgabe(int id){
        RealmResults<dbAufgabe> aufgabeRealmResults = realm.where(dbAufgabe.class)
                .equalTo("id",id)
                .findAll();
        if (aufgabeRealmResults.size() > 0){
            return  aufgabeRealmResults.get(0);
        }else {
            return null;
        }
    }
    public dbFach getFachWithDescription(String descrpt){
        RealmResults<dbFach> faches = realm.where(dbFach.class)
                .equalTo("description",descrpt)
                .findAll();
        if (faches.size() > 0){
            return  faches.get(0);
        }else {
            return null;
        }
    }
    public dbKlausur getKlausur(int id){
        RealmResults<dbKlausur> klausurRealmResults = realm.where(dbKlausur.class)
                .equalTo("serverid",id)
                .findAll();
        if (klausurRealmResults.size() > 0){
            return  klausurRealmResults.get(0);
        }else {
            return null;
        }
    }
    public dbKurs getKurs(int id){
        RealmResults<dbKurs> kursRealmResults = realm.where(dbKurs.class)
                .equalTo("int_id",id)
                .findAll();
        if (kursRealmResults.size() > 0){
            return  kursRealmResults.get(0);
        }else {
            return null;
        }
    }

    public dbVertretung getVertretungFromStunde(dbSchulstunde schulstunde){
        RealmResults<dbVertretung> vertretungRealmResults = realm.where(dbVertretung.class)
                .equalTo("schulstunde.lesson_id",schulstunde.getLesson_id())
                .findAll();
        if (vertretungRealmResults.size() > 0){
            return  vertretungRealmResults.get(0);
        }else {
            return null;
        }
    }

    public RealmResults<dbSchulstunde> getAktiveStunden(){
        RealmResults<dbSchulstunde> schulstundeRealmResults = realm.where(dbSchulstunde.class)
                .equalTo("kurs.aktiv",true)
                .findAll();
        return checkResults(schulstundeRealmResults);
    }
    public RealmResults<dbSchulstunde> getAktiveStundenWithDay(int day){
        RealmResults<dbSchulstunde> schulstundeRealmResults = realm.where(dbSchulstunde.class)
                .equalTo("kurs.aktiv",true)
                .equalTo("day",day)
                .findAll().sort("lesson", Sort.ASCENDING);
        return checkResults(schulstundeRealmResults);
    }


    public dbKurs getKursFromFach(dbFach fach){
        RealmResults<dbKurs> kursRealmResults = realm.where(dbKurs.class)
                .equalTo("fach.id",fach.getId())
                .equalTo("aktiv",true)
                .findAll();
        if (kursRealmResults.size() > 0){
            return  kursRealmResults.get(0);
        }else {
            return null;
        }
    }

    public RealmResults<dbMediaFile> getMediaFilesFromAufgabe(dbAufgabe aufgabe){
        RealmResults<dbMediaFile> mediaFileRealmResults = realm.where(dbMediaFile.class)
                .equalTo("aufgabe.id",aufgabe.getId())
                .findAll();
        return  checkResults(mediaFileRealmResults);
    }


    public RealmResults<dbSchulstunde> getStunden(dbKurs kurs){
        RealmResults<dbSchulstunde> schulstundeRealmResults = realm.where(dbSchulstunde.class)
                .equalTo("kurs.int_id",kurs.getInt_id())
                .findAll();
        return  checkResults(schulstundeRealmResults);
    }



    public dbUser getUser(){
        RealmResults<dbUser> userRealmResults = realm.where(dbUser.class)
                .findAll();
        if (userRealmResults.size() > 0){
            return  userRealmResults.get(0);
        }else {
            return null;
        }

    }


    public RealmResults checkResults(RealmResults realmResults){
        if (realmResults.size() >0 ){
            return realmResults;
        }else {
            return null;
        }
    }
    public RealmResults<dbAufgabe> getUnerledigteAufgaben(){
        RealmResults<dbAufgabe> aufgabeRealmResults = realm.where(dbAufgabe.class)
                .equalTo("erledigt",false)
                .findAll();
        return  checkResults(aufgabeRealmResults);
    }

    public RealmResults<dbAufgabe> getErledigteAufgaben(){
        RealmResults<dbAufgabe> aufgabeRealmResults = realm.where(dbAufgabe.class)
                .equalTo("erledigt",true)
                .findAll();
        return  checkResults(aufgabeRealmResults);
    }

    public RealmResults<dbFach> getAllFaecher(){
        RealmResults<dbFach> aufgabeRealmResults = realm.where(dbFach.class)
                .findAll();
        return  checkResults(aufgabeRealmResults);
    }

}
