package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.BattlecryTargetType;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.minion.MinionTargetableBattlecry;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

import java.util.EnumSet;

public class ShatteredSunCleric extends Minion implements MinionTargetableBattlecry {

    public ShatteredSunCleric() {
        super();
    }

    @Override
    public EnumSet<BattlecryTargetType> getBattlecryTargets() {
        return EnumSet.of(BattlecryTargetType.FRIENDLY_MINIONS);
    }

    /**
     * Battlecry: Give a friendly minion +1/+1
     */
    @Override
    public HearthTreeNode useTargetableBattlecry_core(
            PlayerSide side,
            Minion targetMinion,
            HearthTreeNode boardState,
            Deck deckPlayer0,
            Deck deckPlayer1
        ) throws HSException {
        HearthTreeNode toRet = boardState;
        targetMinion.setAttack((byte)(targetMinion.getAttack() + 1));
        targetMinion.setHealth((byte)(targetMinion.getHealth() + 1));
        return toRet;
    }

}
