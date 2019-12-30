package com.arekmaz.mafia.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.arekmaz.mafia.BaseActivity;
import com.arekmaz.mafia.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class ShowGameCharacter extends BaseActivity {

    private ConstraintLayout mCharacterDisplayCl;

    private boolean mIsViewRotating = false;

    private String mCharacterDisplayText = "Mafia";

    private CardSide mCardSide = CardSide.REVERS;

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

        Dexter.withActivity(this)
                        .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION
                ,Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new BaseMultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        super.onPermissionsChecked(report);
                        Log.i("dupa", "permissions ok");
                        initBluetoothClient();
                    }
                })
                        .withErrorListener(e -> {
                            Log.i("dupa", "permissions request error");
                        })
                        .check();


    }

    private void initBluetoothClient() {
        RxBleClient rxBleClient = RxBleClient.create(this);
        Disposable flowDisposable = rxBleClient.observeStateChanges()
                .switchMap(state -> { // switchMap makes sure that if the state will change the rxBleClient.scanBleDevices() will dispose and thus end the scan
                    Log.i("dupa", "state: " + state);
                    switch (state) {

                        case READY:
                            // everything should work
//                            return rxBleClient.scanBleDevices();
                        case BLUETOOTH_NOT_AVAILABLE:
                            // basically no functionality will work here
                        case LOCATION_PERMISSION_NOT_GRANTED:
                            // scanning and connecting will not work
                        case BLUETOOTH_NOT_ENABLED:
                            // scanning and connecting will not work
                        case LOCATION_SERVICES_NOT_ENABLED:
                            // scanning will not work
                        default:
                            return Observable.empty();
                    }
                })
                .subscribe(
                        rxBleScanResult -> {
                            // Process scan result here.
                            Log.i("dupa", "scan result");
                        },
                        throwable -> {
                            // Handle an error here.
                            Log.i("dupa", "scan error");
                        }
                );
        Map<String, ScanResult> devices = new HashMap<>();
        final Disposable scanSubscription = rxBleClient.scanBleDevices(
                new ScanSettings.Builder()
                        // .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // change if needed
                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES) // change if needed
                        .build()
                // add filters if needed
        )
                .subscribe(
                        scanResult -> {
                            // Process scan result here.
                            if (devices.size() > 5) {
                                return;
                            }
                            devices.put(scanResult.getBleDevice().getMacAddress(), scanResult);
                            RxBleDevice device = scanResult.getBleDevice();
                            Log.i("device-scan", String.format(
                                    "total devices: %s, name: %s, max: %s, connection state: %s, type: %s, phone: %s, audio/video: %s",
                                    String.valueOf(devices.size()),
                                    scanResult.getBleDevice().getName(),
                                    scanResult.getBleDevice().getMacAddress(),
                                    device.getConnectionState().toString(),
                                    device.getBluetoothDevice().getBluetoothClass().getMajorDeviceClass(),
                                    device.getBluetoothDevice().getBluetoothClass().getMajorDeviceClass() == 512,
                                    device.getBluetoothDevice().getBluetoothClass().getMajorDeviceClass() == 1024
                            ));
                        },
                        throwable -> {
                            // Handle an error here.
                            Log.i("device-scan", throwable.getMessage());
                        }
                );
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
    }

    private ViewPropertyAnimator rotateYView(View v, float angle, int duration) {
        return v.animate().rotationY(angle).setDuration(duration);
    }

    private void bindViews() {
        mCharacterDisplayCl = findViewById(R.id.cl_character_card_view);
    }
}
