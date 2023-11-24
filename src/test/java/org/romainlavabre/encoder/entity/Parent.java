package org.romainlavabre.encoder.entity;

import org.romainlavabre.encoder.annotation.Group;
import org.romainlavabre.encoder.annotation.Json;

public class Parent {

    public static class Child {
        @Json( groups = @Group )
        private long id;
    }
}
