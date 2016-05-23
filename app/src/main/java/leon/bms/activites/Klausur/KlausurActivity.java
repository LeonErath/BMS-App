package leon.bms.activites.Klausur;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import leon.bms.R;

public class KlausurActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klausur);

        //die Activity lädt die Daten des speziellen Artikel
        if (getIntent() !=null){
            Intent intent = getIntent();
            Long id = intent.getLongExtra("id",100000000);
            if (id != 100000000){


            }
        }


    }
}
