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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by matt on 30/08/2014.
 */
public class LinkMap extends HashMap<String, List<Link>> {

    public void addLink(String rel, Link link) {
        addLink(rel, link, false);
    }

    public void addLinks(String rel, List<Link> links) {
        List<Link> linksForRel = getLinks(rel);

        linksForRel.addAll(links);
    }

    public void addLink(String rel, Link link, boolean isSingleLink) {

        List<Link> linksForRel = getLinks(rel);

        if (isSingleLink) {
            linksForRel.clear();
        }

        linksForRel.add(link);
    }

    public boolean hasNamedLink(String rel, String name) {
        List<Link> curies = getLinks(rel);
        boolean found = false;

        if (!curies.isEmpty()) {
            for (Link link : curies) {
                if (link.getName().compareToIgnoreCase(name) == 0) {
                    found = true;
                }
            }
        }
        return found;
    }

    private List<Link> getLinks(String rel) {
        List<Link> linksForRel = this.get(rel);

        if (linksForRel == null)
        {
            linksForRel = new ArrayList<>();
            this.put(rel, linksForRel);
        }

        return linksForRel;
    }
}
