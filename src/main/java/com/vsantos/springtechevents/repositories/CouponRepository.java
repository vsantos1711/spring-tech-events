package com.vsantos.springtechevents.repositories;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vsantos.springtechevents.domain.coupon.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {

  List<Coupon> findByEventIdAndValidAfter(UUID eventId, OffsetDateTime currentDate);
}
