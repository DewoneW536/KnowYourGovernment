package com.example.knowyourgovernment;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PoliticianDownloader implements Runnable{
    private static final String TAG = "PoliticianInfoDownload";
    private static final String POLITICIAN_URL_FIRST
            = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyCBSeFgOnGu5FaOvdvGV_j9t4TENo2Slr8&address=";

    private MainActivity mainAct;
    private String searchTarget;
    private final ArrayList<Official> officialList = new ArrayList<>();

    public PoliticianDownloader(MainActivity mainActivity,String searchTarget){
        this.mainAct = mainActivity;
        this.searchTarget = searchTarget;
    }
    @Override
    public void run() {
        Uri.Builder uriBuilder = Uri.parse(POLITICIAN_URL_FIRST + searchTarget).buildUpon();
        uriBuilder.appendQueryParameter("fullText","true");
        String urlToUse = uriBuilder.toString();

        Log.d(TAG,"run: "+urlToUse);

        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(urlToUse);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                Log.d(TAG,"run: HTTP ResonseCode NOT OK: "+connection.getResponseCode());
                return;
            }
            InputStream is = connection.getInputStream();
            BufferedReader bReader = new BufferedReader((new InputStreamReader(is)));

            String sLine;
            while((sLine = bReader.readLine())!=null){
                sb.append(sLine).append('\n');
            }
            Log.d(TAG,"run: "+sb.toString());
        }
        catch(Exception e){
            e.printStackTrace();
            return;
        }
        process(sb.toString());
        Log.d(TAG,"run: ");
    }

    private void process(String s){
        try{
            JSONObject jsonObject = new JSONObject(s);
            JSONObject input = jsonObject.getJSONObject("normalizedInput");

            final String location = input.getString("city") + ", " + input.getString("state") + " " + input.getString("zip");
            JSONArray jArray = jsonObject.getJSONArray("offices");
            JSONArray jArray2 = jsonObject.getJSONArray("officials");

            for(int i=0;i<jArray.length();i++){
                JSONObject oNum = jArray.getJSONObject(i);
                String politicalOffice = oNum.getString("name");
                JSONArray officialIndex = oNum.getJSONArray("officialIndices");
                for(int j=0;j<officialIndex.length();j++){
                    int politicianIndex = officialIndex.getInt(j); //index where person's information is.
                    JSONObject officialObj = (JSONObject)jArray2.get(politicianIndex);
                    String name = officialObj.getString("name");

                    String poliLocation = "";
                    //I basically need to say "if you see the word 'address' anywhere, please use these conditional statements for String poliLocation"
                    //Otherwise (If you can't find anything like that here) just set poliLocation to an empty String since there is literally nothing there in the first place).
                    if(officialObj.has("address")){
                        JSONArray addr = officialObj.getJSONArray("address");
                        JSONObject addrObj = (JSONObject)addr.get(0);
                        if(addrObj.has("line1") && addrObj.has("line2") && addrObj.has("line3")){
                            if(!addrObj.getString("line1").equals("") &&!addrObj.getString("line2").equals("") &&!addrObj.getString("line3").equals("")){
                                poliLocation = addrObj.getString("line1") + ", " + addrObj.getString("line2")
                                        + ", " + addrObj.getString("line3") + ", " +addrObj.getString("city") + ", " + addrObj.getString("state") +
                                        ", " + addrObj.getString("zip");
                            }
                            //If just line1 & line2 have String/characters
                            else if(!addrObj.getString("line1").equals("") && !addrObj.getString("line2").equals("") && addrObj.getString("line3").equals("")){
                                poliLocation = addrObj.getString("line1") + ", " +addrObj.getString("line2")
                                        + ", " + addrObj.getString("city") + ", " + addrObj.getString("state") +
                                        ", " + addrObj.getString("zip");
                            }
                            //If just line1 has String/line2 & line3 are empty.
                            else if(!addrObj.getString("line1").equals("") && addrObj.getString("line2").equals("") && addrObj.getString("line3").equals("")){
                                poliLocation = addrObj.getString("line1") + ", " + addrObj.getString("city")
                                        + ", " + addrObj.getString("state") +
                                        ", " + addrObj.getString("zip");
                            }
                        }
                        else if(addrObj.has("line1") && addrObj.has("line2") && !addrObj.has("line3")){
                            if(!addrObj.getString("line1").equals("") && !addrObj.getString("line2").equals("")){
                                poliLocation = addrObj.getString("line1") + ", " +addrObj.getString("line2")
                                        + ", " +addrObj.getString("city") + ", " + addrObj.getString("state") +
                                        ", " + addrObj.getString("zip");
                            }
                            else if(!addrObj.getString("line1").equals("") && addrObj.getString("line2").equals("")){
                                poliLocation = addrObj.getString("line1") + ", " + addrObj.getString("city") + ", " + addrObj.getString("state") +
                                        ", " + addrObj.getString("zip");
                            }
                        }
                        else if(addrObj.has("line1") && !addrObj.has("line2") && !addrObj.has("line3")){
                            poliLocation = addrObj.getString("line1") + ", " + addrObj.getString("city") + ", " + addrObj.getString("state") +
                                    ", " + addrObj.getString("zip");
                        }
                    }
                    //Keep this here just in case I mess up these if-statements
                    /*
                    poliLocation = addrObj.getString("line1") + addrObj.getString("line2")
                     + addrObj.getString("line3") + addrObj.getString("city") + ", " + addrObj.getString("state") +
                            ", " + addrObj.getString("zip");
                     */
                    String party = officialObj.getString("party");
                    String website;
                    if(officialObj.has("urls") && officialObj.getJSONArray("urls").length() > 0){
                        website = officialObj.getJSONArray("urls").getString(0);
                    }
                    else{
                        website = "NULL";
                    }
                    String phoneNum;
                    if(officialObj.has("phones") && officialObj.getJSONArray("phones").length() > 0){
                        phoneNum = officialObj.getJSONArray("phones").getString(0);
                    }
                    else{
                        phoneNum = "NULL";
                    }
                    String email;
                    if(officialObj.has("emails") && officialObj.getJSONArray("emails").length() != 0){
                        email = officialObj.getJSONArray("emails").getString(0);
                    }
                    else{
                        email = "NULL";
                    }
                    String photoURL;
                    if(officialObj.has("photoUrl")){
                        photoURL = officialObj.getString("photoUrl");
                    }
                    else{
                        photoURL = "NULL";
                    }
                    String faceBook = "NULL";
                    String twitter = "NULL";
                    String youTube = "NULL";
                    if(officialObj.has("channels")){
                        JSONArray channels = officialObj.getJSONArray("channels");
                        for(int k=0; k<channels.length();k++){
                            JSONObject jsonObject2 = (JSONObject)channels.get(k);
                            if(jsonObject2.getString("type").equals("Facebook")){
                                faceBook = jsonObject2.getString("id"); }
                            else if(jsonObject2.getString("type").equals("Twitter")){
                                twitter = jsonObject2.getString("id"); }
                            else if(jsonObject2.getString("type").equals("YouTube")){
                                youTube = jsonObject2.getString("id"); }
                        }
                    }
                    Official o = new Official(politicalOffice,name,party,poliLocation,website,phoneNum,
                            email,photoURL,faceBook,twitter,youTube);

                    officialList.add(o);
                }

            }
            mainAct.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainAct.applyLocation(location);
                    mainAct.addNearByPoliticians(officialList);
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}
