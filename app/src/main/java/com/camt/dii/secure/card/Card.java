package com.camt.dii.secure.card;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Card {
    private UUID cardID;
    private CardType cardType;
    private LocalDateTime created;
    private LocalDateTime expired;
}
