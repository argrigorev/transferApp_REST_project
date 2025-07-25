package ru.netology.springBootDemo.repository;

import org.springframework.stereotype.Repository;
import ru.netology.springBootDemo.model.Card;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CardRepository {
    private final Map<String, Card> cards = new HashMap<>();

    public CardRepository() {
        cards.put("1111222233334444", new Card("1111222233334444", "12/25",
                "123", 10_000));
        cards.put("5555666677778888", new Card("5555666677778888", "11/26",
                "456", 50_000));
    }

    public Card getCard(String number){
        return cards.get(number);
    }

    public void updateCard(Card card){
        cards.put(card.getNumber(), card);
    }

    public boolean exists(String number) {
        return cards.containsKey(number);
    }
}
