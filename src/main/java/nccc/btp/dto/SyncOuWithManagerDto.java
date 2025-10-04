package nccc.btp.dto;

/**
 * 包含 SyncOU 欄位 + SyncUser 部分欄位（用於 native query 的 interface-based projection）
 */
public interface SyncOuWithManagerDto {

  String getOuCode();

  String getOuName();

  String getOuLevel();

  String getParentOuCode();

  Integer getOrderIndex();

  String getMgrAccount();

  String getDisplayName();

  String getCostCenter();

  String getEmail();

  String getHrid();
}
