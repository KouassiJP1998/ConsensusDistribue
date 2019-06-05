/* Coyright Eric Cariou, 2009 - 2011 */

package communication;

import java.net.InetAddress;

/**
 * Identification of a process in the IP context. In addition to the identifier attribute, 
 * adds a couple of IP address and port (the address of a socket)
 */
public class IPProcessIdentifier extends ProcessIdentifier {

    /**
     * IP Address of the process
     */
    protected InetAddress IPadd;
    /**
     * Port of the socket of the process
     */
    protected int port;

    /**
     * @return the IP address
     */
    public InetAddress getIPadd() {
        return IPadd;
    }

    /**
     * @param padd the IP address to set
     */
    public void setIPadd(InetAddress padd) {
        IPadd = padd;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @param id the (supposed unique) identifier of the process
     * @param padd the IP address of the process
     * @param port the port of the socket of the process
     */
    public IPProcessIdentifier(int id, InetAddress padd, int port) {
        super(id);
        IPadd = padd;
        this.port = port;
    }

    /**
     * @param padd
     * @param port
     */
    public IPProcessIdentifier(InetAddress padd, int port) {
        super(0);
        IPadd = padd;
        this.port = port;
    }

    @Override
    public String toString() {
        return new String("Id=" + id + " @=" + IPadd + ":" + port);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IPProcessIdentifier other = (IPProcessIdentifier) obj;
        if (this.IPadd != other.IPadd && (this.IPadd == null || !this.IPadd.equals(other.IPadd))) {
            return false;
        }
        if (this.port != other.port) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.IPadd != null ? this.IPadd.hashCode() : 0);
        hash = 47 * hash + this.port;
        return hash;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new IPProcessIdentifier(id, IPadd, port);
    }
}
