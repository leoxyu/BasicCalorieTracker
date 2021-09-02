# Calorie Tracker Application



 
## Overview
A calorie tracking application that allows the user to add/remove meals with foods that they eat per day, as well as 
information about the food. Days will contain an undefined number of meals and each meal will contain an undefined 
amount of foods. Foods will have custom names and caloric values that can be edited. The program
can then calculate stats regarding a day or weeks' caloric intake etc. Useful for anyone who wants to start keeping 
track of what they eat, as well as get a general sense of daily/weekly caloric intake.
This project is something that could end up being something that is practical and useful rather than just a school 
project, so I'm interested in completing it to its full extent.


## *User Stories*
- **(Phase 1)** As a user, I want to be able to add foods to a meal 
- **(Phase 1)** As a user, I want to be able to remove foods from a meal
- **(Phase 1)** As a user, I want to be able to edit calories for any foods
- **(Phase 1)** As a user, I want to be able to show all foods in a meal
- **(Phase 1)** As a user, I want to be able to add meals to a day
- **(Phase 1)** As a user, I want to be able to remove meals to a day
- **(Phase 1)** As a user, I want to be able to calculate daily stats regarding the food

- **(Phase 2)** As a user, I want to be able to save my days/meals
- **(Phase 2)** As a user, I want to be able to load my saved days/meals 
- **(Phase 2)** As a user, quitting should prompt me to save my added days/meals
- **(Phase 2)** As a user, launching the program should auto load a saved file if it exists.

## *Phase 4: Task 2*
*Test and design a class in your model package that is robust.  You must have at least one method that throws a checked 
exception.  You must have one test for the case where the exception is expected and another where the exception 
is not expected.*

In the Day class, getDayNum() throws a NoDaysExistException when the dayNum is 0. CalorieTracker.createCalendarButton2()
uses this to check if there are days to display to the user. If there are none, then an exception is thrown and an 
error box appears to the user.

## *Phase 4: Task 3*
I think that the nested lists of Calendar, Day and Meal could have all implemented a "NestedList" class or something of 
the sort, since the code between share some similarities. The ui.CalorieTracker classes ended up being quite messy, and
I think more of the panels could've been separate classes like the CalendarPanel. There are also methods in the 
CalorieTracker class that were turned into UI methods from the original console output methods, so if I were to refactor
them, they would be more organized for how they function now, rather than how they were ordered before for the console.