package nccc.btp.angela;

import java.io.Serializable;

public class AngelaVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9104062171323891857L;
	private String ssoId;
	private String keyName;
	private String opMode;
	private String argId;
	private String hexData;

	public String getSsoId() {
		return ssoId;
	}

	public void setSsoId(String ssoId) {
		this.ssoId = ssoId;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getOpMode() {
		return opMode;
	}

	public void setOpMode(String opMode) {
		this.opMode = opMode;
	}

	public String getHexData() {
		return hexData;
	}

	public void setHexData(String hexData) {
		this.hexData = hexData;
	}

	public String getArgId() {
		return argId;
	}

	public void setArgId(String argId) {
		this.argId = argId;
	}

}
