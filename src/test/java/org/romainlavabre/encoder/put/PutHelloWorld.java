package org.romainlavabre.encoder.put;

import org.romainlavabre.encoder.entity.Entity;

public class PutHelloWorld implements Put< Entity > {
    @Override
    public Object build( Entity entity ) {
        return "Hello world !";
    }
}
