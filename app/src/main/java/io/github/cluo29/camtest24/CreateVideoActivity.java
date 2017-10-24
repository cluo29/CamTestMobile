package io.github.cluo29.camtest24;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class CreateVideoActivity extends AppCompatActivity {

    TextView filePathTextView;

    private RadioGroup radioVideoGroup;
    private RadioButton radioVideoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_video);

        filePathTextView = (TextView) findViewById(R.id.textViewCV2) ;

        radioVideoGroup = (RadioGroup) findViewById(R.id.radioVideo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCreateVideo);
        toolbar.setTitle("Add Video");

        setSupportActionBar(toolbar);

        //video chooser
        //click buttonCVChoose

        Button FileChooseButton = (Button) findViewById(R.id.buttonCVChoose);

        FileChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //choose Video Files
                Intent intent = new Intent()
                        .setType("*/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select a file"), 1);

            }
        });

        //portray or landscape
        Button FileConfirmButton = (Button) findViewById(R.id.buttonCVConfirm);
        FileConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //read radio

                int selectedId = radioVideoGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioVideoButton = (RadioButton) findViewById(selectedId);

                Toast.makeText(CreateVideoActivity.this, radioVideoButton.getText(), Toast.LENGTH_LONG).show();

                //add to DB

                finish();

            }
        });

    }
    public Uri selectedfile;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK) {
            selectedfile = data.getData(); //The uri with the location of the file


            //Toast.makeText(this, selectedfile.getPath(), Toast.LENGTH_LONG).show();

            String realPath = getRealPathFromURI(getApplicationContext(),selectedfile);

            Log.d("Stupid HP Keyborad","realPath = "+realPath);

            //textViewCV2
            filePathTextView.setText(realPath);
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
