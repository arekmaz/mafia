package com.arekmaz.mafia.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.arekmaz.mafia.BaseActivity;
import com.arekmaz.mafia.R;
import com.arekmaz.mafia.enums.AppMode;

public class ModeChooserActivity extends BaseActivity {

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
        switch(mode) {
            case HOST:
                startRoomSetupActivity();
                break;
            case PLAYER:
                startPlayerConfigActivity();
                break;
        }
    }

    private void startPlayerConfigActivity() {
        startActivity(PlayerConfigActivity.class);
    }


    private void startRoomSetupActivity() {
        startActivity(RoomSetupActivity.class);
    }

    private <A extends Activity> void startActivity(Class<A> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
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
