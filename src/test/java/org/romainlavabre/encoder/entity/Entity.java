package org.romainlavabre.encoder.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import org.romainlavabre.encoder.annotation.Group;
import org.romainlavabre.encoder.annotation.Json;
import org.romainlavabre.encoder.annotation.JsonPut;
import org.romainlavabre.encoder.annotation.Row;
import org.romainlavabre.encoder.overwrite.ManuallyEncodeRelation;
import org.romainlavabre.encoder.put.PutHelloWorld;

@JsonPut( groups = {
        @Group( name = "put", row = {
                @Row( key = "putted_property", handler = PutHelloWorld.class )
        } )
} )
@jakarta.persistence.Entity
public class Entity {

    @Json( groups = {
            @Group( name = "default" ),
            @Group( name = "rename_key", key = "id_of_entity" )
    } )
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Json( groups = {
            @Group( name = "default" ),
            @Group( name = "rename_key", key = "first_name_of_entity" )
    } )
    private String firstName;

    @Json( groups = {
            @Group( name = "default" ),
            @Group( name = "rename_key", key = "last_name_of_entity" )
    } )
    private String lastName;

    @Json( groups = {
            @Group( name = "default" ),
            @Group( name = "rename_key", key = "age_of_person_of_entity" )
    } )
    private int ageOfPerson;

    @Json( groups = {
            @Group( name = "relation_auto_detected" ),
            @Group( name = "relation_not_auto_detected" ),
            @Group( name = "relation_not_auto_detected_managed_manually", overwrite = ManuallyEncodeRelation.class ),
            @Group( name = "not_only_id", onlyId = false ),
            @Group( name = "merge_ascent", onlyId = false, ascent = true )
    } )
    @OneToOne
    private Relation relation;


    public Relation getRelation() {
        return relation;
    }


    public Entity setRelation( Relation relation ) {
        this.relation = relation;

        return this;
    }
}
