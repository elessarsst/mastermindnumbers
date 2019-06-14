package com.serhatturkman.mestermindnumbers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends Activity {
    private static final String TAG = "Main Activity Log";
    List<Integer> userNumber;
    List<Integer> computerNumber;

    @BindViews({R.id.numberKey0, R.id.numberKey1, R.id.numberKey2, R.id.numberKey3, R.id.numberKey4,
            R.id.numberKey5, R.id.numberKey6, R.id.numberKey7, R.id.numberKey8, R.id.numberKey9})
    List<TextView> numberKeys;

    @BindView(R.id.userNumberIndicator)
    TextView userNumberIndicator;

    @BindView(R.id.clearKey)
    TextView clearKey;

    @BindView(R.id.backSpaceKey)
    TextView backSpaceKey;

    @BindView(R.id.startKey)
    TextView startKey;

    Keypad keypad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        userNumber = new ArrayList<>();
        computerNumber = new ArrayList<>();

        keypad = new Keypad(userNumberIndicator, numberKeys, clearKey, backSpaceKey, startKey);
    }

    public void submit(View view) {
        userNumber = keypad.submit();
        if (userNumber != null) {
            startKey.setEnabled(false);
            Log.d(TAG, "The game can be started...");
            computerNumber = Keypad.createComputerNumber();
            int[] userDigits = {0, 0, 0, 0};
            int[] computerDigits = {0, 0, 0, 0};
            for (int number : userNumber) userDigits[userNumber.indexOf(number)] = number;
            for (int number : computerNumber)
                computerDigits[computerNumber.indexOf(number)] = number;
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("userDigits", userDigits);
            intent.putExtra("computerDigits", computerDigits);
            startActivity(intent);
            finish();
        }
    }


}
