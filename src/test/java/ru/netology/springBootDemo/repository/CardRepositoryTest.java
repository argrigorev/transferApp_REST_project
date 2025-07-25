package ru.netology.springBootDemo.repository;

import org.junit.jupiter.api.Test;
import ru.netology.springBootDemo.model.Card;

import static org.junit.jupiter.api.Assertions.*;

public class CardRepositoryTest {

    @Test
    void getCard_returnsExistingCard() {
        CardRepository repository = new CardRepository();
        Card card = repository.getCard("1111222233334444");
        assertNotNull(card);
        assertEquals("12/25", card.getValidTill());
    };

    @Test
    void existingCard_existsReturnsTrue() {
        CardRepository repository = new CardRepository();
        assertTrue(repository.exists("5555666677778888"));
    }

    @Test
    void updateCard_updatesBalance(){
        CardRepository repository = new CardRepository();
        Card card = repository.getCard("1111222233334444");
        card.setBalance(5000);
        repository.updateCard(card);

        Card updated = repository.getCard("1111222233334444");
        assertEquals(5000, updated.getBalance());
    }
}
