package models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vgrejuc on 3/7/16.
 */
public abstract class Person implements Serializable {
    private java.util.List<Card> Hand;
    protected int bet;
    private int count;


    public Person(){
        this.Hand = new ArrayList<>();
        this.bet = 0;
        this.count = 0;
    }
    //Setter methods
    public abstract void setBet(int val);

    //Getter methods
    public int getBet(){
        return bet;
    }


    public int getCount(){return count;}

    public java.util.List<Card> getHand(){
        return Hand;
    }

    private Card removeTop() {
        return this.Hand.remove(this.Hand.size() - 1);
    }

    public void emptyHand(java.util.List<Card> h) {
        while (this.Hand.size() > 0) {
            h.add(removeTop());
        }
        this.count = 0;
    }

    public void resetBet(){
        this.bet = 0;
    }

    public void setHand(java.util.List<Card> newHand) {
        this.Hand = newHand;
        this.count = countCards(Hand);
    }

    public void addCard(Card c){
        this.Hand.add(c);
        this.count = countCards(Hand);
    }

    private int generateVal(Card c) {
        int rank = c.getValue();
        if (rank >= 2 && rank <= 10)
            return rank; //2-10
        else if (rank >= 11 && rank <= 13)
            return 10; //J,Q,K
        else if (rank == 1)
            return 11; //A

        return -1; //error
    }

    private int countCards(java.util.List<Card> hand) {
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
