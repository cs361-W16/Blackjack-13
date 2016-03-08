package models;

import java.util.ArrayList;

/**
 * Created by vgrejuc on 3/7/16.
 */
public class Person{
    private java.util.List<Card> Hand = new ArrayList<>();
    private int cardCount;
    private int bet = 0;

    //Setter methods
    public void setBet(int val){
        this.bet = val;
    }

    public void setCardCount(int val){
        this.cardCount = val;
    }

    public void setHand(java.util.List<Card> val){
        this.Hand = val;
    }

    //Getter methods
    public int getBet(){
        return this.bet;
    }

    public int getCardCount(){
        return this.cardCount;
    }

    public java.util.List<Card> getHand(){
        return this.Hand;
    }
    ///////////////////////////////////////////////////

    public void emptyHand(java.util.List<Card> hand) {
        while (hand.size() > 0)
            deck.add(removeTop(hand));
    }

    public void addCard(java.util.List<Card> hand) {
        hand.add(removeTop(deck));
    }

    public int generateVal(Card c) {
        int rank = c.getValue();
        if (rank >= 2 && rank <= 10)
            return rank; //2-10
        else if (rank >= 11 && rank <= 13)
            return 10; //J,Q,K
        else if (rank == 1)
            return 11; //A

        return -1; //error
    }

    public int countCards(java.util.List<Card> hand) {
        int count = 0;
        int numAces = 0;

        for (int i = 0; i < hand.size(); ++i) {
            int val = generateVal(hand.get(i));
            if (val == 11)
                numAces++;
            count += val;
        }

        while (count > 21 && numAces > 0) {
            count -= 10; //count-11+1 which turns Ace into a 1
            numAces--;
        }

        return count;
    }


}
