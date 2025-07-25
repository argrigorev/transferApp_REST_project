package ru.netology.springBootDemo.service;

import org.springframework.stereotype.Service;
import ru.netology.springBootDemo.dto.TransferRequest;
import ru.netology.springBootDemo.exception.InvalidInputDataException;
import ru.netology.springBootDemo.exception.TransferProcessingException;
import ru.netology.springBootDemo.logger.TransferLogger;
import ru.netology.springBootDemo.model.Card;
import ru.netology.springBootDemo.repository.CardRepository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TransferService {
    private final CardRepository cardRepository;
    private final Map<String, TransferRequest> operations = new ConcurrentHashMap<>();
    private final TransferLogger logger =  new TransferLogger();
    private final double FEE_PERCENTAGE = 0.01;

    public TransferService(CardRepository repository) {
        this.cardRepository = repository;
    }

    public String createTransfer(TransferRequest request) {
        Card sender = cardRepository.getCard(request.getCardFromNumber());
        if (sender == null) {
            logger.log(request.getCardFromNumber(), request.getCardToNumber(),
                    request.getAmount().getValue(), 0, "FAILED: Sender card not found");
            throw new InvalidInputDataException("Sender card not found");
        }

        if (!sender.getValidTill().equals(request.getCardFromValidTill())) {
            logger.log(request.getCardFromNumber(), request.getCardToNumber(),
                    request.getAmount().getValue(), 0, "FAILED: Invalid valid till date");
            throw new InvalidInputDataException("Invalid valid till date");

        }

        if (!sender.getCvv().equals(request.getCardFromCvv())) {
            logger.log(request.getCardFromNumber(), request.getCardToNumber(),
                    request.getAmount().getValue(), 0, "FAILED: Invalid CVV code");
            throw new InvalidInputDataException("invalid CVV code");
        }

        if (!cardRepository.exists(request.getCardToNumber())) {
            logger.log(request.getCardFromNumber(), request.getCardToNumber(),
                    request.getAmount().getValue(), 0, "FAILED: Receiver card not found");
            throw new InvalidInputDataException("Receiver card not found");
        }

        int amount = request.getAmount().getValue();
        int fee = calculateFee(amount);
        int totalAmount = amount + fee;

        if (sender.getBalance() < totalAmount) {
            logger.log(request.getCardFromNumber(), request.getCardToNumber(),
                    amount, fee, "FAILED: Not enough money");
            throw new InvalidInputDataException("Not enough money on sender's card");
        }

        String operationId = UUID.randomUUID().toString();
        operations.put(operationId, request);
        logger.log(request.getCardFromNumber(), request.getCardToNumber(),
                amount, fee, "OPERATION CREATED");

        return operationId;
    }

    public void confirmOperations(String operationId) {
        TransferRequest request = operations.get(operationId);
        if (request == null) {
            logger.log("UNKNOWN", "UNKNOWN",
                    0, 0, "FAILED: Operation not found");
            throw new InvalidInputDataException("Operation not found");
        }

        try {
            Card sender = cardRepository.getCard(request.getCardFromNumber());
            Card receiver = cardRepository.getCard(request.getCardToNumber());

            int amount = request.getAmount().getValue();
            int fee = calculateFee(amount);

            sender.setBalance(sender.getBalance() - amount - fee);
            receiver.setBalance((receiver.getBalance()) + amount);

            cardRepository.updateCard(sender);
            cardRepository.updateCard(receiver);

            operations.remove(operationId);
            logger.log(request.getCardFromNumber(), request.getCardToNumber(),
                    amount, fee, "OPERATION SUCCESS");
        } catch (Exception e) {
            logger.log(request.getCardFromNumber(), request.getCardToNumber(),
                    request.getAmount().getValue(), 0, "FAILED: " + e.getMessage());
            throw new TransferProcessingException("Transfer failed: " + e.getMessage());
        }
    }

    private int calculateFee(int amount) {
        return (int) (amount * FEE_PERCENTAGE);
    }
}
