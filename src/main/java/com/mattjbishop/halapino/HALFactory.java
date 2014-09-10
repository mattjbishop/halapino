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

import static com.google.common.base.Preconditions.checkNotNull;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by matt on 07/08/2014.
 */
public class HALFactory {

    private static HALFactory factoryInstance;

    // self link templates
    private Map<String, UriBuilder> selfBuilders;

    private LinkMap globalLinks;

    private HALFactory() {
    }

    public static HALFactory getFactory() {
        if (null == factoryInstance) {
            factoryInstance = new HALFactory(); // not thread safe !!!
        }

        return factoryInstance;
    }

    // factory methods

    public HALRepresentation getHALRepresentation(Object resource, UriInfo uriInfo)
            throws HALException {

        HALRepresentation representation = new HALRepresentation();
        Composer composer = new Composer(); // !!! this should be injected

        setGlobalLinks(representation); // what do you do if this is an absolute link?

        composer.compose(resource,representation);

        setLinks(representation, uriInfo);

        return representation;
    }

    public void register(Class resourceObject, UriBuilder templateBuilder) {

        if (selfBuilders == null) {
            selfBuilders = new HashMap<String, UriBuilder>();
        }

        if (selfBuilders.containsKey(resourceObject.getName())) {
            // throw an exception?
        }

        selfBuilders.put(resourceObject.getName(), templateBuilder);
    }

    public void addGlobalLink(String rel, Link link) {
        if (globalLinks == null) {
            globalLinks = new LinkMap();
        }

        globalLinks.addLink(rel, link);
    }

    public void addCurie(String name, String href) {
        Link link = new Link();
        link.setName(name);
        link.setHref(href);
        link.setTemplated("true");
        addGlobalLink("curies", link);
    }

    public UriBuilder getSelfBuilder(Class resourceObject) {
        UriBuilder builder = selfBuilders.get(resourceObject.getName());

        checkNotNull(builder);

        return builder.clone();
    }

    private void setLinks(HALRepresentation representation, UriInfo uriInfo) {

        UriBuilder baseUri = uriInfo.getBaseUriBuilder();
        setLink(representation, baseUri);

        Map<String, List<HALRepresentation>> resources = representation.getEmbedded();

        if (resources != null) {
            for (Map.Entry<String, List<HALRepresentation>> entry : resources.entrySet()) {
                for (HALRepresentation resource : entry.getValue()) {
                    String resourceUri = setLink(resource, baseUri);

                    Link link = new Link();
                    link.setHref(resourceUri);
                    representation.addLink(entry.getKey(), link);
                }
            }
        }
    }

    private void setGlobalLinks(HALRepresentation representation) {
        if (globalLinks != null) {
            representation.addLinks(globalLinks);
        }
    }

    private String setLink(HALRepresentation representation, UriBuilder baseUri) {
        String link = null;
        Object resource = representation.getResource();
        checkNotNull(resource);

        if (resource instanceof SelfBuilder)
        {
            String id = ((SelfBuilder) resource).getId();
            UriBuilder templateBuilder = HALFactory.getFactory().getSelfBuilder(resource.getClass());

            UriBuilder builder = baseUri.clone();

            builder.path(templateBuilder.build(id).toString());

            link = builder.build().toASCIIString();
            representation.setSelfLink(link);
        }

        return link;
    }

}
