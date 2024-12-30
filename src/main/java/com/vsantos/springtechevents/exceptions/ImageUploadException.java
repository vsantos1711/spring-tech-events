package com.vsantos.springtechevents.exceptions;

public class ImageUploadException extends Exception {
  public ImageUploadException(Throwable cause) {
    super("Error uploading image to S3", cause);
  }
}
