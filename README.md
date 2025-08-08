# SafePlan
## Background
Individuals who find themselves in abusive relationships lack a centralized and personalized support resource that helps them navigate through their high-risk situation. To our surprise, none of the apps currently available – Aspire News, myPlan, Bright Sky Canada – provide a tailored questionnaire, personalized plan, *and* ability to store emergency information all in *one* place.  

## Our App, SafePlan 
SafePlan was inspired by the Victim Services Toronto site. We followed the site’s warm colour palette to exude the reliability and support of a support app. 

SafePlan provides trauma-informed support to victims of abuse. Our app allows users to assess, plan, and manage their safety before, during, and after an abusive relationship. With its tailored, flexible safety plans, it emphasizes the uniqueness of each individual’s situation.

Although a safety plan is a great start for an individual looking to escape their situation, SafePlan makes clear through its in-app disclaimer that it is not a substitute for emergency services. 

## Description of Features
SafePlan provides a suite of features. These include, but are not limited to: a login method, emergency exit button, questionnaire, safety plan, ability to store emergency information, reminder scheduling, and links to local support resources. 

Details of each feature are described below. 

### PIN Setup, Storage, & Unlock Flow
- User is prompted to login with Firebase by providing their name, email, and 4- or 6- digit PIN.
- PIN is securely stored in SharedPreferences.
- On each subsequent app launch, a login screen is shown providing two options for the user to sign in: through their PIN or through their Firebase login.
- Follows MVP format.

Resources:
[AuthenticationState](https://firebase.google.com/docs/auth/android/manage-users#persist_a_users_authentication_state) [TextInputEditText](https://developer.android.com/reference/com/google/android/material/textfield/TextInputEditText) [SharedPreferences](https://developer.android.com/reference/android/content/SharedPreferences.html) [KeyGenerator](https://developer.android.com/reference/javax/crypto/KeyGenerator.html) 

### Emergency Exit Button
- A persistent “Exit” icon is found on every screen.
- When tapped, user is redirected to their default browser.

Resources: [FloatingActionButton](https://developer.android.com/reference/com/google/android/material/floatingactionbutton/FloatingActionButton) [ActionView](https://developer.android.com/guide/components/intents-common#Browser) [finishAffinity](https://developer.android.com/reference/android/app/Activity#finishAffinity()) [ClearTask](https://developer.android.com/reference/android/content/Intent#FLAG_ACTIVITY_CLEAR_TASK) 

### Dynamic Questionnaire 
- Questions defined in JSON file.
- Includes a set of warm-up questions, branch-specific questions (user is still in a relationship, planning to leave, post-separation). 
- UI updates in real-time to show one question per page.
- After first user login, questionnaire activity is presented to user.
- User can edit the questionnaire again.

Resources:
[LinearLayout](https://developer.android.com/reference/android/widget/LinearLayout) [View](https://developer.android.com/reference/android/view/View#attr_android:visibility) [Firebase Realtime Database Setup](https://firebase.google.com/docs/database/android/start?_gl=1*hatq0w*_up*MQ..*_ga*MTI3NDY3MDQ0Ni4xNzUwNDUwMTIw*_ga_CW55HF8NVT*czE3NTA0NTAxMTkkbzEkZzAkdDE3NTA0NTAxMTkkajYwJGwwJGgw) 
[Firebase Realtime Database Structure](https://firebase.google.com/docs/database/android/structure-data?_gl=1*1wr6h5z*_up*MQ..*_ga*MTI3NDY3MDQ0Ni4xNzUwNDUwMTIw*_ga_CW55HF8NVT*czE3NTA0NTAxMTkkbzEkZzAkdDE3NTA0NTAxMTkkajYwJGwwJGgw)

### Plan Generation
- Tips defined in JSON file.
- Safety plan consists of tips based on the user’s response to the questionnaire. 
- Tips are dynamic and change based on changes to the questionnaire.
- Tips are shown as a scrollable list in a RecyclerView  

Resources: 
[RecyclerView](https://developer.android.com/develop/ui/views/layout/recyclerview#java) [JSON](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Scripting/JSON) [GSON](https://javadoc.io/doc/com.google.code.gson/gson/latest/com.google.gson/com/google/gson/Gson.html) 

### Storage of Emergency Information
- Users can upload information to four categories: documents, emergency contacts, safe locations, and medications.
- Supports adding, editing, deleting, and viewing items in each category.
- Each category is represented by an Activity and Adapter class and its layout is specified by ViewHolder and CardView
- Items uploaded to Firebase under their user profile. 
- Actual document blobs stored locally.
  
Resources: [UploadFiles](https://firebase.google.com/docs/storage/android/upload-files) [DownloadFiles](https://firebase.google.com/docs/storage/android/download-files?_gl=1*160hjj5*_up*MQ..*_ga*MTA2ODg5OTQ3OS4xNzUwNDUzMzgz*_ga_CW55HF8NVT*czE3NTA0NTMzODMkbzEkZzAkdDE3NTA0NTMzODMkajYwJGwwJGgw#java) [OpenFiles](https://developer.android.com/guide/topics/providers/document-provider) 

### Reminders & Plan Review Notifications
- Users can choose from daily, weekly, or monthly reminders.
- Support for adding, editing, and deleting multiple reminders.
- Push notifications remind users to review or update their plan.
- When tapped, user is brought to app’s log-in screen.

Resources: [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)  [AlarmManager](https://developer.android.com/reference/android/app/AlarmManager) [Notification](https://developer.android.com/develop/ui/views/notifications/build-notification) 

### Support Connection
- Page shows direct links to local victim services, hotlines, shelters, legal aid, and police services based on the user’s selected city
- Uses a JSON file and HashMap to define the resources for the following cities: Toronto, Vancouver, Edmonton, Montreal, Winnipeg. 

Resources: [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html) [JSON](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Scripting/JSON) [GSON](https://javadoc.io/doc/com.google.code.gson/gson/latest/com.google.gson/com/google/gson/Gson.html) 
