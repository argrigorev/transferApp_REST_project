package ru.netology.springBootDemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.springBootDemo.dto.ConfirmRequest;
import ru.netology.springBootDemo.dto.TransferRequest;
import ru.netology.springBootDemo.service.TransferService;

import java.util.Map;

@RestController
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<Map<String, String>> transfer(@RequestBody TransferRequest request){
        String operationId = transferService.createTransfer(request);
        return ResponseEntity.ok(Map.of("operationId", operationId));
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<Map<String, String>> confirm(@RequestBody ConfirmRequest request){
        transferService.confirmOperations(request.getOperationId());
        return ResponseEntity.ok(Map.of("operationId", request.getOperationId()));
    }
}
