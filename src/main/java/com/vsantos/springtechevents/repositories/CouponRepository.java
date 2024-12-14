package com.vsantos.springtechevents.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vsantos.springtechevents.domain.coupon.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {

}
