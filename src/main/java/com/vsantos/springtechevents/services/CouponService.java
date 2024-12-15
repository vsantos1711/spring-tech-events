package com.vsantos.springtechevents.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vsantos.springtechevents.domain.coupon.Coupon;
import com.vsantos.springtechevents.domain.coupon.CouponRequestDTO;
import com.vsantos.springtechevents.domain.event.Event;
import com.vsantos.springtechevents.repositories.CouponRepository;
import com.vsantos.springtechevents.repositories.EventRepository;

@Service
public class CouponService {
  private final CouponRepository couponRepository;
  private final EventRepository eventRepository;

  @Autowired
  public CouponService(CouponRepository couponRepository, EventRepository eventRepository) {
    this.couponRepository = couponRepository;
    this.eventRepository = eventRepository;
  }

  public Coupon createCoupon(UUID eventId, CouponRequestDTO couponDTO) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new IllegalArgumentException("Event not found"));

    Coupon coupon = Coupon.builder()
        .code(couponDTO.code())
        .discount(couponDTO.discount())
        .expirationDate(couponDTO.expirationDate())
        .event(event)
        .build();

    return couponRepository.save(coupon);
  }
}
