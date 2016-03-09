package models;

import java.io.Serializable;

/**
 * Created by Jacob on 3/7/2016.
 */
public class Player extends Person implements Serializable{
    private int bank;

    public Player(int bank){
        super();
        this.bank = bank;
    }
    public Player(){
        super();
        this.bank = 0;
    }

    public void setBet(int newBet){
        int diff = newBet - this.bet;
        this.bank -= diff;
        this.bet = newBet;
    }
    public int getBank(){ return bank;    }

    public void win(){ this.bank += this.bet * 2; }

    public void winBlackJack(){ this.bank += this.bet * 2.5; }

}
