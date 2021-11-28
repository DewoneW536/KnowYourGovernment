package com.example.knowyourgovernment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

public class PhotoDetailedActivity extends AppCompatActivity {
    private TextView userLocation2;
    private TextView govTitle2;
    private TextView politicanName2;
    private TextView party;

    private ImageView partyLogo2;
    private ImageView picOfPolitician2;
    private ConstraintLayout background3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_detail_activity);

        userLocation2 = findViewById(R.id.locationDisplayPDA);
        govTitle2 = findViewById(R.id.politicalPosPDA);
        politicanName2 = findViewById(R.id.namePDA);
        partyLogo2 = findViewById(R.id.politicalLogoPDA);
        party = findViewById(R.id.political_association);
        background3 = findViewById(R.id.officialConstraintPDA);
        picOfPolitician2 = (ImageView) findViewById(R.id.biggerPicturePDA);

        Intent intent = getIntent();
        final Official o = (Official)intent.getSerializableExtra("official");
        if(o == null)return;

        govTitle2.setText(o.getGovTitle());
        politicanName2.setText(o.getPoliticianName());
        Picasso.get().load(o.getImage()).error(R.drawable.missing).placeholder(R.drawable.placeholder).
                into(picOfPolitician2);
        if(intent.hasExtra("location") && intent != null){
            userLocation2.setText(intent.getStringExtra("location"));
        }
        if(o.getPoliticalParty().equals("Democratic Party") || o.getPoliticalParty().equals("Democratic")){
            partyLogo2.setImageDrawable(getResources().getDrawable(R.drawable.dem_logo));
            background3.setBackgroundColor(Color.BLUE);
        }
        else if(o.getPoliticalParty().equals("Republican Party") || o.getPoliticalParty().equals("Republican")){
            partyLogo2.setImageDrawable(getResources().getDrawable(R.drawable.rep_logo));
            background3.setBackgroundColor(Color.RED);
        }
        else{
            partyLogo2.setVisibility(View.INVISIBLE);
            background3.setBackgroundColor(Color.BLACK);
        }
    }
    public void onClickDemOrRepubPDA(View v){
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
}