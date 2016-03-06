package models;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by michaelhilton on 1/25/16.
 */
public class testGame {

    @Test
    public void testGameCreation(){
        Game g = new Game();
        assertNotNull(g);
    }

    @Test
    public void testGameBuildDeck(){
        Game g = new Game();
        g.buildDeck();
        assertEquals(52,g.deck.size());
    }

    @Test
    public void testGameInit(){
        Game g = new Game();
        g.buildDeck();
        g.shuffle();
        assertNotEquals(1,g.deck.get(0).getValue());
    }

    @Test
    public void testDeal(){
        Game g = new Game();
        g.buildDeck();
        g.deal(g.pHand,1);
        assertEquals(1,g.pHand.size());
        assertEquals(13,g.pHand.get(0).getValue());
        assertEquals(Suit.Spades,g.pHand.get(0).getSuit());
        assertEquals(51,g.deck.size());

        g.deal(g.pHand,5);
        assertEquals(6,g.pHand.size());
    }

    @Test
    public void testEmptyHand(){
        Game g = new Game();
        g.buildDeck();

        g.deal(g.pHand,4);

        g.emptyHand(g.pHand);
        assertEquals(0,g.pHand.size());
        assertEquals(52,g.deck.size());
    }

    @Test
    public void testTryBet(){
        Game g = new Game();

        g.tryBet(101);
        assertEquals(true,g.betError);
        assertEquals(0,g.pBet);
        assertEquals(100,g.pBank);

        g.tryBet(5);
        assertEquals(false,g.betError);
        assertEquals(5,g.pBet);
        assertEquals(95,g.pBank);
    }

    @Test
    public void testTryDeal(){
        Game g = new Game();
        g.buildDeck(3);

        g.tryDeal();
        assertEquals(true,g.stillBet);
        assertEquals(0,g.pHand.size());

        g.tryBet(3);
        g.tryDeal();
        assertEquals(false,g.stillBet);
        assertEquals(2,g.pHand.size());

        g.tryBet(3);
        g.tryDeal();
        assertEquals(false,g.stillBet);
        assertEquals(2,g.pHand.size());
    }

    @Test
    public void testGenerateVal(){
        Game g = new Game();

        int t1 = g.generateVal(new Card(5,Suit.Clubs));
        assertEquals(5,t1);

        int t2 = g.generateVal(new Card(12,Suit.Diamonds));
        assertEquals(10,t2);

        int t3 = g.generateVal(new Card(1,Suit.Hearts));
        assertEquals(11,t3);

        int t4 = g.generateVal(new Card(14,Suit.Spades));
        assertEquals(-1,t4);
    }

    @Test
    public void testCountCards(){
        Game g = new Game();

        g.pHand.add(new Card(3,Suit.Hearts));
        g.pHand.add(new Card(12,Suit.Clubs));
        int t1 = g.countCards(g.pHand);
        assertEquals(13,t1);

        g.pHand.set(0, new Card(1,Suit.Spades));
        g.pHand.set(1, new Card(1,Suit.Diamonds));
        int t2 = g.countCards(g.pHand);
        assertEquals(12,t2);

        g.pHand.add(new Card(10,Suit.Clubs));
        g.pHand.add(new Card(10,Suit.Diamonds));
        int t3 = g.countCards(g.pHand);
        assertEquals(22,t3);
    }
}