package io.github.cluo29.camtest24;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class FileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        //set the title of tool bar
        //((AppCompatActivity)this).getSupportActionBar().setTitle("Manage Replay");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarFile);
        toolbar.setTitle("Manage Replay");

        setSupportActionBar(toolbar);

    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_video:

                //Toast.makeText(this, "create_video", Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), CreateVideoActivity.class);

                startActivity(i);

                break;
            default:
                break;
        }
        return true;
    }
}
