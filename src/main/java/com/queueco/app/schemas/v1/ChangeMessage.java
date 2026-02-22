package com.queueco.app.schemas.v1;

import java.util.ArrayList;

import com.queueco.app.PriceQty;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.deser.std.StdDeserializer;

class ChangeMessageDeserializer extends StdDeserializer<ChangeMessage> {

    protected ChangeMessageDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ChangeMessage deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {

    }

}

@JsonDeserialize(using = ChangeMessageDeserializer.class)
public class ChangeMessage {
    private ArrayList<PriceQty> pqArray;
}
