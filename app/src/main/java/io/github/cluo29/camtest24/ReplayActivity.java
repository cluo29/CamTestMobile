package io.github.cluo29.camtest24;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ReplayActivity extends AppCompatActivity {

    class ListItemView {
        ImageView imageView;
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;
        TextView textView6;

    }

    class ListItem {
        private Drawable image;
        private String status;
        private String replayName;
        private String appName;
        private String Day;
        private String Month;
        private String Year;

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
        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }
        public String getDay() {
            return Day;
        }

        public void setDay(String Day) {
            this.Day = Day;
        }
        public String getMonth() {
            return Month;
        }

        public void setMonth(String Month) {
            this.Month = Month;
        }
        public String getYear() {
            return Year;
        }

        public void setYear(String Year) {
            this.Year = Year;
        }

    }


    private ListView mListView;
    private ArrayList<ListItem> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);

        mListView = (ListView) findViewById(R.id.ReplayList);

        //set the title of tool bar

        //((AppCompatActivity)this).getSupportActionBar().setTitle("Manage Replay");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarReplay);
        toolbar.setTitle("Manage Replay");

        setSupportActionBar(toolbar);

        Resources res = this.getResources();

        mList = new ArrayList<ReplayActivity.ListItem>();

        ListItem item = new ListItem();
        item.setImage(res.getDrawable(R.drawable.ic_action_done));
        item.setStatus("Done");
        item.setReplayName("Super Replay");
        item.setAppName("Super App");
        item.setDay("21");
        item.setMonth("1");
        item.setYear("1111");


        mList.add(item);

        MainListViewAdapter adapter = new MainListViewAdapter();

        mListView.setAdapter(adapter);
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


    class MainListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {

            return mList.size();
        }

        @Override
        public Object getItem(int position) {

            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListItemView listItemView;
            if (convertView == null) {
                convertView = LayoutInflater.from(ReplayActivity.this).inflate(
                        R.layout.replay_item, null);
                listItemView = new ListItemView();
                listItemView.imageView = (ImageView) convertView
                        .findViewById(R.id.RListImage);
                listItemView.textView1 = (TextView) convertView
                        .findViewById(R.id.textViewRList1);
                listItemView.textView2 = (TextView) convertView
                        .findViewById(R.id.textViewRList2);
                listItemView.textView3 = (TextView) convertView
                        .findViewById(R.id.textViewRList3);
                listItemView.textView4 = (TextView) convertView
                        .findViewById(R.id.textViewRList4);
                listItemView.textView5 = (TextView) convertView
                        .findViewById(R.id.textViewRList5);
                listItemView.textView6 = (TextView) convertView
                        .findViewById(R.id.textViewRList6);


                convertView.setTag(listItemView);
            } else {

                listItemView = (ListItemView) convertView.getTag();
            }
            Drawable img = mList.get(position).getImage();
            String status = mList.get(position).getStatus();
            String replayName = mList.get(position).getReplayName();
            String appName = mList.get(position).getAppName();
            String Day = mList.get(position).getDay();
            String Month = mList.get(position).getMonth();
            String Year = mList.get(position).getYear();

            listItemView.imageView.setImageDrawable(img);
            listItemView.textView1.setText(status);
            listItemView.textView2.setText(replayName);
            listItemView.textView3.setText(appName);
            listItemView.textView4.setText(Day);
            listItemView.textView5.setText(Month);
            listItemView.textView6.setText(Year);

            return convertView;

            /*

            private String status;
            private String replayName;
            private String appName;
            private String Day;
            private String Month;
            private String Year;
             */
        }
    }




}
