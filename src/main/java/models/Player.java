package models;

import java.io.Serializable;

/**
 * Created by Jacob on 3/7/2016.
 */
public class Player extends Person {
    private int bank;

    public Player(int bank){
        super();
        this.bank = bank;
    }

    public void setBet(int newBet){
        int diff = newBet - this.bet;
        this.bank -= diff;
        this.bet = newBet;
    }
    public int getBank(){ return bank;    }
    public void setBank(int bank){ this.bank = bank; }

    public void win(){
        this.bank += this.bet * 2;
    }

}
