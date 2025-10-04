package nccc.btp.aplog.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class ApLogHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private final byte[] body;

	public ApLogHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		ServletInputStream stream = request.getInputStream();
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = stream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		this.body = swapStream.toByteArray();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(body);
		return new ServletInputStream() {

			@Override
			public int read() throws IOException {
				return bais.read();
			}
			@Override
			public boolean isFinished() {
				return false;
			}
			@Override
			public boolean isReady() {
				return false;
			}
			@Override
			public void setReadListener(ReadListener arg0) {
			}
		};
	}

}
