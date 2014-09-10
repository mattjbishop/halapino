/*
 * Copyright 2014 Matt Bishop
 *
 * Builds on ideas in Halarious HalReflectionHelper https://github.com/surech/halarious
 * Copyright 2014 Stefan Urech
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

import com.mattjbishop.halapino.annotations.HALEmbedded;
import com.mattjbishop.halapino.annotations.HALLink;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by matt on 08/08/2014.
 */
public class ReflectionHelper {

    public static final int IGNORED_FIELD_MODIFIERS = Modifier.FINAL | Modifier.STATIC | Modifier.VOLATILE;

    public static boolean isResource(Field f){
        return isResource(new FieldAttributes(f));
    }

    public static boolean isResource(FieldAttributes field) {
        return field.getAnnotation(HALEmbedded.class) != null;
    }

    public static boolean isLink(Field f) {
        return isLink(new FieldAttributes(f));
    }

    public static boolean isLink(FieldAttributes field){
        HALLink annotation = field.getAnnotation(HALLink.class);
        return (annotation != null);
    }

    /**
     * Answers whether the Field should be copied. By default it ignores (answers 'false' to)
     * static, final, volatile and transient fields.
     *
     * @param f a Field of a Class.
     * @return true if the field should be copied into the resource. Otherwise, false.
     */
    public static boolean isIncluded(Field f)
    {
        return ((f.getModifiers() & IGNORED_FIELD_MODIFIERS) == 0);
    }

    public static Field[] getDeclaredFields(Class<?> type)
    {
        return type.getDeclaredFields();
    }

    public static Link getLink(Field f) {

        Link link = null;

        if (isLink(f)) {
            link = new Link();
            HALLink annotation = f.getAnnotation(HALLink.class);

            if (!annotation.name().isEmpty()) {
                link.setName(annotation.name());
            }

            if (!annotation.title().isEmpty()) {
                link.setTitle(annotation.title());
            }

            if (!annotation.type().isEmpty()) {
                link.setType(annotation.type());
            }

            if (!annotation.profile().isEmpty()) {
                link.setProfile(annotation.profile());
            }

            link.setTemplated(annotation.isTemplated() ? "true" : null);

            link.setDeprecation(annotation.isDeprecated() ? "true" : null);

            if (!annotation.hreflang().isEmpty()) {
                link.setHreflang(annotation.hreflang());
            }

            if (!annotation.curie().isEmpty()) {
                link.setCurie(annotation.curie());
            }
        }

        return link;
    }

    public static Embedded getResource(Field f) {
        Embedded embedded = null;

        if (isResource(f)) {
            embedded = new Embedded();
            HALEmbedded annotation = f.getAnnotation(HALEmbedded.class);

            if (!annotation.curie().isEmpty()) {
                embedded.setCurie(annotation.curie());
            }
        }
        return embedded;
    }
}
