package nccc.btp.dto;

public class CategoryOptionDto {
	
    private String value; // 品號
    private String label; // 品名

    public CategoryOptionDto() {}
    public CategoryOptionDto(String value, String label) {
        this.value = value;
        this.label = label;
    }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}
