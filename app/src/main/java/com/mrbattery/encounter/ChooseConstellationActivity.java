package com.mrbattery.encounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.mrbattery.encounter.constant.Constant;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseConstellationActivity extends AppCompatActivity {

    private int constellationSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_constellation);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rb01, R.id.rb02, R.id.rb03, R.id.rb04, R.id.rb05, R.id.rb06, R.id.rb07, R.id.rb08, R.id.rb09, R.id.rb10, R.id.rb11, R.id.rb12, R.id.rb00,})
    public void chooseConstellation(RadioButton radioButton) {
        switch (radioButton.getId()) {
            case R.id.rb01:
                constellationSelected = 1;
                break;
            case R.id.rb02:
                constellationSelected = 2;
                break;
            case R.id.rb03:
                constellationSelected = 3;
                break;
            case R.id.rb04:
                constellationSelected = 4;
                break;
            case R.id.rb05:
                constellationSelected = 5;
                break;
            case R.id.rb06:
                constellationSelected = 6;
                break;
            case R.id.rb07:
                constellationSelected = 7;
                break;
            case R.id.rb08:
                constellationSelected = 8;
                break;
            case R.id.rb09:
                constellationSelected = 9;
                break;
            case R.id.rb10:
                constellationSelected = 10;
                break;
            case R.id.rb11:
                constellationSelected = 11;
                break;
            case R.id.rb12:
                constellationSelected = 12;
                break;
            case R.id.rb00:
                constellationSelected = 0;
                break;
            default:
                break;
        }

        Constant.setCurrConstellation(constellationSelected);
        finish();
    }

    @OnClick(R.id.iv_close)
    public void close() {
        finish();
    }
}
