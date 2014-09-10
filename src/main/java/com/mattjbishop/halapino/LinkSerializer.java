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
 * Created by matt on 08/08/2014.
 */
public class LinkSerializer
        extends JsonSerializer<Map<String, List<Link>>> {

    public LinkSerializer() {
        super();
    }

    @Override
    public void serialize(Map<String, List<Link>> links, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        writeOutLinks(links, jgen);
        jgen.writeEndObject();
    }

    private void writeOutLinks(Map<String, List<Link>> links, JsonGenerator jgen)
            throws IOException, JsonProcessingException
    {
        for (Map.Entry<String, List<Link>> entry : links.entrySet())
        {
            if (entry.getValue().size() == 1) // Write single link
            {
                Link link = entry.getValue().iterator().next();
                reconcileTemplated(link);
                jgen.writeObjectField(entry.getKey(), link);
            }
            else // Write link array
            {
                jgen.writeArrayFieldStart(entry.getKey());

                for (Link link : entry.getValue())
                {
                    reconcileTemplated(link);
                    jgen.writeObject(link);
                }

                jgen.writeEndArray();
            }

        }

    }

    private void reconcileTemplated(Link link) {
        if (link.isTemplated())
        {
            link.setTemplated(link.hasRelTemplate() ? "true" : "false");
        }
    }

}
