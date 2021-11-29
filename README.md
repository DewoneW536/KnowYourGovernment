# KnowYourGovernment
This is an Android App that, based on the user's location, will automatically display all politicians in the area
(and their public information) unless another geographic region is speicified by said user.

## Features
1. Based on the user's location (after obtaining permission), the app will show the 
   user's area's political officials. The user can also look up a certain location for information regarding that location.

2. Each political official's information (name, political party, image, address, 
   phone, website, and social media handles) will be shown when the political official is clicked on.

3. When the user clicks on the address, the user will be redirected to Google Maps with directions from the current location to the address.

4. When the user clicks on the phone number, the user will be redirected to the phone app with the number pre-populated.

5. When the user clicks on the website, the user will be redirected to the website on the user's browser.

6. When the user clicks on the social media handles, the user will be redirected to the social media app (or the browser, if the app does not exist).

7. All information regarding the political official is derived from Google's Civic Information API.

## Main Concepts
1. Location Services
2. Internet usage and permissions
3. Google API handling
4. Images (via local storage or using the Picasso library for online images)
5. Implicit and explicit intents
6. TextView links
