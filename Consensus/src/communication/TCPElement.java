/* Coyright Eric Cariou, 2009 - 2011 */

package communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class used by communication element: manage communications through TCP sockets
 */
public class TCPElement {

    /**
     * The local socket server of the element
     */
    protected ServerSocket serverSocket;
    /**
     * The port on which the local socket server is bound
     */
    protected int localPort = 0;

    /**
     * Send data through the TCP socket.
     *
     * @param adr IP address of the remote element
     * @param port port used by the server socket of the remote element
     * @param data data to send
     * @throws CommunicationException in case of communication problem
     */
    public void sendData(InetAddress adr, int port, Object data) throws CommunicationException {
        Socket socket;

        // try to connect with the remote element
        try {
            socket = new Socket(adr, port);
        } catch (Exception e) {
            throw new UnreachableProcessException("Impossible to connect with the element:" + e.getMessage());
        }

        // send the data
        try {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(data);
        } catch (Exception e) {
            throw new DataSendingException("Error while sending data: " + e.getMessage());
        }
    }

    /**
     * Wait for the next received message coming from any remote process
     *
     * @return the next received message
     */
    public Message receiveData() {
        Socket socket;
        ObjectInputStream input;
        Message msg;
        try {
            // wait the connection of a remote process
            socket = serverSocket.accept();
            // read its sent message
            input = new ObjectInputStream(socket.getInputStream());
            msg = (Message) input.readObject();
            socket.close();
            return msg;
        } catch (Exception e) {
            System.err.println("[TCP] unexpected exception on receive data: " + e);
            return null;
        }
    }

    /**
     * Initialize the TCP server socket
     *
     * @return <code>true</code> if the server socket has been bound to the required port,
     * <code>false</code> otherwise (the TCP element is not usable).
     */
    public boolean initTCP() {
        try {
            // connection of the socket to the required port
            serverSocket = new ServerSocket(localPort);
            // if the port was 0, localPort does not contain the port used by the socket
            // so we have to get it explicitely
            localPort = serverSocket.getLocalPort();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * @return the localPort
     */
    public int getLocalPort() {
        return localPort;
    }

    /**
     * @param localPort the localPort to set
     */
    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    /**
     * @param localPort the port on which the server socket has to be bound
     */
    public TCPElement(int localPort) {
        super();
        this.localPort = localPort;
    }
}
