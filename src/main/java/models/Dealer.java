package models;

import java.util.ArrayList;

/**
 * Created by vgrejuc on 3/7/16.
 */
public class Dealer extends Person {
    Dealer(){
        this.Hand = new ArrayList<>();
        this.cardCount = 0;
    }

    public void dealerTurn() {
        deal(Hand, 2);
        while (countCards(Hand) < 17) {
            deal(Hand, 1);
        }
        cardCount = countCards(Hand);
        if (cardCount > 21) {
            bank += bet *= 2;
            endHand("Dealer busts. You win!");
        } else if (cardCount > pCCount) {
            endHand("Dealer Wins");
        } else {
            endHand("You win!");
            bank += bet *= 2;
        }
    }

}
