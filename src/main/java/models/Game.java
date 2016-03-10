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
    public String userMessageTwo = "";

    public boolean againDisabled = true;
    public boolean dealDisabled = false;
    public boolean hitDisabled = true;
    public boolean standDisabled = true;
    public boolean splitDisabled = true;
    public boolean doubleDisabled = true;
    public boolean bettingDisabled = false;
    public boolean previousTie = false;
    public boolean doubleDownTwoDisabled = false;
    public boolean hasSplit = false;
    public boolean hitTwoDisabled = false;
    public boolean standTwoDisabled = false;
    public boolean secondStand = false;
    public boolean firstStand = false;




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


    private void dealerTurn(int i) {
        if(i == 1) {
            //Do first hand
            if (player.getCount() == 21 && player.getHand().size() == 2) {
                player.winBlackJack(1);
                endHand("You got Blackjack!");
            } else {
                if(hasSplit && secondStand){
                    dealer.emptyHand(deck);
                    shuffle();
                }
                dealer.setHand(deal(dealer.getHand(), 2));
                while (dealer.getCount() < 17) {
                    dealer.setHand(deal(dealer.getHand(), 1));
                }
                if (dealer.getCount() > 21) {
                    player.win(1);
                    endHand("Dealer busts. You win!");
                } else if (dealer.getCount() == player.getCount()) {
                    //Start new hand with same bet
                    errorFlag = false;
                    userMessage = "Tie! New Hand";
                    againDisabled = true;
                    dealDisabled = false;
                    hitDisabled = true;
                    splitDisabled = true;
                    standDisabled = true;
                    doubleDisabled = true;
                    bettingDisabled = true;
                    previousTie = true;
                } else if (dealer.getCount() > player.getCount()) {
                    endHand("Dealer Wins");
                } else {
                    endHand("You win!");
                    player.win(1);
                }
                standDisabled = true;
            }
        }else if(i == 2) {
            //Do second Hand
            if (player.getSecondCount() == 21 && player.getSecondHand().size() == 2) {
                player.winBlackJack(2);
                endSecondHand("You got Blackjack!");
            } else {
                if(firstStand){
                    dealer.emptyHand(deck);
                    shuffle();
                }
                dealer.setHand(deal(dealer.getHand(), 2));
                while (dealer.getCount() < 17) {
                    dealer.setHand(deal(dealer.getHand(), 1));
                }
                if (dealer.getCount() > 21) {
                    player.win(2);
                    endSecondHand("Dealer busts. You win!");
                } else if (dealer.getCount() > player.getCount()) {
                    endSecondHand("Dealer Wins");
                } else {
                    endSecondHand("You win!");
                    player.win(2);
                }
            }
        }

    }

    public void shuffle() {
        long seed = System.nanoTime();
        Collections.shuffle(deck, new Random(seed));
        //Card c = new Card(9,Suit.Clubs);
        //Card c2 = new Card(9, Suit.Diamonds);
        //deck.add(c);
        //deck.add(c2);
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
        if(previousTie){
            previousTie = false;
            emptyHands();
            shuffle();
        }
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
                //User can split if they want to
                userMessage = "Hit, Stand or Split";
                splitDisabled = false;
            }else{
                userMessage = "Hit or Stand";
            }
            dealDisabled = true;
            againDisabled = true;
            doubleDisabled = false;
            standDisabled = false;
            hitDisabled = false;
            bettingDisabled = true;
        }
    }

    public void split(){
        java.util.List<Card> tempHand = new ArrayList<>();
        player.emptyHand(tempHand);
        player.addCard(tempHand.get(0));
        player.setHand(deal(player.getHand(), 1));
        player.addSecondHandCard(tempHand.get(1));
        player.setSecondHand(deal(player.getSecondHand(),1));
        userMessage = "Hit or Stand";
        userMessageTwo = "Hit or Stand";

        int remainder = player.getBet() % 2;
        player.setBetTwo(player.getBet() / 2);
        player.setBet((player.getBet() / 2) + remainder);

        dealDisabled = true;
        againDisabled = true;
        doubleDisabled = false;
        standDisabled = false;
        hitDisabled = false;
        splitDisabled = true;
        bettingDisabled = true;

        hitTwoDisabled = false;
        standTwoDisabled = false;
        doubleDownTwoDisabled = false;
        hasSplit = true;
        firstStand = false;
        secondStand = false;
    }

    public void hitTwo(){
        player.addSecondHandCard(removeTop(deck));
        if (player.getSecondCount() > 21){
            secondStand = true;
            endSecondHand("You bust");
        } else{
            doubleDownTwoDisabled = true;
        }
    }

    public void endSecondHand(String s){
        userMessageTwo = s;
        doubleDownTwoDisabled = true;
        hitTwoDisabled = true;
        standTwoDisabled = true;
        if(firstStand){
            againDisabled = false;
            hasSplit = false;
        }
    }

    public void standTwo(){
        secondStand = true;
        dealerTurn(2);
    }

    public void tryStand() {
        //Probably unnecessary check until we have split functionality
        if(player.getHand().size() > 0){
            firstStand = true;
            dealerTurn(1);
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
        secondStand = true;
        if (player.getCount() > 21) {
            endHand("You bust");
        } else {
            dealerTurn(1);
        }
    }


   public void doubleDownTwo() {
        player.setBetTwo(player.getBetTwo() * 2);
        dealer.setBet(dealer.getBet() * 2);
        player.setSecondHand(deal(player.getSecondHand(), 1));
        if (player.getSecondCount() > 21) {
            endSecondHand("You bust");
        } else {
            dealerTurn(2);
        }
   }

    private void endHand(String message) {
        userMessage = message;
        againDisabled = false;
        if(hasSplit && (!secondStand)){
                againDisabled = true;
                firstStand = true;
            } else if (hasSplit && secondStand){
            player.emptySecondHand(deck);
            shuffle();
        }
        dealDisabled = true;
        hitDisabled = true;
        standDisabled = true;
        doubleDisabled = true;
        splitDisabled = true;
    }

    //Resets all variables except money and deck
    public void newHand() {
        if(player.getSecondHand().size() > 0){
            player.emptySecondHand(deck);
            shuffle();
        }
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

