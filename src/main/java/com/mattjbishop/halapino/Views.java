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

public class Views {
    public static class HideEmbedded { } // makes sure the embedded resources are not directly rendered in JSON
    public static class HideLinks { } // makes sure any links are not directly rendered in JSON
    public static class HAL { } // default view - does nothing other than make sure the other views are not used
}
