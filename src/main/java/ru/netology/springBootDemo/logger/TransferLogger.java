package ru.netology.springBootDemo.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransferLogger {
    private static final String LOG_FILE = "transfers.log";

    public synchronized void log(String fromCard, String toCard, int amount, int fee, String result) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))){
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            writer.printf("%s | From: %s | To: %s | Amount: %d | Fee: %d | Result: %s%n",
                    timestamp, fromCard, toCard, amount, fee, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
