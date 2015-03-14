package com.hearthsim.test.minion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.hearthsim.model.PlayerModel;
import org.junit.Before;
import org.junit.Test;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.minion.concrete.WaterElemental;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class TestWaterElemental {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        Card fb = new WaterElemental();
        currentPlayer.placeCardHand(fb);

        currentPlayer.setMana((byte) 8);
        currentPlayer.setMaxMana((byte) 8);

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());
    }

    @Test
    public void testFreezesMinionOnAttack() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
        assertEquals(board, ret);

        Minion waterElemental = currentPlayer.getMinions().get(0);
        assertTrue(waterElemental instanceof WaterElemental);

        waterElemental.hasAttacked(false); // unset summoning sickness
        waterElemental.attack(PlayerSide.WAITING_PLAYER, 2, board, false);
        assertTrue(waitingPlayer.getMinions().get(1).getFrozen());
    }

    @Test
    public void testFreezesHeroOnAttack() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board, null, null);
        assertEquals(board, ret);

        Minion waterElemental = currentPlayer.getMinions().get(0);
        assertTrue(waterElemental instanceof WaterElemental);

        waterElemental.hasAttacked(false); // unset summoning sickness
        waterElemental.attack(PlayerSide.WAITING_PLAYER, 0, board, false);
        assertTrue(waitingPlayer.getCharacter(0).getFrozen());
    }
}
