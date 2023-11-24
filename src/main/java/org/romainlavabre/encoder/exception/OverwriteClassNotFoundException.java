package org.romainlavabre.encoder.exception;

public class OverwriteClassNotFoundException extends RuntimeException {
    public OverwriteClassNotFoundException( Class< ? > clazz ) {
        super( "Requested overwrite " + clazz.getSimpleName() + " not found in configurer" );
    }
}
