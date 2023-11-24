package org.romainlavabre.encoder;

import org.junit.Assert;
import org.junit.Test;
import org.romainlavabre.encoder.config.EncoderConfigurer;
import org.romainlavabre.encoder.config.FieldFormat;
import org.romainlavabre.encoder.entity.Entity;
import org.romainlavabre.encoder.entity.Relation;
import org.romainlavabre.encoder.exception.OverwriteClassNotFoundException;
import org.romainlavabre.encoder.exception.PutClassNotFoundException;
import org.romainlavabre.encoder.overwrite.ManuallyEncodeRelation;
import org.romainlavabre.encoder.put.PutHelloWorld;

import java.util.Map;

public class EncoderTest {

    @Test
    public void test_default_snake_case() {
        EncoderConfigurer
                .init()
                .setFieldFormat( FieldFormat.SNAKE_CASE )
                .addOverwrite( new ManuallyEncodeRelation() )
                .addPut( new PutHelloWorld() )
                .build();

        Entity entity1 = new Entity();

        Map< String, Object > result = Encoder.encode( entity1, "default" );

        Assert.assertTrue( result.containsKey( "id" ) );
        Assert.assertTrue( result.containsKey( "first_name" ) );
        Assert.assertTrue( result.containsKey( "last_name" ) );
        Assert.assertTrue( result.containsKey( "age_of_person" ) );
    }


    @Test
    public void test_default_camel_case() {
        EncoderConfigurer
                .init()
                .setFieldFormat( FieldFormat.CAMEL_CASE )
                .addOverwrite( new ManuallyEncodeRelation() )
                .addPut( new PutHelloWorld() )
                .build();

        Entity entity1 = new Entity();

        Map< String, Object > result = Encoder.encode( entity1, "default" );

        Assert.assertTrue( result.containsKey( "id" ) );
        Assert.assertTrue( result.containsKey( "firstName" ) );
        Assert.assertTrue( result.containsKey( "lastName" ) );
        Assert.assertTrue( result.containsKey( "ageOfPerson" ) );
    }


    @Test
    public void test_default_pascal_case() {
        EncoderConfigurer
                .init()
                .setFieldFormat( FieldFormat.PASCAL_CASE )
                .addOverwrite( new ManuallyEncodeRelation() )
                .addPut( new PutHelloWorld() )
                .build();

        Entity entity1 = new Entity();

        Map< String, Object > result = Encoder.encode( entity1, "default" );

        Assert.assertTrue( result.containsKey( "Id" ) );
        Assert.assertTrue( result.containsKey( "FirstName" ) );
        Assert.assertTrue( result.containsKey( "LastName" ) );
        Assert.assertTrue( result.containsKey( "AgeOfPerson" ) );
    }


    @Test
    public void test_rename_key() {
        EncoderConfigurer
                .init()
                .setFieldFormat( FieldFormat.SNAKE_CASE )
                .addOverwrite( new ManuallyEncodeRelation() )
                .addPut( new PutHelloWorld() )
                .build();

        Entity entity1 = new Entity();

        Map< String, Object > result = Encoder.encode( entity1, "rename_key" );

        Assert.assertTrue( result.containsKey( "id_of_entity" ) );
        Assert.assertTrue( result.containsKey( "first_name_of_entity" ) );
        Assert.assertTrue( result.containsKey( "last_name_of_entity" ) );
        Assert.assertTrue( result.containsKey( "age_of_person_of_entity" ) );
    }


    @Test
    public void test_relation_auto_detected() {
        EncoderConfigurer
                .init()
                .setEntityAnnotationDetector( jakarta.persistence.Entity.class )
                .setFieldFormat( FieldFormat.SNAKE_CASE )
                .addOverwrite( new ManuallyEncodeRelation() )
                .addPut( new PutHelloWorld() )
                .build();

        Entity entity1 = new Entity();

        Map< String, Object > result = Encoder.encode( entity1, "relation_auto_detected" );

        Assert.assertTrue( result.containsKey( "relation_id" ) );
        Assert.assertEquals( 1, result.size() );
    }


    @Test
    public void test_relation_not_auto_detected() {
        EncoderConfigurer
                .init()
                .setFieldFormat( FieldFormat.SNAKE_CASE )
                .addOverwrite( new ManuallyEncodeRelation() )
                .addPut( new PutHelloWorld() )
                .build();

        Entity entity1 = new Entity();

        Map< String, Object > result = Encoder.encode( entity1, "relation_not_auto_detected" );

        Assert.assertTrue( result.containsKey( "relation" ) );
        Assert.assertNull( result.get( "relation" ) );
    }


    @Test
    public void test_relation_not_auto_detected_managed_manually() {
        EncoderConfigurer
                .init()
                .setFieldFormat( FieldFormat.SNAKE_CASE )
                .addOverwrite( new ManuallyEncodeRelation() )
                .addPut( new PutHelloWorld() )
                .build();

        Entity entity1 = new Entity();
        entity1.setRelation( new Relation() );

        Map< String, Object > result = Encoder.encode( entity1, "relation_not_auto_detected_managed_manually" );

        Assert.assertTrue( result.containsKey( "relation" ) );
        Assert.assertTrue( result.get( "relation" ) instanceof Map< ?, ? > );
        Assert.assertTrue( ( ( Map< ?, ? > ) result.get( "relation" ) ).containsKey( "id" ) );
        Assert.assertTrue( ( ( Map< ?, ? > ) result.get( "relation" ) ).containsKey( "account_number" ) );
    }


    @Test( expected = OverwriteClassNotFoundException.class )
    public void test_missing_overwrite_throw_exception() {
        EncoderConfigurer
                .init()
                .setFieldFormat( FieldFormat.SNAKE_CASE )
                .addPut( new PutHelloWorld() )
                .build();

        Entity entity1 = new Entity();

        Encoder.encode( entity1, "relation_not_auto_detected_managed_manually" );
    }


    @Test
    public void test_put() {
        EncoderConfigurer
                .init()
                .setFieldFormat( FieldFormat.SNAKE_CASE )
                .addOverwrite( new ManuallyEncodeRelation() )
                .addPut( new PutHelloWorld() )
                .build();

        Entity entity1 = new Entity();
        entity1.setRelation( new Relation() );

        Map< String, Object > result = Encoder.encode( entity1, "put" );

        Assert.assertTrue( result.containsKey( "putted_property" ) );
    }


    @Test( expected = PutClassNotFoundException.class )
    public void test_missing_put_throw_exception() {
        EncoderConfigurer
                .init()
                .setFieldFormat( FieldFormat.SNAKE_CASE )
                .addOverwrite( new ManuallyEncodeRelation() )
                .build();

        Entity entity1 = new Entity();

        Encoder.encode( entity1, "put" );
    }


    @Test
    public void test_relation_not_only_id() {
        EncoderConfigurer
                .init()
                .setEntityAnnotationDetector( jakarta.persistence.Entity.class )
                .setFieldFormat( FieldFormat.SNAKE_CASE )
                .addOverwrite( new ManuallyEncodeRelation() )
                .addPut( new PutHelloWorld() )
                .build();

        Entity entity1 = new Entity();
        entity1.setRelation( new Relation() );

        Map< String, Object > result = Encoder.encode( entity1, "not_only_id" );

        Assert.assertTrue( result.containsKey( "relation" ) );
        Assert.assertTrue( result.get( "relation" ) instanceof Map< ?, ? > );
        Assert.assertTrue( ( ( Map< ?, ? > ) result.get( "relation" ) ).containsKey( "id" ) );
        Assert.assertTrue( ( ( Map< ?, ? > ) result.get( "relation" ) ).containsKey( "account_number" ) );
    }


    @Test
    public void test_relation_ascent() {
        EncoderConfigurer
                .init()
                .setEntityAnnotationDetector( jakarta.persistence.Entity.class )
                .setFieldFormat( FieldFormat.SNAKE_CASE )
                .addOverwrite( new ManuallyEncodeRelation() )
                .addPut( new PutHelloWorld() )
                .build();

        Entity entity1 = new Entity();
        entity1.setRelation( new Relation() );

        Map< String, Object > result = Encoder.encode( entity1, "merge_ascent" );

        Assert.assertFalse( result.containsKey( "relation" ) );
        Assert.assertTrue( result.containsKey( "id" ) );
        Assert.assertTrue( result.containsKey( "account_number" ) );
    }
}
