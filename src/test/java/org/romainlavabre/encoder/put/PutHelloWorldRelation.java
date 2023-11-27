package org.romainlavabre.encoder.put;

import org.romainlavabre.encoder.entity.Relation;

public class PutHelloWorldRelation implements Put< Relation > {
    @Override
    public Object build( Relation relation ) {
        return "Hello world !";
    }
}
