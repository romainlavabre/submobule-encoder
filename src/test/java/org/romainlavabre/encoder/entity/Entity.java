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
import org.romainlavabre.encoder.put.PutHelloWorldEntity;

@JsonPut( groups = {
        @Group( name = "put", row = {
                @Row( key = "putted_property", handler = PutHelloWorldEntity.class )
        } )
} )
@jakarta.persistence.Entity
public class Entity {

    @Json( groups = {
            @Group( name = "default" ),
            @Group( name = "rename_key", key = "id_of_entity" ),
            @Group( name = "proxy" )
    } )
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Json( groups = {
            @Group( name = "default" ),
            @Group( name = "rename_key", key = "first_name_of_entity" ),
            @Group( name = "proxy" )
    } )
    private String firstName;

    @Json( groups = {
            @Group( name = "default" ),
            @Group( name = "rename_key", key = "last_name_of_entity" ),
            @Group( name = "proxy" )
    } )
    private String lastName;

    @Json( groups = {
            @Group( name = "default" ),
            @Group( name = "rename_key", key = "age_of_person_of_entity" ),
            @Group( name = "proxy" )
    } )
    private int ageOfPerson;

    @Json( groups = {
            @Group( name = "proxy" )
    } )
    private boolean old;

    @Json( groups = {
            @Group( name = "proxy" )
    } )
    private boolean money;

    @Json( groups = {
            @Group( name = "relation_auto_detected" ),
            @Group( name = "relation_not_auto_detected" ),
            @Group( name = "relation_not_auto_detected_managed_manually", overwrite = ManuallyEncodeRelation.class ),
            @Group( name = "not_only_id", onlyId = false ),
            @Group( name = "not_only_id_and_put", onlyId = false ),
            @Group( name = "merge_ascent", onlyId = false, ascent = true ),
            @Group( name = "proxy" )
    } )
    @OneToOne
    private Relation relation;


    public long getId() {
        return id;
    }


    public int getAgeOfPerson() {
        return ageOfPerson;
    }


    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public boolean isOld() {
        return old;
    }


    public Boolean hasMoney() {
        return money;
    }


    public Relation getRelation() {
        return relation;
    }


    public Entity setRelation( Relation relation ) {
        this.relation = relation;

        return this;
    }
}
