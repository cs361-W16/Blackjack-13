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

    public boolean againDisabled = true;
    public boolean dealDisabled = false;
    public boolean hitDisabled = true;
    public boolean standDisabled = true;
    public boolean splitDisabled = true;
    public boolean doubleDisabled = true;
    public boolean bettingDisabled = false;
    public boolean dealerShow = false;




    public Game() {
        this.player = new Player(100);
        this.dealer = new Dealer();
        this.deck = new ArrayList<>();
        buildDeck(3);
        shuffle();
    }


    private void emptyHands(){
        dealer.emptyHand(deck);
        player.emptyHand(deck);
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
        dealer.setHand(deal(dealer.getHand(), 2));
        while (dealer.getCount() < 17) {
            dealer.setHand(deal(dealer.getHand(), 1));
        }
        if (dealer.getCount() > 21) {
            player.win();
            endHand("Dealer busts. You win!");
        } else if (dealer.getCount() > player.getCount()) {
            endHand("Dealer Wins");
        } else {
            endHand("You win!");
            if(player.getCount() == 21 && player.getHand().size() == 2){
                player.winBlackJack();
            } else {
                player.win();
            }
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
        userMessage = "Place your Bet";
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


    public void tryStand() {
        //Probably unnecessary check until we have split functionality
        if(player.getHand().size() > 0){
            dealerTurn();
        } else{
            errorFlag = true;
            userMessage = "You must get your initial hand dealt before you can stand";
        }

    }

    public void tryHit() {
        if (player.getHand().size() > 0) {
            player.addCard(removeTop(deck));
            if (player.getCount() > 21) {
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
        if (player.getCount() > 21) {
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
        player.resetBet();
        dealer.resetBet();
        errorFlag = false;
        userMessage = "Place your Bet";
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

