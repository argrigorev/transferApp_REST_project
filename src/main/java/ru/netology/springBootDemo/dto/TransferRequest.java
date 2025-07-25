package ru.netology.springBootDemo.dto;

import ru.netology.springBootDemo.model.Amount;

public class TransferRequest {
    private String cardFromNumber;
    private String cardFromCVV;
    private String cardFromValidTill;
    private String cardToNumber;
    private Amount amount;

    public TransferRequest(String cardFromNumber, String cardFromCVV, String cardFromValidTill, String cardToNumber, Amount amount) {
        this.cardFromNumber = cardFromNumber;
        this.cardFromCVV = cardFromCVV;
        this.cardFromValidTill = cardFromValidTill;
        this.cardToNumber = cardToNumber;
        this.amount = amount;
    }

    public String getCardFromNumber() {
        return cardFromNumber;
    }

    public void setCardFromNumber(String cardFromNumber) {
        this.cardFromNumber = cardFromNumber;
    }

    public String getCardFromCvv() {
        return cardFromCVV;
    }

    public void setCardFromCvv(String getCardFromCvv) {
        this.cardFromCVV = getCardFromCvv;
    }

    public String getCardFromValidTill() {
        return cardFromValidTill;
    }

    public void setCardFromValidTill(String cardFromValidTill) {
        this.cardFromValidTill = cardFromValidTill;
    }

    public String getCardToNumber() {
        return cardToNumber;
    }

    public void setCardToNumber(String cardToNumber) {
        this.cardToNumber = cardToNumber;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }
}
