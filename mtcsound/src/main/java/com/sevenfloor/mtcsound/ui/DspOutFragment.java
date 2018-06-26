package com.sevenfloor.mtcsound.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sevenfloor.mtcsound.R;

public class DspOutFragment extends BaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private SeekBar out1, out2, out3, out4, out5; /* 6, 7, 8 skipped */
    private TextView out1V, out2V, out3V, out4V, out5V;
    private CheckBox mute1, mute2, mute3, mute4, mute5;
    private CheckBox phase1, phase2, phase3, phase4, phase5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dsp_outputs, container, false);

        out1 = (SeekBar)view.findViewById(R.id.seekBarOut1);
        out2 = (SeekBar)view.findViewById(R.id.seekBarOut2);
        out3 = (SeekBar)view.findViewById(R.id.seekBarOut3);
        out4 = (SeekBar)view.findViewById(R.id.seekBarOut4);
        out5 = (SeekBar)view.findViewById(R.id.seekBarOut5);

        out1.setOnSeekBarChangeListener(this);
        out2.setOnSeekBarChangeListener(this);
        out3.setOnSeekBarChangeListener(this);
        out4.setOnSeekBarChangeListener(this);
        out5.setOnSeekBarChangeListener(this);

        out1V = (TextView)view.findViewById(R.id.dsp_out1_v);
        out2V = (TextView)view.findViewById(R.id.dsp_out2_v);
        out3V = (TextView)view.findViewById(R.id.dsp_out3_v);
        out4V = (TextView)view.findViewById(R.id.dsp_out4_v);
        out5V = (TextView)view.findViewById(R.id.dsp_out5_v);

        (mute1 = (CheckBox)view.findViewById(R.id.dsp_out1_mute)).setOnClickListener(this);
        (mute2 = (CheckBox)view.findViewById(R.id.dsp_out2_mute)).setOnClickListener(this);
        (mute3 = (CheckBox)view.findViewById(R.id.dsp_out3_mute)).setOnClickListener(this);
        (mute4 = (CheckBox)view.findViewById(R.id.dsp_out4_mute)).setOnClickListener(this);
        (mute5 = (CheckBox)view.findViewById(R.id.dsp_out5_mute)).setOnClickListener(this);

        (phase1 = (CheckBox)view.findViewById(R.id.dsp_out1_invert)).setOnClickListener(this);
        (phase2 = (CheckBox)view.findViewById(R.id.dsp_out2_invert)).setOnClickListener(this);
        (phase3 = (CheckBox)view.findViewById(R.id.dsp_out3_invert)).setOnClickListener(this);
        (phase4 = (CheckBox)view.findViewById(R.id.dsp_out4_invert)).setOnClickListener(this);
        (phase5 = (CheckBox)view.findViewById(R.id.dsp_out5_invert)).setOnClickListener(this);

        return view;
    }

    @Override
    protected void update() {
        updateBars();
        updateMute();
        updatePhase();
    }

    private void updateMute() {
        int[] params = parseList(audioManager.getParameters("cfg_dsp_out_mute="));
        if (params.length == 8){

            mute1.setChecked(params[0] != 0);
            mute2.setChecked(params[1] != 0);
            mute3.setChecked(params[2] != 0);
            mute4.setChecked(params[3] != 0);
            mute5.setChecked(params[4] != 0);
        }
    }

    private void updatePhase() {
        int[] params = parseList(audioManager.getParameters("cfg_dsp_out_phase="));
        if (params.length == 8){
            phase1.setChecked(params[0] != 0);
            phase2.setChecked(params[1] != 0);
            phase3.setChecked(params[2] != 0);
            phase4.setChecked(params[3] != 0);
            phase5.setChecked(params[4] != 0);
        }
    }

    private void updateBars() {
        int[] params = parseList(audioManager.getParameters("cfg_dsp_out="));
        if (params.length == 8) {
            out1.setProgress(params[0] + 72);
            out2.setProgress(params[1] + 72);
            out3.setProgress(params[2] + 72);
            out4.setProgress(params[3] + 72);
            out5.setProgress(params[4] + 72);
        }

        updateValues();
    }

    private void updateValues() {
        out1V.setText(formatGain(out1.getProgress() - 72));
        out2V.setText(formatGain(out2.getProgress() - 72));
        out3V.setText(formatGain(out3.getProgress() - 72));
        out4V.setText(formatGain(out4.getProgress() - 72));
        out5V.setText(formatGain(out5.getProgress() - 72));
    }

    private String formatGain(int gain) {
        if (gain == 0) return "0 dB";
        return String.format("%+d dB", gain);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (!fromUser) return;

        switch (seekBar.getId()) {
            case R.id.seekBarOut1:
            case R.id.seekBarOut2:
            case R.id.seekBarOut3:
            case R.id.seekBarOut4:
            case R.id.seekBarOut5:
                audioManager.setParameters(String.format("cfg_dsp_out=%d,%d,%d,%d,%d,%d,%d,%d", out1.getProgress() - 72, out2.getProgress() - 72, out3.getProgress() - 72, out4.getProgress() - 72, out5.getProgress() - 72, 0, 0, 0));
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
            case R.id.dsp_out1_mute:
            case R.id.dsp_out2_mute:
            case R.id.dsp_out3_mute:
            case R.id.dsp_out4_mute:
            case R.id.dsp_out5_mute:
                audioManager.setParameters(String.format("cfg_dsp_out_mute=%d,%d,%d,%d,%d,%d,%d,%d",
                        mute1.isChecked() ? 1 : 0,
                        mute2.isChecked() ? 1 : 0,
                        mute3.isChecked() ? 1 : 0,
                        mute4.isChecked() ? 1 : 0,
                        mute5.isChecked() ? 1 : 0,
                        0, 0, 0));
                break;

            case R.id.dsp_out1_invert:
            case R.id.dsp_out2_invert:
            case R.id.dsp_out3_invert:
            case R.id.dsp_out4_invert:
            case R.id.dsp_out5_invert:
                audioManager.setParameters(String.format("cfg_dsp_out_phase=%d,%d,%d,%d,%d,%d,%d,%d",
                        phase1.isChecked() ? 1 : 0,
                        phase2.isChecked() ? 1 : 0,
                        phase3.isChecked() ? 1 : 0,
                        phase4.isChecked() ? 1 : 0,
                        phase5.isChecked() ? 1 : 0,
                        0, 0, 0));
                break;
        }
    }
}
