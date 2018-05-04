package org.fmarin.admintournoi.payment;

import java.util.Arrays;
import java.util.Optional;

public enum PaymentStatus {

  PENDING("Pending"),
  COMPLETED("Completed"),
  ABANDONNED("Abandonned");

  private String label;

  PaymentStatus(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public static PaymentStatus labelOf(String label) {
    Optional<PaymentStatus> status = Arrays.stream(PaymentStatus.values()).filter(i -> label.equals(i.label)).findAny();
    return status.get();
  }
}
