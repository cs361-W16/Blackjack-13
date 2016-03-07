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
    public int bank = 100;
    public int bet = 0;
    public int pCCount = 0; //player card count
    public boolean errorFlag = false;
    public String userMessage = "Place your Bet";
    public boolean againDisabled = true;
    public boolean dealDisabled = false;
    public boolean hitDisabled = true;
    public boolean standDisabled = true;
    public boolean splitDisabled = true;
    public boolean doubleDisabled = true;
    public boolean bettingDisabled = false;

    public java.util.List<Card> dHand = new ArrayList<>();
    public int dCCount = 0; //dealer card count

    public boolean gameOver = false;
    public boolean playerWins = false;

    public Game() {
    }

    //Ranks go from 1 to 13. Aces are 1 and Kings are 13.
    //adds the number of decks to the game
    public void buildDeck(int numDecks) {
        for (int i = 0; i < numDecks; ++i) {
            for (int j = 1; j < 14; ++j) {
                deck.add(new Card(j, Suit.Clubs));
                deck.add(new Card(j, Suit.Hearts));
                deck.add(new Card(j, Suit.Diamonds));
                deck.add(new Card(j, Suit.Spades));
            }
        }
    }

    public void dealerTurn() {
        deal(dHand, 2);
        while (countCards(dHand) < 17) {
            deal(dHand, 1);
        }
        dCCount = countCards(dHand);
        if (dCCount > 21) {
            bank += bet *= 2;
            endHand("Dealer busts. You win!");
        } else if (dCCount > pCCount) {
            endHand("Dealer Wins");
        } else {
            endHand("You win!");
            bank += bet *= 2;
        }
    }

    public void shuffle() {
        long seed = System.nanoTime();
        Collections.shuffle(deck, new Random(seed));
    }

    //removes the top card from a list and returns it
    public Card removeTop(java.util.List<Card> l) {
        return l.remove(l.size() - 1);
    }

    //takes a number of cards from the top of the deck and puts it into a hand
    public void deal(java.util.List<Card> hand, int numCards) {
        for (int i = 0; i < numCards; ++i)
            hand.add(removeTop(deck));
    }

    //empties the hand and puts all of the cards back into the deck
    public void emptyHand(java.util.List<Card> hand) {
        while (hand.size() > 0)
            deck.add(removeTop(hand));
    }

    //will try to bet for the player
    //sets betError to true if it failed, false if it succeeded
    public void tryBet(int amount) {
        if (amount > bank) {
            errorFlag = true;
            userMessage = "You cannot bet more money than you have in the bank";
            return;
        } else {
            bank -= amount;
            bet += amount;
        }
    }

    //will try to deal 2 cards to the player if their bets are >=2
    //keeps stillBet true if it failed, sets to false if it succeeded
    public void tryDeal() {
        if (bet < 2) {
            errorFlag = true;
            userMessage = "You must bet a minimum of $2";
            return;
        } else if (pHand.size() > 0) {
            errorFlag = true;
            userMessage = "You have already been dealt your initial hand";
            return;
        } else {
            deal(pHand, 2);
            pCCount = countCards(pHand);
            if (pHand.get(0).getValue() == pHand.get(1).getValue()) {
                splitDisabled = false;
            }
            dealDisabled = true;
            againDisabled = true;
            doubleDisabled = false;
            standDisabled = false;
            hitDisabled = false;
            bettingDisabled = true;
        }
    }

    //turns a cards rank into a blackjack value
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

    //counts all of the cards in the hand and gets as close to 21 as possible
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

    public void tryHit() {
        if (pHand.size() > 0) {
            pHand.add(removeTop(deck));
            pCCount = countCards(pHand);
            if (pCCount > 21) {
                endHand("You bust");
            } else {
                againDisabled = true;
                dealDisabled = true;
                hitDisabled = false;
                standDisabled = false;
                doubleDisabled = true;
                splitDisabled = true;
            }
        } else {
            errorFlag = true;
            userMessage = "You must get your initial hand dealt before you can hit";
        }
    }

    public void doubleDown() {
        bet *= 2;
        deal(pHand, 1);
        pCCount = countCards(pHand);
        if (pCCount > 21) {
            endHand("You bust");
        } else {
            dealerTurn();
        }
    }

    public void endHand(String message) {
        userMessage = message;
        againDisabled = false;
        dealDisabled = true;
        hitDisabled = true;
        standDisabled = true;
        doubleDisabled = true;
        splitDisabled = true;
    }

    //Resets all variables except money and deck
    public void newHand() {
        bet = 0;
        errorFlag = false;
        userMessage = "Place your Bet";
        pCCount = 0;
        dCCount = 0;
        againDisabled = true;
        dealDisabled = false;
        hitDisabled = true;
        splitDisabled = true;
        standDisabled = true;
        doubleDisabled = true;
        bettingDisabled = false;
        emptyHand(pHand);
        emptyHand(dHand);

    }
}

