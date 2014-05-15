package videostreaming;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;



public abstract class Connection {
	protected ServerSocket server;
    protected Socket socket;
    protected DataOutputStream output;
    protected DataInputStream input;
    protected OutputStream outputStream;
    protected InputStream inputStream;
//    private int port;
    
    
    private String hostname="";
    
    public abstract void establishConnection();

	public DataOutputStream getOutput() {
		return output;
	}

	public DataInputStream getInput() {
		return input;
	}

}
