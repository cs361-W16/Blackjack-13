package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by michaelhilton on 1/25/16.
 */
public class Game {

    public java.util.List<Card> deck = new ArrayList<>();

    //pHand is the players hand and dHand is the dealers hand
    public java.util.List<Card> pHand = new ArrayList<>();
    public int     pBank = 100;
    public int     pBet = 0;
    public boolean betError = false;
    public int     pCCount = 0; //player card count
    public boolean stillBet  = true;
    public boolean hasHit = false;
    public boolean handOver = false;
    public boolean bust = false;
    public boolean canSplit = false;
    public boolean hasSplit = false;
    public boolean dealerBust = false;
    public boolean playerWin = false;

    public java.util.List<Card> dHand = new ArrayList<>();
    public int     dCCount = 0; //dealer card count

    public Game(){
    }

    //Ranks go from 1 to 13. Aces are 1 and Kings are 13.
    public void buildDeck(int numDecks) {
        for(int j=0;j<numDecks;j++) {
            for (int i = 1; i < 14; i++) {
                deck.add(new Card(i, Suit.Clubs));
                deck.add(new Card(i, Suit.Hearts));
                deck.add(new Card(i, Suit.Diamonds));
                deck.add(new Card(i, Suit.Spades));
            }
        }
    }

    public void dealerTurn(){
        deal(dHand, 2);
        while(countCards(dHand) < 17){
            deal(dHand, 1);
        }
        dCCount = countCards(dHand);
        if(dCCount > 21){
            dealerBust = true;
            playerWin = true;
            handOver = true;
            pBet = 0;
        } else if(dCCount > pCCount){
            handOver = true;
            playerWin = false;
        } else{
            handOver = true;
            playerWin = true;
            pBank += pBet *=2;
        }
    }

    public void shuffle() {
        long seed = System.nanoTime();
        Collections.shuffle(deck, new Random(seed));
    }

    //removes the top card from a list and returns it
    public Card removeTop(java.util.List<Card> l) {
        return l.remove(l.size()-1);
    }

    //takes a number of cards from the deck and puts it into a hand
    public void deal(java.util.List<Card> hand, int numCards) {
        for(int i = 0; i < numCards; ++i)
            hand.add(removeTop(deck));
    }

    //empties the hand and puts all of the cards back into the deck
    public void emptyHand(java.util.List<Card> hand) {
        while(hand.size() > 0)
            deck.add(removeTop(hand));
    }

    //will try to bet for the player
    //sets betError to true if it failed, false if it succeeded
    public void tryBet(int amount) {
        if(amount > pBank){
            betError = true;
            return;
        }
        pBank -= amount;
        pBet  += amount;
        betError = false;
    }

    //will try to deal 2 cards to the player if their bets are >=2
    //keeps stillBet true if it failed, sets to false if it succeeded
    public void tryDeal() {
        if(pBet < 2){
            return;
        } else if (stillBet == false) {
            return;
        }else{
            deal(pHand, 2);
            pCCount = countCards(pHand);
            if(pHand.get(0).getValue() == pHand.get(1).getValue()){
                canSplit = true;
            }
            stillBet = false;
        }
    }

    //turns a cards rank into a blackjack value
    public int generateVal(Card c) {
        int rank = c.getValue();
        if(rank >= 2 && rank <= 10)
            return rank; //2-10
        else if(rank >= 11 && rank <= 13)
            return 10; //J,Q,K
        else if(rank == 1)
            return 11; //A

        return -1; //error
    }

    //counts all of the cards in the hand and gets as close to 21 as possible
    public int countCards(java.util.List<Card> hand) {
        int count = 0;
        int numAces = 0;

        for(int i = 0; i < hand.size(); ++i){
            int val = generateVal(hand.get(i));
            if(val == 11)
                numAces++;
            count += val;
        }

        while(count > 21 && numAces > 0){
            count -= 10; //count-11+1 which turns Ace into a 1
            numAces--;
        }

        return count;
    }
    public void tryHit(){
        if((!stillBet) && (!handOver) ){
            pHand.add(removeTop(deck));
            hasHit = true;
            pCCount = countCards(pHand);
            if (pCCount > 21){
                bust = true;
                handOver = true;
                pBet = 0;
            }
        }else{
            return;
        }
    }

    public void doubleDown(){
        pBet *= 2;
        deal(pHand, 1);
        pCCount = countCards(pHand);
        if(pCCount > 21){
            bust = true;
        } else {
            dealerTurn();
        }
    }

    //Resets all variables except money and deck
    public void newHand(){
        pBet = 0;
        betError = false;
        pCCount = 0;
        dCCount = 0;
        stillBet  = true;
        hasHit = false;
        handOver = false;
        bust = false;
        canSplit = false;
        hasSplit = false;
        dealerBust = false;
        playerWin = false;
        emptyHand(pHand);
        emptyHand(dHand);
    }
}

