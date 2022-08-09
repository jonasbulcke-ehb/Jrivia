# Jrivia

Werkstuk Android Development 2021-2022

## Application Flow

When the user opens the application, they will land at the home screen with 4 options

### 1. Start a single-player game

The user needs to fill in a username that will be cached on the device, so they do not have to fill
it in every time. They can also choose how many questions they would like in their game. Then the
game start, where the user is asked a trivia question that they can answer and move to the next
question, with the possibility to go back. After all the questions are filled in, the user can
finish the game and will land on a game overview page. The score, time, correct number of questions
and total number of questions are displayed. The user have the option to check the questions and the
right answers and can return to this overview page if wanted. They can also choose to view the score
board, start a new game, where the slider will be automatically have the value of the number of
questions last game, or go back to the home menu.

### 2. Start a multiplayer game (but this is not available yet)

### 3. Solve the daily quest and view the past 30 daily quests

The user comes at a view with two tabs, one where the user can solve the daily quest of today, if it
is not solved yet at the first tab and view the quests of the past month in the second tab, where
also the total score of the solved quests and number of solved quests will be displayed. The user
can view the details of those past quests if they click on one, like how many guesses it took, what
the score value of it was, if it was even solved or not, and the right answer. It is possible to
swipe through the detail pages, without having to go back first to the past month's quests overview
page.

### 4. View the score board of the single player games

The scores of the single player games are here displayed, categorized by the number of questions.
The user can change the number of questions they would like to see. It is also possible to view all
the scores, without categorizing them by number of questions, this can be done via the menu at the
app bar. At the menu there, the user can also click on the info button if they need more info about
how the scores are ranked. The user can click long on a score, to invoke the contextual app bar. Now
the user have the choice between sharing this score or deleting it.

### 5. Daily quests notifications

Every day, there should be fetched a new daily quest. When this is done successfully, the user will
receive a notification. It could happen that the view is already displayed where the new daily quest
will come, and to make sure the view will be updated with the newest info, the fetch service will
send a broadcast that will be received by this view to make sure it will be updated.

### 6. Widget

There is also a widget available, where the daily quest is displayed. This widget is clickable, and
if clicked, the user will be taken to the view where they can solve this daily quest. The widget
will be updated with a the new daily quest by a broadcast.

## Sources

- [Official Android Documentation](https://d.android.com)
- [Chronometer/Stopwatch](https://www.youtube.com/watch?v=RLnb4vVkftc)
- [Up navigation](https://stackoverflow.com/questions/15559838/actionbar-up-navigation-recreates-parent-activity-instead-of-onresume)
- [Up navigation](https://stackoverflow.com/questions/36391478/how-do-i-navigate-up-to-a-parent-activity-not-on-the-back-stack)
- [Retrofit Tutorial](https://www.youtube.com/watch?v=gaPoV4z5wng)
- [AlarManager vs Handler](https://stackoverflow.com/questions/13228843/should-i-use-alarmmanager-or-handler)
- [Run a task periodically](https://stackoverflow.com/questions/6425611/android-run-a-task-periodically)
- [Contextual Actionbar](https://www.geeksforgeeks.org/how-to-use-material-contextual-actionbar-library-in-android-app/)
- [Quantity strings](https://stackoverflow.com/questions/41950952/how-to-use-android-quantity-strings-plurals)
- [Textview with rounded corners](https://www.geeksforgeeks.org/how-to-add-a-textview-with-rounded-corner-in-android/)
- [Update activity from broadcast receiver](https://stackoverflow.com/questions/25215878/how-to-update-the-ui-of-activity-from-broadcastreceiver)
- [Press back again to exit](https://www.geeksforgeeks.org/how-to-implement-press-back-again-to-exit-in-android/)
- [App Icon by AndroidAssetStudio](https://romannurik.github.io/AndroidAssetStudio/index.html)
