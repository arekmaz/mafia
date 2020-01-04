package com.arekmaz.mafia.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.arekmaz.mafia.BaseActivity;
import com.arekmaz.mafia.R;
import com.arekmaz.mafia.enums.AppMode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
                if(checkIfHotspotIsOn()){
                    startRoomSetupActivity();
                }
                else {
                    showNotCancellableAlert(getResources().getString(R.string.enable_hotspot));
                }
                break;
            case PLAYER:
                if(checkIfWifiIsOn()){
                    startPlayerConfigActivity();
                }
                else {
                    showNotCancellableAlert(getResources().getString(R.string.enable_wifi));
                }
                break;
        }
    }

    private void startPlayerConfigActivity() {
        startActivity(PlayerConfigActivity.class);
    }


    private void startRoomSetupActivity() {
        startActivity(RoomSetupActivity.class);
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

    private boolean checkIfHotspotIsOn(){
        WifiManager wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifimanager);
        }
        catch (Throwable ignored) {}
        return false;
    }

    private boolean checkIfWifiIsOn(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    private void showNotCancellableAlert(String error){
        AlertDialog.Builder l_dialog = new AlertDialog.Builder(this);
        l_dialog.setTitle("Błąd!");
        l_dialog.setMessage(error);
        l_dialog.setCancelable(false);
        l_dialog.setPositiveButton("Ok", (DialogInterface.OnClickListener)(a,b) -> {});
        l_dialog.show();
    }


}
