package org.romainlavabre.encoder.exception;

public class NotInitializedException extends RuntimeException{

    public NotInitializedException(){
        super("Encoder lib not initialized, use EncoderConfigurer for fix it");
    }
}
