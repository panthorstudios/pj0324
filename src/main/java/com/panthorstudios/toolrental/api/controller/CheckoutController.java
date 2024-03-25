package com.panthorstudios.toolrental.api.controller;

import com.panthorstudios.toolrental.api.domain.RentalAgreement;
import com.panthorstudios.toolrental.api.service.CheckoutService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }
    @GetMapping("/checkout")
    public ResponseEntity<RentalAgreement> toolRentalCheckoutGetHandler(
        @RequestParam String toolCode,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkoutDate,
        @RequestParam int rentalDays,
        @RequestParam int discountPercent) {

        RentalAgreement rentalAgreement = checkoutService.toolRentalCheckout(toolCode, checkoutDate, rentalDays, discountPercent);
        return ResponseEntity.ok(rentalAgreement);
    }
}
