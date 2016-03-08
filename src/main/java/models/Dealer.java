package models;

import java.io.Serializable;


/**
 * Created by vgrejuc on 3/7/16.
 */
public class Dealer extends Person implements Serializable{

    public Dealer(){
        super();
    }

    public void setBet(int newBet){
        this.bet = newBet;
    }

}
