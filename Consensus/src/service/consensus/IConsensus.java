package service.consensus;

import communication.*;
import service.*;

public interface IConsensus{  
   public void register(ConsensusElement elt);
   public void initConsensus(boolean value); 
   public void setCurrentValue(Id id, boolean value);
}
