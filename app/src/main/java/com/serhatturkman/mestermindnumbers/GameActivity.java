package com.serhatturkman.mestermindnumbers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class GameActivity extends Activity {
    private static final String TAG = "GameActivityError";
    List<Integer> userNumber;
    List<Integer> computerNumber;

    @BindViews({R.id.userNumberKey1, R.id.userNumberKey2, R.id.userNumberKey3, R.id.userNumberKey4})
    List<TextView> userNumberKeys;

    @BindViews({R.id.computerNumberKey1, R.id.computerNumberKey2, R.id.computerNumberKey3, R.id.computerNumberKey4})
    List<TextView> computerNumberKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        getTheNumbers();
        showTheNumbers();
    }

    private void getTheNumbers() {
        userNumber = new ArrayList<>();
        computerNumber = new ArrayList<>();

        int[] userDigits;
        int[] computerDigits;

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            userDigits = bundle.getIntArray("userDigits");
            computerDigits = bundle.getIntArray("computerDigits");
            if (userDigits != null)
                if (userDigits.length != 4)
                    handleBadIntent();
                else
                    for (int userDigit : userDigits) userNumber.add(userDigit);
            else
                handleBadIntent();
            if (computerDigits != null)
                if (computerDigits.length != 4)
                    handleBadIntent();
                else
                    for (int computerDigit : computerDigits) computerNumber.add(computerDigit);
            else
                handleBadIntent();
        } else
            handleBadIntent();
    }

    private void showTheNumbers() {
        for (Integer userDigit : userNumber)
            userNumberKeys.get(userNumber.indexOf(userDigit)).setText(String.valueOf(userDigit));
        for (Integer computerDigit : computerNumber)
            computerNumberKeys.get(computerNumber.indexOf(computerDigit)).setText(String.valueOf(computerDigit));
    }

    private void handleBadIntent() {
        Log.e(TAG, "Bad Intent Bundle at ");
        Toast.makeText(this, "An error occoured. Please restart the app.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
