package com.arekmaz.mafia.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.arekmaz.mafia.BaseActivity;
import com.arekmaz.mafia.R;

import com.arekmaz.mafia.enums.Role;
import com.arekmaz.mafia.network.Connector;

import static com.arekmaz.mafia.activities.PlayerConfigActivity.USER_NAME_SP_KEY;

public class ShowGameCharacter extends BaseActivity {

    private ConstraintLayout mCharacterDisplayCl;

    private boolean mIsViewRotating = false;

    private String mCharacterDisplayText = "Brak";

    private CardSide mCardSide = CardSide.REVERS;
    private Thread mClientRoleRequestThread;


    public void onRawCharacterRoleResult(String rawRole) {
        mCharacterDisplayText = getString(Role.fromString(rawRole).getDisplayStringId());
        if (mClientRoleRequestThread != null) {
            mClientRoleRequestThread.interrupt();
        }
    }

    private enum CardSide {
        AVERS,
        REVERS
    }

    public interface GameCharacterRequestCallbacks {
        void onRawCharacterRoleResult(String rawRole);

        String getPlayerNick();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_game_character);

        bindViews();
        setup();
    }

    private void setup() {

        TextView characterTextView = mCharacterDisplayCl.findViewById(R.id.tv_character_text);
        String reverseText = getString(R.string.character_card_reverse_content);
        mCharacterDisplayCl.setOnClickListener(v -> {
            if (!mIsViewRotating) {
                mIsViewRotating = true;
                float currentAngle = v.getRotationY();
                float rotateTo = (currentAngle + 180) % 360;
                if (mCardSide == CardSide.AVERS) {
                    characterTextView.setText(reverseText);
                } else {
                    characterTextView.setText("Postać: " + mCharacterDisplayText);
                }
                rotateYView(characterTextView, 360 - rotateTo, 500);
                rotateYView(v, rotateTo, 1000)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mIsViewRotating = false;
                                mCardSide = mCardSide == CardSide.AVERS ? CardSide.REVERS : CardSide.AVERS;
                            }
                        });
            }
        });

        setupRoleRequest();
    }

    private void setupRoleRequest() {
        mClientRoleRequestThread = Connector.initClientThread(new GameCharacterRequestCallbacks() {
            @Override
            public void onRawCharacterRoleResult(String rawRole) {
                ShowGameCharacter.this.onRawCharacterRoleResult(rawRole);
            }

            @Override
            public String getPlayerNick() {
                return getPref(USER_NAME_SP_KEY);
            }
        });
        mClientRoleRequestThread.start();
    }

    private ViewPropertyAnimator rotateYView(View v, float angle, int duration) {
        return v.animate().rotationY(angle).setDuration(duration);
    }

    private void bindViews() {
        mCharacterDisplayCl = findViewById(R.id.cl_character_card_view);
    }
}
