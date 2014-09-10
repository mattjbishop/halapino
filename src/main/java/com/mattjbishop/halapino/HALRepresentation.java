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

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * lightweight representation, serialised by Jackson to JSON.
 */
@JsonPropertyOrder({"_links"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HALRepresentation {

    @JsonSerialize(using = LinkSerializer.class)
    @JsonProperty("_links")
    private LinkMap _links;

    @JsonSerialize(using = EmbeddedSerializer.class)
    @JsonProperty("_embedded")
    private Map<String, List<HALRepresentation>> _embedded;

    @JsonUnwrapped
    private Object resource = null;

    public HALRepresentation() {
        _links = new LinkMap(); // !!! this should be injected
    }

    public Object getResource() {
        return resource;
    }

    public void setResource(Object resource) {
        this.resource = resource;
    }

    public void addLink(String rel, Link link) {
        _links.addLink(rel, link, false);
    }

    public void addLinks(String rel, List<Link> links) {
        checkNotNull(rel, "Can't add link with no rel");
        checkNotNull(links, "Can't add a link that does not exist");
        _links.addLinks(rel, links);
    }

    public void addLinks(LinkMap links) {
        checkNotNull(links);
        _links.putAll(links);
    }

    public void addLink(String rel, Link link, boolean isSingleLink) {
        if (link.isTemplated()) {
            checkArgument(link.hasRelTemplate(), "not a properly formed template link - must have {rel} token");
        }

        _links.addLink(rel, link, isSingleLink);
    }

    public void addEmbedded(String key, HALRepresentation resource) {
        if (_embedded == null) {
            _embedded = new HashMap<String, List<HALRepresentation>>();
        }

        checkNotNull(key, "Cannot embed resource using null key");
        checkNotNull(resource, "Cannot embed null resource");

        List<HALRepresentation> forRel = acquireResourcesForRel(key);
        forRel.add(resource);

    }

    @JsonIgnore
    public Map<String, List<HALRepresentation>> getEmbedded() {
        return _embedded;
    }

    public boolean hasCurie(String name) {
       return _links.hasNamedLink("curies", name);
    }


    public void setSelfLink(String uri) {

        checkNotNull(uri, "Cannot set the self link to be null");

        Link self = new Link();
        self.setHref(uri);

        addLink("self", self, true);
    }

    @JsonIgnore
    public Link getSelfLink()
    {
        List<Link> linksForName = _links.get("self");

        checkNotNull(linksForName);

        return linksForName.iterator().next();
    }

    private List<HALRepresentation> acquireResourcesForRel(String rel)
    {
        List<HALRepresentation> forRel = _embedded.get(rel);

        if (forRel == null)
        {
            forRel = new ArrayList<HALRepresentation>();
            _embedded.put(rel, forRel);
        }

        return forRel;
    }
}