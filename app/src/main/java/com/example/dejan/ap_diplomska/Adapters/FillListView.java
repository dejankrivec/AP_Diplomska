package com.example.dejan.ap_diplomska.Adapters;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.TextView;

import com.example.dejan.ap_diplomska.AppConstants;
import com.example.dejan.ap_diplomska.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URI;

/**
 * Created by dejan on 23.5.2015.
 */
public class FillListView extends CursorAdapter {

    private TextView title,artist;
    private ImageView image_song;
    Bitmap bitmap;
    Uri sArtworkUri;
    Uri albumArtUri;
    Cursor cur;
    Context con;
    public FillListView(Context context, Cursor c) {
        super(context,c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.playlist_listview,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Pridobimo vse ID-je vseh elementov
        Init(view);

        /*long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);*/
        title.setText(cursor.getString(1));
        artist.setText(cursor.getString(2));
        /*bitmap = BitmapFactory.decodeFile(cursor.getString(4));*/
        /*try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), albumArtUri);
            image_song.setImageBitmap(bitmap);
        } catch (IOException e) {
            image_song.setImageResource(R.mipmap.unknown_album);
            e.printStackTrace();
        }*/
        cur = cursor;
        con = context;
        new BitmapWorkerTask().execute();
    }

    private void Init(View view){

        image_song = (ImageView)view.findViewById(R.id.song_image);
        title = (TextView)view.findViewById(R.id.title);
        artist = (TextView)view.findViewById(R.id.artist);
    }

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            long albumId = cur.getLong(cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(con.getContentResolver(), albumArtUri);
            }catch (IOException e){;
                bitmap = BitmapFactory.decodeResource(con.getResources(), R.mipmap.unknown_album);
                e.printStackTrace();
            }
           // image_song.setImageResource(R.mipmap.unknown_album);
            return bitmap;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                image_song.setImageBitmap(bitmap);
            }
            else{
                image_song.setImageResource(R.mipmap.unknown_album);
            }
        }
    }

}
