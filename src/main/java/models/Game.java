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
    public int     pCCount = 0; //player card count
    public boolean betError = false;
    public boolean stillBet  = false;

    public java.util.List<Card> dHand = new ArrayList<>();
    //don't really need a dBet or dBank for now
    //dealer will match so it is just printing/using pBet for the dealer
    public int     dCCount = 0; //dealer card count

    public boolean gameOver = false;
    public boolean playerWins = false;

    public Game(){
    }

    //Ranks go from 1 to 13. Aces are 1 and Kings are 13.
    //adds the number of decks to the game
    public void buildDeck(int numDecks) {
        for(int i = 0; i < numDecks; ++i) {
            for(int j = 1; j < 14; ++j) {
                deck.add(new Card(j, Suit.Clubs));
                deck.add(new Card(j, Suit.Hearts));
                deck.add(new Card(j, Suit.Diamonds));
                deck.add(new Card(j, Suit.Spades));
            }
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

    //takes a number of cards from the top of the deck and puts it into a hand
    public void deal(java.util.List<Card> hand, int numCards) {
        for(int i = 0; i < numCards; ++i)
            hand.add(removeTop(deck));
    }

    //empties the hand and puts all of the cards back into the deck
    public void emptyHand(java.util.List<Card> hand) {
        while(hand.size() > 0)
            deck.add(removeTop(hand));
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
    //sets stillBet to true if it failed, false if it succeeded
    public void tryDeal() {
        if(pBet < 2){
            stillBet = true;
            return;
        }
        deal(pHand,2);
        pCCount = countCards(pHand);
        stillBet = false;
    }

    //adds a card to the players hand and recounts cards
    //sets gameOver to be true if they went over 21, false if otherwise
    public void tryHit() {
        deal(pHand,1);
        pCCount = countCards(pHand);

        gameOver = (pCCount > 21);
    }

    //resets the game
    public void gameOver() {
        //these are needed for display
        pBet = 0;
        pCCount = 0;
        dCCount = 0;
        //needed for testing loss
        gameOver = false;

        //return cards back to deck
        emptyHand(pHand);
        emptyHand(dHand);

        //reshuffle the deck for randomness
        shuffle();
    }

    //resets the game when the player loses
    public void playerLost() {
        //player gets nothing so pBank is unchanged
        gameOver();
    }

    //resets the game when the dealer loses
    public void dealerLost() {
        //player wins their bets and the dealers
        pBank += 2*pBet;
        gameOver();
    }
}

