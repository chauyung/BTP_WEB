package nccc.btp.aplog.mask;

public class DefaultApLogMask implements IApLogMask {

	@Override
	public String mask(String data) {
		StringBuffer sb = new StringBuffer(data);
		if(sb.length() > 15) {
			StringBuffer temp = new StringBuffer();
			for(int i = 0; i< data.length() - 10;i++) {
				temp.append("*");
			}
			sb.replace(6, sb.length() - 4, temp.toString());
		} else if(sb.length() > 8) {
			StringBuffer temp = new StringBuffer();
			for(int i = 0; i< data.length() - 4;i++) {
				temp.append("*");
			}
			sb.replace(3, sb.length() - 1, temp.toString());
		} else if(sb.length() > 3) {
			StringBuffer temp = new StringBuffer();
			for(int i = 0; i< data.length() - 2;i++) {
				temp.append("*");
			}
			sb.replace(1, sb.length() - 1, temp.toString());
		} else {
			StringBuffer temp = new StringBuffer();
			for(int i = 0; i< data.length();i++) {
				temp.append("*");
			}
			sb = temp;
		}
		return sb.toString();
	}

}
