/* Coyright Eric Cariou, 2009 - 2011 */

package service;

/**
 * Define the types of messages: for each service, a given type has to be defined
 */
public enum MessageType {
    NONE,
    IDENTIFICATION,
    BASIC_BROADCAST,
    RELIABLE_BROADCAST,
    CONSENSUS
}
