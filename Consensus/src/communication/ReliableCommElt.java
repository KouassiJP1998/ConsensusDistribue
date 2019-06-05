/* Coyright Eric Cariou, 2009 - 2011 */

package communication;

/**
 * Reliable communication element: ensure that all data are sent to receivers or, in case of
 * communication problems, throw explicit exceptions. 
 * 
 * Note: message sending services can take a certain time to execute and to return. This is
 * due to the fact that we must wait to know if a message has been received or not. So, 
 * we must wait for the (virtual) transmission delay to be past.
 * 
 */
public class ReliableCommElt extends CommunicationElement {

    @Override
    protected void physicalSendMessage(Message msg, ProcessIdentifier id) throws CommunicationException {
        ProcessIdentifier pid = msg.getProcessId();
        int delay;

        // check if the process id contains an @IP/port couple for TCP sending
        if (!(pid instanceof IPProcessIdentifier)) {
            throw new UnreachableProcessException("The process identifier is not valid in IP context: " + pid);
        }

        // check if the packet has to be lost
        if (isPacketLost()) {
            if (debugFault) {
                System.out.println("[COMM] message lost : " + msg);
            }
            return;
        }

        // check if there is a delay transmission to manage
        if (transmissionDelayLowerBound != FaultLevel.NONE && transmissionDelayUpperBound != FaultLevel.NONE) {
            delay = transmissionDelay();

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) { // does not happened ...
            }
            if (debugFault) {
                System.out.println("[COMM] transmission delay of " + delay + " ms for " + msg);
            }
        }

        IPProcessIdentifier IPid = (IPProcessIdentifier) pid;
        msg.setProcessId(id);
        tcp.sendData(IPid.getIPadd(), IPid.getPort(), msg);
    }

    /**
     * Reliable communication : if the remote element is reachable, ensure it receives
     * data, otherwise generate an explicit error.
     *
     * @throws CommunicationException 
     */
    public ReliableCommElt() throws CommunicationException {
        super(0);
    }

    /**
     * @param port
     * @throws CommunicationException
     */
    public ReliableCommElt(int port) throws CommunicationException {
        super(port);
    }
}
