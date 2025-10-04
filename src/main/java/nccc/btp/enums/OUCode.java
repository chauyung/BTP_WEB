package nccc.btp.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OUCode {
  F100("F100"),
  F101("F101"),
  F200("F200"),
  F403("F403"),
  F404("F404"),
  ;

  private final String description;

  OUCode(String description) {
    this.description = description;
  }

  @JsonValue
  public String getDescription() {
    return description;
  }

}
