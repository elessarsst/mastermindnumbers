package com.serhatturkman.mastermindnumbers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends Activity {
    private static final String TAG = "GameActivityError";
    private static final int PLAYER_USER = 0;
    private static final int PLAYER_COMPUTER = 1;
    private static final int DRAW = 2;
    List<Integer> userNumber;
    List<Integer> computerNumber;
    List<Integer> userGuessNumber;
    List<Integer> numberExistanceProbabilities = new ArrayList<>();
    List<List<Integer>> digitProbabilities = new ArrayList<>();
    List<Integer> computersThreePoint = new ArrayList<>();
    List<List<Integer>> computerGuessHistory = new ArrayList<>();
    int roundNumber;


    /**
     *  ButterKnife Assignments Start
     *
     */

    @BindViews({R.id.numberKey0, R.id.numberKey1, R.id.numberKey2, R.id.numberKey3, R.id.numberKey4,
            R.id.numberKey5, R.id.numberKey6, R.id.numberKey7, R.id.numberKey8, R.id.numberKey9})
    List<TextView> numberKeys;

    @BindView(R.id.startKey)
    TextView startKey;

    @BindView(R.id.userNumberIndicator)
    TextView userNumberIndicator;

    @BindView(R.id.clearKey)
    TextView clearKey;

    @BindView(R.id.backSpaceKey)
    TextView backSpaceKey;

    @BindView(R.id.tryKey)
    TextView tryKey;

    @BindViews({R.id.userNumberDigit1, R.id.userNumberDigit2, R.id.userNumberDigit3, R.id.userNumberDigit4})
    List<TextView> userNumberDigits;

    @BindViews({R.id.computerNumberDigit1, R.id.computerNumberDigit2, R.id.computerNumberDigit3, R.id.computerNumberDigit4})
    List<TextView> computerNumberDigits;

    @BindViews({R.id.userRow0, R.id.userRow1, R.id.userRow2, R.id.userRow3, R.id.userRow4, R.id.userRow5, R.id.userRow6, R.id.userRow7, R.id.userRow8, R.id.userRow9,
            R.id.userRow10, R.id.userRow11, R.id.userRow12, R.id.userRow13, R.id.userRow14})
    List<LinearLayout> userRows;

    @BindViews({R.id.computerRow0, R.id.computerRow1, R.id.computerRow2, R.id.computerRow3, R.id.computerRow4, R.id.computerRow5, R.id.computerRow6, R.id.computerRow7, R.id.computerRow8, R.id.computerRow9,
            R.id.computerRow10, R.id.computerRow11, R.id.computerRow12, R.id.computerRow13, R.id.computerRow14})
    List<LinearLayout> computerRows;

    @BindView(R.id.userGuessKeyPad)
    ConstraintLayout userGuessKeyPad;

    @BindView(R.id.computerGuessKeyPad)
    ConstraintLayout computerGuessKeyPad;

    @BindView(R.id.endGameText)
    TextView endGameText;

    @BindView(R.id.endGameSubText)
    TextView endGameSubText;

    /**
     *  ButterKnife Assignments End
     *
     */


    Keypad keypad;

    /**
     * shows the keypad to make a guess
     */
    @OnClick(R.id.tryKey)
    public void guessNumber() {
        userGuessKeyPad.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        getTheNumbers();
        showTheNumbers();
        keypad = new Keypad(userNumberIndicator, numberKeys, clearKey, backSpaceKey, startKey);
        userGuessKeyPad.setVisibility(View.INVISIBLE);
        computerGuessKeyPad.setVisibility(View.INVISIBLE);
        setProbabilities();
    }

    /**
     * Set of probabilities of digits
     */
    private void setProbabilities(){
        List<Integer> firstDigitProbabilities = new ArrayList<>();
        List<Integer> secondDigitProbabilities = new ArrayList<>();
        List<Integer> thirdDigitProbabilities = new ArrayList<>();
        List<Integer> forthDigitProbabilities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numberExistanceProbabilities.add(100);
            firstDigitProbabilities.add(100);
            secondDigitProbabilities.add(100);
            thirdDigitProbabilities.add(100);
            forthDigitProbabilities.add(100);
        }
        digitProbabilities.add(firstDigitProbabilities);
        digitProbabilities.add(secondDigitProbabilities);
        digitProbabilities.add(thirdDigitProbabilities);
        digitProbabilities.add(forthDigitProbabilities);
        digitProbabilities.get(0).set(0, 0);
    }

    /**
     *
     * @param playerType player or computer
     * @param guessCount round number
     * @param guessArray the array of digits to show
     * @return white and red hint points
     */
    private int[] showGuessRow(int playerType, int guessCount, List<Integer> guessArray) {
        TextView key1;
        TextView key2;
        TextView key3;
        TextView key4;
        TextView whiteHint;
        TextView redHint;
        int whiteHintPoints = 0;
        int redHintPoints = 0;

        if (playerType == PLAYER_USER) {
            key1 = userRows.get(guessCount).findViewById(R.id.guessRowPlayerKey1);
            key2 = userRows.get(guessCount).findViewById(R.id.guessRowPlayerKey2);
            key3 = userRows.get(guessCount).findViewById(R.id.guessRowPlayerKey3);
            key4 = userRows.get(guessCount).findViewById(R.id.guessRowPlayerKey4);
            whiteHint = userRows.get(guessCount).findViewById(R.id.guessRowPlayerWhiteHint);
            redHint = userRows.get(guessCount).findViewById(R.id.guessRowPlayerRedHint);

            for (Integer guessedDigit : guessArray) {
                if (computerNumber.indexOf(guessedDigit) > -1) {
                    if (computerNumber.indexOf(guessedDigit) == guessArray.indexOf(guessedDigit))
                        whiteHintPoints++;
                    else
                        redHintPoints++;
                }
            }
        } else {
            key1 = computerRows.get(guessCount).findViewById(R.id.guessRowComputerKey1);
            key2 = computerRows.get(guessCount).findViewById(R.id.guessRowComputerKey2);
            key3 = computerRows.get(guessCount).findViewById(R.id.guessRowComputerKey3);
            key4 = computerRows.get(guessCount).findViewById(R.id.guessRowComputerKey4);
            whiteHint = computerRows.get(guessCount).findViewById(R.id.guessRowComputerWhiteHint);
            redHint = computerRows.get(guessCount).findViewById(R.id.guessRowComputerRedHint);

            for (Integer guessedDigit : guessArray) {
                if (userNumber.indexOf(guessedDigit) > -1) {
                    if (userNumber.indexOf(guessedDigit) == guessArray.indexOf(guessedDigit))
                        whiteHintPoints++;
                    else
                        redHintPoints++;
                }
            }
        }

        key1.setText(String.valueOf(guessArray.get(0)));
        key2.setText(String.valueOf(guessArray.get(1)));
        key3.setText(String.valueOf(guessArray.get(2)));
        key4.setText(String.valueOf(guessArray.get(3)));
        whiteHint.setText(String.valueOf(whiteHintPoints));
        redHint.setText(String.valueOf(redHintPoints * -1));

        return new int[]{whiteHintPoints, redHintPoints};
    }


    /**
     * gets the user and computer numbers from previous activity
     */
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

    /**
     * shows users numbers at the digits
     */
    private void showTheNumbers() {
        for (Integer userDigit : userNumber)
            userNumberDigits.get(userNumber.indexOf(userDigit)).setText(String.valueOf(userDigit));
        Log.d(TAG, "computer : " + computerNumber.toString() + " user : " + userNumber.toString());
    }

    /**
     * closes the activity if anything goes wrong with activity intent
     */
    private void handleBadIntent() {
        Log.e(TAG, "Bad Intent Bundle at ");
        Toast.makeText(this, "An error occoured. Please restart the app.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * back to starting activity
     */
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * Sends submit command to Keypad instance
     */
    @OnClick(R.id.startKey)
    public void submit(View v) {
        userGuessNumber = keypad.submit();
        if (userGuessNumber != null) {
            int[] playerResult = showGuessRow(PLAYER_USER, roundNumber, userGuessNumber);

            keypad.clearNumber();
            userGuessKeyPad.setVisibility(View.INVISIBLE);
            tryKey.setEnabled(false);
            if (playerResult[0] == 4)
                endGame(PLAYER_USER);
            else {
                new Handler().postDelayed(() -> {
                    computerGuessKeyPad.setVisibility(View.VISIBLE);
                    List<Integer> computerGuessNumber = computerGuess();
                    new Handler().postDelayed(() -> {
                        int[] computerResult = showGuessRow(PLAYER_COMPUTER, roundNumber, computerGuessNumber);
                        if (computerResult[0] == 4)
                            endGame(PLAYER_COMPUTER);
                        else {
                            if (roundNumber < 14) {
                                roundNumber++;
                                computerGuessKeyPad.setVisibility(View.INVISIBLE);
                                tryKey.setEnabled(true);
                            } else
                                endGame(DRAW);
                        }
                    }, 3000);
                }, 2000);
            }
        }
    }

    /**
     * shows the result on the middle of the screen at the end of the game
     * @param whoWon player, computer or draw
     */
    private void endGame(int whoWon) {

        for (Integer computerDigit : computerNumber)
            computerNumberDigits.get(computerNumber.indexOf(computerDigit)).setText(String.valueOf(computerDigit));

        switch (whoWon) {
            case PLAYER_USER:
                endGameText.setText(R.string.you_won_text);
                break;
            case PLAYER_COMPUTER:
                endGameText.setText(R.string.i_won_text);
                break;
            case DRAW:
                endGameText.setText(R.string.draw_text);
                break;
            default:
                endGameText.setText(R.string.draw_text);
                break;
        }
        computerGuessKeyPad.setVisibility(View.INVISIBLE);
        userGuessKeyPad.setVisibility(View.INVISIBLE);
        tryKey.setEnabled(false);
        endGameText.setVisibility(View.VISIBLE);
        endGameSubText.setVisibility(View.VISIBLE);
    }


    /**
     * makes a guess for a digit depending on the probabilities
     * @param digitProbabilities digit specific probability
     * @param computerGuess current guess to not to repeat digit values.
     * @return an int value to use in computer guess
     */
    private int digitGuess(List<Integer> digitProbabilities, List<Integer> computerGuess) {
        int generatedDigit = new Random().nextInt(10);
        int maximumValueOfProbability = 0;
        for (int value : digitProbabilities)
            if (value > maximumValueOfProbability)
                maximumValueOfProbability = value;

        while (numberExistanceProbabilities.get(generatedDigit) <= 0 ||
                digitProbabilities.get(generatedDigit) != maximumValueOfProbability ||
                computerGuess.indexOf(generatedDigit) > -1) {
            if (computerGuess.size() == 0)
                generatedDigit = new Random().nextInt(9) + 1;
            else
                generatedDigit = new Random().nextInt(10);
        }
        return generatedDigit;
    }

    /**
     * computer logic to guess a good number depending on hints
     * @return a 4-digit number to guess
     */
    private List<Integer> computerGuess() {

        List<Integer> computerGuessNumber = new ArrayList<>();

        while(computerGuessHistory.contains(computerGuessNumber) || computerGuessNumber.size() < 4) {
            computerGuessNumber.clear();
            if(!computersThreePoint.isEmpty()) {
                int changeIndex = new Random().nextInt(4);
                computerGuessNumber.addAll(computersThreePoint);
                computerGuessNumber.set(changeIndex, digitGuess(digitProbabilities.get(changeIndex), computerGuessNumber));

            } else
                for(int i = 0; i<4; i++)
                    computerGuessNumber.add(digitGuess(digitProbabilities.get(i), computerGuessNumber));
            Log.d(TAG, "Generated Number :" + computerGuessNumber);
        }

        int whiteHintPoints = 0;
        int redHintPoints = 0;
        for (Integer guessedDigit : computerGuessNumber) {
            if (userNumber.indexOf(guessedDigit) > -1) {
                if (userNumber.indexOf(guessedDigit) == computerGuessNumber.indexOf(guessedDigit))
                    whiteHintPoints++;
                else
                    redHintPoints++;
            }
        }

        if(whiteHintPoints + redHintPoints == 4) {
            computersThreePoint.clear();
            for (int i = 0; i < 10; i++) {
                if (computerGuessNumber.indexOf(i) < 0)
                    numberExistanceProbabilities.set(i, 0);
            }
            if(redHintPoints == 4) {
                for (int i = 0; i < 4; i++)
                    digitProbabilities.get(i).set(digitProbabilities.get(i).get(computerGuessNumber.get(i)), 0);
            }
        } else if (whiteHintPoints + redHintPoints == 3 && computersThreePoint.isEmpty()) {
            computersThreePoint.addAll(computerGuessNumber);
        } else if (whiteHintPoints + redHintPoints == 0) {
            for (int i = 0; i < 10; i++) {
                if (computerGuessNumber.indexOf(i) >= 0)
                    numberExistanceProbabilities.set(i, 0);
            }
        }

        computerGuessHistory.add(computerGuessNumber);
        Log.d(TAG, "Computer Guess Number: " +computerGuessNumber.toString());
        return computerGuessNumber;
    }
}