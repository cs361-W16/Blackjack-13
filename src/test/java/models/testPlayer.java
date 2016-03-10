package models;

import org.apache.commons.lang.ObjectUtils;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
/**
 * Created by Jacob on 3/7/2016.
 */
public class testPlayer {

    @Test
    public void testHand(){
        Player p = new Player(100);
        p.addCard(new Card(8, Suit.Diamonds));
        assertEquals(p.getHand().size(), 1);
        java.util.List<Card> temp = new ArrayList<>();
        p.emptyHand(temp);
        assertEquals(p.getHand().size(), 0);
        assertEquals(temp.get(0).getSuit(), Suit.Diamonds );
    }

    @Test
    public void testBank(){
        Player p = new Player(100);
        assertEquals(p.getBank(), 100);
        p.setBet(20);
        assertEquals(p.getBank(), 80);
        assertEquals(p.getBet(), 20);
    }

    @Test
    public void testPlayerConstructor(){
        Player p = new Player();
        assertEquals(0, p.getBank());
        assertNotEquals(null, p.getSecondHand());
        assertEquals(0, p.getSecondCount());
        assertEquals(0, p.getBetTwo());
    }

    @Test
    public void testWinBlackJack(){
        Player p = new Player(100);
        p.setBet(10);
        p.winBlackJack(1);
        assertEquals(115, p.getBank());
        p.setBetTwo(10);
        p.winBlackJack(0);
        assertEquals(140,p.getBank());
    }

    @Test
    public void testSetSecondHand(){
        Player p = new Player(100);
        java.util.List<Card> temp = new ArrayList<>();
        for(int i = 0; i < 2; ++i)
            temp.add(new Card(2, Suit.Clubs));
        p.setSecondHand(temp);
        assertEquals(temp, p.getSecondHand());
        assertEquals(4, p.getSecondCount());
    }

    @Test
    public void testEmptySecondHand(){
        Player p = new Player(100);
        for(int i = 0; i < 2; ++i)
            p.addSecondHandCard(new Card(1, Suit.Clubs));
        java.util.List<Card> temp = new ArrayList<>();
        p.emptySecondHand(temp);
        assertEquals(0, p.getSecondHand().size());
        assertEquals(0, p.getSecondCount());
    }
}
