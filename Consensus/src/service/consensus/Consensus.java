package service.consensus;

import communication.*;
import java.util.Iterator;
import service.*;

public class Consensus extends Service implements IConsensus {
    int max = 10;
    protected IIdentification idService;
    public ConsensusElement consensusElt [] = new ConsensusElement[max];
    public ProcessIdentifier idConsensus;
    public boolean value;
    int nbP = 0;


    public void setIdentificationService(IIdentification idService) {
        this.idService = idService;
    }
    
    public void register(ConsensusElement elt){
        this.consensusElt[nbP] = elt;
        this.nbP = nbP +1;
    }
   
    public void initConsensus(boolean value){
        this.value = value;
        for(int i=0;i<nbP;i++){
           setCurrentValue(this.consensusElt[i].id,value);
        }  
   }
   
   public void setCurrentValue(Id id, boolean value){

        ProcessIdentifier pId;
        String data;
        Iterator it;
        CompoundException exceptions = null;
        CommunicationException firstException = null;
        it = idService.getAllIdentifiers().iterator();
        // send the data to all the processes
        while (it.hasNext()) {
            pId = (ProcessIdentifier) it.next();
            try {
                // simulate the crash of the process during the broadcast
                commElt.crashProcess();
                if(value == true){
                    data = "true";
                }else{
                    data = "false";
                }
                
                commElt.sendMessage(new TypedMessage(pId, data, MessageType.BASIC_BROADCAST));
                commElt.sendMessage(new TypedMessage(id, value, MessageType.CONSENSUS));
            } catch (CommunicationException e) {
                if (firstException == null) firstException = e;
                else {
                    if (exceptions == null) {
                        exceptions = new CompoundException();
                        exceptions.addException(firstException);
                    }
                    exceptions.addException(e);
                }
            }
        }
   }
}
