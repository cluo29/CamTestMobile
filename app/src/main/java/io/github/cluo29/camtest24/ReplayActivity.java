package io.github.cluo29.camtest24;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class ReplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);

        //set the title of tool bar

        //((AppCompatActivity)this).getSupportActionBar().setTitle("Manage Replay");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarReplay);
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

                Intent i = new Intent(getApplicationContext(), CreateReplayActivity.class);

                startActivity(i);

                break;
            default:
                break;
        }
        return true;
    }


    class ListItem {
        private Drawable image;
        private String status;
        private String replayName;

        public Drawable getImage() {
            return image;
        }

        public void setImage(Drawable image) {
            this.image = image;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        public String getReplayName() {
            return replayName;
        }

        public void setReplayName(String replayName) {
            this.replayName = replayName;
        }

        

    }


}
