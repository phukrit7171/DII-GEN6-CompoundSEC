package com.camt.dii.secure.strategy;

import com.camt.dii.secure.card.Card;
import com.camt.dii.secure.common.Floor;

public interface AccessStrategy {
    boolean checkAccess(Card card, Floor floor, String room);
}