package leon.bms;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pwittchen.swipe.library.Swipe;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @AufgabenActivity ist für die erstellung von Aufgaben zuständig
 * Es speichert die erstellten Aufgaben in der Datenbank
 * Die Aufgaben beinhalten ein Kurs, ein Datum und min. eine Beschreibung
 * dazu soll noch die Möglichkeit kommen von Bildern und anderen hilfreichen Informationen
 * an die Aufgabe "dranzuhängen"
 */
public class AufgabenActivity extends AppCompatActivity implements PhotoAdapter.ViewHolder.ClickListener {

    private static String TAG = AufgabenActivity.class.getSimpleName();
    private final String overlayColor = "#73000000";
    private final String overlayTransparentColor = "#00000000";
    // @datePickerDialog wird gebraucht für die Auswahl der Datums
    DatePickerDialog datePickerDialog;
    RelativeLayout overlay;
    private PendingIntent pendingIntent;
    // @dateString hier wird das Datum als String gespeichert
    String dateString, dateAnzeige;
    // @dateCalendar ist das heutige Datum @calendar2 ist das Datum welches der User auswählt
    Calendar dateCalendar, calendar2 = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();
    TextView textViewDatePicker;
    EditText editTextBeschreibung, editTextNotizen;
    // zeigt eine Liste mit den Kursen an
    Spinner spinner;
    // @selectedItem speichert den ausgewählten Kurs
    String selectedItem;
    // FloatingActionButton für die Funktionen zum Bilder machen oder aus der Gallarie auswählen
    FloatingActionButton fabCamera, fabGallarie, fabAnimate;
    // @fabVisible wichtig für die Animation vom ein und ausblenden von FAB
    boolean fabVisible = false;
    String picturePath;
    Swipe swipe;
    // @picturePaths speichert alle Pfade für die Bilder
    List<String> picturePaths = new ArrayList<>();
    // Wichtiger Code für das machen von Fotos
    static int RESULT_LOAD_IMG = 1;
    // @aufgabeLoad wichtig für das ändern von Aufaben
    dbAufgabe aufgabeLoad = null;
    // @photoAdapter ist ein Adapter für den recyclerView für das anzeigen von den Bildern
    private PhotoAdapter photoAdapter;

    /**
     * ActionModeCallback und ActionMode ist für die veränderun der Toolbar bei einer Auswahl der
     * der Kurse. Über die "neue" Toolbar kann der User sein Auswahl abschließen oder noch weiter
     * informationen zu den Auswahl bekommen.
     */
    private ActionModeCallback actionModeCallback = new ActionModeCallback(this);
    private ActionMode actionMode;
    File photoFile = null;
    View mView;

    /**
     * Automatische generierte Methode
     * Hier passiert die Magie
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = LayoutInflater.from(this).inflate(R.layout.activity_aufgaben, null);
        setContentView(mView);

        // initiate views
        editTextBeschreibung = (EditText) findViewById(R.id.editTextBeschreibung);
        editTextNotizen = (EditText) findViewById(R.id.editTextNotizen);
        textViewDatePicker = (TextView) findViewById(R.id.textViewDatePicker);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        overlay = (RelativeLayout) findViewById(R.id.overlay);
        // initiate FloatinActionButtons
        fabCamera = (FloatingActionButton) findViewById(R.id.fabCamera);
        fabGallarie = (FloatingActionButton) findViewById(R.id.fabGallarie);
        // setUp der Kursauswahl für die Aufgaben
        spinner = (Spinner) findViewById(R.id.spinner2);

        // setUp der toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        toolbar.setTitle("Neue Aufgabe");
        /**  Wichtig: @setDisplayHosAsUpEnabled erstellt einen "Back"-Button und die Activity ist "Vor" der Activity die sie aufuruft,
         *  sodass einfach wieder "zurück" gegangen werden kann
         */

        // kurseList wird durch die Methode sortierteListe() mit allen verfügbaren Kursen gefüllt

        Log.d(TAG, picturePaths.size() + "");
        // Adapter bekommt die Kurseliste für die Anzeige der Kurse
        photoAdapter = new PhotoAdapter(this, picturePaths);
        // setUp RecyclerView

        recyclerView.setAdapter(photoAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, 0, false));
        recyclerView.setHasFixedSize(true);


        fabAnimation();

        /** @fabCamera is for taking Pictures
         *  check if Button is invisble
         *  makes new Intent to get Access to the Camera
         *  if camera successfully takes a picture starActivityForResult is called
         **/
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabVisible != false) {
                    fabAnimateOut();
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {

                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(takePictureIntent, RESULT_LOAD_IMG);
                        }
                    }

                }
            }
        });
        /** @fabGallarie ist for take Picture from your gallary
         *  check if button is visible
         *  make new Intent for getting Access to the External Storage to read the Images
         *  if successfull startActivityForResult gets called
         **/
        fabGallarie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabVisible != false) {
                    fabAnimateOut();
                    // Create intent to Open Image applications like Gallery, Google Photos
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

                }
            }
        });


        chooseDate();

        // Liste mit alle ausgewählten Kursen wird rausgesucht
        List<dbKurs> allActiveKurse = new dbKurs().getAllActiveKurse();
        // Spinner Drop down elements
        List<String> fachlist = new ArrayList<String>();
        for (dbKurs kurs : allActiveKurse) {
            // der Spinner beinhaltet nur die Namen der Kurse
            fachlist.add(kurs.fach);
        }
        // Spinner wird mit der Liste konfiguriert
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fachlist);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        // @setOnItemSelectedListener wird ausgelöst wenn ein Kurs ausgewählt wurde
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // name des Kurses wird in selectedItem gespeichert
                selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /** @getIntent checks if the Activity is for a new Aufgabe or if a Aufgabe gets changed
         *  if intent isnt null the Aufgabe get loaded
         **/
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Long id = intent.getLongExtra("id", 0);
            reloadData(id);
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editTextNotizen, InputMethodManager.SHOW_IMPLICIT);

        }
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if back Button gets pressed
                startActivity(new Intent(AufgabenActivity.this, MainActivity.class));
            }
        });


    }

    private void chooseDate() {
        // Listener für das auswählen eines Datums
        textViewDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /** erstellt einen neuen DatePickerDialog der die Auswahl eines Daums ermöglicht
                 *  gibt das ausgewählte Jahr @year , den Monat @monthOfYeah und Tag @dayOfMontg zurück
                 */
                datePickerDialog = new DatePickerDialog(AufgabenActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        // ausgewähltes Datum wird in calendar2 gepseichert
                        calendar2 = Calendar.getInstance();
                        calendar2.set(Calendar.YEAR, year);
                        calendar2.set(Calendar.MONTH, monthOfYear);
                        calendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        //Datum wird als String gespeichert für die Anzeige
                        dateAnzeige = DateUtils.formatDateTime(AufgabenActivity.this, calendar2.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE);
                        // heutiges Datum wird in @dateCalendar gespeichert
                        dateCalendar = Calendar.getInstance();

                        /** Jetzt muss nur noch überprüft werden ob das ausgewählte Datum in der Zukunft liegt
                         *  Dies wird mit der methode .before() getestet. Solange das heutige Datum @dateCalendar
                         *  VOR dem ausgwählten liegt ist alles okay
                         */
                        if (dateCalendar.before(calendar2) == true) {
                            //wenn das Datum Okay ist wird es angezeigt und als Toast ausgegeben
                            Toast.makeText(AufgabenActivity.this, "Datum: " + dateAnzeige, Toast.LENGTH_SHORT).show();
                            textViewDatePicker.setText(dateAnzeige);
                            dateString = calendar2.get(Calendar.YEAR) + "." + (calendar2.get(Calendar.MONTH) + 1) + "." + calendar2.get(Calendar.DAY_OF_MONTH);
                            Log.d(TAG, dateString + "");
                        } else {
                            //ist es nicht Okay wird es dem User durch ein Toast mitgeteilt
                            Toast.makeText(AufgabenActivity.this, "Datum nicht möglich", Toast.LENGTH_SHORT).show();

                        }


                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                // datePickerDialog wird angezeigt
                datePickerDialog.show();
            }
        });
    }

    private void reloadData(Long id) {
        // Liste mit alle ausgewählten Kursen wird rausgesucht
        List<dbKurs> allActiveKurse = new dbKurs().getAllActiveKurse();
        aufgabeLoad = new dbAufgabe().getAufgabe(id);
        // check if aufgabe got load successfully
        if (aufgabeLoad != null) {

            editTextBeschreibung.setText(aufgabeLoad.beschreibung);
            editTextNotizen.setText(aufgabeLoad.notizen);
            // setUp the kurs for the Aufgabe
            List<String> fachList = new ArrayList<>();
            for (dbKurs kurs : allActiveKurse) {
                fachList.add(kurs.fach);
            }
            int position = fachList.indexOf(aufgabeLoad.kurs.fach);
            spinner.setSelection(position);

            // setUp Time to the DatePicker
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.M.d");
            try {
                calendar2.setTime(sdf2.parse(aufgabeLoad.zuletztAktualisiert));// all done
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateAnzeige = DateUtils.formatDateTime(AufgabenActivity.this, calendar2.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE);
            textViewDatePicker.setText(dateAnzeige);
            dateString = aufgabeLoad.zuletztAktualisiert;

            /** loads the images and applys them to the adapter
             **/
            if (aufgabeLoad.getMediaFile(aufgabeLoad.getId()).size() != 0) {
                Log.d("IMAGE", "Bild wird geladen");
                List<dbMediaFile> dbMediaFileList = new dbAufgabe().getMediaFile(aufgabeLoad.getId());
                for (int i = 0; i < dbMediaFileList.size(); i++) {
                    dbMediaFile dbMediaFile = dbMediaFileList.get(i);
                    photoAdapter.addPhoto(dbMediaFile.path);
                }


            }
        }
    }

    private void fabAnimation() {
        // prepare FloatingActionButton for Animation
        fabCamera.setAlpha(0f);
        fabGallarie.setAlpha(0f);
        fabCamera.animate().setDuration(0).rotation(270).translationY(-112).scaleX(0).scaleY(0).alpha(0);
        fabGallarie.animate().setDuration(0).rotation(270).translationY(-208).scaleX(0).scaleY(0).alpha(0);
        // appply Animation on OnClick
        fabAnimate = (FloatingActionButton) findViewById(R.id.fabAnimate);
        fabAnimate.animate().setDuration(0).scaleX(0).scaleY(0).alpha(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fabAnimate.animate().setDuration(500).scaleX(1).scaleY(1).alpha(1f).setInterpolator(new AccelerateDecelerateInterpolator());
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.d(TAG,"Animation got canceled");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        fabAnimate.setRippleColor(Color.parseColor("#000000"));
        fabAnimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabVisible == false) {
                    fabAnimateIn();
                } else {
                    fabAnimateOut();
                }

            }
        });
    }

    private void fabAnimateOut() {
        /** if fabVisible == true FloatinActionButtons are visible
         *  check SDK for Anmation
         *  make FloationActionButtons invisible with Animation
         *  Animation fade FloatingActionButton out and let them "fly" out
         *  AccelerateDeccelerateInterpolator for smooth Animation
         **/
        fabAnimate.animate().rotation(0).setDuration(300).setInterpolator(new AccelerateDecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                fabGallarie.animate().setDuration(300).alpha(1f).rotation(-270).scaleY(0).scaleX(0).alpha(0).setInterpolator(new AccelerateDecelerateInterpolator());
                fabCamera.animate().setDuration(300).alpha(1f).rotation(-270).scaleY(0).scaleX(0).alpha(0).setInterpolator(new AccelerateDecelerateInterpolator());
                fabAnimate.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E91E63")));

                ObjectAnimator colorFade = ObjectAnimator.ofObject(overlay, "backgroundColor", new ArgbEvaluator(), Color.parseColor(overlayColor), Color.parseColor(overlayTransparentColor));
                colorFade.setDuration(300);
                colorFade.start();


            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fabAnimate.setRippleColor(Color.parseColor("#000000"));
                enableDisableButtons(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.d(TAG,"Animation got canceled");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        fabVisible = false;
    }

    private void fabAnimateIn() {
        /** if fabVisible == false FloatinActionButtons are invisible
         *  check SDK for Anmation
         *  make FloationActionButtons visible with Animation
         *  Animation fade FloatingActionButton in and let them "fly" in
         *  AccelerateDeccelerateInterpolator for smooth Animation
         **/

        fabAnimate.animate().rotation(45).setDuration(300).setInterpolator(new AccelerateDecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //.translationY(-112)
                //.translationY(-208)
                fabGallarie.animate().setDuration(300).rotation(360).alpha(1f).scaleY(1).scaleX(1).alpha(1).setInterpolator(new AccelerateDecelerateInterpolator());
                fabCamera.animate().setDuration(300).rotation(360).alpha(1f).scaleY(1).scaleX(1).alpha(1).setInterpolator(new AccelerateDecelerateInterpolator());
                fabAnimate.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));

                ObjectAnimator colorFade = ObjectAnimator.ofObject(overlay, "backgroundColor", new ArgbEvaluator(), Color.parseColor(overlayTransparentColor), Color.parseColor(overlayColor));
                colorFade.setDuration(300);
                colorFade.start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fabAnimate.setRippleColor(Color.parseColor("#E91E63"));
                enableDisableButtons(false);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.d(TAG,"Animation got canceled");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        fabVisible = true;
    }

    private void enableDisableButtons(boolean b) {
        editTextBeschreibung.setEnabled(b);
        editTextNotizen.setEnabled(b);
        spinner.setEnabled(b);
        textViewDatePicker.setEnabled(b);

    }


    @Override
    protected void onResume() {
        super.onResume();
        fabAnimation();
    }

    /**
     * @onSaveInstanceState gets called if Activity gets destroyed
     * for example when screen gets rotated
     * saves the data for restoration
     **/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ArrayList<String> test = (ArrayList<String>) photoAdapter.getList();
        outState.putStringArrayList("paths", test);
        super.onSaveInstanceState(outState);
    }

    /**
     * @onRestoreSaveInstanceState gets called if Activity gets restored
     * for example when screen gets rotated
     * loads the data if it is aviable
     **/
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("paths")) {
                ArrayList<String> paths = savedInstanceState.getStringArrayList("paths");
                photoAdapter.newData(paths);
            }
                fabAnimation();

        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * @onActivityResult gets called if image was taken
     * applys the image to the photoAdapter
     **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMG) {
                if (photoFile != null) {
                    photoAdapter.addPhoto(photoFile.getAbsolutePath());
                    photoFile = null;

                }
                if (data != null) {
                    // parse the data to get the path
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    photoAdapter.addPhoto(picturePath);

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * @onOptionsItemSelected wird ausgelöst wenn ein MenuItem geclickt wird
     * Es gibt zwei MenuItems in der Toolbar / home : ein Pfeil der die Activity schließt /
     * menu_finnish ein Hacken wenn die Aufgabe gespeichert werden soll
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //wenn home geclickt wird soll er die Activity schließen
            case android.R.id.home:
                exitAcitivity();
                return true;
            //wenn menu_finnish geclickt wird , wird die Aufgabe gespeichert
            case R.id.menu_finnish:

                dateCalendar = Calendar.getInstance();
                // es wird überprüft ob alle notwendigen Daten ausgefüllt worden sind
                if (checkSave() == true) {
                    saveAufgabe();
                    Log.d("AufgabeActitviy", "Aufgabe wurde erstellt");
                    // Activity wird geschlossen
                    exitAcitivity();
                    return true;
                }


                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkSave() {
        if (calendar2 != null) {
            if (dateCalendar.before(calendar2) == true) {
                if (selectedItem != null) {
                    if (!editTextBeschreibung.getText().toString().equals("")) {
                        return true;
                    } else {
                        Toast.makeText(AufgabenActivity.this, "Titel ist leer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AufgabenActivity.this, "Es wurde kein Fach ausgewählt", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AufgabenActivity.this, "ausgewähltes Datum nicht möglich", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(AufgabenActivity.this, "Es wurde kein Datum ausgewählt", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void saveAufgabe() {
        if (aufgabeLoad == null) {
            aufgabeLoad = new dbAufgabe();
        }
        // Daten werden eingetragen
        aufgabeLoad.abgabeDatum = fromCalendarToString(calendar2);
        aufgabeLoad.erstelltAm = fromCalendarToString(Calendar.getInstance());
        Log.d(TAG, dateString + "");
        aufgabeLoad.zuletztAktualisiert = dateString;
        aufgabeLoad.erledigt = false;

        aufgabeLoad.kurs = new dbKurs().getKursWithFach(selectedItem);
        aufgabeLoad.beschreibung = editTextBeschreibung.getText().toString();
        if (editTextNotizen.getText().toString() != "") {
            aufgabeLoad.notizen = editTextNotizen.getText().toString();
        }


        // Aufgabe wird in der Datenbank gespeichert
        aufgabeLoad.save();
        if (aufgabeLoad.getMediaFile(aufgabeLoad.getId()).size() != 0) {
            List<dbMediaFile> mediaFileList = aufgabeLoad.getMediaFile(aufgabeLoad.getId());
            for (dbMediaFile mediaFile : mediaFileList) {
                mediaFile.delete();
            }
        }
        // Photos werden in der mediaFile Datenbank gespeichert und die Beziehung
        // zu den aufgaben werden hergestellt
        picturePaths = photoAdapter.getList();
        if (picturePaths.size() != 0) {
            for (String path : picturePaths) {
                dbMediaFile mediaFile = new dbMediaFile();
                mediaFile.path = path;
                mediaFile.aufgaben = aufgabeLoad;
                mediaFile.save();
                Log.d("Photo", "Photo gespeichert: " + path);
            }
        }


        createNotification(this, aufgabeLoad);
    }

    public void createNotification(Context context, dbAufgabe aufgabeLoad) {
        Log.d("onRecieve", "trigger");
        //create an Notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_done_white_48dp)
                .setContentTitle(aufgabeLoad.beschreibung)
                .setSubText(aufgabeLoad.kurs.fach)
                .setTicker("Neue Hausaufgabe !")
                .setCategory(Notification.CATEGORY_EVENT)
                .setContentText(aufgabeLoad.notizen);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        String multiLines = aufgabeLoad.notizen;
        String[] text;
        String delimiter = "\n";
        text = multiLines.split(delimiter);
        // Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Details: ");
        // Moves events into the expanded layout
        for (int i = 0; i < text.length; i++) {
            inboxStyle.addLine(text[i]);
        }
        // Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, AufgabenActivity.class);
        resultIntent.putExtra("id", aufgabeLoad.getId());
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(AufgabenActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.

        mNotificationManager.notify(1, mBuilder.build());
    }


    /**
     * Wichtige Methode die das erstellte Menü mit der ActionBar verbindet
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_aufgaben, menu);
        return true;
    }

    // @fromCalendarToString wandelt ein Datum in ein String um.
    // Wichtig für das Speichern des Datum da in der Datenbank kein Calendar gepeichert werden kann
    public String fromCalendarToString(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("MM d yyyy");
        Log.d("CALENDAR", format.format(calendar.getTime()));
        return format.format(calendar.getTime());
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Homework_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        Log.d("image:", image.getAbsolutePath());
        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    // applys exit animation if you leave the Activity
    public void exitAcitivity() {
        onBackPressed();
    }

    /**
     * Ändert die Makierung (Selection) in das entgegengesetze
     * <p/>
     * Wenn es das letzte Item in der Liste ist wird der actionMode beendet
     * Darf nur aufgerufen werden wenn der ActionMode nicht null ist
     *
     * @param position Position von dem Item
     */
    private void toggleSelection(int position) {
        // ändert die Makierung
        photoAdapter.toggleSelection(position);
        // count ist die Anzahl der ausgewählten Kurse
        int count = photoAdapter.getSelectedItemCount();

        // wenn kein Item mehr ausgewählt ist wird der actionMode beendet
        if (count == 0) {
            actionMode.finish();
        } else {
            // zählt im Titel mit wie viele Kurse ausgewählt worden sind
            actionMode.setTitle(String.valueOf(count) + " Selected");
            actionMode.invalidate();
        }
    }

    /**
     * @ActionModeCallback ist für die "neue" Toolbar zuständig sowie die Fertigstellung
     * der Kursauswahl wenn der User fertig ist.
     */
    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")

        private Context context;
        // Tag für die Log-Datein
        private final String TAG = ActionModeCallback.class.getSimpleName();

        public ActionModeCallback(Context context) {
            // Context für Anzeigen in der UI
            this.context = context;
        }

        // Hier wird das Menü erstellt selected_menu.xml
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // selected_menu ist ein spezielles Menü das angezeigt werden soll
            // findet sich unter res -> menu
            mode.getMenuInflater().inflate(R.menu.selected_menu_delete, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        /**
         * @onACtionItemClicked wird ausgelöst wenn eins der Menü Items gecklickt wird
         * je nach Menü Item wird entweder
         * die Auswahl fertig gestellt
         * Informationen zur Kursauswahl gegeben
         */
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_delete:
                    List<Integer> photoPositions = photoAdapter.getSelectedItems();
                    for (Integer i : photoPositions) {
                        photoAdapter.removeItem(i);
                    }
                    mode.finish();
                    break;
            }
            return true;
        }

        /**
         * @onDestroyActionMode wird ausgelöst wenn der User die Auswahl aufheben möchte
         * gibt an kursauswahlAdapter an die Auswahl aufzulösen
         * und der ActionMode wird beendet
         */
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            photoAdapter.clearSelection();
            actionMode = null;
        }

    }


    @Override
    public void onItemClicked(int position) {
        if (actionMode != null) {
            toggleSelection(position);
        } else {
            // neuer Intent
        }
        Log.d(TAG, "On photo clicked");
    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
        return false;
    }
}
