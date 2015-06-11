package com.example.dejan.ap_diplomska.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.dejan.ap_diplomska.MainActivity;

/**
 * Created by dejan on 22.5.2015.
 */
public class BasicFragment extends Fragment {

    public static MainActivity mActivity;
    private String tag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(tag, "onCreate");
    }

    public boolean onBackPressed() {
        Log.i(tag, "onBackPressed");
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(tag, "onStart");
    }

    @Override
    public void onPause() {
        Log.i(tag, "onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(tag, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(tag, "onDetach");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(tag, "onResume");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
        Log.i(tag, "onAttach");
    }
}
