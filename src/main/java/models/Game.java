package models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by michaelhilton on 1/25/16.
 */
public class Game implements Serializable {

    public java.util.List<Card> deck;

    public Player player;
    public Dealer dealer;

    public boolean errorFlag = false;
    public String userMessage = "Place your Bet";

    public int pCCount = 0;
    public int dCCount = 0;
    public boolean againDisabled = true;
    public boolean dealDisabled = false;
    public boolean hitDisabled = true;
    public boolean standDisabled = true;
    public boolean splitDisabled = true;
    public boolean doubleDisabled = true;
    public boolean bettingDisabled = false;




    public Game(int numDecks, int playerBank) {
        this.player = new Player(playerBank);
        this.dealer = new Dealer();
        this.deck = new ArrayList<>();
        buildDeck(numDecks);
        shuffle();
    }

    private void emptyHands(){
        deck.addAll(dealer.emptyHand());
        deck.addAll(player.emptyHand());
    }

    //Ranks go from 1 to 13. Aces are 1 and Kings are 13.
    //adds the number of decks to the game
    private void buildDeck(int numDecks) {
        for (int i = 0; i < numDecks; ++i) {
            for (int j = 1; j < 14; ++j) {
                deck.add(new Card(j, Suit.Clubs));
                deck.add(new Card(j, Suit.Hearts));
                deck.add(new Card(j, Suit.Diamonds));
                deck.add(new Card(j, Suit.Spades));
            }
        }
    }


    private void dealerTurn() {
        deal(dealer.getHand(), 2);
        while (countCards(dealer.getHand()) < 17) {
            deal(dealer.getHand(), 1);
        }
        if (countCards(dealer.getHand()) > 21) {
            player.win();
            endHand("Dealer busts. You win!");
        } else if (countCards(dealer.getHand()) > countCards(player.getHand())) {
            endHand("Dealer Wins");
        } else {
            endHand("You win!");
            player.win();
        }
    }

    private void shuffle() {
        long seed = System.nanoTime();
        Collections.shuffle(deck, new Random(seed));
    }

    //removes the top card from a list and returns it


    //takes a number of cards from the top of the deck and puts it into a hand

    private java.util.List<Card> deal(java.util.List<Card> hand, int numCards) {
        for (int i = 0; i < numCards; ++i) {
            hand.add(removeTop(deck));
        }
        return hand;
    }

    private Card removeTop(java.util.List<Card> hand) {
        return hand.remove(hand.size() - 1);
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


    //will try to bet for the player
    //sets betError to true if it failed, false if it succeeded
    public void tryBet(int amount) {
        if (amount > player.getBank()) {
            errorFlag = true;
            userMessage = "You cannot bet more money than you have in the bank";
            return;
        } else {
            player.setBet(player.getBet() + amount);
            dealer.setBet(dealer.getBet() + amount);
        }
    }

    //will try to deal 2 cards to the player if their bets are >=2
    //keeps stillBet true if it failed, sets to false if it succeeded

    public void tryDeal() {
        if (player.getBet() < 2) {
            errorFlag = true;
            userMessage = "You must bet a minimum of $2";
            return;
        } else if (player.getHand().size() > 0) {
            errorFlag = true;
            userMessage = "You have already been dealt your initial hand";
            return;
        } else {
            player.setHand(deal(player.getHand(), 2));
            pCCount = countCards(player.getHand());
            if (player.getHand().get(0).getValue() == player.getHand().get(1).getValue()) {
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


    public void tryHit() {
        if (player.getHand().size() > 0) {
            player.addCard(removeTop(deck));
            pCCount = countCards(player.getHand());
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
        player.setBet(player.getBet() * 2);
        dealer.setBet(dealer.getBet() * 2);
        player.setHand(deal(player.getHand(), 1));
        pCCount = countCards(player.getHand());
        if (pCCount > 21) {
            endHand("You bust");
        } else {
            dealerTurn();
        }
    }

    private void endHand(String message) {
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
        player.setBet(0);
        dealer.setBet(0);
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
        emptyHands();
    }
}

