/* Coyright Eric Cariou, 2009 - 2011 */

package communication;

import java.util.Vector;

/**
 * Utility class for implementation of services: synchronized buffer of elements of any type T
 */
public class SynchronizedBuffer<T> {

    /**
     * Vector of unread received elements
     */
    private Vector<T> elements = new Vector<T>();

    /**
     * Get and remove the next available element in the buffer
     *
     * @param synchronicity if true, wait for an element to be available in the buffer, if not,
     * return immediatly (and return <code>null</code> if the buffer was empty)
     * @return an unread element of the buffer, or the <code>null</code> value
     */
    public synchronized T removeElement(boolean synchronicity) {
        if ((!synchronicity) && elements.isEmpty()) {
            return null;
        }

        while (elements.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(" **  " + e);
            }
        }

        T elt = elements.firstElement();
        try {
            elements.removeElementAt(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(" **  " + e);
        }

        return elt;
    }

    /**
     *
     * @return the number of elements in the buffer
     */
    public synchronized int available() {
        return elements.size();
    }

    /**
     * Add an element in the buffer
     * @param elt
     */
    public synchronized void addElement(T elt) {
        elements.add(elt);

        notifyAll();
    }
}
