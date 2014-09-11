## Halapino

Halapino creates [HAL](http://stateless.co/hal_specification.html)-compliant [HATEOAS](https://blog.apigee.com/detail/hateoas_101_introduction_to_a_rest_api_style_video_slides) JSON within [dropwizard](https://dropwizard.github.io/dropwizard/) apps.

### How it works

Halapino takes your standard dropwizard representation object and wraps it up in a HALRepresentation. This is serialised by [Jackson](https://github.com/FasterXML/jackson) with the help of a couple of custom serialisers.

### Why build another HAL implementation when there are some good ones already available?

If you have not seen the existing Java HAL implementations, find them by following the links on the main [HAL page](http://stateless.co/hal_specification.html). Current implementations include:

* halbuilder
* halarious
* hyperexpress

While these implementations are more complete (Halapino does not have a deserialiser, for example), Halapino has been specifically designed to work with [dropwizard](http://dropwizard.io). It:

* Uses Jackson for JSON serialisation (utilising Jackson annotations and a couple of custom serialisers to do most of the work);
* Uses Jersey resource path configuration to reduce the amount of HAL-specific configuration (following the [DRY](http://programmer.97things.oreilly.com/wiki/index.php/Don't_Repeat_Yourself) principle);
* Uses the uriInfo object injected by Jersey  to generate absolute links;
* Can be added to existing resources with just a few changes - a couple of annotations and some setup code are all you need to get going.    

### Getting started
Check out the [wiki](https://github.com/mattjbishop/halapino/wiki) for information on getting started.

### License
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 
[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)
 
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
