/*
 * Copyright 2014 Matt Bishop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mattjbishop.halapino;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by matt on 07/08/2014.
 *
 * serializes embedded resources
 */
public class EmbeddedSerializer
       extends JsonSerializer<Map<String, List<HALRepresentation>>> {

    public EmbeddedSerializer() {
        super();
    }

    @Override
    public void serialize(Map<String, List<HALRepresentation>> embeddedResources, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        if (embeddedResources != null) {
            jgen.writeStartObject();
            writeOutResources(embeddedResources, jgen);
            jgen.writeEndObject();
        }

    }

    private void writeOutResources(Map<String, List<HALRepresentation>> resources, JsonGenerator jgen)
            throws IOException, JsonProcessingException {
        for (Map.Entry<String, List<HALRepresentation>> entry : resources.entrySet()) {

            if (entry.getValue().size() == 1) // Write single resource
            {
                HALRepresentation resource = entry.getValue().iterator().next();
                jgen.writeObjectField(entry.getKey(), resource);
            }
            else // write resource collection
            {
                jgen.writeArrayFieldStart(entry.getKey());

                for (HALRepresentation resource : entry.getValue())
                {
                    jgen.writeObject(resource);
                }

                jgen.writeEndArray();
            }
        }
    }
}
