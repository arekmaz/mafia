package com.arekmaz.mafia.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.arekmaz.mafia.BaseActivity;
import com.arekmaz.mafia.R;

import java.net.InetAddress;

public class LiveRoomActivity extends BaseActivity {

    public class NetworkObserver{
        public void update(InetAddress address, String name){
            //...
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);
    }
}
