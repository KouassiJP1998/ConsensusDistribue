/* Coyright Eric Cariou, 2009 - 2011 */

package service;

import service.consensus.*;
import communication.CommunicationException;
import communication.ReliabilitySetting;

/**
 * Definition of the global service access point.
 */
public interface IDistributedServices {

    /**
     * Connect the process to the system using default value, that is, without
     * communication delays or errors.
     * @throws CommunicationException in case of problem during the connection
     */
    void connect() throws CommunicationException;

    /**
     * Connect the process to the system using reliability settings.
     * @param setting reliability settings to use for the current process
     * @throws CommunicationException in case of problem during the connection
     */
    void connect(ReliabilitySetting setting) throws CommunicationException;

    /**
     * Connect the process to the system using reliability settings and a specific TCP port.
     * @param setting reliability settings to use for the current process
     * @param localPort TCP port to be used by the server socket of the communication layer
     * @throws CommunicationException in case of problem during the connection
     */
    void connect(ReliabilitySetting setting, int localPort) throws CommunicationException;

    /**
     * Disconnect the process from the system
     */
    void disconnect();

    /**
     * @return the communication service access point
     */
    public ICommunication getCommunicationService();

    /**
     * @return the identification service access point
     */
    public IIdentification getIdentificationService();

    /**
     * @return the basic broadcast service access point
     */
    public IBroadcast getBasicBroadcastService();

    /**
     * @return the reliable broadcast service access point
     */
    public IBroadcast getReliableBroadcastService();
    
    /**
     * récupère le point d'accès sur le service
     * @return 
     */
    public IConsensus getConsensusService();
}
