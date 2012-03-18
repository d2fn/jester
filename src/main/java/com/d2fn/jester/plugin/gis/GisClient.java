package com.d2fn.jester.plugin.gis;

import com.d2fn.jester.plugin.gis.response.GisImageResult;
import com.d2fn.jester.plugin.gis.response.GisResponse;
import com.yammer.dropwizard.client.JerseyClient;
import com.yammer.dropwizard.json.Json;
import com.yammer.dropwizard.logging.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GisClient {

    private static Log log = Log.forClass(GisClient.class);
    private static final String api = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";

    private HttpClient httpClient;
    
    private MediaType jsType;
    private Json json = new Json();
    
    public GisClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        Map<String, String> params = new HashMap<String, String>();
        params.put("charset","utf-8");
        jsType = new MediaType("text","javascript", params);
    }
    
    public String search(String q) throws Exception {
        String encodedQ = URLEncoder.encode(q.trim(), "UTF-8");
        HttpGet get = new HttpGet(api + encodedQ);
        HttpResponse httpResponse = httpClient.execute(get);
        InputStream is = httpResponse.getEntity().getContent();
        try {
            GisResponse response = json.readValue(is, GisResponse.class);
            List<GisImageResult> images = response.getResponseData().getResults();
            int i = new Random(System.currentTimeMillis()).nextInt(images.size());
            return images.get(i).getUrl();
        }
        catch(Exception e) {
            log.error(e, "error running google image search for '{}'", q);
        }
        finally {
            if(is != null) is.close();
        }
//        GisResponse response = httpClient.get(new URI(api + encodedQ), jsType, GisResponse.class);
        return null;
    }
}
