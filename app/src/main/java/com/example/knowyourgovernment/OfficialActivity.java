package com.example.knowyourgovernment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {
    private TextView userLocation;
    private TextView govTitle;
    private TextView politicanName;
    private TextView party;

    private ImageView partyLogo;
    private ImageView picOfPolitician;

    private ImageButton faceBookButton;
    private ImageButton twitterButton;
    private ImageButton youTubeButton;

    private TextView addressContent;
    private TextView phoneNumContent;
    private TextView websiteLink;
    private TextView emailLink;

    private ConstraintLayout background2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.official_activity);

        Intent intent = getIntent();
        final Official o = (Official)intent.getSerializableExtra("official");
        if(o == null)return;

        userLocation = findViewById(R.id.userLocationOAPortrait);
        if(intent.hasExtra("location") && intent != null){
            userLocation.setText(intent.getStringExtra("location"));
        }

        govTitle = findViewById(R.id.govPosOAPortrait);
        govTitle.setText(o.getGovTitle());

        politicanName = findViewById(R.id.name_of_politicianOA);
        politicanName.setText(o.getPoliticianName());

        partyLogo = findViewById(R.id.politicalLogo);

        party = findViewById(R.id.political_association);
        party.setText("(" + o.getPoliticalParty() + ")");
        background2 = findViewById(R.id.officialActConstraint); //change color from Red to Blue to Black

        if(party.getText().toString().equals("(Democratic)")){
            background2.setBackgroundColor(Color.BLUE);
            partyLogo.setImageDrawable(getResources().getDrawable(R.drawable.dem_logo));
        }
        else if(party.getText().toString().equals("(Democratic Party)")){
            background2.setBackgroundColor(Color.BLUE);
            partyLogo.setImageDrawable(getResources().getDrawable(R.drawable.dem_logo));
        }
        else if(party.getText().toString().equals("(Republican Party)")){
            background2.setBackgroundColor(Color.RED);
            partyLogo.setImageDrawable(getResources().getDrawable(R.drawable.rep_logo));
        }
        else {
            background2.setBackgroundColor(Color.BLACK);
            partyLogo.setVisibility(View.INVISIBLE);
        }

        picOfPolitician = (ImageView) findViewById(R.id.imageOfPolitician);

        addressContent = findViewById(R.id.adressInfo);
        if(!o.getAddress().equals("NULL")){
            addressContent.setText(o.getAddress());
            Linkify.addLinks(addressContent,Linkify.MAP_ADDRESSES);
        }

        phoneNumContent = findViewById(R.id.phoneNumberInfo);
        if(!o.getPhoneNumber().equals("NULL")){
            phoneNumContent.setText(o.getPhoneNumber());
            Linkify.addLinks(phoneNumContent,Linkify.PHONE_NUMBERS);
        }

        websiteLink = findViewById(R.id.websiteLink);
        if(!o.getWebSite().equals("NULL")){
            websiteLink.setText(o.getWebSite());
            Linkify.addLinks(websiteLink,Linkify.WEB_URLS);
        }

        emailLink = findViewById(R.id.emailInfo);
        if(!o.getEmail().equals("NULL")){
            emailLink.setText(o.getEmail());
            Linkify.addLinks(emailLink,Linkify.EMAIL_ADDRESSES);
        }

        faceBookButton = findViewById(R.id.faceBookClicker);
        twitterButton = findViewById(R.id.twitterClicker);
        youTubeButton = findViewById(R.id.youTubeClicker);
        //If politician has this form of social media, let it be seen, else make it invisible to users:
        if(o.getFaceBook().equals("NULL"))faceBookButton.setVisibility(View.INVISIBLE);
        if(o.getTwitter().equals("NULL"))twitterButton.setVisibility(View.INVISIBLE);
        if(o.getYouTube().equals("NULL"))youTubeButton.setVisibility(View.INVISIBLE);


        //Make the links clickable, need to create intents so websites can be accessed though.





        Picasso.get().load(o.getImage()).error(R.drawable.missing).placeholder(R.drawable.placeholder).
                into(picOfPolitician);

    }
    public void onClickFacebook(View v) {
        Intent intent = getIntent();
        final Official o = (Official)intent.getSerializableExtra("official");
        String fbName = o.getFaceBook();
        String FACEBOOK_URL = "https://www.facebook.com/" + fbName;

        String urlToUse;
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);

            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + fbName;
            }
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToUse));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL));
        }
        startActivity(intent);
    }
    public void onClickTwitter(View v){
        Intent intent = getIntent();
        final Official o = (Official)intent.getSerializableExtra("official");
        String user = o.getTwitter();
        String twitterAppUrl = "twitter://user?screen_name=" + user;
        String twitterWebUrl = "https://twitter.com/" + user;
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAppUrl));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterWebUrl));
        }
        startActivity(intent);
    }
    public void onClickYouTube(View v){
        Intent intent = getIntent();
        final Official o = (Official)intent.getSerializableExtra("official");
        String name = o.getYouTube();
        try{
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        }
        catch(ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://youtube.com/" + name)));
        }
    }
    public void onClickDemOrRepub(View v){
        if(party.getText().toString().equals("(Democratic Party)")){
            String urlS = "https://democrats.org";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(urlS));
            startActivity(intent);
        }
        else if(party.getText().toString().equals("(Republican Party)")){
            String urlS = "https://www.gop.com";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(urlS));
            startActivity(intent);
        }

    }
    public void onClickPhoto(View v){
        //If Picasso is able to pull up a picture of the Politician,
        //Take user to the next activity (photoDetailActivity)
        //which should have all the information for this politician created
        Intent intent = getIntent();
        final Official o = (Official)intent.getSerializableExtra("official");
        String image = o.getImage();
        if(!image.equals("NULL")){
            Intent switchIntent = new Intent(this,PhotoDetailedActivity.class);
            switchIntent.putExtra("official",o);
            switchIntent.putExtra("location",userLocation.getText().toString());
            //OfficialActivity.this.startActivity(switchIntent);
            startActivity(switchIntent);
        }
    }

}
