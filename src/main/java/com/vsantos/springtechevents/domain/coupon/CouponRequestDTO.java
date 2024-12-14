package com.vsantos.springtechevents.domain.coupon;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public record CouponRequestDTO(
        String code,
        Integer discount,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date expirationDate) {

}