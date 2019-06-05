/* Coyright Eric Cariou, 2009 - 2011 */

package service;

import communication.ProcessIdentifier;
import java.util.Vector;

/**
 * Identification service allowing a process to get its identifier, to leave the system and
 * to know the identifiers of the other processes of the system.
 */
public interface IIdentification {

   /**
    * Multicast IP adress for contacting the identification server
    */
   public final static String ipGroup = "228.5.6.7";
   /**
    * Multicast port for contacting the identification server
    */
   public final static int port = 5000;

   /**
    * @return the identifier of the current process
    */
   public ProcessIdentifier getMyIdentifier();

   /**
    * @return the list of identifiers of all processes of the system (excepting the one
    * of the current process)
    */
   public Vector<ProcessIdentifier> getAllIdentifiers();

   /**
    * Leave the system: the identification server remove the current process from the process list identifiers
    * and inform the other processes of the leaving of the current process.
    */
   public void leaveSystem();
  
}
