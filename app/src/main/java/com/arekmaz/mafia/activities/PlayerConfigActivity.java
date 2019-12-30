package com.arekmaz.mafia.activities;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.arekmaz.mafia.BaseActivity;
import com.arekmaz.mafia.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class PlayerConfigActivity extends BaseActivity {

    private TextInputLayout mUserNameTil;

    private Button mBackBt;
    private Button mGoForwardBt;

    private static final String USER_NAME_SP_KEY = "USER_NAME";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_config);

        bindViews();
        setup();
    }

    private void setup() {
        mBackBt.setOnClickListener(v -> {
            onBackPressed();
        });

        mGoForwardBt.setOnClickListener(this::onGoForwardPressed);

        Objects.requireNonNull(mUserNameTil.getEditText()).setText(getSavedUserName());
        mUserNameTil.requestFocus();
    }

    private String getSavedUserName() {
        String userName = getPref(USER_NAME_SP_KEY);
        return userName;
    }

    private void onGoForwardPressed(View view) {
        resetErrors();
        if (!validateUserConfig()) {
            return;
        }
        saveUserName();
        launchShowGameCharacterActivity();
    }

    private void launchShowGameCharacterActivity() {
        startActivity(ShowGameCharacter.class);
    }

    private void saveUserName() {
        String userName = Objects.requireNonNull(mUserNameTil.getEditText()).getText().toString();
        writePref(USER_NAME_SP_KEY, userName);
    }

    private boolean validateUserConfig() {
        boolean userNameValid = validateUserName();
        return userNameValid;
    }

    private boolean validateUserName() {
        Editable userName = Objects.requireNonNull(mUserNameTil.getEditText()).getText();
        if (userName != null && !userName.toString().isEmpty()) {
            return true;
        }
        String errorMessage = getString(R.string.user_name_required_error_message);
        mUserNameTil.setError(errorMessage);
        return false;
    }

    private void resetErrors() {
        mUserNameTil.setError(null);
    }

    private void bindViews() {
        mUserNameTil = findViewById(R.id.til_user_name);

        mBackBt = findViewById(R.id.bt_back);
        mGoForwardBt = findViewById(R.id.bt_next);
    }
}
