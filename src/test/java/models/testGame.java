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
        g.deal(g.pHand);
        assertEquals(1,g.pHand.size());
        assertEquals(13,g.pHand.get(0).getValue());
        assertEquals(Suit.Spades,g.pHand.get(0).getSuit());
        assertEquals(51,g.deck.size());
    }

    @Test
    public void testEmptyHand(){
        Game g = new Game();
        g.buildDeck();

        for(int i = 0; i < 4; ++i)
            g.deal(g.pHand);

        g.emptyHand(g.pHand);
        assertEquals(0,g.pHand.size());
        assertEquals(52,g.deck.size());
    }
}