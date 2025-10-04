package nccc.btp.aplog.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;


public class MultiReadHttpServletResponse extends HttpServletResponseWrapper {
    private ByteArrayOutputStream cachedBytes = new ByteArrayOutputStream();
    private HttpServletResponse response;
    private PrintWriter pw;
   
    
    public MultiReadHttpServletResponse(HttpServletResponse response) throws IOException {
        super(response);
        this.response = response;
    }
    
    
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
    	return new ServletOutputStreamWrapper(this.cachedBytes, this.response);
    	
    }

    @Override
    public PrintWriter getWriter() throws IOException {
    	pw = new PrintWriter(new OutputStreamWriter(this.cachedBytes, this.response.getCharacterEncoding()));
    	return pw;
    }

    public byte[] getBuffer(){
        if(pw!=null){
        	pw.flush();
        }
        return cachedBytes.toByteArray();
    }

    private static class ServletOutputStreamWrapper extends ServletOutputStream { 
    	private ByteArrayOutputStream outputStream;
    	private HttpServletResponse response;
    	
    	public ServletOutputStreamWrapper(ByteArrayOutputStream outputStream, HttpServletResponse response) {
    		this.outputStream = outputStream;
    		this.response = response;
    	}
    	
//    	public boolean isReady() { 
//    		return true; 
//    	} 
//    	public void setWriteListener(WriteListener listener) {
//    		
//    	} 
    	
    	@Override 
    	public void write(int b) throws IOException { 
    		this.outputStream.write(b); 
    		this.outputStream.flush();
    	} 
    	
    	@Override 
    	public void flush() throws IOException {
    		if (! this.response.isCommitted()) {
    			byte[] body = this.outputStream.toByteArray();
    			ServletOutputStream outputStream = this.response.getOutputStream();
    			outputStream.write(body); 
    			outputStream.flush(); 
    		} 
    	}

		@Override
		public boolean isReady() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setWriteListener(WriteListener arg0) {
			// TODO Auto-generated method stub
			
		} 
    }
   
}