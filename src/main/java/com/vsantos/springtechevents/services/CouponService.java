package com.vsantos.springtechevents.services;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vsantos.springtechevents.domain.coupon.Coupon;
import com.vsantos.springtechevents.domain.coupon.CouponRequestDTO;
import com.vsantos.springtechevents.domain.coupon.CouponResponseDTO;
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

  public List<Coupon> consultCoupons(UUID eventId, OffsetDateTime currentDate) {
    return couponRepository.findByEventIdAndValidAfter(eventId, currentDate);
  }

  public CouponResponseDTO createCoupon(UUID eventId, CouponRequestDTO couponDTO) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new IllegalArgumentException("Event not found"));

    Coupon coupon = Coupon.builder()
        .code(couponDTO.code())
        .discount(couponDTO.discount())
        .valid(couponDTO.valid())
        .event(event)
        .build();

    couponRepository.save(coupon);

    return CouponResponseDTO.builder()
        .code(coupon.getCode())
        .discount(coupon.getDiscount())
        .valid(coupon.getValid())
        .eventId(eventId)
        .build();
  }
}
