package com.sevenfloor.mtcsound.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sevenfloor.mtcsound.R;

public class DspInFragment extends BaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private SeekBar in1, in2, in3, in4, in5, in6;
    private TextView in1V, in2V, in3V, in4V, in5V, in6V;
    private CheckBox mute1, mute2, mute3, mute4, mute5, mute6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dsp_inputs, container, false);

        in1 = (SeekBar)view.findViewById(R.id.seekBarDigL);
        in2 = (SeekBar)view.findViewById(R.id.seekBarDigR);
        in3 = (SeekBar)view.findViewById(R.id.seekBarFmL);
        in4 = (SeekBar)view.findViewById(R.id.seekBarFmR);
        in5 = (SeekBar)view.findViewById(R.id.seekBarAuxL);
        in6 = (SeekBar)view.findViewById(R.id.seekBarAuxR);

        in1.setOnSeekBarChangeListener(this);
        in2.setOnSeekBarChangeListener(this);
        in3.setOnSeekBarChangeListener(this);
        in4.setOnSeekBarChangeListener(this);
        in5.setOnSeekBarChangeListener(this);
        in6.setOnSeekBarChangeListener(this);

        in1V = (TextView)view.findViewById(R.id.dsp_dig_l_v);
        in2V = (TextView)view.findViewById(R.id.dsp_dig_r_v);
        in3V = (TextView)view.findViewById(R.id.dsp_fm_l_v);
        in4V = (TextView)view.findViewById(R.id.dsp_fm_r_v);
        in5V = (TextView)view.findViewById(R.id.dsp_aux_l_v);
        in6V = (TextView)view.findViewById(R.id.dsp_aux_r_v);

        (mute1 = (CheckBox)view.findViewById(R.id.dsp_dig_l_mute)).setOnClickListener(this);
        (mute2 = (CheckBox)view.findViewById(R.id.dsp_dig_r_mute)).setOnClickListener(this);
        (mute3 = (CheckBox)view.findViewById(R.id.dsp_fm_l_mute)).setOnClickListener(this);
        (mute4 = (CheckBox)view.findViewById(R.id.dsp_fm_r_mute)).setOnClickListener(this);
        (mute5 = (CheckBox)view.findViewById(R.id.dsp_aux_l_mute)).setOnClickListener(this);
        (mute6 = (CheckBox)view.findViewById(R.id.dsp_aux_r_mute)).setOnClickListener(this);

        return view;
    }

    @Override
    protected void update() {
        updateBars();
        updateMute();
    }

    private void updateMute() {
        int[] params = parseList(audioManager.getParameters("cfg_dsp_in_mute="));
        if (params.length == 6){
            mute1.setChecked(params[0] != 0);
            mute2.setChecked(params[1] != 0);
            mute3.setChecked(params[2] != 0);
            mute4.setChecked(params[3] != 0);
            mute5.setChecked(params[4] != 0);
            mute6.setChecked(params[5] != 0);
        }
    }

    private void updateBars() {
        int[] params = parseList(audioManager.getParameters("cfg_dsp_in="));
        if (params.length == 6) {
            in1.setProgress(params[0] + 72);
            in2.setProgress(params[1] + 72);
            in3.setProgress(params[2] + 72);
            in4.setProgress(params[3] + 72);
            in5.setProgress(params[4] + 72);
            in6.setProgress(params[5] + 72);
        }

        updateValues();
    }

    private void updateValues() {
        in1V.setText(formatGain(in1.getProgress() - 72));
        in2V.setText(formatGain(in2.getProgress() - 72));
        in3V.setText(formatGain(in3.getProgress() - 72));
        in4V.setText(formatGain(in4.getProgress() - 72));
        in5V.setText(formatGain(in5.getProgress() - 72));
        in6V.setText(formatGain(in6.getProgress() - 72));
    }

    private String formatGain(int gain) {
        if (gain == 0) return "0 dB";
        return String.format("%+d dB", gain);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (!fromUser) return;

        switch (seekBar.getId()) {
            case R.id.seekBarDigL:
            case R.id.seekBarDigR:
            case R.id.seekBarFmL:
            case R.id.seekBarFmR:
            case R.id.seekBarAuxL:
            case R.id.seekBarAuxR:
                audioManager.setParameters(String.format("cfg_dsp_in=%d,%d,%d,%d,%d,%d", in1.getProgress() - 72, in2.getProgress() - 72, in3.getProgress() - 72, in4.getProgress() - 72, in5.getProgress() - 72, in6.getProgress() - 72));
                break;
        }
        updateValues();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dsp_dig_l_mute:
            case R.id.dsp_dig_r_mute:
            case R.id.dsp_fm_l_mute:
            case R.id.dsp_fm_r_mute:
            case R.id.dsp_aux_l_mute:
            case R.id.dsp_aux_r_mute:
                audioManager.setParameters(String.format("cfg_dsp_in_mute=%d,%d,%d,%d,%d,%d",
                        mute1.isChecked() ? 1 : 0,
                        mute2.isChecked() ? 1 : 0,
                        mute3.isChecked() ? 1 : 0,
                        mute4.isChecked() ? 1 : 0,
                        mute5.isChecked() ? 1 : 0,
                        mute6.isChecked() ? 1 : 0));
                break;
        }
    }
}
