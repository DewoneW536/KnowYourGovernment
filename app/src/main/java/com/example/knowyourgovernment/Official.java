package com.example.knowyourgovernment;

import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.Objects;

public class Official implements Serializable, Comparable<Official> {
    private String govTitle;
    private String politicianName;
    private String politicalParty;
    private String address;
    private String webSiteLink;
    private String phoneNumber;
    private String image;
    private String faceBook;
    private String twitter;
    private String youTube;

    public Official(String govTitle,String politicianName,String politicalParty,String address,String webSite,String phoneNumber,
                    String image, String faceBook, String twitter,String youTube){
        this.govTitle = govTitle;
        this.politicianName = politicianName;
        this.politicalParty = politicalParty;
        this.address = address;
        this.webSiteLink = webSite;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.faceBook = faceBook;
        this.twitter = twitter;
        this.youTube = youTube;
    }
    public String getGovTitle(){return govTitle;}
    public String getPoliticianName(){return politicianName;}
    public String getPoliticalParty(){return politicalParty;}
    public String getAddress(){return address;}
    public String getPhoneNumber(){return phoneNumber;}
    public String getImage(){return image;}
    public String getFaceBook(){return faceBook;}
    public String getTwitter(){return twitter;}
    public String getYouTube(){return youTube;}
    public String getWebSite(){return webSiteLink;}

    @Override
    public boolean equals(Object o){
        if(this == o)return true;
        if(o == null || getClass() != o.getClass())return false;
        Official official = (Official)o;
        return govTitle.equals(official.govTitle)&&politicianName.equals(official.politicianName);
    }
    @Override
    public int hashCode(){return Objects.hash(govTitle,politicianName);}

    @Override
    public int compareTo(Official official) { return govTitle.compareTo(official.getGovTitle()); }
}
