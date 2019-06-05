/* Coyright Eric Cariou, 2009 - 2011 */

package service;

import service.broadcast.ReliableBroadcastService;

import service.id.IdentificationService;
import communication.CommunicationElement;
import communication.CommunicationException;
import communication.ReliabilitySetting;
import communication.ReliableCommElt;
import communication.UnreliableCommElt;
import service.broadcast.BasicBroadcastService;
import service.consensus.*;

/** 
 * Access point to the middleware and associated services.
 */
public class DistributedServicesMiddleware implements IDistributedServices {

    /**
     * The underlying communication element for communicating with other processes.
     */
    protected CommunicationElement commElt;

    /**
     * Communication service access point
     */
    protected ProxyCommunication commService;

    /**
     * Identification service access point
     */
    protected IdentificationService idService;

    /**
     * Basic broadcast access point.
     */
    protected BasicBroadcastService basicBroadcastService;

    /**
     * Reliable broadcast access point.
     */
    protected ReliableBroadcastService reliableBroadcastService;

    /**
     * Message dispatcher for dispatching received message to the associated service
     */
    
    protected Consensus consensus;
    
    protected ConsensusElement consensusElt;
    
    protected MessageDispatcher dispatcher;

    @Override
    public void connect() throws CommunicationException {
        commElt = new ReliableCommElt();
        initServices();
    }

    @Override
    public void connect(ReliabilitySetting setting) throws CommunicationException {
        if (setting.isReliable()) {
            commElt = new ReliableCommElt();
        } else {
            commElt = new UnreliableCommElt();
        }
        commElt.setPacketLostLevel(setting.getPacketLostLevel());
        commElt.setTransmissionDelayLowerBound(setting.getTransmissionDelayLowerBound());
        commElt.setTransmissionDelayUpperBound(setting.getTransmissionDelayUpperBound());
        commElt.setDebugFault(setting.isDebugFault());
        commElt.setCrashLevel(setting.getCrashLevel());
        initServices();
    }

    @Override
    public void connect(ReliabilitySetting setting, int localPort) throws CommunicationException {
        if (setting.isReliable()) {
            commElt = new ReliableCommElt(localPort);
        } else {
            commElt = new UnreliableCommElt(localPort);
        }
        commElt.setPacketLostLevel(setting.getPacketLostLevel());
        commElt.setTransmissionDelayLowerBound(setting.getTransmissionDelayLowerBound());
        commElt.setTransmissionDelayUpperBound(setting.getTransmissionDelayUpperBound());
        commElt.setDebugFault(setting.isDebugFault());
        commElt.setCrashLevel(setting.getCrashLevel());
        initServices();
    }

    @Override
    public void disconnect() {
        idService.leaveSystem();
    }

    @Override
    public ICommunication getCommunicationService() {
        return commService;
    }

    @Override
    public IIdentification getIdentificationService() {
        return idService;
    }

    @Override
    public IBroadcast getBasicBroadcastService() {
        return basicBroadcastService;
    }

    @Override
    public IBroadcast getReliableBroadcastService() {
        return reliableBroadcastService;
    }
    
    @Override
    public IConsensus getConsensusService() {
        return consensus;
    }
    /**
     * Initialize the services.
     */
    protected void initServices() {
        (new MessageReader(commElt, dispatcher)).start();
        idService.initialize(dispatcher, commElt, MessageType.IDENTIFICATION);
        commService.initialize(dispatcher, commElt, MessageType.NONE);
        
        basicBroadcastService.initialize(dispatcher, commElt, MessageType.BASIC_BROADCAST);
        basicBroadcastService.setIdentificationService(idService);
        
        reliableBroadcastService.initialize(dispatcher, commElt, MessageType.RELIABLE_BROADCAST);
        reliableBroadcastService.setIdentificationService(idService);
        
        consensus.initialize(dispatcher, commElt, MessageType.CONSENSUS);
        consensus.setIdentificationService(idService);
    }

    public DistributedServicesMiddleware() {
        dispatcher = new MessageDispatcher();
        idService = new IdentificationService();
        commService = new ProxyCommunication();
        basicBroadcastService = new BasicBroadcastService();
        reliableBroadcastService = new ReliableBroadcastService();
        consensus = new Consensus();
    }

    /**
     * Inner class that manage all received messages and dispatch them to
     * the required services thanks to a message dispatcher
     */
    protected class MessageReader extends Thread {

        /**
         * Underlying communication element
         */
        ICommunication commElt;
        /**
         * The message dispatcher
         */
        MessageDispatcher dispatcher;

        @Override
        public void run() {
            TypedMessage msg;
            // when a message is received, it is managed by the message dispatcher
            while (true) {
                msg = (TypedMessage) commElt.synchReceiveMessage();
                dispatcher.newEvent(msg);
            }
        }

        /**
         *
         * @param commElt
         * @param dispatcher
         */
        public MessageReader(ICommunication commElt, MessageDispatcher dispatcher) {
            this.commElt = commElt;
            this.dispatcher = dispatcher;
        }
    }
}
