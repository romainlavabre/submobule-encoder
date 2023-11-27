package org.romainlavabre.encoder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.romainlavabre.encoder.annotation.Group;
import org.romainlavabre.encoder.annotation.Json;
import org.romainlavabre.encoder.annotation.JsonPut;
import org.romainlavabre.encoder.annotation.Row;
import org.romainlavabre.encoder.put.PutHelloWorldRelation;

@JsonPut( groups = {
        @Group( name = "not_only_id_and_put", row = {
                @Row( key = "putted_property", handler = PutHelloWorldRelation.class )
        } )
} )
@Entity
public class Relation {


    @Json( groups = {
            @Group( name = "_default" ),
            @Group( name = "relation_not_auto_detected" ),
            @Group( name = "not_only_id" ),
            @Group( name = "merge_ascent" )
    } )
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Json( groups = {
            @Group( name = "_default" ),
            @Group( name = "relation_not_auto_detected" ),
            @Group( name = "not_only_id" ),
            @Group( name = "merge_ascent" )
    } )
    private String accountNumber;
}
