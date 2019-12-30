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

public class ShowGameCharacter extends BaseActivity {

    private ConstraintLayout mCharacterDisplayCl;

    private boolean mIsViewRotating = false;

    private String mCharacterDisplayText = "Mafia";

    private CardSide mCardSide = CardSide.AVERS;

    private enum CardSide {
        AVERS,
        REVERS
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

        mCharacterDisplayCl.setOnClickListener(v -> {
            if (!mIsViewRotating) {
                mIsViewRotating = true;
                float currentAngle = v.getRotationY();
                float rotateTo = (currentAngle + 180) % 360;
                if (mCardSide == CardSide.AVERS) {
                    characterTextView.setText("Naciśnij aby odwrócić");
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
    }

    private ViewPropertyAnimator rotateYView(View v, float angle, int duration) {
        return v.animate().rotationY(angle).setDuration(duration);
    }

    private void bindViews() {
        mCharacterDisplayCl = findViewById(R.id.cl_character_card_view);
    }
}
