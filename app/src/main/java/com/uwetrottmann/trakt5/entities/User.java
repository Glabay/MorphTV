package com.uwetrottmann.trakt5.entities;

import com.google.gson.annotations.SerializedName;
import org.threeten.bp.OffsetDateTime;

public class User {
    public String about;
    public int age;
    public String gender;
    public UserIds ids;
    public Images images;
    @SerializedName("private")
    public Boolean isPrivate;
    public OffsetDateTime joined_at;
    public String location;
    public String name;
    public String username;
    public Boolean vip;
    public Boolean vip_ep;

    public static class UserIds {
        public String slug;
    }
}
