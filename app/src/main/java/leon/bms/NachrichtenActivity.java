package leon.bms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NachrichtenActivity extends AppCompatActivity {
TextView textViewTitel,textViewContent,textViewOk;
    String titel,content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nachrichten);

        textViewTitel = (TextView) findViewById(R.id.textViewTitle);
        textViewContent = (TextView) findViewById(R.id.textViewContent);
        textViewOk = (TextView) findViewById(R.id.textViewOk);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            titel= extras.getString("titel");
            content = extras.getString("content");

        }
        textViewTitel.setText(titel);
        textViewContent.setText(content);
        textViewOk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onBackPressed();
                return true;
            }
        });
    }
}
