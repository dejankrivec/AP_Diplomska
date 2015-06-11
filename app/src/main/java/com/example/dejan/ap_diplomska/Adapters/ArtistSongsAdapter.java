package com.example.dejan.ap_diplomska.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dejan.ap_diplomska.AppConstants;
import com.example.dejan.ap_diplomska.R;

import org.w3c.dom.Text;

/**
 * Created by dejan on 30.5.2015.
 */
public class ArtistSongsAdapter extends CursorAdapter {

    TextView name,album;

    public ArtistSongsAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.artist_songs_listiew,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Init(view);
        name.setText(cursor.getString(1));
        album.setText(cursor.getString(3));
    }

    private void Init(View view){

        name = (TextView)view.findViewById(R.id.artist_song_name);
        album = (TextView)view.findViewById(R.id.artist_song_album);
    }
}
