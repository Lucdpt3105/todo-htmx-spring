package com.atoolz.htmx.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(ResponseStatusException.class)
  public String handleResponseStatus(ResponseStatusException ex, Model model) {
    int status = ex.getStatusCode().value();
    log.warn("HTTP {} — {}", status, ex.getReason());
    model.addAttribute("status", status);
    model.addAttribute("error", resolveErrorTitle(status));
    model.addAttribute("message", ex.getReason() != null ? ex.getReason() : ex.getMessage());
    return "error";
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String handleGeneral(Exception ex, Model model) {
    log.error("Unexpected error: {}", ex.getMessage(), ex);
    model.addAttribute("status", 500);
    model.addAttribute("error", "Internal Server Error");
    model.addAttribute("message", "Something went wrong on our end. Please try again later.");
    return "error";
  }

  private String resolveErrorTitle(int status) {
    return switch (status) {
      case 400 -> "Bad Request";
      case 404 -> "Not Found";
      case 405 -> "Method Not Allowed";
      case 409 -> "Conflict";
      case 422 -> "Unprocessable Entity";
      case 503 -> "Service Unavailable";
      default -> "Error " + status;
    };
  }
}
