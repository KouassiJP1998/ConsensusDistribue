/* Coyright Eric Cariou, 2009 - 2011 */

import communication.CommunicationException;
import communication.FaultLevel;
import communication.Message;
import communication.ReliabilitySetting;

import service.ICommunication;
import service.DistributedServicesMiddleware;
import service.IDistributedServices;
import service.IIdentification;
import service.IBroadcast;

import service.consensus.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Chat extends Thread {

    IDistributedServices services;
    ICommunication commService;
    IIdentification idService;
    IBroadcast broadcastService;
    IConsensus consensusService;

    public void init() {

        // setting of the simulated system
        ReliabilitySetting setting = new ReliabilitySetting();
        setting.setTransmissionDelayLowerBound(FaultLevel.NONE);
        setting.setTransmissionDelayUpperBound(FaultLevel.NONE);
        setting.setPacketLostLevel(FaultLevel.NONE);
        setting.setCrashLevel(FaultLevel.NONE);
        setting.setReliable(false);
        setting.setDebugFault(true);

        // connection to the system
        services = new DistributedServicesMiddleware();
        try {
            services.connect(setting);
        } catch (CommunicationException e) {
            System.err.println("Impossible de se connecter : " + e);
        }
        // get the service access points
        commService = services.getCommunicationService();
        idService = services.getIdentificationService();
        broadcastService = services.getBasicBroadcastService();
        consensusService = services.getConsensusService();

        // as we are not directly informed when the process id has been received, wait a short time
        // to be almost sure to have received it when printing the identifier
        try { Thread.sleep(200); } catch(Exception e) { }
        System.out.println("OK, connexion réalisée, je suis : " + idService.getMyIdentifier()+ "\n");
    }

    public void papoter() {
        int nbT = 0;
        int nbF = 0;
        boolean decision = false;
        String message = null;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        consensusService.initConsensus(true);
                
        while (true) {
            // read the user entry
            System.out.println("Commande : Saisir 'consensus' pour rejoindre le service");
            System.out.println("Votre message ('end' pour finir) :");
            try {
                message = input.readLine();
            } catch (IOException ex) {
                System.err.println("Mauvaise Saisie : " + ex);
                System.exit(2);
            }
            
            if (message.equals("consensus")) {
                System.out.println("Faites votre vote !");
                if(message.equals("true")){
                    
                    nbT = nbT +1;
                    consensusService.initConsensus(decision);
                    ConsensusElement elt = new ConsensusElement();
                    consensusService.register(elt);
                }
                else 
                {
                    nbF = nbF + 1;
                    System.out.println("Veuillez Saisie true ou false !");
                }
             
                               
                System.out.print(" --> Notification aux  utilisateurs ... ");
                try {
                    broadcastService.broadcast("a été ajouté au consensus !");
                } catch (CommunicationException ex) {
                    Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                consensusService.initConsensus(true);
            }
            
            // if end, disconnect from the system and exit the JVM
            if (message.equals("end")) {
                if(nbT>nbF){
                    
                     System.out.println("La décision prioritaire est True !");
                     decision = true;
                }
                else{
                    
                    System.out.println("La décision prioritaire est False !");
                    decision = false;
                    
                }
                System.out.println("A la prochaine !");
                services.disconnect();
                System.exit(0);
            }

            // brodcast the message
            try {
                System.out.print(" --> Envoi message ... ");
                broadcastService.broadcast(message);
                broadcastService.broadcast(decision);
                System.out.println("done");
            } catch (CommunicationException ex) {
                System.err.println(" *** communication problem: " + ex);
            }
        }
    }

    @Override
    public void run() {
        // wait in an infinite loop for a message to be received
        Message msg;
        while (true) {
            msg = broadcastService.synchDeliver();
            System.out.println("[" + msg.getProcessId().getId() + "] " + msg.getData());
        }
    }

    public void Chat() {
    }

    public static void main(String argv[]) {
        Chat chat = new Chat();
        chat.init();
        chat.start();
        chat.papoter();
    }
}
