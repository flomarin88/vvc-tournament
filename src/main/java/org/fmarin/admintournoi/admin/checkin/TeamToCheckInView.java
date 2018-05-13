package org.fmarin.admintournoi.admin.checkin;

public class TeamToCheckInView {

    private final Long id;
    private final String name;
    private final String levelLabel;
    private final String captainName;
    private final String captainEmail;
    private final String captainPhone;
    private final Integer paymentVerificationCode;
    private final boolean toPay;
    private final boolean isPresent;

    public TeamToCheckInView(Long id, String name, String levelLabel, String captainName,
                             String captainEmail, String captainPhone, Integer paymentVerificationCode, boolean toPay, boolean isPresent) {
        this.id = id;
        this.name = name;
        this.levelLabel = levelLabel;
        this.captainName = captainName;
        this.captainEmail = captainEmail;
        this.captainPhone = captainPhone;
        this.paymentVerificationCode = paymentVerificationCode;
        this.toPay = toPay;
        this.isPresent = isPresent;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLevelLabel() {
        return levelLabel;
    }

    public String getCaptainName() {
        return captainName;
    }

    public String getCaptainEmail() {
        return captainEmail;
    }

    public String getCaptainPhone() {
        return captainPhone;
    }

    public Integer getPaymentVerificationCode() {
        return paymentVerificationCode;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public boolean isToPay() {
        return toPay;
    }
}
