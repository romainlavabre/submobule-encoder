package org.romainlavabre.encoder.config;

import org.romainlavabre.encoder.Encoder;
import org.romainlavabre.encoder.Formatter;
import org.romainlavabre.encoder.exception.NotInitializedException;
import org.romainlavabre.encoder.exception.OverwriteClassNotFoundException;
import org.romainlavabre.encoder.exception.PutClassNotFoundException;
import org.romainlavabre.encoder.overwritter.Overwrite;
import org.romainlavabre.encoder.put.Put;

import java.util.ArrayList;
import java.util.List;

public class EncoderConfigurer {
    private static EncoderConfigurer INSTANCE;

    protected       Class             entityAnnotationDetector;
    protected final List< Overwrite > overwrites;
    protected final List< Put >       puts;
    protected       FieldFormat       fieldFormat = FieldFormat.SNAKE_CASE;


    public EncoderConfigurer() {
        INSTANCE   = this;
        overwrites = new ArrayList<>();
        puts       = new ArrayList<>();
    }


    public static EncoderConfigurer init() {
        Encoder.init();
        return new EncoderConfigurer();
    }


    public Class getEntityAnnotationDetector() {
        return entityAnnotationDetector;
    }


    /**
     * If you're using JPA, pass the Entity.class (or other) annotation
     * This allows the encoder to understand what the entities are and convert them to ID
     *
     * @param annotation Annotation class (jakarta.persistence.Entity, other ...)
     */
    public EncoderConfigurer setEntityAnnotationDetector( Class< ? > annotation ) {
        this.entityAnnotationDetector = annotation;

        return this;
    }


    public Overwrite getOverwrite( Class< ? extends Overwrite< ? > > overwrite ) {
        for ( Overwrite o : overwrites ) {
            if ( o.getClass().equals( overwrite ) ) {
                return o;
            }
        }

        throw new OverwriteClassNotFoundException( overwrite );
    }


    /**
     * Instantiate the overwrite classes and give them
     *
     * @param overwrite Overwrite class implementation
     */
    public EncoderConfigurer addOverwrite( Overwrite overwrite ) {
        this.overwrites.add( overwrite );

        return this;
    }


    public Put getPut( Class< ? extends Put > put ) {
        for ( Put p : puts ) {
            if ( p.getClass().equals( put ) ) {
                return p;
            }
        }

        throw new PutClassNotFoundException( put );
    }


    /**
     * Instantiate the put classes and give them
     *
     * @param put Put class implementation
     */
    public EncoderConfigurer addPut( Put put ) {
        this.puts.add( put );

        return this;
    }


    public String format( String fieldName ) {
        switch ( fieldFormat ) {
            case SNAKE_CASE -> {
                return Formatter.toSnakeCase( fieldName );
            }
            case PASCAL_CASE -> {
                return Formatter.toPascalCase( fieldName );
            }
        }

        return fieldName;
    }


    /**
     * Default is snake case
     *
     * @param fieldFormat SNAKE_CASE, CAMEL_CASE, PASCAL_CASE
     */
    public EncoderConfigurer setFieldFormat( FieldFormat fieldFormat ) {
        this.fieldFormat = fieldFormat;

        return this;
    }


    public void build() {
    }


    public static EncoderConfigurer getInstance() {
        if ( INSTANCE == null ) {
            throw new NotInitializedException();
        }

        return INSTANCE;
    }

}
