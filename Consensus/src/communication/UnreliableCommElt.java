/* Coyright Eric Cariou, 2009 - 2011 */

package communication;

/**
 * Unreliable element: send a message without the possibility to know if it has been
 * received by the other element or has been lost.
 */
public class UnreliableCommElt extends CommunicationElement {

    /**
     * @throws CommunicationException
     */
    public UnreliableCommElt() throws CommunicationException {
        super(0);
    }

    /**
     * @param port
     * @throws CommunicationException
     */
    public UnreliableCommElt(int port) throws CommunicationException {
        super(port);
    }

    /* (non-Javadoc)
     * @see communication.CommunicationElement#physicalSendMessage(communication.Message)
     */
    @Override
    protected void physicalSendMessage(Message msg, ProcessIdentifier senderId) throws CommunicationException {
        ProcessIdentifier pid = msg.getProcessId();
        int delay = 0;

        // check if the process id contains an @IP/port couple for TCP sending
        if (!(pid instanceof IPProcessIdentifier)) {
            throw new UnreachableProcessException("The process identifier is not valid in IP context: " + pid);
        }

        if (isPacketLost()) {
            if (debugFault) {
                System.out.println("[Comm] message lost : " + msg);
            }
            return;
        }
        if (transmissionDelayLowerBound != FaultLevel.NONE && transmissionDelayUpperBound != FaultLevel.NONE) {
            delay = transmissionDelay();
        } else {
            delay = 0;
        }

        // launch a new thread that will wait the delay before sending the message
        // this allows the direct return of a message sending service
        Sender sender = new Sender(tcp, msg, delay, senderId);
        // set the thread priority to the highest to ensure the message will be sent
        // whatever the other threads are doing
        sender.setPriority(Thread.MAX_PRIORITY);
        sender.start();

    }

    /**
     * Thread that wait a given delay before sending a message
     */
    protected class Sender extends Thread {

        /**
         * TCP element used for sending the message
         */
        protected TCPElement tcp;
        /**
         * The message to send
         */
        protected Message msg;
        /**
         * The delay to wait before sending the message
         */
        protected int delay;
        /**
         * The identifier of the message sender
         */
        protected ProcessIdentifier senderId;

        @Override
        public void run() {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) { // does not happened ...
            }

            if (debugFault) {
                System.out.println("[COMM] transmission delay of " + delay + " ms for " + msg);
            }

            IPProcessIdentifier IPid = (IPProcessIdentifier) msg.getProcessId();
            try {
                msg.setProcessId(senderId);
                tcp.sendData(IPid.getIPadd(), IPid.getPort(), msg);
            } catch (Exception e) {
                // unreliable context: nothing to do, the sender is not informed
                // on communication problems
            }
        }

        /**
         * @param tcp
         * @param msg
         * @param delay
         */
        public Sender(TCPElement tcp, Message msg, int delay, ProcessIdentifier senderId) {
            super();
            this.tcp = tcp;
            this.msg = msg;
            this.delay = delay;
            this.senderId = senderId;
        }
    }
}
