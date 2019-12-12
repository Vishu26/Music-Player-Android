package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private View play;
    private int song = 1;
    private ListView listView;
    private String[] string;
    private ListAdapter adapter;
    private MediaPlayer mpintro;
    private Handler handler;
    private SeekBar seekBar;
    private TextView sdur, edur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        play = findViewById(R.id.play);
        listView = findViewById(R.id.lyrics);
        sdur = findViewById(R.id.sdur);
        edur = findViewById(R.id.edur);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(song==1){
                    play.setBackground(getResources().getDrawable(R.mipmap.play));
                    song=0;
                    mpintro.pause();
                }
                else{
                    play.setBackground(getResources().getDrawable(R.mipmap.pause));
                    song=1;
                    mpintro.start();
                }
            }
        });
        seekBar = findViewById(R.id.seekBar);
        handler = new Handler();
        string = new String[]{"Whose hands play in my mind\n" +
                "જેના હાથમા રમે છે મારા મનની ઘૂઘરીઓ",
                "From whose drum lightning my feet\n" +
                "જેના ઢોલથી ઝબુકે મારા પગની વીજળીઓ",
                "Such a ray has come,\n" +
                "એવો આવ્યો રે આવ્યો અસવાર રે," ,
                "Let me become the dust of his feet\n" +
                "હું એની ડમરીની ધૂળ બની જઉં",
                "Give that rhythm and I will train.\n" +
                "એ તાલ દે અને હું તાલી દઉં." ,
                "He rattled his head in a dumb tone\n" +
                "એણે મૂંગા ભૂંગામાં પાડી ધાડ રે",
                "He planted trees in the desert of salt\n" +
                "એણે મીઠાના રણમાં વાવ્યું ઝાડ રે",
                "He cooked dreams and I sat down\n" +
                "એણે સપના રાંધ્યા હું બેઠી ખઉં",
                "Give that rhythm and I will train.\n" +
                "એ તાલ દે અને હું તાલી દઉં." ,
                "I did not run\n" +
                "એણે ચાલતી ન'તી હું તોય આંતરી",
                "I want to cheat\n" +
                "મારે છેતરાવું'તું એવી છેતરી",
                "It made me want to be a caddy.\n" +
                "એણે પગલી પાડી હું કેડી થઉં." ,
                "Such a ray has come,\n" +
                "એવો આવ્યો રે આવ્યો અસવાર રે,",
                "Let me become the dust of his feet\n" +
                "હું એની ડમરીની ધૂળ બની જઉં",
                "Give that rhythm and I will train.\n" +
                "એ તાલ દે અને હું તાલી દઉં."};
        adapter = new ListAdapter(this, R.layout.list_view, string);
        listView.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){


            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

            }

        } else {



            mpintro = MediaPlayer.create(this, Uri.parse("/storage/emulated/0/Vsongs/Asvaar.mp3"));
            mpintro.setLooping(true);
            mpintro.start();
            seekBar.setMax(mpintro.getDuration()/1000);
            MainActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if(mpintro != null && mpintro.isPlaying()){
                        int mCurrentPosition = mpintro.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                        int currentDuration = mpintro.getCurrentPosition();
                        updatePlayer(currentDuration, mpintro.getDuration());
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(mpintro != null && fromUser){
                        mpintro.seekTo(progress * 1000);
                    }
                }
            });
            MediaMetadataRetriever metaRetriever= new MediaMetadataRetriever();
            metaRetriever.setDataSource(getRealPathFromURI(Uri.parse("/storage/emulated/0/Vsongs/Asvaar.mp3")));
            String artist = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

            TextView name = findViewById(R.id.songname);
            name.setText(title);
            TextView ar = findViewById(R.id.artistname);
            ar.setText(artist);
        }
    }
    private String getRealPathFromURI(Uri uri) {
        File myFile = new File(uri.getPath().toString());
        String s = myFile.getAbsolutePath();
        return s;
    }
    private void updatePlayer(int currentDuration, int Duration){
        sdur.setText("" + milliSecondsToTimer((long) currentDuration));
        edur.setText("" + milliSecondsToTimer((long) Duration - currentDuration));
    }

    public  String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }
}
