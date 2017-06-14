package com.shortly.shortlyapp.UI.Activities.MainActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.widgets.VideoPlayerView;

public class MainActivity extends AppCompatActivity {

    VideoPlayerView videoPlayerView;

    int currentTime =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        videoPlayerView = (VideoPlayerView)findViewById(R.id.videoView);
//https://s3-eu-west-1.amazonaws.com/shorttest20/trans/1/12cca117efffeab1901e47c4658102f5.mp4
//        videoPlayerView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.movie,false);
        videoPlayerView.setVideoPath("https://s3-eu-west-1.amazonaws.com/shorttest20/trans/1/12cca117efffeab1901e47c4658102f5.mp4",false);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause(){
        currentTime = videoPlayerView.getCurrentTime();
        super.onPause();

    }

    @Override
    public void onResume(){
        super.onResume();

        if(currentTime>0){
            videoPlayerView.setCurrentTime(currentTime);

        }
        videoPlayerView.setCurrentTime(50);
        videoPlayerView.play();


    }
}
