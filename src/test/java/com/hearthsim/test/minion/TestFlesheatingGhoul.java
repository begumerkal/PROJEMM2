package com.hearthsim.test.minion;

import com.hearthsim.card.Card;
import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.*;
import com.hearthsim.card.spellcard.concrete.TheCoin;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestFlesheatingGhoul {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    private Deck deck;

    @Before
    public void setup() throws HSException {
        Card cards[] = new Card[10];
        for (int index = 0; index < 10; ++index) {
            cards[index] = new TheCoin();
        }

        deck = new Deck(cards);

        board = new HearthTreeNode(new BoardModel(deck, deck));
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new HarvestGolem());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new StormwindChampion());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new LootHoarder());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new Abomination());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());

        currentPlayer.setMana((byte) 10);
        waitingPlayer.setMana((byte) 10);

        Minion fb = new FlesheatingGhoul();
        currentPlayer.placeCardHand(fb);
    }

    @Test
    public void test0() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.WAITING_PLAYER, 0, board, deck, deck);

        assertNull(ret);

        assertEquals(currentPlayer.getHand().size(), 1);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 4);
        assertEquals(currentPlayer.getMana(), 10);
        assertEquals(waitingPlayer.getMana(), 10);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getMinions().get(0).getTotalHealth(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getTotalHealth(), 6);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalHealth(), 4);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 3);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalAttack(), 7);
    }

    @Test
    public void test1() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 3, board, deck, deck);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 4);
        assertEquals(waitingPlayer.getNumMinions(), 4);
        assertEquals(currentPlayer.getMana(), 7);
        assertEquals(waitingPlayer.getMana(), 10);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);

        assertEquals(currentPlayer.getMinions().get(0).getTotalHealth(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getTotalHealth(), 6);
        assertEquals(currentPlayer.getMinions().get(3).getTotalHealth(), 4);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalHealth(), 1);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalHealth(), 4);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalHealth(), 7);

        assertEquals(currentPlayer.getMinions().get(0).getAuraHealth(), 1);
        assertEquals(currentPlayer.getMinions().get(1).getAuraHealth(), 1);
        assertEquals(currentPlayer.getMinions().get(2).getAuraHealth(), 0);
        assertEquals(currentPlayer.getMinions().get(3).getAuraHealth(), 1);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 4);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 3);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 7);
        assertEquals(currentPlayer.getMinions().get(3).getTotalAttack(), 4);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 3);
        assertEquals(waitingPlayer.getMinions().get(1).getTotalAttack(), 5);
        assertEquals(waitingPlayer.getMinions().get(2).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getMinions().get(3).getTotalAttack(), 7);

        //---------------------------------------------------------------
        Minion attacker = currentPlayer.getCharacter(3);
        ret = attacker.attack(PlayerSide.WAITING_PLAYER, 2, board, false);

        assertFalse(ret == null);
        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 3);
        assertEquals(waitingPlayer.getNumMinions(), 1);
        assertEquals(currentPlayer.getMana(), 7);
        assertEquals(waitingPlayer.getMana(), 10);
        assertEquals(currentPlayer.getHero().getHealth(), 28); //2 damage from Abomination's deathrattle
        assertEquals(waitingPlayer.getHero().getHealth(), 28); //2 damage from Abomination's deathrattle

        assertEquals(currentPlayer.getMinions().get(0).getTotalHealth(), 2);
        assertEquals(currentPlayer.getMinions().get(1).getTotalHealth(), 1);
        assertEquals(currentPlayer.getMinions().get(2).getTotalHealth(), 2);
        assertEquals(waitingPlayer.getMinions().get(0).getTotalHealth(), 5);

        assertEquals(currentPlayer.getMinions().get(0).getAuraHealth(), 0);
        assertEquals(currentPlayer.getMinions().get(1).getAuraHealth(), 0);
        assertEquals(currentPlayer.getMinions().get(2).getAuraHealth(), 0);

        assertEquals(currentPlayer.getMinions().get(0).getTotalAttack(), 3);
        assertEquals(currentPlayer.getMinions().get(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getMinions().get(2).getTotalAttack(), 3 + 4); //4 minions died, including the Stormwind Champion
        assertEquals(waitingPlayer.getMinions().get(0).getTotalAttack(), 6);
    }
}
