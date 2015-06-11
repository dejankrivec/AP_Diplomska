package com.example.dejan.ap_diplomska.Fragments;

import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.example.dejan.ap_diplomska.AppConstants;
import com.example.dejan.ap_diplomska.R;
import com.example.dejan.ap_diplomska.Service.MyService;
import com.example.dejan.ap_diplomska.view.VerticalSeekBar;

/**
 * Created by dejan on 22.5.2015.
 */
public class SettingsFragment extends BasicFragment{

    private VerticalSeekBar seekBar1,seekBar2,seekBar3,seekBar4,seekBar5;
    private TextView text1,text2,text3,text4,text5;
    private Equalizer equalizer;
    private LinearLayout layout;
    private VerticalSeekBar seekbar_array[];
    TextView seekbar_label[];
    private int new_level;

    public SettingsFragment(){

    }
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Init(view);

        seekbar_array = new VerticalSeekBar[]{seekBar1,seekBar2,seekBar3,seekBar4,seekBar5};
        seekbar_label = new TextView[]{text1,text2,text3,text4,text5};

        equalizer = new Equalizer(0, MyService.getMediaPlayer());
        equalizer.setEnabled(true);
        short BandsNumber = equalizer.getNumberOfBands();
        final short range[] = equalizer.getBandLevelRange();

        for(short i=0;i<BandsNumber;i++)
        {
            final short band  = i;
            int freq_range = equalizer.getCenterFreq((short)i);
            Log.i("center",String.valueOf(freq_range));
            seekbar_label[i].setText(CalculateBand(freq_range));

            seekbar_array[i].setMax((range[1] - range[0])-1000);
            seekbar_array[i].setProgress(((range[1] - range[0])-1000)/2);

            seekbar_array[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    new_level = range[0] + (range[1] - range[0]) * i / 100;
                    //equalizer.setBandLevel(band, (short) new_level);
                    equalizer.setBandLevel(band,
                            (short) (i + -1000));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }

    private void Init(View view){
        seekBar1 = (VerticalSeekBar)view.findViewById(R.id.seekbar1);
        seekBar2 = (VerticalSeekBar)view.findViewById(R.id.seekbar2);
        seekBar3 = (VerticalSeekBar)view.findViewById(R.id.seekbar3);
        seekBar4 = (VerticalSeekBar)view.findViewById(R.id.seekbar4);
        seekBar5 = (VerticalSeekBar)view.findViewById(R.id.seekbar5);

        text1 = (TextView)view.findViewById(R.id.text1);
        text2 = (TextView)view.findViewById(R.id.text2);
        text3 = (TextView)view.findViewById(R.id.text3);
        text4 = (TextView)view.findViewById(R.id.text4);
        text5 = (TextView)view.findViewById(R.id.text5);
    }

    private String CalculateBand(int freg_range){

        int range = freg_range/1000;
        String range_convert = null;
        if(range > 1000){
            range_convert = String.valueOf(range/1000 + " Hz");
        }else
            range_convert = String.valueOf(range);
        return range_convert;
    }
}
