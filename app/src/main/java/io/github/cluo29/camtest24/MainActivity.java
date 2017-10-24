package io.github.cluo29.camtest24;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button  FileButton = (Button) findViewById(R.id.fileButton);

        FileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Manage Video Files
                Intent i = new Intent(getApplicationContext(), FileActivity.class);

                startActivity(i);

            }
        });

        Button  ReplayButton = (Button) findViewById(R.id.replayButton);

        ReplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Manage Video Files
                Intent i = new Intent(getApplicationContext(), ReplayActivity.class);

                startActivity(i);

            }
        });
    }
}
