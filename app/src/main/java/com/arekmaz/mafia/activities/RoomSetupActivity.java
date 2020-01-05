package com.arekmaz.mafia.activities;


import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.arekmaz.mafia.BaseActivity;
import com.arekmaz.mafia.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.regex.Pattern;

public class RoomSetupActivity extends BaseActivity {

    private static final int MIN_PLAYERS = 6;
    private static final int MAX_PLAYERS = 30;
    private static final String ROOM_NAME_SP_KEY = "ROOM_NAME";
    private static final String ROOM_POPULATION_SP_KEY = "ROOM_POPULATION";

    private TextInputLayout mRoomNameTil;
    private TextInputLayout mRoomPopulationTil;

    private Button mBackBt;
    private Button mForwardBt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_setup);

        bindViews();

        setup();
    }

    private void setup() {
        mBackBt.setOnClickListener(v -> {
            onBackPressed();
        });
        mForwardBt.setOnClickListener(v -> {
            onForwardPressed();
        });
        Objects.requireNonNull(mRoomPopulationTil.getEditText()).setFilters(new InputFilter[] {
                (charSequence, i, i1, spanned, i2, i3) -> {
                    try {
                        String input = charSequence.toString() + spanned.toString();
                        if (Pattern.compile("^0+$").matcher(input).matches()) {
                            return "";
                        }
                        return null;
                    } catch (NumberFormatException nfe) {
                    }
                    return "";
                }
        });

        restoreSavedRoomConfig();

        mRoomNameTil.requestFocus();
    }

    private void restoreSavedRoomConfig() {
        restoreSavedRoomName();
        restoreSavedRoomPopulation();
    }

    private void restoreSavedRoomPopulation() {
        String savedRawRoomPopulation = getPref(ROOM_POPULATION_SP_KEY);
        Objects.requireNonNull(mRoomPopulationTil.getEditText()).setText(savedRawRoomPopulation);
    }

    private void restoreSavedRoomName() {
        String savedRoomName = getPref(ROOM_NAME_SP_KEY);
        Objects.requireNonNull(mRoomNameTil.getEditText()).setText(savedRoomName);
    }

    private void onForwardPressed() {
        clearErrors();
        if (!validateRoomConfig()) {
            return;
        }
        saveRoomConfig();
        launchRoomControlActivity();
    }

    private void launchRoomControlActivity() {
        startActivity(RoomControlActivity.class);
    }

    private void saveRoomConfig() {
        saveRoomName();
        saveRoomPopulation();
    }

    private void saveRoomPopulation() {
        String roomPopulation = Objects.requireNonNull(mRoomPopulationTil.getEditText()).getText().toString();
        writePref(ROOM_POPULATION_SP_KEY, roomPopulation);
    }

    private void saveRoomName() {
        String roomName = Objects.requireNonNull(mRoomNameTil.getEditText()).getText().toString();
        writePref(ROOM_NAME_SP_KEY, roomName);
    }

    private boolean validateRoomConfig() {
        boolean isRoomNameValid = validateRoomName();
        boolean isRoomPopulationValid = validateRoomPopulation();
        return isRoomNameValid && isRoomPopulationValid;
    }

    private boolean validateRoomPopulation() {
        String rawRoomPopulation = Objects.requireNonNull(mRoomPopulationTil.getEditText()).getText().toString();
        try {
            int roomPopulation = Integer.parseInt(rawRoomPopulation);
            if (roomPopulation >= MIN_PLAYERS && roomPopulation <= MAX_PLAYERS) {
                return true;
            }
            mRoomPopulationTil.setError(
                String.format("Liczba graczy musi być pomiędzy %s, a %s", MIN_PLAYERS, MAX_PLAYERS)
            );
            return false;
        } catch(NumberFormatException e) {
            mRoomPopulationTil.setError("Liczba graczy wymagana");
            return false;
        }
    }

    private boolean validateRoomName() {
        String roomName = Objects.requireNonNull(mRoomNameTil.getEditText()).getText().toString();
        if (roomName.isEmpty()) {
            mRoomNameTil.setError("Nazwa pokoju wymagana");
            return false;
        }
        return true;
    }

    private void clearErrors() {
        mRoomPopulationTil.setError(null);
        mRoomNameTil.setError(null);
    }

    private void bindViews() {
        mRoomNameTil = findViewById(R.id.til_room_name);
        mRoomPopulationTil = findViewById(R.id.til_room_population);

        mBackBt = findViewById(R.id.bt_back);
        mForwardBt = findViewById(R.id.bt_next);
    }
}
