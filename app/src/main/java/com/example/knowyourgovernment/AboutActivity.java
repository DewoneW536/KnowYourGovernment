package com.example.knowyourgovernment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    private TextView googleApiLink;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        googleApiLink = findViewById(R.id.apiAttributionAbout);
        Linkify.addLinks(googleApiLink,Linkify.WEB_URLS);
    }
    //@Override
    public void onClickGoogleApi(View v){
        String googleCivicAPI = "https://developers.google.com/civic-information";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(googleCivicAPI));
        startActivity(intent);
    }
}
