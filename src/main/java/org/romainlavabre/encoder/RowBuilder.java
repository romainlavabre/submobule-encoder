package org.romainlavabre.encoder;


import org.romainlavabre.encoder.annotation.Group;
import org.romainlavabre.encoder.annotation.JsonPut;
import org.romainlavabre.encoder.annotation.Row;
import org.romainlavabre.encoder.config.EncoderConfigurer;
import org.romainlavabre.encoder.parser.FieldParser;
import org.romainlavabre.encoder.put.Put;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class RowBuilder {

    private String group;

    private Put put;

    private String key;


    public FieldParser.ParsingResult parse( Object entity ) {
        return FieldParser.ParsingResult.of(
                key,
                put.build( entity )
        );
    }


    public static List< RowBuilder > build( Class< ? > entity ) {
        List< RowBuilder > result  = new ArrayList<>();
        JsonPut            jsonPut = entity.getAnnotation( JsonPut.class );

        if ( jsonPut == null ) {
            return result;
        }

        for ( Group group : jsonPut.groups() ) {
            for ( Row row : group.row() ) {
                RowBuilder rowBuilder = new RowBuilder();
                rowBuilder.group = group.name();
                rowBuilder.key   = row.key();
                rowBuilder.put   = EncoderConfigurer.getInstance().getPut( row.handler() );

                result.add( rowBuilder );
            }
        }

        return result;
    }


    public String getGroup() {
        return group;
    }
}
