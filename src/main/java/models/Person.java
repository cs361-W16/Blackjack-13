package models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vgrejuc on 3/7/16.
 */
public abstract class Person implements Serializable {
    private java.util.List<Card> Hand;
    protected int bet;


    public Person(){
        this.Hand = new ArrayList<>();
        this.bet = 0;
    }
    //Setter methods
    public abstract void setBet(int val);

    //Getter methods
    public int getBet(){
        return this.bet;
    }



    public java.util.List<Card> getHand(){
        return this.Hand;
    }

    private Card removeTop() {
        return this.Hand.remove(this.Hand.size() - 1);
    }

    public java.util.List<Card> emptyHand() {
        java.util.List<Card> tempHand = new ArrayList<>();
        while (this.Hand.size() > 0) {
            tempHand.add(removeTop());
        }
        return tempHand;
    }

    public void setHand(java.util.List<Card> newHand) {
        this.Hand = newHand;
    }

    public void addCard(Card c){
        this.Hand.add(c);
    }


}
