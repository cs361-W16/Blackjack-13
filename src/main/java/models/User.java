package models;

import java.util.ArrayList;

/**
 * Created by vgrejuc on 3/7/16.
 */
public class User extends Person {
    public int bank = 100;
    public int bet = 0;

    User(){
        this.Hand = new ArrayList<>();
        this.cardCount = 0;
        this.bet = 0;
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

    public void tryDeal() {
        if (bet < 2) {
            errorFlag = true;
            userMessage = "You must bet a minimum of $2";
            return;
        } else if (Hand.size() > 0) {
            errorFlag = true;
            userMessage = "You have already been dealt your initial hand";
            return;
        } else {
            deal(Hand, 2);
            cardCount = countCards(Hand);
            if (Hand.get(0).getValue() == Hand.get(1).getValue()) {
                splitDisabled = false;
            }
            dealDisabled = true;
            againDisabled = true;
            doubleDisabled = false;
            standDisabled = false;
            hitDisabled = false;
            bettingDisabled = true;
        }
    }

    public void tryHit() {
        if (Hand.size() > 0) {
            Hand.add(removeTop(deck));
            cardCount = countCards(Hand);
            if (cardCount > 21) {
                endHand("You bust");
            } else {
                againDisabled = true;
                dealDisabled = true;
                hitDisabled = false;
                standDisabled = false;
                doubleDisabled = true;
                splitDisabled = true;
            }
        } else {
            errorFlag = true;
            userMessage = "You must get your initial hand dealt before you can hit";
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
