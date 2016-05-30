package leon.bms.activites.aufgabe;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.meetic.marypopup.MaryPopup;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import leon.bms.AlarmReciever;
import leon.bms.R;
import leon.bms.activites.main.MainActivity;
import leon.bms.adapters.PhotoAdapter;
import leon.bms.database.dbAufgabe;
import leon.bms.database.dbFach;
import leon.bms.database.dbKurs;
import leon.bms.database.dbMediaFile;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @AufgabenActivity ist für die erstellung von Aufgaben zuständig
 * Es speichert die erstellten Aufgaben in der Datenbank
 * Die Aufgaben beinhalten ein Kurs, ein Datum und min. eine Beschreibung
 * dazu soll noch die Möglichkeit kommen von Bildern und anderen hilfreichen Informationen
 * an die Aufgabe "dranzuhängen"
 */
public class AufgabenActivity extends AppCompatActivity implements PhotoAdapter.ViewHolder.ClickListener {
    BroadcastReceiver broadcastReceiver;
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
    // @picturePaths speichert alle Pfade für die Bilder
    List<String> picturePaths = new ArrayList<>();
    // Wichtiger Code für das machen von Fotos
    static int CAMERA_CODE = 1;
    static int GALLARY_CODE = 2;
    // @aufgabeLoad wichtig für das ändern von Aufaben
    dbAufgabe aufgabe = null;
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
    MaryPopup popup;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
        getSupportActionBar().setTitle("Neue Aufgabe");
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

        popup = MaryPopup.with(this)
                .cancellable(true)
                .draggable(true)
                .center(true)
                .inlineMove(true)
                .scaleDownDragging(true)
                .shadow(false)
                .scaleDownCloseOnDrag(true)
                .fadeOutDragging(true)
                .openDuration(300)
                .closeDuration(300)
                .blackOverlayColor(Color.parseColor(overlayColor))
                .backgroundColor(Color.parseColor("#00000000"));


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
                            startActivityForResult(takePictureIntent, CAMERA_CODE);
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
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLARY_CODE);

                }
            }
        });


        chooseDate();

        // Liste mit alle ausgewählten Kursen wird rausgesucht
        List<dbKurs> allActiveKurse = new dbKurs().getAllActiveKurse();
        // Spinner Drop down elements
        final List<String> fachlist = new ArrayList<String>();
        for (dbKurs kurs : allActiveKurse) {
            // der Spinner beinhaltet nur die Namen der Kurse
            fachlist.add(kurs.fachnew.name);
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
                selectedItem = fachlist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedItem = null;
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


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

    @Override
    public void onBackPressed() {
        if (!popup.close(true)) {
            super.onBackPressed();
        } else {
            popup.close(true);
        }
    }

    private void reloadData(Long id) {
        // Liste mit alle ausgewählten Kursen wird rausgesucht
        List<dbKurs> allActiveKurse = new dbKurs().getAllActiveKurse();
        aufgabe = new dbAufgabe().getAufgabe(id);
        // check if aufgabe got load successfully
        if (aufgabe != null) {

            editTextBeschreibung.setText(aufgabe.beschreibung);
            editTextNotizen.setText(aufgabe.notizen);
            // setUp the kurs for the Aufgabe
            List<String> fachList = new ArrayList<>();
            for (dbKurs kurs : allActiveKurse) {
                fachList.add(kurs.fachnew.name);
            }
            int position = fachList.indexOf(aufgabe.kurs.fachnew);
            spinner.setSelection(position);

            // setUp Time to the DatePicker
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.M.d");
            try {
                calendar2.setTime(sdf2.parse(aufgabe.zuletztAktualisiert));// all done
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateAnzeige = DateUtils.formatDateTime(AufgabenActivity.this, calendar2.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE);
            textViewDatePicker.setText(dateAnzeige);
            dateString = aufgabe.zuletztAktualisiert;

            /** loads the images and applys them to the adapter
             **/
            if (aufgabe.getMediaFile(aufgabe.getId()).size() != 0) {
                Log.d("IMAGE", "Bild wird geladen");
                List<dbMediaFile> dbMediaFileList = new dbAufgabe().getMediaFile(aufgabe.getId());
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
        fabCamera.animate().setDuration(1).rotation(270).scaleX(0).scaleY(0).alpha(0);
        fabGallarie.animate().setDuration(1).rotation(270).scaleX(0).scaleY(0).alpha(0);
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
                Log.d(TAG, "Animation got canceled");
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
                Log.d(TAG, "Animation got canceled");
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
            if (requestCode == CAMERA_CODE) {
                if (photoFile != null) {
                    photoAdapter.addPhoto(photoFile.getAbsolutePath());
                    photoFile = null;

                }
            }
            if (requestCode == GALLARY_CODE) {
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

    private Bitmap getScaledBitmap(String picturePath, int width, int height) {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(picturePath, sizeOptions);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
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
                    if (!editTextBeschreibung.getText().toString().trim().equals("")) {
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
        if (aufgabe == null) {
            aufgabe = new dbAufgabe();
        }
        // Daten werden eingetragen
        aufgabe.abgabeDatum = fromCalendarToString(calendar2);
        aufgabe.erstelltAm = fromCalendarToString(Calendar.getInstance());
        Log.d(TAG, dateString + "");
        aufgabe.zuletztAktualisiert = dateString;
        aufgabe.erledigt = false;
        dbFach fach = new dbFach().getFachWithName(selectedItem);
        aufgabe.kurs = fach.getKursWithFachId(fach.getId());
        aufgabe.beschreibung = editTextBeschreibung.getText().toString();
        if (editTextNotizen.getText().toString() != "") {
            aufgabe.notizen = editTextNotizen.getText().toString();
        }


        // Aufgabe wird in der Datenbank gespeichert
        if (checkAufgabe(aufgabe)) {
            Log.d(TAG, "Aufgabe wurde gespeichert!");
            aufgabe.save();

            if (aufgabe.getMediaFile(aufgabe.getId()).size() != 0) {
                List<dbMediaFile> mediaFileList = aufgabe.getMediaFile(aufgabe.getId());
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
                    mediaFile.aufgaben = aufgabe;
                    mediaFile.save();
                    Log.d("Photo", "Photo gespeichert: " + path);
                }
            }

            createNotification(aufgabe.getId());

        }

    }

    public void createNotification(Long id) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 3);
        Intent myIntent = new Intent(this, AlarmReciever.class);
        myIntent.setAction("com.tutorialspoint.CUSTOM_INTENT");
        myIntent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                100, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

        Log.d("AUFGABEN","alarm set");

    }




    public boolean checkAufgabe(dbAufgabe aufgabe) {
        if (aufgabe.abgabeDatum != null) {
            if (aufgabe.beschreibung != null) {
                if (aufgabe.kurs != null) {
                    if (aufgabe.erledigt != null) {
                        if (aufgabe.zuletztAktualisiert != null)
                            return true;
                    }
                }
            }
        }
        return false;
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
    public void onItemClicked(int position, View v) {
        String photoPath = photoAdapter.getPhoto(position);
        Bitmap myBitmap = getScaledBitmap(photoPath, 800, 800);
        View popupImageLayout = LayoutInflater.from(this).inflate(R.layout.popup_image, null, false);
        ImageView imageView = (ImageView) popupImageLayout.findViewById(R.id.image);
        imageView.setImageBitmap(myBitmap);
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
        popup
                .content(popupImageLayout)
                .from(v)
                .show();
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
