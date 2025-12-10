package com.example.battleshipsonline.controller.dto.response;

public record FireResponse(String fireResultType, String sunkShipName, String message, String winnerUsername) {
}
