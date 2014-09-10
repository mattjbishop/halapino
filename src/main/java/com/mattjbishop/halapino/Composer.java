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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Created by matt on 08/08/2014.
 * Creates the fully formed HAL representation for passing back to clients
 */
public class Composer {

    protected void compose(Object from, HALRepresentation to)
            throws HALException{

        to.setResource(from);

        Field[] fields = ReflectionHelper.getDeclaredFields(from.getClass());

        for (Field f : fields) {
            if (ReflectionHelper.isIncluded(f)) {
                read(to, from, f);
            }
        }
    }

    private void read(HALRepresentation to, Object from, Field field)
            throws HALException {

        if (ReflectionHelper.isLink(field)) {
            readLink(to, from, field);
        }

        if (ReflectionHelper.isResource(field)) {
            readResource(to, from, field);
        }
    }

    private void readLink(HALRepresentation to, Object from, Field field)
            throws HALException {

        field.setAccessible(true);

        Link link = ReflectionHelper.getLink(field);
        checkNotNull(link, "Field is not a link");

        String linkName = addCurie(to, field.getName(), link.getCurie());

        Object value = null;
        try {
            value = field.get(from);
        } catch (IllegalAccessException e) {
            throw new HALException(e.getMessage());
        }

        if (value instanceof String) {
            link.setHref((String) value);
        }

        to.addLink(linkName, link);
    }

    private void readResource(HALRepresentation to, Object from, Field field)
            throws HALException{
        field.setAccessible(true);

        Object value = null;
        try {
            value = field.get(from);
        } catch (IllegalAccessException e) {
            throw new HALException(e.getMessage());
        }

        Embedded resource = ReflectionHelper.getResource(field);
        checkNotNull(resource, "Field is not an embedded resource");

        String embeddedName = addCurie(to, field.getName(), resource.getCurie());

        if (value instanceof Collection) {
            for (Object o : (Collection)value) {
                readResource(to, o, embeddedName);
            }
        } else {
            readResource(to, value, embeddedName);
        }

    }

    private void readResource(HALRepresentation to, Object from, String name)
            throws HALException {
        HALRepresentation embeddedResource = new HALRepresentation();
        compose(from, embeddedResource);
        to.addEmbedded(name, embeddedResource);
    }

    private String addCurie(HALRepresentation representation, String fieldName, String curie) {
        checkNotNull(fieldName, "fieldname is null");
        String name = fieldName;

        if ((curie != null) &&  (!curie.isEmpty())){
            checkArgument(representation.hasCurie(curie), "no curie definition found");
            name = curie + ":" + fieldName;
        }
        return name;
    }
}
