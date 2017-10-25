package io.github.cluo29.camtest24;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;

import io.github.cluo29.camtest24.providers.Replay_Provider.Replay_Info;

public class CreateReplayActivity extends AppCompatActivity {

    EditText replayNameET;
    EditText appNameET;
    TextView videoTV;
    Button pickButton;
    Button confirmButton;
    RadioGroup radioOGroup; //orientation group
    RadioGroup radioMGroup; //Mode group

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_replay);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCreateReplay);
        toolbar.setTitle("Create Replay");

        setSupportActionBar(toolbar);

        //editTextCR1
        //replayName
        replayNameET = (EditText) findViewById(R.id.editTextCR1);

        //editTextCR2
        //app Name
        appNameET = (EditText) findViewById(R.id.editTextCR2);

        //textView6
        //videoTV
        videoTV = (TextView) findViewById(R.id.textView6);

        //buttonCR1
        //pick button
        pickButton = (Button) findViewById(R.id.buttonCR1);

        //buttonCR2
        //confirm button
        confirmButton = (Button) findViewById(R.id.buttonCR2);

        //orientation radio group
        //radioOGroup
        radioOGroup = (RadioGroup) findViewById(R.id.radioReplay);

        //mode radio group
        //radioMGroup
        radioMGroup = (RadioGroup) findViewById(R.id.radioReplay2);

        //add video chooser to pick button
        pickButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick( View view) {

                //choose Video Files
                Intent intent = new Intent()
                        .setType("*/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select a file"), 1);
            }
        });

        //put input into DB
        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick( View view) {

                String replayNameString = replayNameET.getText().toString();;

                String appNameString = appNameET.getText().toString();

                //video string realPath

                int selectOID = radioOGroup.getCheckedRadioButtonId();

                RadioButton OrientationRadioButton = (RadioButton)findViewById(selectOID);

                String OrientationString = OrientationRadioButton.getText().toString();

                int selectMID = radioMGroup.getCheckedRadioButtonId();

                RadioButton ModeRadioButton = (RadioButton)findViewById(selectMID);

                String ModeString = ModeRadioButton.getText().toString();

                Calendar dateData = Calendar.getInstance();

                ContentValues rowData = new ContentValues();
                rowData.put(Replay_Info.TIMESTAMP, System.currentTimeMillis());
                rowData.put(Replay_Info.DAY, dateData.get(Calendar.DAY_OF_MONTH));
                rowData.put(Replay_Info.MONTH, dateData.get(Calendar.MONTH));
                rowData.put(Replay_Info.YEAR, dateData.get(Calendar.YEAR));
                rowData.put(Replay_Info.REPLAYNAME, replayNameString);
                rowData.put(Replay_Info.APPNAME, appNameString);
                rowData.put(Replay_Info.VIDEO, realPath);
                rowData.put(Replay_Info.ORIENTATION, OrientationString);
                rowData.put(Replay_Info.MODE, ModeString);
                rowData.put(Replay_Info.STATUS, "Pending");
                getContentResolver().insert(Replay_Info.CONTENT_URI,rowData);
                finish();
            }
        });
    }

    public Uri selectedfile;

    String realPath="";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK) {
            selectedfile = data.getData(); //The uri with the location of the file

            //Toast.makeText(this, selectedfile.getPath(), Toast.LENGTH_LONG).show();

            realPath = getRealPathFromURI(getApplicationContext(),selectedfile);

            //textViewCV2
            videoTV.setText(realPath);
        }
    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(contentUri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        assert cursor != null;
        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }
}
