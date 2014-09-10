## Halapino

Halapino creates [HAL](http://stateless.co/hal_specification.html)-compliant [HATEOAS](https://blog.apigee.com/detail/hateoas_101_introduction_to_a_rest_api_style_video_slides) JSON within [dropwizard](https://dropwizard.github.io/dropwizard/) apps.

### How it works

Halapino takes your standard dropwizard representation and wraps it up in a HALRepresentation. This is serialised by [Jackson](https://github.com/FasterXML/jackson) with the help of a couple of custom serialisers.

### Why build another HAL implementation when there are some good ones already available?

If you have not seen the existing Java HAL implementations, check them out by following the links on the main [HAL page](http://stateless.co/hal_specification.html). Current implementations include:

* halbuilder
* halarious
* hyperexpress

While these implementations are more complete (halapino does not have a deserialiser, for example), Halapino has been specifically designed to work with [dropwizard](http://dropwizard.io). It:

* Uses Jackson for JSON serialisation (utilising a couple of custom serialisers to do some of the work);
* Uses reflection to get the resource paths (ensuring that you DRY by configuring the paths for links in halapino separately);
* Uses the Jersey uriInfo to generate absolute links;
* Can be added to existing resources with few changes - a couple of annotations are all you need to get going.    

### Getting started
To be done!