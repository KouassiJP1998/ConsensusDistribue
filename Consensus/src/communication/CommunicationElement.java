/* Coyright Eric Cariou, 2009 - 2011 */

package communication;

import service.ICommunication;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Communication element for a process to communicate with other processes. The underlying
 * communication support is TCP. Fault and communication delays are simulated through a set
 * of parameters.
 */
public abstract class CommunicationElement implements ICommunication {

    /**
     * Level of lost of packet sent through the network
     */
    protected FaultLevel packetLostLevel = FaultLevel.NONE;

    /**
     * Level of the minimum delay for a message to join the receiver element
     */
    protected FaultLevel transmissionDelayLowerBound = FaultLevel.NONE;

    /**
     * Level of the maximum delay for a message to join the receiver element
     */
    protected FaultLevel transmissionDelayUpperBound = FaultLevel.NONE;

    /**
     * Level of crash of the process
     */
    protected FaultLevel crashLevel = FaultLevel.NONE;

    /**
     * Precise if debug information, concerning message lost and transmission delays,
     * must be printed on standard output
     */
    protected boolean debugFault = false;

    /**
     * Identifier of the process
     */
    protected IPProcessIdentifier myPid;

    /**
     * Internal buffer for received messages
     */
    protected SynchronizedBuffer msgBuffer;

    /**
     * TCP element for physically sending and receiving messages
     */
    protected TCPElement tcp;

    /**
     * Generator of random numbers for managing the lost and delays for messages
     */
    protected Random randGen;

    /**
     * @return the transmissionDelayLowerBound
     */
    public FaultLevel getTransmissionDelayLowerBound() {
        return transmissionDelayLowerBound;
    }

    /**
     * @param transmissionDelayLowerBound the transmissionDelayLowerBound to set
     */
    public void setTransmissionDelayLowerBound(FaultLevel transmissionDelayLowerBound) {
        this.transmissionDelayLowerBound = transmissionDelayLowerBound;
    }

    /**
     * @return the transmissionDelayUpperBound
     */
    public FaultLevel getTransmissionDelayUpperBound() {
        return transmissionDelayUpperBound;
    }

    /**
     * @param transmissionDelayUpperBound the transmissionDelayUpperBound to set
     */
    public void setTransmissionDelayUpperBound(FaultLevel transmissionDelayUpperBound) {
        this.transmissionDelayUpperBound = transmissionDelayUpperBound;
    }

    /**
     * @return the packetLostLevel
     */
    public FaultLevel getPacketLostLevel() {
        return packetLostLevel;
    }

    /**
     * @param packetLostLevel the packetLostLevel to set
     */
    public void setPacketLostLevel(FaultLevel packetLostLevel) {
        this.packetLostLevel = packetLostLevel;
    }

    /**
     * @return the process crash level
     */
    public FaultLevel getCrashLevel() {
        return crashLevel;
    }

    /**
     * @param crashLevel the crash level to set
     */
    public void setCrashLevel(FaultLevel crashLevel) {
        this.crashLevel = crashLevel;
    }
    
    /**
     * @return the debugFault
     */
    public boolean isDebugFault() {
        return debugFault;
    }

    /**
     * @param debugFault the debugFault to set
     */
    public void setDebugFault(boolean debugFault) {
        this.debugFault = debugFault;
    }

    /**
     * Get the complete process identifier. Notice that the IP and port field are automatically
     * set once the communication element has been created. The identifier integer field is not
     * set and has the value of 0 by default.
     *
     * @return the process identifier
     */
    public IPProcessIdentifier getMyPid() {
        return myPid;
    }

    /**
     * Modify the (supposed unique) identifier integer value inside the process identifier.
     * The other fields (IP and port) of the complete process identifier have not to be changed
     * because they are initialized when the communication element is created and must not
     * change after that.
     *
     * @param id the identifier of the process
     */
    public void setMyPid(int id) {
        myPid.setId(id);
    }

    /* (non-Javadoc)
     * @see communication.ICommunication#sendMessage(communication.Message)
     */
    @Override
    public void sendMessage(Message msg) throws CommunicationException {
        physicalSendMessage(msg, myPid);
    }

    /* (non-Javadoc)
     * @see communication.ICommunication#sendMessage(communication.ProcessIdentifier, java.lang.Object)
     */
    @Override
    public void sendMessage(ProcessIdentifier id, Object data) throws CommunicationException {
        sendMessage(new Message(id, data));
    }

    /* (non-Javadoc)
     * @see communication.ICommunication#synchReceiveMessage()
     */
    @Override
    public synchronized Message synchReceiveMessage() {
        return (Message) msgBuffer.removeElement(true);
    }

    /* (non-Javadoc)
     * @see communication.ICommunication#asynchReceiveMessage()
     */
    @Override
    public synchronized Message asynchReceiveMessage() {
        return (Message) msgBuffer.removeElement(false);
    }

    /* (non-Javadoc)
     * @see communication.ICommunication#availableMessage()
     */
    @Override
    public synchronized boolean availableMessage() {
        return (msgBuffer.available() > 0);
    }

    /**
     * Method called to "physically" send a message through TCP sockets. Will be specialized
     * in ReliableCommElt and UnreliableCommElt depending on the reliability policy.
     *
     * @param msg the message to be sent (including the receiver id)
     * @param id the id of the sender
     * @throws CommunicationException in case of communication problem
     */
    protected abstract void physicalSendMessage(Message msg, ProcessIdentifier id) throws CommunicationException;

    /**
     * @return true if the packet must be lost according to the lost fault level
     */
    protected boolean isPacketLost() {
        // get a random number between 0 and 5
        // if this number is lower than the lost level, the message has not to be sent
        float randNb = randGen.nextFloat() * 5;
        return (randNb < packetLostLevel.getValue());
    }

    /**
     * @return the transmission delay to wait before sending the message according to
     * the transmission delay bounds
     */
    protected int transmissionDelay() {
        int upper, lower;
        int delay;
        lower = transmissionDelayLowerBound.getValue();
        lower = lower * lower * lower * 40;

        upper = transmissionDelayUpperBound.getValue();
        upper = upper * upper * upper * upper * 50;
        upper = randGen.nextInt(upper);

        if (upper < lower) {
            delay = lower;
        } else {
            delay = upper;
        }
        return delay;
    }
  
    public void crashProcess() {
        // get a random number between 0 and 5
        // if this number is lower than the crash level, exit the Java program
        float randNb = randGen.nextFloat() * 5;
        if (randNb < (float)crashLevel.getValue()) {
            if (debugFault) System.err.println(" [EXIT] Process has crashed");
            System.exit(1);
        }
    }

    /**
     * Create and fully initialize a communication element, using any available TCP port
     *
     * @throws CommunicationException in case of problem (the socket can not be bound to
     * required port)
     */
    public CommunicationElement() throws CommunicationException {
        this(0);
    }

    /**
     * Create and fully initialize a communication element
     *
     * @param port the port on which to bind the TCP socket server of the communication element.
     * 0 for taken any available port
     * @throws CommunicationException in case of problem (the socket can not be bound to
     * required port)
     */
    public CommunicationElement(int port) throws CommunicationException {
        transmissionDelayUpperBound = FaultLevel.NONE;
        transmissionDelayLowerBound = FaultLevel.NONE;
        packetLostLevel = FaultLevel.NONE;
        crashLevel = FaultLevel.NONE;
        randGen = new Random();
        msgBuffer = new SynchronizedBuffer();
        tcp = new TCPElement(port);
        if (!tcp.initTCP()) {
            throw new CommunicationException("Impossible to initialize the TCP element on port " + port);
        }
       // myPid = new IPProcessIdentifier(tcp.serverSocket.getInetAddress(), tcp.serverSocket.getLocalPort());
        try {
            myPid = new IPProcessIdentifier(InetAddress.getLocalHost(), tcp.serverSocket.getLocalPort());
        } catch (UnknownHostException e) {
            throw new CommunicationException("Impossible to get the IP host ("+e.getMessage()+")");
        }
        //System.out.println("XXXX adr locale : " + myPid);
        (new Receiver(msgBuffer, tcp)).start();
    }

    /**
     * Thread that loops indefinitely for receiving messages through the TCP element
     * associated with the communication element
     */
    protected class Receiver extends Thread {

        /**
         *  Message buffer in which received messages are placed
         */
        protected SynchronizedBuffer msgBuffer;
        /**
         * TCP element managing the socket for receiving messages
         */
        protected TCPElement tcp;

        @Override
        public void run() {
            Message msg;
            while (true) {
                msg = tcp.receiveData();
                if (msg == null) {
                    System.err.println("[TCP] Receiver thread stops ...");
                    return;
                }
                msgBuffer.addElement(msg);
            }
        }

        /**
         * @param msgBuffer
         * @param tcp
         */
        public Receiver(SynchronizedBuffer msgBuffer, TCPElement tcp) {
            super();
            this.msgBuffer = msgBuffer;
            this.tcp = tcp;
        }
    }
    
}
