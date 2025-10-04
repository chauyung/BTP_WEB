package nccc.btp.enums;

import com.fasterxml.jackson.annotation.JsonValue;

// 用來控管前端頁面顯示
public enum Mode {
  INITMODE("initMode"), // 初始化模式
  ADDMODE("addMode"), // 新增模式
  EDITMODE("editMode"), // 編輯模式
  VIEWMODE("viewMode"), // 查看模式
  DELETEMODE("DeleteMode") // 刪除模式
  ;

  private final String description;

  Mode(String description) {
    this.description = description;
  }

  @JsonValue
  public String getDescription() {
    return description;
  }

  public boolean isInitMode() {
    return this == INITMODE;
  }

  public boolean isAddMode() {
    return this == ADDMODE;
  }

  public boolean isEditMode() {
    return this == EDITMODE;
  }

  public boolean isViewMode() {
    return this == VIEWMODE;
  }

  public boolean isDeleteMode() {
    return this == DELETEMODE;
  }

}
