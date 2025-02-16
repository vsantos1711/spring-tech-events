package com.vsantos.springtechevents.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {
  @Value("${aws.region}")
  private String awsRegion;

  @Bean
  public AmazonS3 s3Client() {
    return AmazonS3ClientBuilder
        .standard()
        .withCredentials(new DefaultAWSCredentialsProviderChain())
        .withRegion(awsRegion)
        .build();
  }
}