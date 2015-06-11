package com.example.dejan.ap_diplomska;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.HashMap;

/**
 * Created by dejan on 22.5.2015.
 */
public class AppConstants {

    // FRAGMENTS
    public static final int TAB_PLAYLIST = 1;
    public static final String FRAGMENT_PLAYLIST = "playlist_fragment";

    public static final int TAB_ARTIST = 2;
    public static final String FRAGMENT_ARTIST = "artist_fragment";

    public static final int TAB_SETTINGS = 3;
    public static final String FRAGMENT_SETTING = "settings_fragment";

    // DATA - SONGS
    public static Cursor playcursor,Artist,Playlist,Albums,ArtistSongs;
    public static Context context;

    //NAVIGATION
    public static ImageView play;

    // SERVICE INTENT
    public static Intent ServiceIntent;

    //STATUS FOR SONG
    public static boolean SingleSong = false;
    public static boolean SongPause = false;
    public static Integer SongId = 0;
    public static Integer NotificationID = 10;

    // DATA SAVE
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;

    public static HashMap<String, Cursor> map = new HashMap<String, Cursor>();
    public static String Selection;
}
