package co.devguilds.radiotemp;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class HomeActivity extends ActionBarActivity implements  MediaController.MediaPlayerControl, MediaPlayer.OnPreparedListener {

    Button playButton;
    ImageView playView;
    TextView txtFm;




    String stream = "http://s9.voscast.com:8868/;stream1501837481714/1";

    MediaPlayer mediaPlayer;
    boolean prepared = false;
    boolean started = false;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        txtFm = (TextView)findViewById(R.id.FMtextView);


        playButton = (Button)findViewById(R.id.playButton);
        playView = (ImageView)findViewById(R.id.playView);
        playButton.setEnabled(false);
        playButton.setText("LOADING");
        playView.setVisibility(View.GONE);

        mediaPlayer = new MediaPlayer();
         mediaController = new MediaController(this){
            @Override
            public void hide() {

            }
        };

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer.setOnPreparedListener(this);



        new PlayerTask().execute(stream);
        mediaPlayer.start();


//        playButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(started){
//                    started = false;
//                    mediaPlayer.start();
//                    playButton.setText("PLAY");
//                    (findViewById(R.id.playView)).setVisibility(View.GONE);
//                } else{
//                    started = true;
//                    mediaPlayer.start();
//                    playButton.setText("PAUSE");
//                    (findViewById(R.id.playView)).setVisibility(View.VISIBLE);
//                    txtFm.setText("FM Mode");
//
//                }
//
//
//            }
//        });




    }

    @Override
    public void start() {
    mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
       mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(findViewById(R.id.layoutMain));
        mediaController.setEnabled(true);
        mediaController.show();

    }


    class PlayerTask extends AsyncTask<String,Void,Boolean> {

         @Override
         protected Boolean doInBackground(String... strings) {

             try {
                 mediaPlayer.setDataSource(strings[0]);
                 mediaPlayer.prepare();
                 prepared = true;
             } catch (IOException e) {
                 e.printStackTrace();
             }

             return prepared;
         }

         @Override
         protected void onPostExecute(Boolean aBoolean) {
             super.onPostExecute(aBoolean);

             playButton.setEnabled(true);
             playButton.setText("PLAY");
             Toast.makeText(HomeActivity.this, "Ready", Toast.LENGTH_SHORT).show();
         }
     }


    @Override
    protected void onPause() {
        super.onPause();
        if(started){
            mediaPlayer.pause();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(started){
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(prepared){
            mediaPlayer.release();
        }

    }
}
