package com.arekmaz.mafia.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arekmaz.mafia.BaseActivity;
import com.arekmaz.mafia.R;
import com.arekmaz.mafia.adapters.PlayerViewAdapter;
import com.arekmaz.mafia.entity.Player;

import static com.arekmaz.mafia.activities.RoomSetupActivity.ROOM_NAME_SP_KEY;
import static com.arekmaz.mafia.activities.RoomSetupActivity.ROOM_POPULATION_SP_KEY;

public class RoomControlActivity extends BaseActivity {

    private RecyclerView mPlayersRv;
    private TextView mRoomHeaderTv;
    private TextView mRoomInfoTv;

    public interface PlayerActionsCallbacks {
        void onPlayerAdded(Player newPlayer);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_control);

        bindViews();
        setup();
    }

    private void setup() {

        setupPlayersRecyclerView();
        setupRoomName();
        setupRoomInfo();
    }

    private void setupRoomInfo() {
        updateRoomInfo(0);
    }

    private void onRoomPlayerCountChanged(int newPlayersCount) {
        updateRoomInfo(newPlayersCount);
    }

    private void updateRoomInfo(int playersCount) {
        String roomPopulation = getPref(ROOM_POPULATION_SP_KEY);
        String roomInfoContentTpl = getString(R.string.room_info_content_tpl);
        String appliedTpl = roomInfoContentTpl
                .replace("{{roomPopulation}}", roomPopulation)
                .replace("{{playersCount}}", String.valueOf(playersCount));
        mRoomInfoTv.setText(appliedTpl);
    }

    private void setupRoomName() {
        String roomName = getPref(ROOM_NAME_SP_KEY);
        String roomHeaderContentTpl = getString(R.string.room_header_content_tpl);
        String appliedTpl = roomHeaderContentTpl
                .replace("{{roomName}}", roomName);
        mRoomHeaderTv.setText(appliedTpl);
    }



    private void setupPlayersRecyclerView() {

        mPlayersRv.setHasFixedSize(true);

        mPlayersRv.setLayoutManager(new LinearLayoutManager(this));

        mPlayersRv.setAdapter(new PlayerViewAdapter());
    }

    private void bindViews() {
        mPlayersRv = findViewById(R.id.players_rv);
        mRoomHeaderTv = findViewById(R.id.tv_room_name);
        mRoomInfoTv = findViewById(R.id.tv_room_info);
    }
}
