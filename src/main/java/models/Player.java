package models;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by Jacob on 3/7/2016.
 */
public class Player extends Person implements Serializable{
    private int bank;
    private java.util.List<Card> secondHand;
    private int secondCount;
    private int betTwo;
    public Player(int bank){
        super();
        this.bank = bank;
        this.secondHand = new ArrayList<>();
        this.secondCount = 0;
        this.betTwo = 0;
    }
    public Player(){
        super();
        this.bank = 0;
        this.secondHand = new ArrayList<>();
        this.secondCount = 0;
        this.betTwo = 0;
    }

    public void setBet(int newBet){
        int diff = newBet - this.bet;
        this.bank -= diff;
        this.bet = newBet;
    }
    public int getBank(){ return bank;    }

    public void win(int i){
        if (i == 1) {
            this.bank += this.bet * 2;
        } else {
            this.bank += this.betTwo * 2;
        }
    }

    public void winBlackJack(int i) {
        if (i == 1) {
            this.bank += this.bet * 2.5;
        } else {
            this.bank += this.betTwo * 2.5;
        }
    }

    private Card removeSecondTop(){ return this.secondHand.remove(this.secondHand.size() - 1);}

    public void setSecondHand(java.util.List<Card> newHand){
        secondHand = newHand;
        this.secondCount = countCards(secondHand);
    }

    public void addSecondHandCard(Card c){
        this.secondHand.add(c);
        this.secondCount = countCards(secondHand);
    }

    public java.util.List<Card> getSecondHand(){
        return secondHand;
    }

    public void emptySecondHand(java.util.List<Card> h) {
        while (this.secondHand.size() > 0){
            h.add(removeSecondTop());
        }
        this.secondCount = 0;
    }

    public int getSecondCount(){
        return secondCount;
    }

    public void setBetTwo(int b){
        betTwo = b;
    }

    public int getBetTwo(){
        return betTwo;
    }

}
