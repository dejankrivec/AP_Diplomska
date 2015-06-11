package com.example.dejan.ap_diplomska.Adapters;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dejan.ap_diplomska.AppConstants;
import com.example.dejan.ap_diplomska.R;

import java.io.IOException;

/**
 * Created by dejan on 29.5.2015.
 */
public class ArtistAdapter extends CursorAdapter {

    private TextView artis_name;
    private ImageView artist_image;
    Bitmap bitmap;

    public ArtistAdapter(Context context, Cursor c) {super(context, c,0);}

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.artist_gridview,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Pridobimo vse ID-je vseh elementov
        Init(view);

        //artis_name.setText(AppConstants.Artist.getString(1));
        artis_name.setText(cursor.getString(1));
        //bitmap = BitmapFactory.decodeFile(cursor.getString(2));
        artist_image.setImageResource(R.mipmap.unknown_album);
    }

    private void Init(View view){

        artis_name = (TextView)view.findViewById(R.id.artis_name);
        artist_image = (ImageView)view.findViewById(R.id.artist_image);
    }
}
