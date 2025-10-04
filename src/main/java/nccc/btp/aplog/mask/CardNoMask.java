package nccc.btp.aplog.mask;

import org.apache.commons.lang3.StringUtils;

public class CardNoMask implements IApLogMask {
	
	/**
	 * 遮罩卡號, 僅留前6後4碼為名碼, 中間6碼顯示 *.
	 * 
	 * @param cardNo
	 * @return
	 */
	@Override
	public String mask(String data) {
		if (StringUtils.isNotBlank(data)) {
			StringBuffer sb = new StringBuffer();
			if (data.length() >= 6) {
				sb.append(data.substring(0, 6));
				sb.append("******");
				if (data.length() > 12) {
					sb.append(data.substring(12));
				}
			} else 
				sb.append(data);
			
			return sb.toString();
		}
		
		return data;
	}
	
}
