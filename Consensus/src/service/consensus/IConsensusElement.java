package service.consensus;

public interface IConsensusElement {  
   public boolean askForValue(Id id);
   public boolean resultConsensus(Id id);  
}