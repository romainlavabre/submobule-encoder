package org.romainlavabre.encoder.overwritter;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface Overwrite< T > {

    /**
     * @param data Subject class
     * @return Value of field
     */
    Object overwrite( T data );
}
