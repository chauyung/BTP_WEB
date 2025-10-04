package nccc.btp.aplog.mask;

import org.apache.commons.lang3.StringUtils;

public class FirstCharMask implements IApLogMask {

	@Override
	public String mask(String data) {
		if(StringUtils.isBlank(data)) {
			return "";
		}
		if (data.length() == 1) {
			return "*";
		}
		String v = data.substring(0,1);
		return StringUtils.rightPad(v, data.length() - 1,"*");
	}

}
