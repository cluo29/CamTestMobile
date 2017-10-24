package io.github.cluo29.camtest24;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class CreateReplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_replay);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCreateReplay);
        toolbar.setTitle("Create Replay");

        setSupportActionBar(toolbar);
    }
}
