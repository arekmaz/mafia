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
import com.arekmaz.mafia.enums.Role;
import com.arekmaz.mafia.network.Connector;

import java.util.ArrayList;
import java.util.List;

import static com.arekmaz.mafia.activities.RoomSetupActivity.ROOM_NAME_SP_KEY;
import static com.arekmaz.mafia.activities.RoomSetupActivity.ROOM_POPULATION_SP_KEY;

public class RoomControlActivity extends BaseActivity {

    private RecyclerView mPlayersRv;
    private TextView mRoomHeaderTv;
    private TextView mRoomInfoTv;


    private List<Player> mPlayers = new ArrayList<>();
    private Thread mServerThread;
    private PlayerViewAdapter mPlayersAdapter;

    public interface PlayerActionsCallbacks {
        void onPlayerAdded(Player newPlayer);

        Role getNextPlayerRole();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_control);

        bindViews();
        setup();
    }

    @Override
    protected void onResume() {
        setupRoomServer();
        super.onResume();
    }

    @Override
    protected void onPause() {
        stopRoomServer();
        super.onPause();
    }

    private void stopRoomServer() {
        mServerThread.interrupt();
        mServerThread = null;
    }

    private void setup() {
        setupPlayersRecyclerView();
        setupRoomName();
        setupRoomInfo();
    }

    private void setupRoomServer() {
        int maxPlayersCount = 6;
        mServerThread =  Connector.initServerThread(
            new PlayerActionsCallbacks() {
                @Override
                public void onPlayerAdded(Player newPlayer) {
                    synchronized (RoomControlActivity.this) {
                        mPlayers.add(newPlayer);
                        runOnUiThread(() -> {
                            mPlayersAdapter.notifyDataSetChanged();
                        });
                        if (mPlayers.size() >= maxPlayersCount
                                && mServerThread != null
                                && !mServerThread.isInterrupted()) {
                            mServerThread.interrupt();
                        }
                    }
                }

                @Override
                public Role getNextPlayerRole() {
                    return Role.CITIZEN;
                }
            }
        );
        mServerThread.start();
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

        mPlayersAdapter = new PlayerViewAdapter(mPlayers, getResources());

        mPlayersRv.setAdapter(mPlayersAdapter);
    }

    private void bindViews() {
        mPlayersRv = findViewById(R.id.players_rv);
        mRoomHeaderTv = findViewById(R.id.tv_room_name);
        mRoomInfoTv = findViewById(R.id.tv_room_info);
    }
}
