package leon.bms;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/** @AufgabenActivity ist für die erstellung von Aufgaben zuständig
 *  Es speichert die erstellten Aufgaben in der Datenbank
 *  Die Aufgaben beinhalten ein Kurs, ein Datum und min. eine Beschreibung
 *  dazu soll noch die Möglichkeit kommen von Bildern und anderen hilfreichen Informationen
 *  an die Aufgabe "dranzuhängen"
 */
public class AufgabenActivity extends AppCompatActivity{

    // @datePickerDialog wird gebraucht für die Auswahl der Datums
    DatePickerDialog datePickerDialog;
    // @dateString hier wird das Datum als String gespeichert
    String dateString;
    // @dateCalendar ist das heutige Datum @calendar2 ist das Datum welches der User auswählt
    Calendar dateCalendar,calendar2;
    Calendar calendar= Calendar.getInstance();
    // Views
    TextView textViewDatePicker;
    EditText editTextBeschreibung,editTextNotizen;
    // zeigt eine Liste mit den Kursen an
    Spinner spinner;
    // @selectedItem speichert den ausgewählten Kurs
    String selectedItem;
    FloatingActionButton fabCamera,fabGallarie;
    Intent bildintent;
    int Kameracode = 1;
    CardView cardViewImage;
    File photoFile = null;
   LinearLayout linearLayout;
    int counter =0;
    String picturePath;
    List<String> picturePaths=new ArrayList<>();
    static int RESULT_LOAD_IMG = 1;

    /**
     * Automatische generierte Methode
     * Hier passiert die Magie
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Verbindung zum layout acitivity_aufgaben
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(new Fade().setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator()));
        }
        setContentView(R.layout.activity_aufgaben);

        // setUp der toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_18dp);
        toolbar.setTitle("Neue Aufgabe");
        /**  Wichtig: @setDisplayHosAsUpEnabled erstellt einen "Back"-Button und die Activity ist "Vor" der Activity die sie aufuruft,
         *  sodass einfach wieder "zurück" gegangen werden kann
         */


        linearLayout = (LinearLayout) findViewById(R.id.linearLayout1);
        cardViewImage = (CardView) findViewById(R.id.cardView3);
        fabCamera = (FloatingActionButton) findViewById(R.id.fabCamera);
        fabGallarie = (FloatingActionButton) findViewById(R.id.fabGallarie);

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bildintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (bildintent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        picturePaths.add(photoFile.getAbsolutePath());
                        Log.d("TAG", "intent ausgeführt");
                        bildintent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(bildintent, Kameracode);
                    }
                }
            }
        });
        fabGallarie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

            }
        });


        editTextBeschreibung = (EditText) findViewById(R.id.editTextBeschreibung);
        editTextNotizen = (EditText) findViewById(R.id.editTextNotizen);
        textViewDatePicker = (TextView) findViewById(R.id.textViewDatePicker);


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
                        dateString = DateUtils.formatDateTime(AufgabenActivity.this, calendar2.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE);

                        // heutiges Datum wird in @dateCalendar gespeichert
                        dateCalendar = Calendar.getInstance();

                        /** Jetzt muss nur noch überprüft werden ob das ausgewählte Datum in der Zukunft liegt
                         *  Dies wird mit der methode .before() getestet. Solange das heutige Datum @dateCalendar
                         *  VOR dem ausgwählten liegt ist alles okay
                         */
                        if (dateCalendar.before(calendar2) == true) {
                            //wenn das Datum Okay ist wird es angezeigt und als Toast ausgegeben
                            Toast.makeText(AufgabenActivity.this, "Datum: " + dateString, Toast.LENGTH_SHORT).show();
                            textViewDatePicker.setText(dateString);
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

        // setUp der Kursauswahl für die Aufgaben
        spinner = (Spinner) findViewById(R.id.spinner2);
        // Liste mit alle ausgewählten Kursen wird rausgesucht
        List<dbKurs> allActiveKurse = new dbKurs().getAllActiveKurse();
        // Spinner wird mit der Liste konfiguriert
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        for (dbKurs kurs : allActiveKurse) {
            // der Spinner beinhaltet nur die Namen der Kurse
            spinnerAdapter.add(kurs.fach);
        }
        spinnerAdapter.notifyDataSetChanged();
        // @setOnItemSelectedListener wird ausgelöst wenn ein Kurs ausgewählt wurde
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // name des Kurses wird in selectedItem gespeichert
                selectedItem = parent.getItemAtPosition(position).toString();
                // Dem User wird mitgeteilt welchen Kurs er ausgewählt hat
                Toast.makeText(parent.getContext(), "Selected: " + selectedItem, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            Long id = intent.getLongExtra("id", 0);
            Log.d("TAG", id + "");
            List<dbMediaFile> mediaFiles = dbMediaFile.listAll(dbMediaFile.class);
            Log.d(getClass().getSimpleName(), mediaFiles.size() + " SIZE");
            if (mediaFiles.size() > 0) {
                dbMediaFile mediaFile = mediaFiles.get(0);
                Log.d("TAG", mediaFile.aufgaben.getId() + " ");
            }

            dbAufgabe aufgabe = new dbAufgabe().getAufgabe(id);
            if (aufgabe != null) {
                editTextBeschreibung.setText(aufgabe.beschreibung);
                editTextNotizen.setText(aufgabe.notizen);
                List<String> fachList = new ArrayList<>();
                for (dbKurs kurs : allActiveKurse) {
                    fachList.add(kurs.fach);
                }
                int position = fachList.indexOf(aufgabe.kurs.fach);
                spinner.setSelection(position);


                Calendar abgabeDatum = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("MM d yyyy");
                try {
                    abgabeDatum.setTime(sdf.parse(aufgabe.abgabeDatum));// all done
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                dateString = DateUtils.formatDateTime(AufgabenActivity.this, abgabeDatum.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE);
                textViewDatePicker.setText(dateString);


                if (aufgabe.getMediaFile(aufgabe.getId()).size() != 0) {
                    Log.d("IMAGE", "Bild wird geladen");
                    List<dbMediaFile> dbMediaFileList = new dbAufgabe().getMediaFile(aufgabe.getId());
                    for (int i = 0; i < dbMediaFileList.size(); i++) {
                        File imgFile = new File(dbMediaFileList.get(i).path);
                        if (imgFile.exists()) {
                            Uri uriFile = Uri.fromFile(imgFile);
                            ImageView imageViewCamera = new ImageView(this);
                            imageViewCamera.setImageURI(uriFile);
                            imageViewCamera.setId(counter++);
                            RelativeLayout.LayoutParams paramsImage = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                            paramsImage.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            paramsImage.height = 400;
                            imageViewCamera.setPadding(8, 8, 8, 8);
                            imageViewCamera.setLayoutParams(paramsImage);
                            linearLayout.addView(imageViewCamera, paramsImage);
                            cardViewImage.setVisibility(1);
                        }
                    }


                }
            }


        }
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AufgabenActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK){

            if(requestCode == Kameracode){
                Bundle extras = data.getExtras();

                ImageView imageViewCamera = new ImageView(this);
                imageViewCamera.setId(counter++);
                RelativeLayout.LayoutParams paramsImage = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                paramsImage.addRule(RelativeLayout.CENTER_HORIZONTAL);
                paramsImage.height = 400;
                imageViewCamera.setPadding(8, 8, 8, 8);
                imageViewCamera.setLayoutParams(paramsImage);
                if (extras!=null){

                imageViewCamera.setImageBitmap((Bitmap) extras.get("data"));
                }else {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    picturePaths.add(picturePath);
                    imageViewCamera.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }

                linearLayout.addView(imageViewCamera,paramsImage);
                cardViewImage.setVisibility(1);

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /** @onOptionsItemSelected wird ausgelöst wenn ein MenuItem geclickt wird
     *  Es gibt zwei MenuItems in der Toolbar / home : ein Pfeil der die Activity schließt /
     *   menu_finnish ein Hacken wenn die Aufgabe gespeichert werden soll
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
                if (calendar2!=null&&dateCalendar.before(calendar2) == true&&selectedItem!=null && editTextBeschreibung.getText().toString()!="") {
                            dbAufgabe aufgabe = new dbAufgabe();
                            // Daten werden eingetragen
                            aufgabe.abgabeDatum = fromCalendarToString(calendar2);
                            aufgabe.erstelltAm = fromCalendarToString(Calendar.getInstance());
                            aufgabe.zuletztAktualisiert = fromCalendarToString(Calendar.getInstance());
                            aufgabe.erledigt = false;

                            aufgabe.kurs = new dbKurs().getKursWithFach(selectedItem);
                            aufgabe.beschreibung = editTextBeschreibung.getText().toString();
                            if (editTextNotizen.getText().toString()!= "") {
                                aufgabe.notizen = editTextNotizen.getText().toString();
                            }


                            // Aufgabe wird in der Datenbank gespeichert
                            aufgabe.save();
                            if (picturePaths.size()!=0){
                                for (String path:picturePaths){
                                    dbMediaFile mediaFile = new dbMediaFile();
                                    mediaFile.path = path;
                                    mediaFile.aufgaben = aufgabe;
                                    mediaFile.save();
                                    Log.d("Photo","Photo gespeichert: "+path);
                                }
                            }
                            Log.d("AufgabeActitviy","Aufgabe wurde erstellt");
                            // Activity wird geschlossen
                            exitAcitivity();
                            return true;

                    }


                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /** Wichtige Methode die das erstellte Menü mit der ActionBar verbindet
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_aufgaben, menu);
        return true;
    }
    // @fromCalendarToString wandelt ein Datum in ein String um.
    // Wichtig für das Speichern des Datum da in der Datenbank kein Calendar gepeichert werden kann
    public String fromCalendarToString (Calendar calendar){
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
        Log.d("image:",image.getAbsolutePath());
        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }
    public void exitAcitivity(){
        Fade fade = new Fade();
        fade.setDuration(500);
        getWindow().setEnterTransition(fade);
        onBackPressed();
    }


}
