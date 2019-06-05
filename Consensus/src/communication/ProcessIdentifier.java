/* Coyright Eric Cariou, 2009 - 2011 */

package communication;

/**
 * Generic identifier of a process. Is simply defines an identifier value, that is supposed
 * to be unique for all processes of the system.
 */
public class ProcessIdentifier implements java.io.Serializable {

	
	/**
	 * The (supposed unique) identifier of a process
	 */
	int id;

	/**
	 * @return the process identifier
	 */
	public int getId() {
		return id;
	}


	/**
	 * @param id the process identifier to set
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * @param id
	 */
	public ProcessIdentifier(int id) {
		super();
		this.id = id;
	}


	
}
