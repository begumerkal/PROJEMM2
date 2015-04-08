package com.hearthsim.test.card;

import com.hearthsim.card.Card;
import com.hearthsim.card.minion.concrete.BoulderfistOgre;
import com.hearthsim.card.minion.concrete.RaidLeader;
import com.hearthsim.card.spellcard.concrete.Bite;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.BoardModel;
import com.hearthsim.model.PlayerModel;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestBite {

    private HearthTreeNode board;
    private PlayerModel currentPlayer;
    private PlayerModel waitingPlayer;

    @Before
    public void setup() throws HSException {
        board = new HearthTreeNode(new BoardModel());
        currentPlayer = board.data_.getCurrentPlayer();
        waitingPlayer = board.data_.getWaitingPlayer();

        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.CURRENT_PLAYER, new BoulderfistOgre());

        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new RaidLeader());
        board.data_.placeMinion(PlayerSide.WAITING_PLAYER, new BoulderfistOgre());

        currentPlayer.placeCardHand(new Bite());

        currentPlayer.setMana((byte) 9);
        waitingPlayer.setMana((byte) 9);

        currentPlayer.setMaxMana((byte) 8);
        waitingPlayer.setMaxMana((byte) 8);

        board.data_.resetMana();
        board.data_.resetMinions();
    }

    @Test
    public void test1() throws HSException {
        Card theCard = currentPlayer.getHand().get(0);
        HearthTreeNode ret = theCard.useOn(PlayerSide.CURRENT_PLAYER, 0, board);

        assertFalse(ret == null);

        assertEquals(currentPlayer.getHand().size(), 0);
        assertEquals(currentPlayer.getNumMinions(), 2);
        assertEquals(waitingPlayer.getNumMinions(), 2);
        assertEquals(currentPlayer.getMana(), 4);
        assertEquals(waitingPlayer.getMana(), 8);
        assertEquals(currentPlayer.getHero().getHealth(), 30);
        assertEquals(waitingPlayer.getHero().getHealth(), 30);
        assertEquals(currentPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(currentPlayer.getCharacter(2).getHealth(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getHealth(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getHealth(), 7);

        assertEquals(currentPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(currentPlayer.getCharacter(2).getTotalAttack(), 7);
        assertEquals(waitingPlayer.getCharacter(1).getTotalAttack(), 2);
        assertEquals(waitingPlayer.getCharacter(2).getTotalAttack(), 7);

        assertEquals(currentPlayer.getHero().getArmor(), 4);
        assertEquals(currentPlayer.getHero().getExtraAttackUntilTurnEnd(), 4);
    }
}
