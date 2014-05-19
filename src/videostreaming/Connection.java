package videostreaming;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;



public abstract class Connection {
	protected ServerSocket server;
    protected Socket socket;
    protected DataOutputStream output;
    protected DataInputStream input;
    protected OutputStream outputStream;
    protected InputStream inputStream;

    protected PrintWriter out;
    protected BufferedReader in;
    
    
    private String hostname="";
    
    public abstract void establishConnection();

	public PrintWriter getOut() {
		return out;
	}

	public BufferedReader getIn() {
		return in;
	}
	
}
