package service.consensus;
import service.*;

public class ConsensusElement extends Service implements IConsensusElement {
    public Consensus consensus;
    public boolean valeur;
    public Id id;
    
    @Override
   public boolean askForValue(Id id){
       this.consensus.setCurrentValue(id, valeur);
       return this.consensus.value;
   }
   
    @Override
   public boolean resultConsensus(Id id){
       return consensus.value;      
    } 
}
