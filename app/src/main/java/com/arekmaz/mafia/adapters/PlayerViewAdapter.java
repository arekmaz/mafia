package com.arekmaz.mafia.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arekmaz.mafia.R;
import com.arekmaz.mafia.entity.Player;

import java.util.List;

public class PlayerViewAdapter extends RecyclerView.Adapter<PlayerViewAdapter.PlayerViewHolder> {

    private List<Player> mPlayers;


    public PlayerViewAdapter(List<Player> players) {
        mPlayers = players;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_control_player_item, parent, false);
        PlayerViewHolder vh = new PlayerViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player currentPlayer = mPlayers.get(position);
        holder.bind(currentPlayer);
    }


    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView mNickTv;
        TextView mRoleTv;

        PlayerViewHolder(View v) {
            super(v);
            mNickTv = v.findViewById(R.id.player_nick_tv);
            mRoleTv = v.findViewById(R.id.player_role_tv);
        }

        public void bind(Player player) {
            mNickTv.setText(player.getNick().toUpperCase());
            mRoleTv.setText(player.getRole().value);
        }
    }
}
