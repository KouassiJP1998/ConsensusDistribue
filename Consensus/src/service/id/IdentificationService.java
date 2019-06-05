/* Coyright Eric Cariou, 2009 - 2011 */

package service.id;

import communication.CommunicationElement;
import communication.Message;
import communication.ProcessIdentifier;
import service.ICommunication;
import service.IIdentification;
import service.MessageDispatcher;
import service.MessageType;
import service.Service;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Iterator;
import java.util.Vector;

/**
 * Identification service allowing for a process to know all process identifiers of the system.
 */
public class IdentificationService extends Service implements IIdentification, Runnable {

    /**
     * Identifiers of all processes of the system.
     */
    protected Vector<ProcessIdentifier> identifiers;

    /**
     * Multicast socket used to communication with the identification server.
     */
    protected MulticastSocket socket;

    /**
     * IP address of the multicast group
     */
    protected InetAddress group;

    public ProcessIdentifier getMyIdentifier() {
        return ((CommunicationElement) commElt).getMyPid();
    }

    public Vector<ProcessIdentifier> getAllIdentifiers() {
        return (Vector<ProcessIdentifier>)identifiers.clone();
    }

    public void leaveSystem() {
        sendData(new RemoveIdData(((CommunicationElement) commElt).getMyPid()));
    }

    /**
     * Send a request to the identification server for getting the process identifier. Do not wait for
     * the response to be received.
     */
    protected void sendIdRequestToServer() {
        System.out.println(" --> requête au serveur d'identification " + socket.getInetAddress() + ":" + socket.getLocalPort());
        sendData(new RequestIdData(((CommunicationElement) commElt).getMyPid()));
    }

    /**
     * Send a data (i.e. a request) to the identification server.
     * @param data the data to send
     */
    protected void sendData(IdentificationData data) {
        try {
            byte tab[] = data.serialize();
            DatagramPacket packet = new DatagramPacket(tab, tab.length, group, IIdentification.port);
            socket.send(packet);
        } catch (Exception e) {
            System.err.println("[ERROR] Multicast communication problem! ");
            System.err.println(e);
        }
    }

    @Override
    public void initialize(MessageDispatcher dispatcher, ICommunication commElt, MessageType myType) {
        super.initialize(dispatcher, commElt, myType);

        try {
            group = InetAddress.getByName(IIdentification.ipGroup);
            socket = new MulticastSocket(IIdentification.port);
            socket.joinGroup(group);
        } catch (Exception e) {
            System.err.println("[ERROR] Multicast communication problem! ");
            System.err.println(e);
        }

        (new Thread(this)).start();
    }

    /**
     * Print on the standard output the set of process identifiers.
     */
    protected void printIdentifiers() {
        Iterator it = identifiers.iterator();
        ProcessIdentifier id;
        while (it.hasNext()) {
            id = (ProcessIdentifier) it.next();
            System.out.println(" + " + id);
        }
    }

    /**
     * Wait for data coming from the identification server.
     */
    public void run() {
        Message msg;
        Object data;

        sendIdRequestToServer();

        while (true) {
            msg = (Message) buffer.removeElement(true);
            data = msg.getData();

            // the identifier of the current process
            if (data instanceof SetIdData) {
                ((CommunicationElement) commElt).getMyPid().setId(((SetIdData) data).getProcessId().getId());
                System.out.println(" Reçu mon Id, je suis : " + getMyIdentifier());
            }

            // all identifiers of all the processes
            if (data instanceof AllIdData) {
                identifiers = ((AllIdData) data).getIdentifiers();
                // remove the identifier of the current process
                identifiers.remove(getMyIdentifier());
                //printIdentifiers();
            }

            // ping message: nothing to do, has just to be received
            if (data instanceof PingData) {
                //System.out.println(" Ping !");
            }
        }
    }
}
