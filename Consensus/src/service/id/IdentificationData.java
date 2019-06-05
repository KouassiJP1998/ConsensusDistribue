/* Coyright Eric Cariou, 2009 - 2011 */

package service.id;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Abstract data for the identification service. This class is specialized depending
 * of the concrete data to store.
 */
public abstract class IdentificationData implements java.io.Serializable {

    /**
     * When using UDP socket, we must manage data under a byte array.
     * @return the serialized version of the current data
     */
    public byte[] serialize() {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(this);
            return byteStream.toByteArray();
        } catch (IOException e) {
            System.err.println("[ERROR] Serialization error ! ");
            System.err.println(e);
        }
        return null;
    }

    /**
     * When using UDP socket, we must manage data under a byte array.
     * @param data the serialized version of a data
     * @return the data object corresponding to the serialized version
     */
    public static IdentificationData unserialize(byte[] data) {
        ObjectInputStream objectStream = null;
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
            objectStream = new ObjectInputStream(byteStream);
            return (IdentificationData) objectStream.readObject();
        } catch (Exception e) {
            System.err.println("[ERROR] Serialization error ! ");
            System.err.println(e);
        }
        return null;
    }
}
