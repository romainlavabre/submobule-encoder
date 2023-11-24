package org.romainlavabre.encoder;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class TypeResolver {

    public static boolean isArrayOrCollection( Field field ) {
        return field.getType().isArray() || Collection.class.isAssignableFrom( field.getType() );
    }
}
