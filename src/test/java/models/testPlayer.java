package models;

import org.junit.Test;
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
        java.util.List<Card> temp = p.emptyHand();
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
}
