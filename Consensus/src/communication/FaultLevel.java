/* Coyright Eric Cariou, 2009 - 2011 */

package communication;

/**
 * Fault level: set the level of packet lost or transmission delay for a communication element.
 * Five values are defined: NONE, LOW, MEDIUM, HIGH, HIGHEST and FULL.
 */
public enum FaultLevel {
    /**
     * 
     */
    NONE,
    /**
     *
     */
    LOW,
    /**
     * 
     */
    MEDIUM,
    /**
     *
     */
    HIGH,
    /**
     * 
     */
    HIGHEST,
    /**
     * 
     */
    FULL;
	
    /**
     * The value of the fault level
     */
    protected int value;
	
	/**
	 * The value of a fault level as an integer.
	 * 
	 * @return 0 for NONE, 1 for LOW, 2 for MEDIUM, 3 for HIGH, 4 for HIGHEST, 5 for FULL
	 */
	public int getValue()
	{
		switch(this) {
			case NONE : return 0;
			case LOW : return 1;
			case MEDIUM : return 2;
			case HIGH : return 3;
			case HIGHEST : return 4;
			case FULL : return 5;
		}
		return 0;
	}
}
