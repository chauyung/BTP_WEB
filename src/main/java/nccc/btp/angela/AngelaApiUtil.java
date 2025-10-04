package nccc.btp.angela;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AngelaApiUtil {

	private SSLContext ctx = null;
	private int readTimeout = 5000;
	private int connectTimeOut = 5000;
	private int timeoutRetryTimes = 15;

	private String angelaUrl;
	private String ssoId;
	private String keyName;
	private boolean enableSSl = false;

	public AngelaApiUtil(String angelaUrl, String ssoid, String keyName)
			throws Exception, NoSuchAlgorithmException, KeyManagementException {
		this.angelaUrl = angelaUrl;
		this.ssoId = ssoid;
		this.keyName = keyName;
		intInternetEnv(angelaUrl);
	}

	private void intInternetEnv(String angelaUrl) throws Exception {
		if (angelaUrl == null || angelaUrl.trim().length() <= 0) {
			throw new Exception("init error!!");
		} else {
			if (angelaUrl.toLowerCase().startsWith("https")) {
				enableSSl = true;
				try {
					ctx = SSLContext.getInstance("TLSv1.2");
					ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
					SSLContext.setDefault(ctx);
					HttpsURLConnection.setDefaultSSLSocketFactory(ctx != null ? ctx.getSocketFactory() : null);
					HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
						@Override
						public boolean verify(String hostname, SSLSession session) {
							return true;
						}
					});
				} catch (NoSuchAlgorithmException e) {
					throw e;
				} catch (KeyManagementException e) {
					throw e;
				} catch (Exception e) {
					throw e;
				}
			}
		}
	}

	/**
	 * use default sso id and key label for NCCC String encrypt
	 * 
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public String doNcccStrEncrypt(String source) throws Exception {
		return doNcccStrEncrypt(source, ssoId, keyName);
	}

	/**
	 * use default sso id and user define key label for NCCC String encrypt
	 * 
	 * @param source
	 * @param label
	 * @return
	 * @throws Exception
	 */
	public String doNcccStrEncrypt(String source, String label) throws Exception {
		return doNcccStrEncrypt(source, ssoId, label);
	}

	/**
	 * use user define sso id and key label for NCCC String encrypt
	 * 
	 * @param source
	 * @param ssoId
	 * @param label
	 * @return
	 * @throws Exception
	 */
	public String doNcccStrEncrypt(String source, String ssoId, String label) throws Exception {
		if (source == null || source.trim().length() <= 0) {
			throw new Exception("no source data!");
		}
		if (ssoId == null || ssoId.trim().length() <= 0) {
			throw new Exception("no ssoId info!");
		}
		if (label == null || label.trim().length() <= 0) {
			throw new Exception("no keyname info!");
		}
		byte[] sourceByteArr = source.getBytes("big5");
		String hexStr = getHexString(sourceByteArr);
		AngelaVo vo = new AngelaVo();
		vo.setHexData(hexStr);
		vo.setOpMode("encrypt");
		// vo.setArgId("3001");
		// -- M2023089_R112153_PCI DSS4.0檔案及字串加解密修改
		vo.setArgId("4000");
		vo.setKeyName(label);
		vo.setSsoId(ssoId);
		String rtnValueHex = request(vo);
		return rtnValueHex;
	}

	/**
	 * use default sso id and key label for NCCC String decrypt.
	 * 
	 * @param enCodeHexStr
	 * @return
	 * @throws Exception
	 */
	public String doNcccStrDecrypt(String enCodeHexStr) throws Exception {
		return doNcccStrDecrypt(enCodeHexStr, ssoId, keyName);
	}

	/**
	 * use default sso id and user define key label for NCCC String decrypt.
	 * 
	 * @param enCodeHexStr
	 * @param label
	 * @return
	 * @throws Exception
	 */
	public String doNcccStrDecrypt(String enCodeHexStr, String label) throws Exception {
		return doNcccStrDecrypt(enCodeHexStr, ssoId, label);
	}

	/**
	 * use user define sso id and key label for NCCC String decrypt.
	 * 
	 * @param enCodeHexStr
	 * @param ssoId
	 * @param label
	 * @return
	 * @throws Exception
	 */
	public String doNcccStrDecrypt(String enCodeHexStr, String ssoId, String label) throws Exception {
		if (enCodeHexStr == null || enCodeHexStr.trim().length() <= 0) {
			throw new Exception("no enCode Hex String data!");
		}
		if (ssoId == null || ssoId.trim().length() <= 0) {
			throw new Exception("no ssoId info!");
		}
		if (label == null || label.trim().length() <= 0) {
			throw new Exception("no keyname info!");
		}
		byte[] enCodeByte = hexStringToByteArray(enCodeHexStr);
		String hexStr = getHexString(enCodeByte);
		AngelaVo vo = new AngelaVo();
		vo.setHexData(hexStr);
		vo.setOpMode("decrypt");
		// vo.setArgId("3001");
		// -- M2023089_R112153_PCI DSS4.0檔案及字串加解密修改
		vo.setArgId("4000");
		vo.setKeyName(label);
		vo.setSsoId(ssoId);
		String rtnValue = request(vo);
		byte[] rtnValueByteArr = hexDecode(rtnValue);
		return new String(rtnValueByteArr);
	}

	/**
	 * request HSM .
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public String request(AngelaVo vo) throws Exception {
		URL url = null;
		String returnStr = null;
		if (vo.getOpMode() == null || vo.getOpMode().trim().length() <= 0) {
			throw new Exception("setting error!! [op mode not setting.]");
		} else if ("encrypt".equals(vo.getOpMode())) {
//			url = new URL(new StringBuffer(angelaUrl ).append( "/v1/encrypt").toString());
			url = new URL(angelaUrl + "/v1/encrypt");
		} else if ("decrypt".equals(vo.getOpMode())) {
//			url = new URL(new StringBuffer(angelaUrl ).append( "/v1/decrypt").toString());
			url = new URL(angelaUrl + "/v1/decrypt");
		} else {
			throw new Exception("setting error!! [op mode only decrypt or encrypt.]");
		}
		int errorTimes = 0;
		for (int i = 0; i < timeoutRetryTimes; i++) {
			try {
				if (enableSSl) {
					returnStr = requestHttps(vo, url);
					break;
				} else {
					throw new Exception("not support http protocol");
				}
			} catch (AngelaNotAvailableException ae) {
				errorTimes++;
				if (errorTimes > timeoutRetryTimes) {
					throw new Exception("Angela Not Available!!(14006)");
				}
				log.warn("Angela Not Available!! try again...[" + errorTimes + "]");
				Thread.sleep(4000);
			} catch (SocketTimeoutException st) {
				errorTimes++;
				if (errorTimes > timeoutRetryTimes) {
					throw st;
				}
				log.warn("socket time out!! try again...[" + errorTimes + "]");
				Thread.sleep(4000);
			} catch (Exception ex) {
				throw ex;
			}
		}
		if (returnStr == null || returnStr.trim().length() <= 0) {
			throw new AngelaNotAvailableException("Angela Not Available!! [no return data!]");
		}
		return returnStr;
	}

	public String requestHttps(AngelaVo vo, URL url) throws Exception {

		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setConnectTimeout(connectTimeOut);
		conn.setReadTimeout(readTimeout);
		log.debug("connect to [" + angelaUrl + conn.getURL().getPath() + "]...");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);

		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setDoOutput(true);

		String jsonInputString = genRequestJson(vo);

		OutputStream os = (OutputStream) conn.getOutputStream();
		byte[] input = jsonInputString.getBytes("big5");
		os.write(input, 0, input.length);
		os.flush();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "big5"));
		StringBuilder response = new StringBuilder();
		String responseLine = null;
		while ((responseLine = br.readLine()) != null) {
			response.append(responseLine.trim());
		}
		String rtnValueHex = parseResultFromJson(response);

		return rtnValueHex;
	}

	private String parseResultFromJson(StringBuilder response) throws Exception {
		if (response == null) {
			throw new Exception("no response data!");
		}
		ObjectMapper objectMapper = new ObjectMapper();
		String rtnJson = response.toString();
//		log.debug(rtnJson);
		String rtnValueHex = "";
		ResponseVo rspvo = objectMapper.readValue(rtnJson, ResponseVo.class);
		if (rspvo != null) {
			if (rspvo.getResult() != 1) {
				if (rspvo.getResult() == 0) {
					if (rspvo.getErrorCode() == 14006) {
						throw new AngelaNotAvailableException(
								"Angela API execute error [" + rspvo.getResult() + "]. check error code["
										+ rspvo.getErrorCode() + "]==> errr message[" + rspvo.getErrorMessage() + "]");
					} else {
						throw new Exception("Angela API execute error [" + rspvo.getResult() + "]. check error code["
								+ rspvo.getErrorCode() + "]==> errr message[" + rspvo.getErrorMessage() + "]");
					}
				}
				if (rspvo.getResult() < 0) {
					throw new Exception("Angela API execute error [" + rspvo.getResult() + "]. unknow SSOID...");
				}
			}
			log.info("call angela api success : " + rspvo.getResult());
			rtnValueHex = rspvo.getData();
		} else {
			throw new Exception("unknow info from response...[" + rtnJson + "]");
		}
		return rtnValueHex;
	}

	private String genRequestJson(AngelaVo vo) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonInputString = "";
		if ("encrypt".equals(vo.getOpMode())) {
			EncryptReqVo reqVo = new EncryptReqVo();
			reqVo.setSsoid(vo.getSsoId());
			reqVo.setKeyName(vo.getKeyName());
			reqVo.setEncryptMode(Integer.parseInt(vo.getArgId()));
			reqVo.setDataFormat("hex");
			reqVo.setData(vo.getHexData());
			reqVo.setResponseFormat("hex");
			jsonInputString = objectMapper.writeValueAsString(reqVo);
		} else if ("decrypt".equals(vo.getOpMode())) {
			DecryptReqVo reqVo = new DecryptReqVo();
			reqVo.setSsoid(vo.getSsoId());
			reqVo.setKeyName(vo.getKeyName());
			reqVo.setDecryptMode(Integer.parseInt(vo.getArgId()));
			reqVo.setDataFormat("hex");
			reqVo.setData(vo.getHexData());
			reqVo.setResponseFormat("hex");
			jsonInputString = objectMapper.writeValueAsString(reqVo);
		} else {
			throw new Exception("unknow option mode!");
		}
		log.debug("data[" + jsonInputString + "]");
		return jsonInputString;
	}

	public byte[] hexDecode(String hex) {
		// A null string returns an empty array
		if (hex == null || hex.length() == 0) {
			return new byte[0];
		} else if (hex.length() < 3) {
			return new byte[] { (byte) (Integer.parseInt(hex, 16) & 0xff) };
		}
		// Adjust accordingly for odd-length strings
		int count = hex.length();
		int nibble = 0;
		if (count % 2 != 0) {
			count++;
			nibble = 1;
		}
		byte[] buf = new byte[count / 2];
		char c = 0;
		int holder = 0;
		int pos = 0;
		for (int i = 0; i < buf.length; i++) {
			for (int z = 0; z < 2 && pos < hex.length(); z++) {
				c = hex.charAt(pos++);
				if (c >= 'A' && c <= 'F') {
					c -= 55;
				} else if (c >= '0' && c <= '9') {
					c -= 48;
				} else if (c >= 'a' && c <= 'f') {
					c -= 87;
				}
				if (nibble == 0) {
					holder = c << 4;
				} else {
					holder |= c;
					buf[i] = (byte) holder;
				}
				nibble = 1 - nibble;
			}
		}
		return buf;
	}

	public byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	private String getHexString(byte[] data) {
		byte[] bytes = data;
		char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public int getConnectTimeOut() {
		return connectTimeOut;
	}

	public void setConnectTimeOut(int connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
	}

	public int getTimeoutRetryTimes() {
		return timeoutRetryTimes;
	}

	public void setTimeoutRetryTimes(int timeoutRetryTimes) {
		this.timeoutRetryTimes = timeoutRetryTimes;
	}

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, Exception {

		String angelaIp = "https://192.168.13.24:3443";
		String ssoid = "AML422097494gfs";
		String keyName = "PINAP1";

		AngelaApiUtil angela = new AngelaApiUtil(angelaIp, ssoid, keyName);
		String data = "amlap123";
		String encData = angela.doNcccStrEncrypt(data, ssoid, keyName);
		String data2 = angela.doNcccStrDecrypt(encData);
		System.out.println("data = " + data);
		System.out.println("encData = " + encData);
		System.out.println("data2 = " + data2);

	}

}
