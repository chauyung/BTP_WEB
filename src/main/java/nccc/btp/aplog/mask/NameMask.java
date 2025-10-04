package nccc.btp.aplog.mask;

import org.apache.commons.lang3.StringUtils;

public class NameMask implements IApLogMask {

	@Override
	public String mask(String data) {
		String maskChar = "*";
		if (data.length() != data.getBytes().length) {
			maskChar = "Ｏ";
		}
		if(StringUtils.isBlank(data)) {
			return "";
		}
		String v = data.substring(0,1);
		if (data.length() == 2) {
			return v + maskChar;
		}
		return StringUtils.rightPad(v, data.length() - 1 , maskChar) + data.substring(data.length() - 1);
	}

}
