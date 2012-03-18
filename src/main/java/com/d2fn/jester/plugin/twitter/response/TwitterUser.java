package com.d2fn.jester.plugin.twitter.response;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * TwitterUser
 * @author Dietrich Featherston
 */
public class TwitterUser {

    @NotEmpty
    @JsonProperty("id_str")
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty("screen_name")
    private String screenName;

    @JsonProperty("profile_image_url_https")
    private String profilePicUrl;

    @JsonProperty
    private String description;
    
    public String getName() {
        return name;
    }
    
    public String getScreenName() {
        return screenName;
    }
    
    public String getProfilePicUrl() {
        return profilePicUrl;
    }
    
    public String getDescription() {
        return description;
    }
    
//    user: {
//        id: 415379657
//        id_str: "415379657"
//        name: "BabaBooey Althusser"
//        screen_name: "GnarlMarx"
//        location: "Seattle, WA"
//        description: "Gay Dog 2012. The world's only expert on Jockstrap Wizards. Formerly @pubic_mischief. A queer dude who combats queer liberalism."
//        url: "http://favstar.fm/users/GnarlMarx"
//        protected: false
//        followers_count: 1169
//        friends_count: 850
//        listed_count: 52
//        created_at: "Fri Nov 18 07:59:21 +0000 2011"
//        favourites_count: 4803
//        utc_offset: -32400
//        time_zone: "Alaska"
//        geo_enabled: false
//        verified: false
//        statuses_count: 33094
//        lang: "en"
//        contributors_enabled: false
//        is_translator: false
//        profile_background_color: "C0DEED"
//        profile_background_image_url: "http://a0.twimg.com/profile_background_images/376658607/narwhal_knitting_medium.jpg"
//        profile_background_image_url_https: "https://si0.twimg.com/profile_background_images/376658607/narwhal_knitting_medium.jpg"
//        profile_background_tile: true
//        profile_image_url: "http://a0.twimg.com/profile_images/1712827359/Buffy_216_normal.png"
//        profile_image_url_https: "https://si0.twimg.com/profile_images/1712827359/Buffy_216_normal.png"
//        profile_link_color: "0084B4"
//        profile_sidebar_border_color: "C0DEED"
//        profile_sidebar_fill_color: "DDEEF6"
//        profile_text_color: "333333"
//        profile_use_background_image: true
//        show_all_inline_media: false
//        default_profile: false
//        default_profile_image: false
//        following: null
//        follow_request_sent: null
//        notifications: null
//    }
}
