package com.example.mafia.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mafia.R;
import com.example.mafia.enums.AppMode;

public class ModeChooserActivity extends AppCompatActivity {

    private Button mChooseHostModeBt;
    private Button mChoosePlayerModeBt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_chooser);

        bindViews();
        setup();
    }
    private void onModeChosen(AppMode mode) {

    }

    private void setup() {
        mChoosePlayerModeBt.setOnClickListener(v -> {
            onModeChosen(AppMode.PLAYER);
        });

        mChooseHostModeBt.setOnClickListener(v -> {
            onModeChosen(AppMode.HOST);
        });
    }


    private void bindViews() {
        mChooseHostModeBt = findViewById(R.id.bt_choose_host_mode);
        mChoosePlayerModeBt = findViewById(R.id.bt_choose_player_mode);
    }


}
