package org.fmarin.admintournoi.subscription;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "level")
    private Integer level;
    @Column(name = "captain_name")
    private String captainName;
    @Column(name = "captain_email")
    private String captainEmail;
    @Column(name = "captain_phone")
    private String captainPhone;
    @Column(name = "player_2_name")
    private String player2Name;
    @Column(name = "player_2_email")
    private String player2Email;
    @Column(name = "player_3_name")
    private String player3Name;
    @Column(name = "player_3_email")
    private String player3Email;
    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "payment_transaction_id")
    private String paymentTransactionId;
    @Column(name = "payment_status")
    private String paymentStatus;
    @Column(name = "payment_processed_at")
    private LocalDateTime paymentProcessedAt;
    @Column(name = "payment_verification_code")
    private Integer paymentVerficationCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public String getCaptainEmail() {
        return captainEmail;
    }

    public void setCaptainEmail(String captainEmail) {
        this.captainEmail = captainEmail;
    }

    public String getCaptainPhone() {
        return captainPhone;
    }

    public void setCaptainPhone(String captainPhone) {
        this.captainPhone = captainPhone;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public String getPlayer2Email() {
        return player2Email;
    }

    public void setPlayer2Email(String player2Email) {
        this.player2Email = player2Email;
    }

    public String getPlayer3Name() {
        return player3Name;
    }

    public void setPlayer3Name(String player3Name) {
        this.player3Name = player3Name;
    }

    public String getPlayer3Email() {
        return player3Email;
    }

    public void setPlayer3Email(String player3Email) {
        this.player3Email = player3Email;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(String paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getPaymentProcessedAt() {
        return paymentProcessedAt;
    }

    public void setPaymentProcessedAt(LocalDateTime paymentProcessedAt) {
        this.paymentProcessedAt = paymentProcessedAt;
    }

    public Integer getPaymentVerficationCode() {
        return paymentVerficationCode;
    }

    public void setPaymentVerficationCode(Integer paymentVerficationCode) {
        this.paymentVerficationCode = paymentVerficationCode;
    }
}
