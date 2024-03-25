package com.panthorstudios.toolrental.api.controller;

import com.panthorstudios.toolrental.api.domain.RentalAgreement;
import com.panthorstudios.toolrental.api.service.CheckoutService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class CheckoutController {

    private final CheckoutService checkoutService;

    protected record RentalAgreementRequest(String toolCode, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate checkoutDate, int rentalDays, int discountPercent){};

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }
    @PostMapping("/checkout")
    public ResponseEntity<RentalAgreement> toolRentalCheckoutGetHandler(@RequestBody RentalAgreementRequest rentalAgreementRequest) {
        RentalAgreement rentalAgreement = checkoutService.toolRentalCheckout(rentalAgreementRequest.toolCode(),
                rentalAgreementRequest.checkoutDate(),
                rentalAgreementRequest.rentalDays(),
                rentalAgreementRequest.discountPercent());

        return ResponseEntity.ok(rentalAgreement);
    }
}
