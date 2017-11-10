package io.github.cluo29.camtest24;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.github.cluo29.camtest24.providers.Replay_Provider.Replay_Info;
import io.github.cluo29.camtest24.providers.Result_Provider.Result_Info;

public class ManageReplayActivity extends AppCompatActivity {

    String replayName;
    String appName;

    TextView TVReplayName;
    TextView TVAppName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_replay);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarManageReplay);
        toolbar.setTitle("Manage Replay");

        replayName = getIntent().getStringExtra("ReplayName");
        appName = getIntent().getStringExtra("AppName");

        //Toast.makeText(this, replayName, Toast.LENGTH_LONG).show();
        TVReplayName = (TextView) findViewById(R.id.textViewMR1);
        TVAppName = (TextView) findViewById(R.id.textViewMR2);

        TVReplayName.setText(replayName);
        TVAppName.setText(appName);

        Button  launchButton = (Button) findViewById(R.id.buttonMR1);

        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the ID of this one

                int itemID=0;

                Cursor cursor = getContentResolver().query(Replay_Info.CONTENT_URI, null,
                        Replay_Info.REPLAYNAME+"='"+replayName+"' AND "+
                                Replay_Info.APPNAME+"='"+appName+"'", null, Replay_Info._ID + " DESC");

                if (cursor != null && (cursor.moveToNext())) {
                    itemID = cursor.getInt(cursor.getColumnIndex(Replay_Info._ID));

                }
                if (cursor != null && !cursor.isClosed())
                {
                    cursor.close();
                }

                //set this one scheduled
                //set all others saved or done...

                ContentValues values = new ContentValues();

                values.put(Replay_Info.STATUS, "Scheduled");
                getContentResolver().update(Replay_Info.CONTENT_URI,values,Replay_Info._ID+"=?",new String[]{String.valueOf(itemID)}); //id is the id of the row you wan to update


                Toast.makeText(ManageReplayActivity.this, "It is ready to run", Toast.LENGTH_LONG).show();

            }
        });

        Button  exportButton = (Button) findViewById(R.id.buttonMR2);

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //export to csv.
                Toast.makeText(ManageReplayActivity.this, "Exported", Toast.LENGTH_LONG).show();

                //use ; to split
                Cursor cursor = getContentResolver().query(Result_Info.CONTENT_URI, null,
                        null, null, Result_Info._ID + " ASC");

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String TYPE = cursor.getString(cursor.getColumnIndex(Result_Info.TYPE));
                        String TITLE = cursor.getString(cursor.getColumnIndex(Result_Info.TITLE));
                        String INFO = cursor.getString(cursor.getColumnIndex(Result_Info.INFO));
                        String EVENTTIME = cursor.getString(cursor.getColumnIndex(Result_Info.EVENTTIME));

                        String outputLine = TYPE + ";" +
                                TITLE + ";" +
                                INFO + ";" +
                                EVENTTIME;

                        Log.d("123", "out = "+ outputLine);

                        File file = new File(Environment.getExternalStorageDirectory() +
                                "/Documents/output.csv");

                        try {
                            if (!file.exists()) {
                                file.createNewFile();
                            }

                            FileOutputStream out = new FileOutputStream(file, true);
                            //true means append

                            StringBuffer sb = new StringBuffer();
                            sb.append(outputLine + "\r\n");
                            out.write(sb.toString().getBytes("utf-8"));//注意需要转换对应的字符集
                            out.flush();
                            out.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                if (cursor != null && !cursor.isClosed())
                {
                    cursor.close();
                }


            }
        });

    }
}
