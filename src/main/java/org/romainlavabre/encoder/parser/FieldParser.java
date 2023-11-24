package org.romainlavabre.encoder.parser;


import org.romainlavabre.encoder.Encoder;
import org.romainlavabre.encoder.TypeResolver;
import org.romainlavabre.encoder.annotation.Group;
import org.romainlavabre.encoder.annotation.Json;
import org.romainlavabre.encoder.config.EncoderConfigurer;
import org.romainlavabre.encoder.overwritter.DefaultOverwrite;
import org.romainlavabre.encoder.overwritter.Overwrite;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class FieldParser {

    private String group;

    private Overwrite overwrite;

    private String key;

    private boolean relation;

    private boolean onlyId;

    private boolean ascent;

    private Field field;

    private Method method;

    private boolean isCollectionOrArray;

    private Field relationFieldId;


    public ParsingResult parse( Object entity ) {
        Object value;

        if ( method != null ) {
            try {
                value = method.invoke( entity );
            } catch ( IllegalAccessException | InvocationTargetException e ) {
                e.printStackTrace();
                return ParsingResult.of( key );
            }
        } else {
            try {
                value = field.get( entity );
            } catch ( IllegalAccessException e ) {
                e.printStackTrace();
                return ParsingResult.of( key );
            }
        }


        Object newValue;

        if ( value != null ) {
            if ( relation ) {
                if ( isCollectionOrArray ) {
                    newValue = new ArrayList<>();

                    for ( Object subValue : ( Collection ) value ) {
                        if ( subValue == null ) {
                            continue;
                        }

                        ( ( List ) newValue ).add( onlyId ? getIdOfRelation( subValue ) : Encoder.encode( subValue, group ) );
                    }
                } else {
                    newValue = onlyId ? getIdOfRelation( value ) : Encoder.encode( value, group );
                }
            } else {
                newValue = value;
            }
        } else {
            newValue = null;
        }


        if ( overwrite != null ) {
            return ParsingResult.of(
                    key,
                    overwrite.overwrite( entity )
            );
        }

        return ParsingResult.of(
                key,
                newValue
        );
    }


    private Long getIdOfRelation( Object relation ) {
        try {
            for ( Method method : relation.getClass().getDeclaredMethods() ) {
                if ( method.getName().equals( "get" + relationFieldId.getName().substring( 0, 1 ).toUpperCase() + relationFieldId.getName().substring( 1 ) ) && method.getParameterCount() == 0 ) {
                    method.setAccessible( true );
                    return ( Long ) method.invoke( relation );
                }
            }
        } catch ( IllegalAccessException | InvocationTargetException e ) {
        }

        try {
            return ( Long ) relationFieldId.get( relation );
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
            return null;
        }
    }


    public static List< FieldParser > build( Field field ) {
        List< FieldParser > result = new ArrayList<>();
        Json                json   = field.getAnnotation( Json.class );

        if ( json == null ) {
            return result;
        }

        field.setAccessible( true );

        for ( Group group : json.groups() ) {
            FieldParser fieldParser = new FieldParser();
            fieldParser.group               = group.name();
            fieldParser.ascent              = group.ascent();
            fieldParser.onlyId              = group.onlyId();
            fieldParser.overwrite           = !group.overwrite().equals( DefaultOverwrite.class ) ? EncoderConfigurer.getInstance().getOverwrite( group.overwrite() ) : null;
            fieldParser.field               = field;
            fieldParser.isCollectionOrArray = TypeResolver.isArrayOrCollection( field );

            for ( Method method : field.getDeclaringClass().getDeclaredMethods() ) {
                if ( method.getName().equals( "get" + field.getName().substring( 0, 1 ).toUpperCase() + field.getName().substring( 1 ) ) && method.getParameterCount() == 0 ) {
                    method.setAccessible( true );
                    fieldParser.method = method;
                }
            }

            if ( group.forceEncoding()
                    || ( EncoderConfigurer.getInstance().getEntityAnnotationDetector() != null && field.getType().getAnnotation( EncoderConfigurer.getInstance().getEntityAnnotationDetector() ) != null )
                    || ( Collection.class.isAssignableFrom( field.getType() ) && ( ( ( Class< ? > ) ( ( ParameterizedType ) field.getGenericType() ).getActualTypeArguments()[ 0 ] ).isAnnotationPresent( EncoderConfigurer.getInstance().getEntityAnnotationDetector() ) ) ) ) {
                fieldParser.relation = true;
            }

            if ( fieldParser.relation && group.onlyId() ) {
                try {
                    if ( fieldParser.isCollectionOrArray ) {
                        fieldParser.relationFieldId = ( ( Class< ? > ) ( ( ParameterizedType ) field.getGenericType() ).getActualTypeArguments()[ 0 ] ).getDeclaredField( "id" );
                    } else {
                        fieldParser.relationFieldId = field.getType().getDeclaredField( "id" );
                    }
                } catch ( NoSuchFieldException e ) {
                    e.printStackTrace();
                }

                fieldParser.relationFieldId.setAccessible( true );
            }

            fieldParser.key = !group.key().isBlank() ? group.key() : ( fieldParser.relation && fieldParser.onlyId ? EncoderConfigurer.getInstance().format( field.getName() + "Id" ) : EncoderConfigurer.getInstance().format( field.getName() ) );

            result.add( fieldParser );
        }

        return result;
    }


    public String getGroup() {
        return group;
    }


    public boolean isAscent() {
        return ascent;
    }


    public static class ParsingResult {
        private String key;

        private Object value;


        public static ParsingResult of( String key ) {
            ParsingResult parsingResult = new ParsingResult();
            parsingResult.key   = key;
            parsingResult.value = null;
            return parsingResult;
        }


        public static ParsingResult of( String key, Object value ) {
            ParsingResult parsingResult = new ParsingResult();
            parsingResult.key   = key;
            parsingResult.value = value;
            return parsingResult;
        }


        public String getKey() {
            return key;
        }


        public Object getValue() {
            return value;
        }
    }
}
