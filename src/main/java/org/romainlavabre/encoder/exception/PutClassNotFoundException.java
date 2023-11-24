package org.romainlavabre.encoder.exception;

public class PutClassNotFoundException extends RuntimeException {
    public PutClassNotFoundException( Class< ? > clazz ) {
        super( "Requested put " + clazz.getSimpleName() + " not found in configurer" );
    }
}
