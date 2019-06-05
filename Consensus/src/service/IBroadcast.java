/* Coyright Eric Cariou, 2009 - 2011 */

package service;

import communication.Message;
import communication.CommunicationException;

/**
 * Common interface for all broadcast implementation.
 */
public interface IBroadcast {

   /**
    * Broadcast data to all processes of the system.
    * @param data the data to be sent
    * @throws CommunicationException in case of problem
    */
   void broadcast(Object data) throws CommunicationException;

   /**
    * Deliver synchronously the last available received message. If no message is available,
    * wait for the next one.
    * @return the last received message
    */
   Message synchDeliver();

   /**
    * Deliver asynchronously the last available received message. If no message is available,
    * return <code>null</code> immediatly.
    * @return the last received message or <code>null</code> if none
    */
   Message asynchDeliver();

   /**
    * @return <code>true</code> if one received message is available, <code>false</code> otherwise
    */
   boolean availableMessage();
}
