package org.fmarin.admintournoi.admin.round;

public class RoundListView {

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getBranch() {
    return branch;
  }

  public void setBranch(String branch) {
    this.branch = branch;
  }

  public String getBranchColor() {
    return branchColor;
  }

  public void setBranchColor(String branchColor) {
    this.branchColor = branchColor;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTypeLabel() {
    return typeLabel;
  }

  public void setTypeLabel(String typeLabel) {
    this.typeLabel = typeLabel;
  }

  public String getTypeValue() {
    return typeValue;
  }

  public void setTypeValue(String typeValue) {
    this.typeValue = typeValue;
  }

  public String getFields() {
    return fields;
  }

  public void setFields(String fields) {
    this.fields = fields;
  }

  public String getTypeLast() {
    return typeLast;
  }

  public void setTypeLast(String typeLast) {
    this.typeLast = typeLast;
  }

  public String getFieldsLast() {
    return fieldsLast;
  }

  public void setFieldsLast(String fieldsLast) {
    this.fieldsLast = fieldsLast;
  }

  public String getTeams() {
    return teams;
  }

  public void setTeams(String teams) {
    this.teams = teams;
  }
}
