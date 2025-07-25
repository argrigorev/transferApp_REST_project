package ru.netology.springBootDemo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.springBootDemo.dto.TransferRequest;
import ru.netology.springBootDemo.exception.InvalidInputDataException;
import ru.netology.springBootDemo.exception.TransferProcessingException;
import ru.netology.springBootDemo.model.Amount;
import ru.netology.springBootDemo.model.Card;
import ru.netology.springBootDemo.repository.CardRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TransferServiceTest {
    private CardRepository cardRepository;
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        cardRepository = mock(CardRepository.class);
        transferService = new TransferService(cardRepository);
    }

    @Test
    void createTransfer_successful() {
        var sender = new Card("1111222233334444", "12/25","123", 10_000);
        var receiver = new Card("5555666677778888", "11/26", "321", 5_000);

        when(cardRepository.getCard("1111222233334444")).thenReturn(sender);
        when(cardRepository.exists("5555666677778888")).thenReturn(true);

        var request = new TransferRequest(
                "1111222233334444",
                "123",
                "12/25",
                "5555666677778888",
                new Amount(1_000, "RUB")
        );

        String operationId = transferService.createTransfer(request);

        assertNotNull(operationId);
    }

    @Test
    void createTransfer_Throws_ifSenderNotFound() {
        when(cardRepository.getCard("1111222233334444")).thenReturn(null);

        var request = new TransferRequest(
                "1111222233334444",
                "123",
                "12/25",
                "5555666677778888",
                new Amount(1000, "RUB")
        );

        assertThrows(InvalidInputDataException.class, () -> transferService.createTransfer(request));
    }

    @Test
    void createTransfer_Throws_ifInvalidValidTill() {
        var sender = new Card("1111222233334444", "12/25", "123", 10_000);

        when(cardRepository.getCard("1111222233334444")).thenReturn(sender);

        var request = new TransferRequest(
                "1111222233334444",
                "123",
                "11/99",
                "5555666677778888",
                new Amount(1000, "RUB")
        );
        assertThrows(InvalidInputDataException.class, () -> transferService.createTransfer(request));
    }

    @Test
    void createTransfer_Throws_ifInvalidCvv() {
        var sender = new Card("1111222233334444", "12/25", "123", 10_000);

        when(cardRepository.getCard("1111222233334444")).thenReturn(sender);

        var request = new TransferRequest(
                "1111222233334444",
                "12/25",
                "000",
                "5555666677778888",
                new Amount(1000, "RUB")
        );
        assertThrows(InvalidInputDataException.class, () -> transferService.createTransfer(request));
    }

    @Test
    void createTransfer_Throws_ifReceiverNotFound() {
        var sender = new Card("1111222233334444", "12/25", "123", 10_000);

        when(cardRepository.getCard("1111222233334444")).thenReturn(sender);
        when(cardRepository.exists("5555666677778888")).thenReturn(false);

        var request = new TransferRequest(
                "1111222233334444",
                "123",
                "12/25",
                "5555666677778888",
                new Amount(1000, "RUB")
        );

        assertThrows(InvalidInputDataException.class, () -> transferService.createTransfer(request));
    }

    @Test
    void createTransfer_Throws_ifNotEnoughMoney() {
        var sender = new Card("1111222233334444", "12/25", "123", 500);

        when(cardRepository.getCard("1111222233334444")).thenReturn(sender);
        when(cardRepository.exists("5555666677778888")).thenReturn(true);

        var request = new TransferRequest(
                "1111222233334444",
                "123",
                "12/25",
                "5555666677778888",
                new Amount(10_000, "RUB")
        );

        assertThrows(InvalidInputDataException.class, () -> transferService.createTransfer(request));
    }

    @Test
    void confirmTransfer_successful() {
        var sender = new Card("1111222233334444", "12/25", "123", 10_000);
        var receiver = new Card("5555666677778888", "11/26", "456", 5_000);

        when(cardRepository.getCard("1111222233334444")).thenReturn(sender);
        when(cardRepository.getCard("5555666677778888")).thenReturn(receiver);
        when(cardRepository.exists("5555666677778888")).thenReturn(true);

        var request = new TransferRequest(
                "1111222233334444", "123", "12/25", "5555666677778888",
                new Amount(1_000, "RUB")
        );

        String operationId = transferService.createTransfer(request);

        transferService.confirmOperations(operationId);

        assert sender.getBalance() == 10_000 - 1_000 - 10;
        assert receiver.getBalance() == 5_000 + 1_000;
    }

    @Test
    void confirmOperations_Throws_ifOperationNotFound() {
        var invalidOperationId = "nonExistingId";

        var ex = assertThrows(InvalidInputDataException.class,
                () -> transferService.confirmOperations(invalidOperationId)
        );

        assert ex.getMessage().contains("Operation not found");
    }

    @Test
    void confirmOperations_Throws_ifRepositoryFails(){
        var sender = new Card("1111222233334444", "12/25", "123", 10_000);
        var receiver = new Card("5555666677778888", "11/26", "456", 5_000);

        when(cardRepository.getCard("1111222233334444")).thenReturn(sender);
        when(cardRepository.getCard("5555666677778888")).thenReturn(receiver);
        when(cardRepository.exists("5555666677778888")).thenReturn(true);
        doThrow(new RuntimeException("Error")).when(cardRepository).updateCard(receiver);

        var request = new TransferRequest(
                "1111222233334444", "123", "12/25", "5555666677778888",
                new Amount(1_000, "RUB")
        );

        String operationId = transferService.createTransfer(request);

        var ex = assertThrows(TransferProcessingException.class, () ->
                transferService.confirmOperations(operationId)
        );

        assert ex.getMessage().contains("Transfer failed");
    }
}
