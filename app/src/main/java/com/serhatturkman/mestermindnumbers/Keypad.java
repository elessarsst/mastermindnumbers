package com.serhatturkman.mestermindnumbers;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Keypad {
    private TextView userNumberIndicator;
    private List<Integer> inputNumber;
    private List<TextView> numberKeys;
    private TextView startKey;

    Keypad(TextView userNumberIndicator, List<TextView> numberKeys, TextView clearKey, TextView backSpaceKey, TextView startKey) {
        this.userNumberIndicator = userNumberIndicator;
        this.numberKeys = numberKeys;
        this.startKey = startKey;

        inputNumber = new ArrayList<>();

        backSpaceKey.setOnClickListener(v -> backSpaceNumber());
        clearKey.setOnClickListener(v -> clearNumber());

        for(TextView key : numberKeys)
            key.setOnClickListener(v -> addNumber(key));

        clearNumber();
    }

    private void addNumber(TextView key) {
        if (inputNumber.size() == 0) {
            numberKeys.get(0).setEnabled(true);
        }
        if (inputNumber.size() < 4) {
            key.setEnabled(false);
            inputNumber.add(Integer.valueOf(key.getText().toString()));
            setUserNumberText();
        }

        if (inputNumber.size() == 4) {
            startKey.setEnabled(true);
        }
    }


    private void setUserNumberText() {
        StringBuilder userNumberStringBuilder = new StringBuilder();
        for (Integer digit : inputNumber) userNumberStringBuilder.append(digit);
        userNumberIndicator.setText(userNumberStringBuilder.toString());
    }

    void clearNumber() {
        // Disable Key "0" Enable Other Numbers
        for (TextView key : numberKeys) {
            key.setEnabled(!key.getText().toString().equals("0"));
        }
        // Empty The Array
        inputNumber.clear();
        userNumberIndicator.setText("");
        startKey.setEnabled(false);
    }

    private void backSpaceNumber() {
        if (inputNumber.size() > 0) {
            numberKeys.get(inputNumber.get(inputNumber.size() - 1)).setEnabled(true);
            inputNumber.remove(inputNumber.size() - 1);
            setUserNumberText();
            startKey.setEnabled(false);
        }
        if (inputNumber.size() == 0)
            clearNumber();
    }

    List<Integer> submit() {
        if (inputNumber.size() == 4) {
            return inputNumber;
        } else
            return null;
    }

    static List<Integer> createComputerNumber() {
        List<Integer> computerNumber = new ArrayList<>();
        computerNumber.add(new Random().nextInt(9) + 1);
        int i = 0;
        while (i < 3) {
            Integer randomDigit = new Random().nextInt(10);
            boolean isDuplicated = false;
            for (Integer computerDigit : computerNumber) {
                if (computerDigit.equals(randomDigit)) {
                    isDuplicated = true;
                    break;
                }
            }
            if (!isDuplicated) {
                computerNumber.add(randomDigit);
                i++;
            }
        }
        return computerNumber;
    }
}
