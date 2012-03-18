package com.d2fn.jester.plugin.gis.response;

import org.codehaus.jackson.annotate.JsonProperty;

import java.net.URI;

/**
 * GisImageResult
 * @author Dietrich Featherston
 */
public class GisImageResult {
    
    @JsonProperty("GsearchResultClass")
    private String searchResultClass;
    
    @JsonProperty
    private String url;
    
    public String getUrl() {
        return url;
    }
    
//    GsearchResultClass: "GimageSearch"
//    width: "368"
//    height: "289"
//    imageId: "ANd9GcQKh7wPMr0sbO3tB1nrGmXneAlO-tI5yoAobelXzlRn33smWzhgIQjXbw"
//    tbWidth: "122"
//    tbHeight: "96"
//    unescapedUrl: "http://seedmagazine.com/images/uploads/turtle_article.jpg"
//    url: "http://seedmagazine.com/images/uploads/turtle_article.jpg"
//    visibleUrl: "seedmagazine.com"
//    title: "Male <b>Turtles</b> Can&#39;t Stand the Heat § SEEDMAGAZINE."
//    titleNoFormatting: "Male Turtles Can&#39;t Stand the Heat § SEEDMAGAZINE."
//    originalContextUrl: "http://seedmagazine.com/content/article/male_turtles_cant_stand_the_heat/"
//    content: "Male box <b>turtle</b>."
//    contentNoFormatting: "Male box turtle."
//    tbUrl: "http://t3.gstatic.com/images?q=tbn:ANd9GcQKh7wPMr0sbO3tB1nrGmXneAlO-tI5yoAobelXzlRn33smWzhgIQjXbw"
}
