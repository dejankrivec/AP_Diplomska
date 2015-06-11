package com.example.dejan.ap_diplomska.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.dejan.ap_diplomska.AppConstants;
import com.example.dejan.ap_diplomska.MainActivity;
import com.example.dejan.ap_diplomska.R;

import java.io.IOException;

/**
 * Created by dejan on 23.5.2015.
 */
public class MyService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    static MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean isPlaying = false;
    private boolean isResume = false;
    //private boolean firstTime = true;
    private static MyService service;
    private Intent showTaskIntent;
    private final Handler handler = new Handler();
    private Runnable run;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!AppConstants.preferences.getBoolean("FirstTime",true)){
            if(!isResume)
                UpdateSeekbar.run();
            SetSeekbar();
        }else{
            SetSeekbar();
            AppConstants.editor.putBoolean("FirstTime", true);
            AppConstants.editor.commit();
        }
        /*if(AppConstants.SingleSong)
        {
            if(AppConstants.SongPause){
                Resume();
            }else{
                Init();
            }
        }
        SetNotification();*/
        // Da lahko dostopamo do public funckij Objekt Myservice
        service = this;
        return(START_NOT_STICKY);
    }

    public static void SongSelected(){
        service.isPlaying = false;
        service.isResume = false;
        Init();
    }

    public static void Init(){

        if(!service.isPlaying){
            if(service.isResume){
                service.Resume();
            }else{
                if(mediaPlayer != null)
                    service.Stop();
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnCompletionListener(service);
                mediaPlayer.setOnPreparedListener(service);
                try {
                    AppConstants.playcursor.moveToPosition(AppConstants.SongId);
                    mediaPlayer.setDataSource(AppConstants.playcursor.getString(4));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                service.SetSeekbar();
                //service.Play();
                service.StoreSharedPreferences(); // shranimo podatke o skladbi
                MainActivity.SetSongData(); // prikazemo podatke o skladbi
            }
            service.isPlaying = true;
        }
        else{
            service.Pause();
            service.isPlaying = false;
            service.isResume = true;
        }
    }
    private void Play(){
        mediaPlayer.start();
        service.RunForeground();
        service.UpdateSeekbar.run();
    }
    private void Pause(){
        AppConstants.editor.putInt("SongDuration", mediaPlayer.getCurrentPosition());
        AppConstants.editor.commit();
        Log.i("Pavzaaaaa", String.valueOf(mediaPlayer.getCurrentPosition()));
        mediaPlayer.pause();
        stopForeground(true);
        handler.removeCallbacks(UpdateSeekbar);
    }
    private void Resume(){
        if(AppConstants.preferences.getInt("SongDuration", 0) > 0)
            mediaPlayer.seekTo(AppConstants.preferences.getInt("SongDuration", 0));
        mediaPlayer.start();
        service.RunForeground();
        service.UpdateSeekbar.run();
    }
    private void Stop(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    public static void navigation(boolean next){
        if(next)
            service.Next();
        else
            service.Previous();
    }
    private void Next(){
        if(AppConstants.SongId == AppConstants.playcursor.getCount()-1)
            AppConstants.SongId = 0;
        else
            AppConstants.SongId ++;
        service.isPlaying = false;
        service.isResume = false;
        service.handler.removeCallbacks(service.run);
        AppConstants.editor.putInt("SongDuration", 0);
        AppConstants.editor.commit();
        Init();
       // service.stopForeground(true); // zbrise notification
    }
    private void Previous(){
        if(AppConstants.SongId == 0)
            AppConstants.SongId = AppConstants.playcursor.getCount()-1;
        else
            AppConstants.SongId --;
        service.isPlaying = false;
        service.isResume = false;
        service.handler.removeCallbacks(service.run);
        AppConstants.editor.putInt("SongDuration", 0);
        AppConstants.editor.commit();
        Init();
        // service.stopForeground(true); // zbrise notification
    }

    private void RunForeground()
    {
        Notification note = GetNotification();
        startForeground(AppConstants.NotificationID, note); // ID naj bo unikaten zaradi posodabljanja stanja
    }

    public void StopForeground(){
        stopForeground(true);
    }

    public void onDestroy() {
        Log.i("destroy","destroy");
        mediaPlayer.release();
        StopForeground();
    }

    /*private void UpdateSeekbar(){

        MainActivity.UpdateSeekbar(mediaPlayer.getCurrentPosition());

        run = new Runnable() {
            @Override
            public void run() {
                UpdateSeekbar();
            }
        };handler.postDelayed(run, 500);

    }*/

    private Runnable UpdateSeekbar = new Runnable() {
        @Override
        public void run() {
            MainActivity.UpdateSeekbar(mediaPlayer.getCurrentPosition());
            handler.postDelayed(UpdateSeekbar, 500);
        }
    };

    private void SetSeekbar(){
        if(mediaPlayer != null)
            MainActivity.SetSeekbar(AppConstants.playcursor.getInt(5));
        else{
            MainActivity.SetSeekbar(0);
        }
    } // nastavimo koncno dolzino seekbarja, ter zacetno pozicijo

    private Notification GetNotification(){

        showTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
        showTaskIntent.setAction(Intent.ACTION_MAIN);
        showTaskIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        showTaskIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); // addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                showTaskIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent).build();
        notification.contentView = new RemoteViews(this.getPackageName(),R.layout.notification);
        notification.contentView.setTextViewText(R.id.notificaion_title, AppConstants.playcursor.getString(1));
        notification.contentView.setImageViewResource(R.id.notificaion_next, R.mipmap.unknown_album);
        return notification;
    }

    private void StoreSharedPreferences(){

        AppConstants.editor.putString("title", AppConstants.playcursor.getString(1));
        AppConstants.editor.putString("artist",AppConstants.playcursor.getString(2));
        AppConstants.editor.putString("album", AppConstants.playcursor.getString(3));
        AppConstants.editor.putInt("songID", AppConstants.playcursor.getPosition());

        AppConstants.editor.commit();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

        service.Next();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Play();
    }
   /* private void stop() {
        if (isPlaying) {
            Log.w(getClass().getName(), "Got to stop()!");
            isPlaying=false;
            stopForeground(true);

        }
    }*/
    public static Integer getMediaPlayer(){
        return mediaPlayer.getAudioSessionId();
    }
}
