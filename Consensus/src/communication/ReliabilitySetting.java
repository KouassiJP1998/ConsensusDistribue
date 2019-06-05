/* Coyright Eric Cariou, 2009 - 2011 */

package communication;

/**
 * Set the reliabily settings for a communication element.
 */
public class ReliabilitySetting {

    /**
     * Precise if debug information, concerning message lost and transmission delays,
     * must be printed on standard output
     */
    private boolean reliable = true;
    private FaultLevel packetLostLevel = FaultLevel.NONE;
    private FaultLevel transmissionDelayLowerBound = FaultLevel.NONE;
    private FaultLevel transmissionDelayUpperBound = FaultLevel.NONE;
    private FaultLevel crashLevel = FaultLevel.NONE;
    private boolean debugFault = false;

    public boolean isDebugFault() {
        return debugFault;
    }

    public FaultLevel getPacketLostLevel() {
        return packetLostLevel;
    }

    public boolean isReliable() {
        return reliable;
    }

    public FaultLevel getTransmissionDelayLowerBound() {
        return transmissionDelayLowerBound;
    }

    public FaultLevel getTransmissionDelayUpperBound() {
        return transmissionDelayUpperBound;
    }

    public FaultLevel getCrashLevel() {
        return crashLevel;
    }

    /**
     * @param reliable the reliable to set
     */
    public void setReliable(boolean reliable) {
        this.reliable = reliable;
    }

    /**
     * @param packetLostLevel the packetLostLevel to set
     */
    public void setPacketLostLevel(FaultLevel packetLostLevel) {
        this.packetLostLevel = packetLostLevel;
    }

    /**
     * @param transmissionDelayLowerBound the transmissionDelayLowerBound to set
     */
    public void setTransmissionDelayLowerBound(FaultLevel transmissionDelayLowerBound) {
        this.transmissionDelayLowerBound = transmissionDelayLowerBound;
    }

    /**
     * @param transmissionDelayUpperBound the transmissionDelayUpperBound to set
     */
    public void setTransmissionDelayUpperBound(FaultLevel transmissionDelayUpperBound) {
        this.transmissionDelayUpperBound = transmissionDelayUpperBound;
    }

    /**
     * @param crashLevel the crashLevel to set
     */
    public void setCrashLevel(FaultLevel crashLevel) {
        this.crashLevel = crashLevel;
    }

    /**
     * @param debugFault the debugFault to set
     */
    public void setDebugFault(boolean debugFault) {
        this.debugFault = debugFault;
    }

    /**
     * Create a set of reliabily values for a communication element
     * @param reliable reliabily (information on packet losts when sending messages) of the communication
     * @param packetLostLevel level of lost of packet send through the network
     * @param transmissionDelayLowerBound level of the minimum delay for a message to join the receiver element
     * @param transmissionDelayUpperBound level of the minimum delay for a message to join the receiver element
     * @param crashLevel level of process crash
     * @param debugFault precise if debug information, concerning message lost and transmission delays,
     * must be printed on standard output
     */
    public ReliabilitySetting(boolean reliable, FaultLevel packetLostLevel,
            FaultLevel transmissionDelayLowerBound, FaultLevel transmissionDelayUpperBound,
            FaultLevel crashLevel, boolean debugFault) {
        this.reliable = reliable;
        this.packetLostLevel = packetLostLevel;
        this.transmissionDelayLowerBound = transmissionDelayLowerBound;
        this.transmissionDelayUpperBound = transmissionDelayUpperBound;
        this.crashLevel = crashLevel;
        this.debugFault = debugFault;
    }

    /**
     * Create a set of reliability values for a communication with default value (no error).
     */
    public ReliabilitySetting() {
        
    }
}
