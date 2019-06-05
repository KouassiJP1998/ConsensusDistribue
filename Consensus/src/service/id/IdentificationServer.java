/* Coyright Eric Cariou, 2009 - 2011 */

package service.id;

import communication.CommunicationElement;
import communication.CommunicationException;
import communication.ProcessIdentifier;
import communication.ReliableCommElt;
import service.IIdentification;
import service.MessageType;
import service.TypedMessage;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Iterator;
import java.util.Vector;

import service.consensus.*;

/**
 * The identification server has to started before launching any process. Its goal is
 * to set the identifiers of the processes of the system and to make them knowing
 * the identifiers of all the processes. <br /><br />
 * The server communicates with the processes in two ways, depending on the communication direction:
 * <ul>
 * <li>UDP multicast, from process to server: to be able to launch the server on any machine without requiring
 * for a process to know the localisation of the server, multicast communication is used.
 * The IP/port of the multicast group is set in the <code>service.IIdentification</code>
 * interface. Moreover, as the communication element of a process can loose messages and
 * considering that the communication with the identification server must be reliable, a process sends
 * messages to the server only through UDP multicast.
 * </li>
 * <li>Communication element, from server to process: a reliable communication element without
 * additionnal errors is used to communicate with the processes.</li>
 * </ul>
 */
public class IdentificationServer extends Thread {

    /**
     * Set of all the process identifiers of the system
     */
    protected Vector<ProcessIdentifier> identifiers = new Vector<ProcessIdentifier>();

    /**
     * The last identifier used. Allow a unique identifier management.
     */
    protected int lastId = 0;

    /**
     * The communication element used for sending data to processes
     */
    protected CommunicationElement commElt;

    /**
     * The UDP multicast socket that is used by a process to contact the identification server
     */
    protected MulticastSocket socket;

    /**
     * The multicast IP group address
     */
    protected InetAddress group;

    /**
     * @return the set of process identifiers
     */
    public Vector<ProcessIdentifier> getIdentifiers() {
        return identifiers;
    }

    /**
     * @param identifiers set the process identifier set
     */
    public void setIdentifiers(Vector<ProcessIdentifier> identifiers) {
        this.identifiers = identifiers;
        //System.out.println(" [INFO] Identifiers modified");
        //printIdentifiers();
    }

    /**
     * Print on standard output all process identifiers.
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
     * Send all process identifiers to each process.
     */
    protected void sendAllId() {
        Iterator it = identifiers.iterator();
        ProcessIdentifier id = null;
        AllIdData allId = new AllIdData(identifiers);
        while (it.hasNext()) {
            try {
                id = (ProcessIdentifier) it.next();
                commElt.sendMessage(new TypedMessage(id, allId, MessageType.IDENTIFICATION));
            } catch (CommunicationException e) {
                System.err.println("[WARNING] Communication problem with " + id + " -> "+ e);
            }
        }
    }

    /**
     * Wait for a data received on the UDP multicast group.
     * @return the received data, <code>null</code> in case of problem
     */
    protected IdentificationData receiveData() {
        try {
            byte[] data = new byte[1000];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            socket.receive(packet);
            return IdentificationData.unserialize(packet.getData());
        } catch (Exception e) {
            System.err.println("[ERROR] Multicast communication problem! ");
            System.err.println(e);
        }
        return null;
    }

    /**
     * Initialize the server by creating its multicast socket.
     */
    protected void initialize() {
        try {
            group = InetAddress.getByName(IIdentification.ipGroup);
            socket = new MulticastSocket(IIdentification.port);
            socket.joinGroup(group);
        } catch (Exception e) {
            System.err.println("[ERROR] Multicast communication problem! ");
            System.err.println(e);
        }
    }

    @Override
    /**
     * In an infinite loop, wait for requests coming from processes on the multicast socket.
     */
    public void run() {
        initialize();
        System.out.println("--> Identification server launched <--");

        ProcessIdentifier id;
        IdentificationData data;

        while (true) {
            data = receiveData();

            // request of an identifier for a new process. Set the id of the process and
            // send it back to the process
            if (data instanceof RequestIdData) {
                id = ((RequestIdData) data).getProcessId();
                id.setId(++lastId);
                System.out.println(" *** New connected: " + id);
                identifiers.add(id);
                try {
                    commElt.sendMessage(new TypedMessage(id, new SetIdData(id), MessageType.IDENTIFICATION));
                }
                catch(CommunicationException e) {
                   System.err.println("[WARNING] Communication problem with " + id + " -> "+ e);
                }

                //send the identifier set to all processes as it has changed
                sendAllId();
            }

            // request for leaving the system
            if (data instanceof RemoveIdData) {
                System.out.println(" *** " + ((RemoveIdData) data).getProcessId() + " is leaving");
                identifiers.remove(((RemoveIdData) data).getProcessId());
                sendAllId();
               }
        }
    }

    public IdentificationServer() throws CommunicationException {
        identifiers = new Vector<ProcessIdentifier>();
        commElt = new ReliableCommElt();
        this.start();
        (new PingManager(commElt)).start();
    }

    public static void main(String argv[]) {
        try {
            IdentificationServer server = new IdentificationServer();
            
            //Lancement du consensus de test
            Consensus consensus = new Consensus();
            consensus.initConsensus(true);
        } catch (CommunicationException ex) {
            System.err.println("[ERROR] while launching identification server: " + ex);
        }
        
    }

    /**
     * The heartbreak thread send regularly an hearbreak message to each process. If a communication
     * problem is detected with a process, it is considered as crashed and its identifier is removed
     * from the identifier list.
     */
    protected class PingManager extends Thread {

        CommunicationElement commElt;

        /**
         * Delay between each heartbreak round.
         */
        protected int delay = 3000;

        @Override
        public void run() {
            Iterator it;
            ProcessIdentifier id = null;
            PingData hb = new PingData();

            // store the identifiers of valid processes
            Vector<ProcessIdentifier> newIdentifiers;
            // if a communication problem is detected, the identifier list has to be changed
            boolean hasChanged = false;

            while (true) {

                try { Thread.sleep(delay); } catch (Exception e) {
                    System.err.println (" [PING] breaking sleep: "+e);
                }
                hasChanged = false;
                newIdentifiers = new Vector<ProcessIdentifier>();
                System.out.println("  --+-- Ping round  --+-- ");

                it = getIdentifiers().iterator();
                while (it.hasNext()) {
                    try {
                        id = (ProcessIdentifier) it.next();
                        commElt.sendMessage(new TypedMessage(id, hb, MessageType.IDENTIFICATION));
                        System.out.println(" ++ Communication ok with " + id);
                        newIdentifiers.add(id);
                    } catch (CommunicationException e) {
                        System.err.println(" -- Communication problem with " + id + " -> "+ e);
                        hasChanged = true;
                    }
                }
                if (hasChanged) {
                    setIdentifiers(newIdentifiers);
                    sendAllId();
                }
            }
        }

        public PingManager(CommunicationElement commElt) {
            this.commElt = commElt;
        }
    }
}
