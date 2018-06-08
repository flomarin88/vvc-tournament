package org.fmarin.admintournoi.admin.round;

public final class RoundListViewBuilder {
  private Long id;
  private String branch;
  private String branchColor;
  private String name;
  private String status;
  private String typeLabel;
  private String typeValue;
  private String fields;
  private String typeLast;
  private String fieldsLast;
  private String teams;

  private RoundListViewBuilder() {
  }

  public static RoundListViewBuilder aRoundListView() {
    return new RoundListViewBuilder();
  }

  public RoundListViewBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public RoundListViewBuilder withBranch(String branch) {
    this.branch = branch;
    return this;
  }

  public RoundListViewBuilder withBranchColor(String branchColor) {
    this.branchColor = branchColor;
    return this;
  }

  public RoundListViewBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public RoundListViewBuilder withStatus(String status) {
    this.status = status;
    return this;
  }

  public RoundListViewBuilder withTypeLabel(String typeLabel) {
    this.typeLabel = typeLabel;
    return this;
  }

  public RoundListViewBuilder withTypeValue(String typeValue) {
    this.typeValue = typeValue;
    return this;
  }

  public RoundListViewBuilder withFields(String fields) {
    this.fields = fields;
    return this;
  }

  public RoundListViewBuilder withTypeLast(String typeLast) {
    this.typeLast = typeLast;
    return this;
  }

  public RoundListViewBuilder withFieldsLast(String fieldsLast) {
    this.fieldsLast = fieldsLast;
    return this;
  }

  public RoundListViewBuilder withTeams(String teams) {
    this.teams = teams;
    return this;
  }

  public RoundListView build() {
    RoundListView roundListView = new RoundListView();
    roundListView.setId(id);
    roundListView.setBranch(branch);
    roundListView.setBranchColor(branchColor);
    roundListView.setName(name);
    roundListView.setStatus(status);
    roundListView.setTypeLabel(typeLabel);
    roundListView.setTypeValue(typeValue);
    roundListView.setFields(fields);
    roundListView.setTypeLast(typeLast);
    roundListView.setFieldsLast(fieldsLast);
    roundListView.setTeams(teams);
    return roundListView;
  }
}
