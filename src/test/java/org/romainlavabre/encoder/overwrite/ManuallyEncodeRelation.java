package org.romainlavabre.encoder.overwrite;

import org.romainlavabre.encoder.Encoder;
import org.romainlavabre.encoder.entity.Entity;
import org.romainlavabre.encoder.overwritter.Overwrite;

public class ManuallyEncodeRelation implements Overwrite< Entity > {
    @Override
    public Object overwrite( Entity data ) {
        return Encoder.encode( data.getRelation() );
    }
}
