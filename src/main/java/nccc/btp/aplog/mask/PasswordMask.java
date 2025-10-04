package nccc.btp.aplog.mask;

public class PasswordMask implements IApLogMask {

	@Override
	public String mask(String data) {
		return "********";
	}

}
