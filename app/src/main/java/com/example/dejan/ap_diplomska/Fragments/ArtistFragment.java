package com.example.dejan.ap_diplomska.Fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.MergeCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dejan.ap_diplomska.Adapters.ArtistAdapter;
import com.example.dejan.ap_diplomska.Adapters.ArtistSongsAdapter;
import com.example.dejan.ap_diplomska.Adapters.FillListView;
import com.example.dejan.ap_diplomska.AppConstants;
import com.example.dejan.ap_diplomska.Data.ReadSDCard;
import com.example.dejan.ap_diplomska.R;
import com.example.dejan.ap_diplomska.Service.MyService;

import java.util.zip.Inflater;

/**
 * Created by dejan on 22.5.2015.
 */
public class ArtistFragment extends BasicFragment {

    private ArtistAdapter artistAdapter;
    private ArtistSongsAdapter adapter;
    private GridView ArtistList;
    private ListView ArtistSongs;
    private ProgressDialog dialog;
    private RelativeLayout backlayout;

    public ArtistFragment(){

    }
    public static ArtistFragment newInstance() {
        ArtistFragment fragment = new ArtistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.artist_fragment,container,false);
        ArtistList = (GridView)view.findViewById(R.id.artist_gridview);
        ArtistSongs = (ListView)view.findViewById(R.id.artist_listview);
        backlayout = (RelativeLayout)view.findViewById(R.id.artist_title_back);
        new LoadSongs().execute(AppConstants.Artist);

        ArtistList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {

                ArtistList.setVisibility(View.INVISIBLE);
                ArtistSongs.setVisibility(View.VISIBLE);
                backlayout.setVisibility(View.VISIBLE);

                // Pridobimo ime Artista na katerega smo kliknala ter nato pridobimo vse skladbe ki mu ustrezajo
                TextView a = (TextView)view.findViewById(R.id.artis_name);
                AppConstants.Selection = a.getText().toString();
                ReadSDCard.getSongForArtist(a.getText().toString());

                LayoutInflater layoutInflater = (LayoutInflater)AppConstants.context.getSystemService( AppConstants.context.LAYOUT_INFLATER_SERVICE );
                RelativeLayout ll = (RelativeLayout)layoutInflater.inflate(R.layout.shuffle_listview, null, false);
                if(adapter == null)
                    ArtistSongs.addHeaderView(ll);

                adapter = new ArtistSongsAdapter(AppConstants.context, AppConstants.ArtistSongs);
                ArtistSongs.setAdapter(adapter);

            }
        });

        ArtistSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //AppConstants.SingleSong = true;
                //AppConstants.SongId = i-1; // -1 ker ima listview indexe od 1 dalje Cursor s skladbami pa od 0 dalje
                //MergeCursor mergeCursor = new MergeCursor(new Cursor[] { AppConstants.Playlist, AppConstants.ArtistSongs });
                //AppConstants.Artist.close();
                Log.i("stevilo albumov", String.valueOf(AppConstants.Artist.getCount()));
                Log.i("before", String.valueOf(AppConstants.Playlist.getCount()));
                //AppConstants.Playlist = AppConstants.Albums;
                Log.i("after", String.valueOf(AppConstants.Playlist.getCount()));
                //MyService.SongSelected();
                AppConstants.playcursor = AppConstants.map.get(AppConstants.Selection);
                AppConstants.SingleSong = true;
                AppConstants.SongId = i-1; // -1 ker ima listview indexe od 1 dalje Cursor s skladbami pa od 0 dalje
                MyService.SongSelected();
            }
        });

        backlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArtistSongs.setVisibility(View.INVISIBLE);
                backlayout.setVisibility(View.INVISIBLE);
                ArtistList.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private class LoadSongs extends AsyncTask<Cursor, Cursor, ArtistAdapter> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setCancelable(false);
            dialog.setMessage("Loading ...");
            dialog.show();
        }
        protected ArtistAdapter doInBackground(Cursor... args) {

            artistAdapter = new ArtistAdapter(AppConstants.context,AppConstants.Artist);
            return artistAdapter;
        }
        protected void onPostExecute(ArtistAdapter adapter) {

            ArtistList.setAdapter(adapter);
            dialog.dismiss();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    /*@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSubmit:
                submitEmergency();
                break;
        }
    }*/
}
