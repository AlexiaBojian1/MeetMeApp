# Presence: Group 26 app development project

## Introduction
Presence is a cutting-edge mobile application that revolutionizes messaging by leveraging the power of location. With Presence, users can post messages that are automatically geo-tagged to their current location on a map. These messages can be viewed by anyone, but only if they enter a specific radius around the message. Additionally, Presence incorporates a unique marker radius on the map, providing subtle hints to users about the presence of nearby messages. This innovative messaging experience offers an exciting and dynamic way for users to connect and interact.

<br/>

## User Scenario
*note: as the app relies on gps, for the best user experience, please use the app outside.*

*note: the app works in both vertical and horizontal, so try rotating a few times while going through the scenario*

<br/>

### Regular user
1. When opening the app for the first time, you should see a popup asking for location permissions, press don't allow. This will make the popup disappear and show a message, and then reappear again. This time press "Allow only while using the app".
2. Now you will be in the login screen where you can see a field to enter a phone number. You can try to enter invalid phone numbers such as "+12345" or "++123", and you will see a small messaging telling you to enter a valid phone number. Feel free to try any other invalid combination
3. Enter your own phone number that you would like to create an account with and click the "send Verification Code" button.
4. You will be redirected to a google page which verifies that you are not a robot after which you will return to the app in the verification screen.
5. Within a couple of seconds you should receive a text message with your verification code. You can again try to enter invalid codes, which do not allow you to proceed.
6. Enter the code you received via the text message and click the "check verification code" button.
7. After moving to the next screen, click the underlined "terms and conditions" text. In the TOS screen you can scroll to read all the text. When you have finished double-checking your user rights, click the "go back" button or your phones back button. Then press the "confirm and continue" button.
8. In the registration screen, enter into the textfield "Name" any nickname that you want for your account. You can also try to enter a an empty name, a name under 3 or over 15 characters; all of these should display a message to choose a valid nickname after clicking the "continue" button.
9. When you have chosen an appropriate nickname, click the "continue" button to move to the app's home screen.
10. After a bit of loading time, and with proper gps signal, the map should appear, along with markers, if any, showing the presence of a message around your location.
11. Scroll the map to any random place in Amsterdam and click the "center map" button at the bottom left of the screen. After a short delay, this will zoom the map back onto your current location.
12. Tap the "new post" button at the bottom center of the screen. This will take you to a new screen where you can post your own message.
13. Try pressing the "post" button without typing anything and you will see a message indicating that you need a valid message to be able to post.
14. Enter any message you wish the world could see and press "post". After a short delay, your message will be posted, and it will bring you back to the home screen, where your message will now be visible on the map. Repeat step 12 and post another message.
15. Above the marker where your message currently is, you will see a small little info screen showing the user who posted the message, the message content, and the number of votes (upvote - downvotes).
16. Tap the info window to bring you to a more detailed view of your message. Here you can see the actual upvote;downvote distribution, date and location of posting. You can also see a "delete message" button but don't tap that yet. Press the back button to go the home screen.
17. Tap the "my posts" button in the bottom right of the screen to view all the messages that you have ever posted. Since you have only posted one message right now, there should only be one displayed. Tap on the message.
18. You will now be in the same screen as in step 16. Tap the "delete message" button. This will bring you back to the "my posts" screen and you can confirm yourself that the message is indeed gone.
19. Go to the home screen. If you are in range of any messages that are not your own, click on their info window just as before to go to a new view message screen. Otherwise go to step 23.
20. In this new screen, press any combination of like(L) and dislike(DL) buttons, eg. L->L->DL->L->DL->DL->L will result in a single like applied to the message.
21. Tap the report flag in the top right of the screen to bring you to the report screen. Enter a reason for reporting the message in the report text field, eg. "This message hurt my feelings ;-;", and press the "report" button. This will send a report to the moderators and bring you back to the home screen.
22. If you look at the message you voted on in the map, you will see a corresponding change to the number under the heart indicating the amount of votes.
23. Click on the user icon in the top left of the screen. You will now be in the user settings screen, so type a new nickname in the textfield, and press back to apply the changes. In the home screen, you will now see a new nickname.
24. Exit the app and close it fully. Open the app again and it will automically keep you logged in and direct you to the home screen.
25. Feel free to play around more, post messages, view messages etc. until you feel like continuing. 
26. Go back to the user settings and press the "sign out" button, and then the "confirm" button in the popup. This will return you to the initial login activity. You can now follow the moderator user scenario.


### Moderator user
1. Type in the test moderator phone number "+10987654321" into the phone number field, and press the "send verification code" button.
2. Type in the test moderator verification code "123456", and press the "check verification code" button.
3. Doing this will bring you to the moderator menu where you should see three large buttons. Click the "moderate messages" button.
4. In this screen, you can see all the messages that currently exist on the map, regardless of your proximity to them. They are sorted based on the date of posting, so find and tap the message you posted in the user scenario.
5. You will now see a detailed view of your message similarly to before. Click on the user icon in the top right.
6. In this screen, along with all the user details like phone number and nickname, you can see all the messages posted by the user. Tap the "block user" button to ban the user from posting messages.
7. Navigate back to the moderator menu by using your phones back button. Tap the "moderate users" button. You will now be able to see all the different registered users.
8. Tap the card assosciated with the account you made in the regular user scenario. You will be brought to the same screen as in step 6. 
9. Go back to the moderator menu. Tap the "view reports" button. In this screen you will see an overview of all the filed reports. Tap on the report that you made in the regular user scenario or in case you didn't, any random report.
10. You will now see a more detailed view of the report, as well as the user who submitted the report. Tap the "view reported message" button. You will see a similar screen to step 5. Tap the "delete message" button.
11. Tap the same report as before. Pressing the "view reported message" button will show a note that the message no longer exists. As this report is useless now, press the "delete report" button.
12. Navigate back to the moderator menu, and you can play around and try to look at different users, and their individual messages.
13. When you are done, go back to the moderator menu and press the sign out button in the top right of the screen. Press "cancel". You will notice that nothing happens.
14. Press the sign out button again, but this time press "confirm" and you will be redirected to the login screen once again. You can now move to the final clean up section of the scenario.


### Clean up
1. Log back into the account you made in the regular user scenario following steps 3-6
2. Once you are in the home screen, click on the user icon in the top left to navigate to the user settings screen.
3. Press the "delete account" button, and to confirm, press "yes, delete it"
4. Your account, along will all your messages will be deleted.

<br/>

## Third-party sources
No code from 3rd part Java sources was used without changing or adapting to fit to our application.
MessageAdapter.java and NicknameAdapter.java contain code adapted from snippets from the official [FireBase documentation](https://firebase.google.com/docs/firestore/solutions/geoqueries). Code is under the Apache 2.0 license.