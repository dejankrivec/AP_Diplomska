package com.example.dejan.ap_diplomska.Fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;
import android.widget.*;

import com.example.dejan.ap_diplomska.Adapters.FillListView;
import com.example.dejan.ap_diplomska.AppConstants;
import com.example.dejan.ap_diplomska.R;
import com.example.dejan.ap_diplomska.Service.MyService;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by dejan on 22.5.2015.
 */
public class PlayListFragment extends BasicFragment {

    FillListView playlist_adapter;
    ListView Playlist;
    private ProgressDialog dialog;

    public PlayListFragment(){

    }
    public static PlayListFragment newInstance() {
        PlayListFragment fragment = new PlayListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.playlist_fragment,container,false);
        Playlist = (ListView)view.findViewById(R.id.playlist_listview);
        new LoadSongs().execute(AppConstants.Playlist);
        LayoutInflater layoutInflater = (LayoutInflater)AppConstants.context.getSystemService( AppConstants.context.LAYOUT_INFLATER_SERVICE );
        RelativeLayout ll = (RelativeLayout)layoutInflater.inflate(R.layout.shuffle_listview, null, false);

        Playlist.addHeaderView(ll);

       Playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {

                // Povemo da gre za specificno pesem ter podamo tudi id pesmi
                AppConstants.playcursor = AppConstants.map.get("mainlist");
                AppConstants.SingleSong = true;
                AppConstants.SongId = i-1; // -1 ker ima listview indexe od 1 dalje Cursor s skladbami pa od 0 dalje
                MyService.SongSelected();
            }
        });
        return view;
    }
    private class LoadSongs extends AsyncTask<Cursor, Cursor, FillListView> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setCancelable(false);
            dialog.setMessage("Loading ...");
            dialog.show();
        }
        protected FillListView doInBackground(Cursor... args) {

            playlist_adapter = new FillListView(AppConstants.context,AppConstants.Playlist);
            return playlist_adapter;
        }
        protected void onPostExecute(FillListView adapter) {

            Playlist.setAdapter(adapter);
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
