package com.example.dejan.ap_diplomska.Data;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.dejan.ap_diplomska.AppConstants;
import com.example.dejan.ap_diplomska.Fragments.BasicFragment;
import com.example.dejan.ap_diplomska.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dejan on 23.5.2015.
 */
public class ReadSDCard extends BasicFragment{

    public ReadSDCard(){

    }

    public static void getSongs(){
        // Preberemo vse skladbe
        String[] podatkiSongs = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID};
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        AppConstants.Playlist = ((MainActivity) AppConstants.context).managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                podatkiSongs, selection, null, MediaStore.Audio.Media.TITLE);

        // Preberemo vse izvajalce
        String[] podatkiArtist = {MediaStore.Audio.Artists._ID,MediaStore.Audio.Artists.ARTIST};
        String where = MediaStore.Audio.Artists.ARTIST + "=?";
        AppConstants.Artist = ((MainActivity) AppConstants.context).managedQuery(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                podatkiArtist,null, null, MediaStore.Audio.Media.ARTIST);

        // Dobimo izvajalce in vse slike posameznih abumov ki jih imajo izvajalci
        String[] podatkiAlbum= {MediaStore.Audio.Albums._ID,MediaStore.Audio.Albums.ARTIST, MediaStore.Audio.Albums.ALBUM_ART,MediaStore.Audio.Albums.ALBUM};
        AppConstants.Albums = ((MainActivity) AppConstants.context).managedQuery(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                podatkiAlbum,null, null, MediaStore.Audio.Media.ARTIST);

        // dodamo glavno playlisto v mapo in artiste
        AppConstants.map.put("mainlist", AppConstants.Playlist);
        AppConstants.map.put("artist", AppConstants.Artist);
    }
    public static void getSongForArtist(String ArtistName)
    {
        String[] columns = { MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID};

        String where = MediaStore.Audio.Media.ARTIST + "=?";

        String whereVal[] = { ArtistName };

        String orderBy = android.provider.MediaStore.Audio.Media.TITLE;

        AppConstants.ArtistSongs = ((MainActivity) AppConstants.context).managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns,
                where, whereVal, orderBy);

        // dodamo v mapo cursor z skladbami ki ustrezajo artistu
        AppConstants.map.put(ArtistName, AppConstants.ArtistSongs);
    }
}
