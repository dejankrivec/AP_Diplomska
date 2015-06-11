package com.example.dejan.ap_diplomska;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.*;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.example.dejan.ap_diplomska.Data.ReadSDCard;
import com.example.dejan.ap_diplomska.Fragments.ArtistFragment;
import com.example.dejan.ap_diplomska.Fragments.BasicFragment;
import com.example.dejan.ap_diplomska.Fragments.PlayListFragment;
import com.example.dejan.ap_diplomska.Fragments.SettingsFragment;
import com.example.dejan.ap_diplomska.Service.MyService;

import java.util.Stack;


public class MainActivity extends FragmentActivity {
    private RelativeLayout menu;
    private ImageView next,previous;
    private static TextView title,artist,album;
    private static SeekBar seekBar;
    SparseArray<Stack<Fragment>> mStack;
    int mCurrentTab = AppConstants.TAB_PLAYLIST;
    Point p;
    PopupWindow popup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppConstants.context = this;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ReadSDCard.getSongs();
            }
        });
        // Inicialization
        //ReadSDCard.GetSongs();
        Init();

        // zapisemo si vrednosti fragmentov
        mStack = new SparseArray<>();
        mStack.put(AppConstants.TAB_PLAYLIST, new Stack<Fragment>());
        mStack.put(AppConstants.TAB_ARTIST, new Stack<Fragment>());
        mStack.put(AppConstants.TAB_SETTINGS, new Stack<Fragment>());


        // setamo prvi fragmenta, ki je tudi main fragment
        pushFragments(AppConstants.TAB_PLAYLIST, AppConstants.FRAGMENT_PLAYLIST, new PlayListFragment().newInstance(), false, false);

        // inicializiramo SharedPreferences
        AppConstants.preferences = getApplicationContext().getSharedPreferences("SongData",MODE_PRIVATE);
        AppConstants.editor = AppConstants.preferences.edit();

        // Pozenemo service
        AppConstants.ServiceIntent = new Intent(AppConstants.context,MyService.class);
        startService(AppConstants.ServiceIntent);

        // Na zacetku nastavimo nazadnje igrano skladbo
        AppConstants.SongId = AppConstants.preferences.getInt("songID", 0);
        // postavimo
        // prebermo pesmi v igrani cursor
        AppConstants.playcursor = AppConstants.map.get("mainlist");
        AppConstants.preferences.getInt("songID", 0);
        AppConstants.playcursor.moveToPosition(AppConstants.SongId);
        SetSongData();
    }

    // Pridobimo IDje elementov
    private void Init(){
        AppConstants.play = (ImageView)findViewById(R.id.play);
        next = (ImageView)findViewById(R.id.next);
        previous = (ImageView)findViewById(R.id.previous);
        AppConstants.play.setOnClickListener(playNavigation);
        next.setOnClickListener(playNavigation);
        previous.setOnClickListener(playNavigation);
        menu = (RelativeLayout)findViewById(R.id.menu_popup);
        menu.setOnClickListener(onPopUp);

        title = (TextView)findViewById(R.id.song_title);
        artist = (TextView)findViewById(R.id.song_artist);
        album = (TextView)findViewById(R.id.song_album);

        seekBar = (SeekBar)findViewById(R.id.seekbar);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int[] location = new int[2];
        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        FrameLayout a = (FrameLayout)findViewById(R.id.fragment_main);
        a.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        p = new Point();
        p.x = location[0];
        p.y = location[1];
    }
    View.OnClickListener playNavigation = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.play:
                    MyService.Init();
                    break;
                case R.id.previous:
                    MyService.navigation(false);
                    break;
                case R.id.next:
                    MyService.navigation(true);
                    break;
            }
        }
    };

    View.OnClickListener onPopUp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (p != null) // poacakamo da pridobimo lokacijo PopUpMenuja
                ShowPopUp(MainActivity.this, p);
        }
    };
    View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.img1:
                    mCurrentTab = AppConstants.TAB_PLAYLIST;
                    //pushFragments(AppConstants.TAB_PLAYLIST, AppConstants.FRAGMENT_PLAYLIST, new PlayListFragment().newInstance(), false, true);
                    popup.dismiss();
                    break;
                case R.id.img2:
                    mCurrentTab = AppConstants.TAB_ARTIST;
                    //pushFragments(AppConstants.TAB_ARTIST, AppConstants.FRAGMENT_ARTIST, new ArtistFragment().newInstance(), false, true);
                    popup.dismiss();
                    break;
                case R.id.img3:
                    mCurrentTab = AppConstants.TAB_SETTINGS;
                    //pushFragments(AppConstants.TAB_SETTINGS, AppConstants.FRAGMENT_SETTING, new SettingsFragment().newInstance(), false, true);
                    popup.dismiss();
                    break;
                case R.id.img4:
                    mCurrentTab = AppConstants.TAB_SETTINGS;
                    //pushFragments(AppConstants.TAB_SETTINGS, AppConstants.FRAGMENT_SETTING, new SettingsFragment().newInstance(), false, true);
                    popup.dismiss();
                    break;
            }
            if (mStack.get(mCurrentTab).size() == 0) {
                  /*
                   *    First time this tab is selected. So add first fragment of that tab.
                   *    Dont need animation, so that argument is false.
                   *    We are adding a new fragment which is not present in stack. So add to stack is true.
                   */
                if (mCurrentTab == AppConstants.TAB_PLAYLIST) {
                    pushFragments(AppConstants.TAB_PLAYLIST, AppConstants.FRAGMENT_PLAYLIST, new PlayListFragment().newInstance(), false, true);
                } else if (mCurrentTab == AppConstants.TAB_ARTIST)
                    pushFragments(AppConstants.TAB_ARTIST, AppConstants.FRAGMENT_ARTIST, new ArtistFragment().newInstance(), false, true);
                else if (mCurrentTab == AppConstants.TAB_SETTINGS)
                    pushFragments(AppConstants.TAB_SETTINGS, AppConstants.FRAGMENT_SETTING, new SettingsFragment().newInstance(), false, true);
            }else {
                  /*
                   *    We are switching tabs, and target tab is already has atleast one fragment.
                   *    No need of animation, no need of stack pushing. Just show the target fragment
                   */

                //pushFragments(tabId, mStacks.get(tabId).lastElement(), false,false);
                Fragment frag = mStack.get(mCurrentTab).lastElement();
                pushFragments(mCurrentTab, frag.getTag(), (BasicFragment) frag, false, false);
            }
        }
    };

    public void pushFragments(int tabId, String tag, BasicFragment fragment, boolean shouldAnimate, boolean shouldAdd) {

        if (shouldAdd)
            mStack.get(tabId).push(fragment);


        fragment.setTag(tag);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        //ft.replace(R.id.fragment_main, fragment);

        if(manager.getFragments()!=null) {
            for (Fragment f : manager.getFragments()) {
                if(f!=null)
                    ft.hide(f);
            }
        }

        if(!fragment.isAdded())
            ft.add(R.id.fragment_main, fragment, tag);
        else
            ft.show(fragment);
        ft.commit();
    }


    private void ShowPopUp(Activity context,Point p){

        // Inflate the popup_layout.xml
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_main, viewGroup);
        // Creating the PopupWindow

        popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        popup.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popup.setFocusable(true);


        // Clear the default translucent background - znebimo se odmika
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + 0, p.y + 0);

        // Getting a reference to Close button, and close the popup when clicked.
        ImageView imag1 = (ImageView) layout.findViewById(R.id.img1);
        ImageView imag2 = (ImageView) layout.findViewById(R.id.img2);
        ImageView imag3 = (ImageView) layout.findViewById(R.id.img3);
        ImageView imag4 = (ImageView) layout.findViewById(R.id.img4);
        ImageView imag5 = (ImageView) layout.findViewById(R.id.img5);
        imag1.setOnClickListener(onclick);
        imag2.setOnClickListener(onclick);
        imag3.setOnClickListener(onclick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mStack.get(mCurrentTab).size() == 0) {
            return;
        }

        /*Now current fragment on screen gets onActivityResult callback..*/
        mStack.get(mCurrentTab).lastElement().onActivityResult(requestCode, resultCode, data);
    }

    public static void SetSongData(){

        // ce se nimamo shranjenih podatkov o skladbi, to se zgodi samo na zacetku ko se prvic zazene aplikacija
        if(AppConstants.preferences.getString("title", "").equals("") && AppConstants.preferences.getString("artist", "").equals("") &&
                AppConstants.preferences.getString("album", "").equals("") ){

            AppConstants.playcursor.moveToPosition(AppConstants.SongId);
            title.setText(AppConstants.playcursor.getString(1));
            artist.setText(AppConstants.playcursor.getString(2));
            album.setText(AppConstants.playcursor.getString(3));
        }
        else{

            title.setText(AppConstants.preferences.getString("title", ""));
            artist.setText(AppConstants.preferences.getString("artist", ""));
            album.setText(AppConstants.preferences.getString("album", ""));
        }
    }

    public static void UpdateSeekbar(Integer time){
        seekBar.setProgress(time);
    }
    public static void SetSeekbar(Integer time){
        seekBar.setMax(time);
        seekBar.setProgress(AppConstants.preferences.getInt("SongDuration", 0));
    }

    @Override
    protected void onDestroy() {
        AppConstants.editor.putBoolean("FirstTime",true);
        AppConstants.editor.commit();
        super.onDestroy();
    }
}
