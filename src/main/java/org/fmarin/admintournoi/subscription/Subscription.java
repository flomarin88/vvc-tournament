package org.fmarin.admintournoi.subscription;

public class Subscription {

    private Long tournamentId;
    private String name;
    private int level;
    private String captainName;
    private String captainEmail;
    private String captainPhone;
    private String player2Name;
    private String player2Email;
    private String player3Name;
    private String player3Email;

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
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
}
