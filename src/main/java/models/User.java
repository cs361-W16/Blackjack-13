package models;

import java.util.ArrayList;

/**
 * Created by vgrejuc on 3/7/16.
 */
public class User extends Person {
    public int bank = 100;

    Person(){
        setBet(0);
        setCardCount(0);
    }

    public void tryBet(int amount) {
        if (amount > bank) {
            errorFlag = true;
            userMessage = "You cannot bet more money than you have in the bank";
            return;
        } else {
            bank -= amount;
            bet += amount;
        }
    }

    public void doubleDown() {
        bet *= 2;
        deal(Hand, 1);
        cardCount = countCards(Hand);
        if (cardCount > 21) {
            endHand("You bust");
        } else {
            dealerTurn();
        }
    }

}
