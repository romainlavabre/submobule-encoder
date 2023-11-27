package org.romainlavabre.encoder;

import org.romainlavabre.encoder.parser.EntityParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Encoder {

    private final static List< EntityParser > entitiesParser = new ArrayList<>();


    public static < T > Map< String, Object > encode( final T entity ) {
        return Encoder.encode( entity, "_default" );
    }


    public static Map< String, Object > encode( final Map< String, Object > entity ) {
        return Encoder.encode( entity, "_default" );
    }


    public static < T > List< Map< String, Object > > encode( final List< T > entity ) {
        return Encoder.encode( entity, "_default" );
    }


    public static < T > List< Map< String, Object > > encode( final T[] entity ) {
        return Encoder.encode( entity, "_default" );
    }


    public static < T > Map< String, Object > encode( final T entity, final String group ) {
        return Encoder.core( entity, group );
    }


    public static < T > Map< String, Object > encode( final Map< String, T > entity, final String group ) {
        final Map< String, Object > map = new HashMap<>();

        for ( final Map.Entry< String, T > entry : entity.entrySet() ) {
            map.put( entry.getKey(), Encoder.core( entry.getValue(), group ) );
        }

        return map;
    }


    public static < T > List< Map< String, Object > > encode( final List< T > entity, final String group ) {

        final List< Map< String, Object > > list = new ArrayList<>();

        for ( final T unit : entity ) {
            list.add( Encoder.core( unit, group ) );
        }

        return list;
    }


    public static < T > List< Map< String, Object > > encode( final T[] entities, final String group ) {
        final List< Map< String, Object > > list = new ArrayList<>();

        for ( final T unit : entities ) {
            list.add( Encoder.core( unit, group ) );
        }

        return list;
    }


    protected static Map< String, Object > core( final Object entity, final String targetGroup ) {
        if ( entity == null ) {
            return null;
        }

        Class< ? > search;

        if ( entity.getClass().getSimpleName().toUpperCase().contains( "HIBERNATE" )
                && entity.getClass().getSimpleName().toUpperCase().contains( "PROXY" ) ) {
            search = entity.getClass().getSuperclass();
        } else {
            search = entity.getClass();
        }

        for ( EntityParser entityParser : Encoder.entitiesParser ) {
            if ( entityParser.getType().equals( search ) ) {
                return entityParser.parse( entity, targetGroup );
            }
        }

        EntityParser entityParser = new EntityParser( search );

        Encoder.entitiesParser.add( entityParser );

        return entityParser.parse( entity, targetGroup );
    }


    public static void init() {
        entitiesParser.clear();
    }
}
