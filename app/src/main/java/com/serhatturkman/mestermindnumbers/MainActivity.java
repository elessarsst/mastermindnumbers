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
import butterknife.OnClick;

public class MainActivity extends Activity {
    private static final String TAG = "Main Activity Log";
    List<Integer> userNumber;
    List<Integer> computerNumber;

    @BindViews({R.id.key0, R.id.key1, R.id.key2, R.id.key3, R.id.key4, R.id.key5, R.id.key6, R.id.key7, R.id.key8, R.id.key9})
    List<TextView> keys;

    @BindView(R.id.userNumberIndicator)
    TextView userNumberIndicator;

    @BindView(R.id.startKey)
    TextView startKey;

    @OnClick({R.id.key1, R.id.key2, R.id.key3, R.id.key4, R.id.key5, R.id.key6, R.id.key7, R.id.key8, R.id.key9, R.id.key0})
    public void addNumber(TextView key) {
        if (userNumber.size() == 0) {
            keys.get(0).setEnabled(true);
        }
        if (userNumber.size() < 4) {
            key.setEnabled(false);
            userNumber.add(Integer.valueOf(key.getText().toString()));
            setUserNumberText();
        }

        if (userNumber.size() == 4) {
            startKey.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        userNumber = new ArrayList<>();
        computerNumber = new ArrayList<>();

        resetArray(getCurrentFocus());
    }

    public void resetArray(View view) {
        // Disable Key "0" Enable Other Numbers
        for (TextView key : keys) {
            key.setEnabled(!key.getText().toString().equals("0"));
        }
        // Empty The Array
        userNumber.clear();
        userNumberIndicator.setText("");
        startKey.setEnabled(false);
    }

    public void backSpaceNumber(View view) {
        if (userNumber.size() > 0) {
            keys.get(userNumber.get(userNumber.size() - 1)).setEnabled(true);
            userNumber.remove(userNumber.size() - 1);
            setUserNumberText();
            startKey.setEnabled(false);
        }
        if (userNumber.size() == 0)
            resetArray(getCurrentFocus());
    }

    public void startGame(View view) {
        if (userNumber.size() == 4) {
            //TODO Start The Game
            Log.d(TAG, "The game can be started...");
            createComputerNumber();
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

    private void setUserNumberText() {
        StringBuilder userNumberStringBuilder = new StringBuilder();
        for (Integer digit : userNumber) userNumberStringBuilder.append(digit);
        userNumberIndicator.setText(userNumberStringBuilder.toString());
    }

    private void createComputerNumber() {
        computerNumber.add(new Random().nextInt(9) + 1);
        int i = 0;
        while (i < 3) {
            Integer randomDigit = new Random().nextInt(10);
            boolean isDuplicated = false;
            for(Integer computerDigit : computerNumber) {
                if(computerDigit.equals(randomDigit)) {
                    isDuplicated = true;
                    break;
                }
            }
            if(!isDuplicated) {
                computerNumber.add(randomDigit);
                i++;
            }
        }
    }
}
