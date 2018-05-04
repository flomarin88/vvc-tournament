package org.fmarin.admintournoi.subscription;

import org.fmarin.admintournoi.payment.PaymentStatus;

import java.time.LocalDateTime;

public final class TeamBuilder {
    private Long id;
    private String name;
    private Level level;
    private String captainName;
    private String captainEmail;
    private String captainPhone;
    private String player2Name;
    private String player2Email;
    private String player3Name;
    private String player3Email;
    private Tournament tournament;
    private LocalDateTime createdAt;
    private String paymentTransactionId;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentProcessedAt;
    private Integer paymentVerificationCode;
    private boolean present;

    private TeamBuilder() {
    }

    public static TeamBuilder aTeam() {
        return new TeamBuilder();
    }

    public TeamBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TeamBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TeamBuilder withLevel(Level level) {
        this.level = level;
        return this;
    }

    public TeamBuilder withCaptainName(String captainName) {
        this.captainName = captainName;
        return this;
    }

    public TeamBuilder withCaptainEmail(String captainEmail) {
        this.captainEmail = captainEmail;
        return this;
    }

    public TeamBuilder withCaptainPhone(String captainPhone) {
        this.captainPhone = captainPhone;
        return this;
    }

    public TeamBuilder withPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
        return this;
    }

    public TeamBuilder withPlayer2Email(String player2Email) {
        this.player2Email = player2Email;
        return this;
    }

    public TeamBuilder withPlayer3Name(String player3Name) {
        this.player3Name = player3Name;
        return this;
    }

    public TeamBuilder withPlayer3Email(String player3Email) {
        this.player3Email = player3Email;
        return this;
    }

    public TeamBuilder withTournament(Tournament tournament) {
        this.tournament = tournament;
        return this;
    }

    public TeamBuilder withCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public TeamBuilder withPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
        return this;
    }

    public TeamBuilder withPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
        return this;
    }

    public TeamBuilder withPaymentProcessedAt(LocalDateTime paymentProcessedAt) {
        this.paymentProcessedAt = paymentProcessedAt;
        return this;
    }

    public TeamBuilder withPaymentVerificationCode(Integer paymentVerificationCode) {
        this.paymentVerificationCode = paymentVerificationCode;
        return this;
    }

    public TeamBuilder withPresent(boolean present) {
        this.present = present;
        return this;
    }

    public Team build() {
        Team team = new Team();
        team.setId(id);
        team.setName(name);
        team.setLevel(level);
        team.setCaptainName(captainName);
        team.setCaptainEmail(captainEmail);
        team.setCaptainPhone(captainPhone);
        team.setPlayer2Name(player2Name);
        team.setPlayer2Email(player2Email);
        team.setPlayer3Name(player3Name);
        team.setPlayer3Email(player3Email);
        team.setTournament(tournament);
        team.setCreatedAt(createdAt);
        team.setPaymentTransactionId(paymentTransactionId);
        team.setPaymentStatus(paymentStatus);
        team.setPaymentProcessedAt(paymentProcessedAt);
        team.setPaymentVerificationCode(paymentVerificationCode);
        team.setPresent(present);
        return team;
    }
}
