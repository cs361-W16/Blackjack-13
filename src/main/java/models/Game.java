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
    public java.util.List<Card> dHand = new ArrayList<>();


    public Game(){
    }

    //Ranks go from 1 to 13. Aces are 1 and Kings are 13.
    public void buildDeck() {
        for(int i = 1; i < 14; i++){
            deck.add(new Card(i,Suit.Clubs));
            deck.add(new Card(i,Suit.Hearts));
            deck.add(new Card(i,Suit.Diamonds));
            deck.add(new Card(i,Suit.Spades));
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

    //takes a card from the deck and puts it into a hand
    public void deal(java.util.List<Card> hand) {
        hand.add(removeTop(deck));
    }

    //empties the hand and puts all of the cards back into the deck
    public void emptyHand(java.util.List<Card> hand) {
        while(hand.size() > 0)
            deck.add(removeTop(hand));
    }
}

