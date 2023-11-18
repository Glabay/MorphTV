package com.uwetrottmann.trakt5.entities;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Crew {
    public List<CrewMember> art;
    public List<CrewMember> camera;
    @SerializedName("costume & make-up")
    public List<CrewMember> costumeAndMakeUp;
    public List<CrewMember> directing;
    public List<CrewMember> production;
    public List<CrewMember> sound;
    public List<CrewMember> writing;
}
