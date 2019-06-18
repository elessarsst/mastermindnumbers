package com.serhatturkman.mastermindnumbers;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Keypad is a class with the supporting methods for type input. It's reusable with it's "keypad.xml" layout resource file
 */
class Keypad {
    private TextView userNumberIndicator;
    private List<Integer> inputNumber;
    private List<TextView> numberKeys;
    private TextView startKey;

    /**
     *
     *
     * @param userNumberIndicator TextView Indicator for user's variable
     * @param numberKeys List of TextViews which are used as number buttons
     * @param clearKey Clear key button
     * @param backSpaceKey Backspace key button
     * @param startKey Submit key button
     */
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

    /**
     *
     * @param key adds a number to the specific TextView to show the digit
     *
     */
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


    /**
     * To show the number in the userNumberIndicator TextView field
     */
    private void setUserNumberText() {
        StringBuilder userNumberStringBuilder = new StringBuilder();
        for (Integer digit : inputNumber) userNumberStringBuilder.append(digit);
        userNumberIndicator.setText(userNumberStringBuilder.toString());
    }

    /**
     *  clears both the variable and the Textview indicator
     */
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


    /**
     * Deletes one digit. If there's only one digit, also clears the TextView indicator
     */
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

    /**
     *
     * @return user input variable length check.
     */
    List<Integer> submit() {
        if (inputNumber.size() == 4) {
            return inputNumber;
        } else
            return null;
    }


    /**
     * Creates a random four-digit number with it's digits are unique
     * @return generated number as list of integers
     */
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
