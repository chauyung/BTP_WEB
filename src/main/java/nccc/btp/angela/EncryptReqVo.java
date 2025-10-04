package nccc.btp.angela;

import java.io.Serializable;

public class EncryptReqVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2069490302618805965L;

	/**
	 * sample request json
		{
		"ssoid": "{{SSOID}}",
		"keyName": "FISC",
		"encryptMode": 2,
		"dataFormat": "hex",
		"data": "7f52a7ab38c7c6a6c33cc5358f7501e1edb1cd69bcb3bb2a",
		"responseFormat": "hex"
		}
	 */

	public EncryptReqVo() {

	}

	private String ssoid;
	private String keyName;
	private int encryptMode;
	private String dataFormat;
	private String data;
	private String responseFormat;

	public String getSsoid() {
		return ssoid;
	}

	public void setSsoid(String ssoid) {
		this.ssoid = ssoid;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public int getEncryptMode() {
		return encryptMode;
	}

	public void setEncryptMode(int encryptMode) {
		this.encryptMode = encryptMode;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getResponseFormat() {
		return responseFormat;
	}

	public void setResponseFormat(String responseFormat) {
		this.responseFormat = responseFormat;
	}


}
