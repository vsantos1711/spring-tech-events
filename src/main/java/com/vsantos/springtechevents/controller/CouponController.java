package com.vsantos.springtechevents.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vsantos.springtechevents.domain.coupon.CouponRequestDTO;
import com.vsantos.springtechevents.domain.coupon.CouponResponseDTO;
import com.vsantos.springtechevents.services.CouponService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/coupon")
public class CouponController {

  private final CouponService couponService;

  @Autowired
  public CouponController(CouponService couponService) {
    this.couponService = couponService;
  }

  @PostMapping("/event/{eventId}")
  @Operation(summary = "Create a coupon for an event")
  public ResponseEntity<CouponResponseDTO> createCoupon(
      @PathVariable UUID eventId,
      @RequestBody @Valid CouponRequestDTO couponDTO) {
    CouponResponseDTO coupon = couponService.createCoupon(eventId, couponDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(coupon);
  }
}
