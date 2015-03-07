package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class Gruul extends Minion {

    public Gruul() {
        super();
    }

    /**
     *
     * At the end of each turn, gain +1/+1
     *
     */
    @Override
    public HearthTreeNode endTurn(PlayerSide thisMinionPlayerIndex, HearthTreeNode boardModel, Deck deckPlayer0, Deck deckPlayer1) throws HSException {
        HearthTreeNode tmpState = super.endTurn(thisMinionPlayerIndex, boardModel, deckPlayer0, deckPlayer1);
        this.addHealth((byte)1);
        this.addMaxHealth((byte)1);
        this.addAttack((byte)1);
        return tmpState;
    }
}
