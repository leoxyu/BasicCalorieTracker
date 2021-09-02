
package ui;

import model.*;
import persistance.JsonReader;
import persistance.JsonWriter;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import static javax.swing.BoxLayout.*;

public class CalorieTracker extends JFrame {
    private static final String JSON = "./data/calorieTracker.json";
    Calendar calendar;

    JsonWriter writer = new JsonWriter("./data/calendar.json");
    JsonReader reader = new JsonReader("./data/calendar.json");

    private JPanel calendarPanel;

    JPanel mainPanel = new JPanel();


    //constructor
    public CalorieTracker() {
        int result = JOptionPane.showConfirmDialog(new JFrame(), "Load previous save?", "Calorie Tracker",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            getSave();
        } else {
            calendar = new Calendar();
        }
        startTrackerUI();
        centreOnScreen();
    }

    //EFFECTS: starts the program's UI
    private void startTrackerUI() {
        mainPanel.setBackground(new Color(229, 232, 182));

        calendarPanel = new CalendarPanel();
        calendarWindow();

        mainPanel.add(calendarPanel);

        add(mainPanel);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        closeJFrame();

    }

    //EFFECTS: primary window for the calendar, includes options for adding/editing days with buttons
    private void calendarWindow() {
        calendarPanel.setLayout(new BoxLayout(calendarPanel, Y_AXIS));

        JButton calButton1 = createCalendarButton1();

        JButton calButton2 = createCalendarButton2();

        JButton calButton3 = createCalendarButton3();

        calendarPanel.add(calButton1);
        calendarPanel.add(calButton2);
        calendarPanel.add(calButton3);
    }

    //EFFECTS: creates button for editing a day
    private JButton createCalendarButton3() {
        JButton calButton3 = new JButton("Edit a day");
        calButton3.addActionListener(evt -> {
            final JFrame window = new JFrame();
            String number = JOptionPane.showInputDialog(window,
                    "Which day would you like to edit? (enter number)", null);
            try {
                int num = Integer.parseInt(number);
                try {
                    editDay(calendar.getDay(num - 1));
                } catch (IndexOutOfBoundsException e) {
                    createErrorWindow("This day does not exist.");
                }
            } catch (NumberFormatException e) {
                createErrorWindow("Invalid day number.");
            }


        });
        return calButton3;
    }

    //EFFECTS: creates a button to display existing days
    private JButton createCalendarButton2() {
        JButton calButton2 = new JButton("View days");
        calButton2.addActionListener(e -> {
            JFrame previousDays = new JFrame();
            JTextArea days = new JTextArea();
            days.setEditable(false);
            days.setFont(new Font("Ariel", Font.PLAIN, 40));
            try {
                calendar.getDay(0).getDayNum();
                for (Day d : calendar.days) {
                    if (d.getNumberOfMeals() == 1) {
                        days.append("Day " + d.getDayNum() + " - " + d.getNumberOfMeals() + " meal \n");
                    } else {
                        days.append("Day " + d.getDayNum() + " - " + d.getNumberOfMeals() + " meals \n");
                    }
                }
                calendarButtonSetup(previousDays, days);
            } catch (Exception exception) {
                createErrorWindow("There are no days yet.");
            }
        });
        return calButton2;
    }


    //EFFECTS: creates an error JFrame
    //based on the examples given here:
    //https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
    private static void createErrorWindow(String s) {
        JOptionPane.showMessageDialog(new JFrame(),
                s,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    //EFFECTS: sets the buttons to be visible
    private void calendarButtonSetup(JFrame previousDays, JTextArea days) {
        previousDays.add(days);
        previousDays.pack();
        previousDays.setVisible(true);
        previousDays.setLocationRelativeTo(null);
    }

    //EFFECTS: creates button for adding a day
    private JButton createCalendarButton1() {
        JButton calButton1 = new JButton("Add a day");
        calButton1.addActionListener(e -> {
            playSound("./data/beep.wav");
            addDay();
        });
        return calButton1;
    }

    //EFFECTS: primary window for editing a day, with buttons corresponding to methods
    private void dayWindow(Day day) {
        JFrame dayWindow = new JFrame();
        dayWindow.setLayout(new BorderLayout());
        dayWindow.setSize(300, 200);
        JPanel subPanel = new JPanel();
        subPanel.setBackground(new Color(180, 196, 174));
        subPanel.setLayout(new BoxLayout(subPanel, Y_AXIS));

        JButton dayButton1 = createDayButton1(day);

        JButton dayButton2 = createDayButton2(day);

        JButton dayButton3 = createDayButton3(day);

        JButton dayButton4 = createDayButton4(day);

        JButton dayButton5 = createDayButton5(day);

        tryEditDay(day, subPanel);

        subPanel.add(dayButton1);
        subPanel.add(dayButton2);
        subPanel.add(dayButton3);
        subPanel.add(dayButton4);
        subPanel.add(dayButton5);

        dayWindow.add(subPanel);

        dayWindow.setVisible(true);
        dayWindow.setLocationRelativeTo(null);

    }

    private void tryEditDay(Day day, JPanel subPanel) {
        try {
            subPanel.add(new JLabel("Now editing Day " + day.getDayNum()));
        } catch (Exception exception) {
            createErrorWindow("This day does not exist.");
        }
    }

    //EFFECTS: creates button for calculating daily calorie total
    private JButton createDayButton5(Day day) {
        JButton dayButton5 = new JButton("Daily Calorie Total");
        dayButton5.addActionListener(e -> JOptionPane.showMessageDialog(new JFrame(),
                ("The day's calorie total is " + day.totalCalories()),
                "Calorie Total",
                JOptionPane.PLAIN_MESSAGE));
        return dayButton5;
    }

    //EFFECTS: creates button for editing a meal
    private JButton createDayButton4(Day day) {
        JButton dayButton4 = new JButton("Edit a meal");
        dayButton4.addActionListener(e -> findMealToEdit(day));
        return dayButton4;
    }

    //EFFECTS: creates button for printing all meals
    private JButton createDayButton3(Day day) {
        JButton dayButton3 = new JButton("Print all meals");
        dayButton3.addActionListener(e -> {
            JFrame previousMeals = new JFrame();
            JTextArea meals = new JTextArea();
            meals.setEditable(false);
            meals.setFont(new Font("Ariel", Font.PLAIN, 40));
            if (day.getNumberOfMeals() == 0) {
                meals.append("There are no meals in this day.");
            } else {
                for (Meal m : day.day) {
                    meals.append(m.getMealType() + " has " + m.getMealSize() + " foods in it \n");
                }
            }

            calendarButtonSetup(previousMeals, meals);
        });
        return dayButton3;
    }

    //EFFECTS: creates button to remove a specified meal
    private JButton createDayButton2(Day day) {
        JButton dayButton2 = new JButton("Remove selected meal");
        dayButton2.addActionListener(e -> removeMeal(day));
        return dayButton2;
    }

    //EFFECTS: creates button to add a meal
    private JButton createDayButton1(Day day) {
        JButton dayButton1 = new JButton("Add a meal");
        dayButton1.addActionListener(e -> {
            playSound("./data/beep.wav");
            addMeal(day);
        });
        return dayButton1;
    }

    //EFFECTS: primary meal editing window, has buttons to access editing methods
    private void mealWindow(Meal meal) {
        JFrame mealWindow = new JFrame();
        mealWindow.setLayout(new BorderLayout());
        mealWindow.setSize(300, 200);
        JPanel subPanel = new JPanel();
        subPanel.setBackground(new Color(162, 171, 171));
        subPanel.setLayout(new BoxLayout(subPanel, Y_AXIS));

        JButton mealButton1 = createMealButton1(meal);

        JButton mealButton2 = createMealButton2(meal);

        JButton mealButton3 = createMealButton3(meal);

        JButton mealButton4 = createMealButton4(meal);

        JButton mealButton5 = createMealButton5(meal);

        subPanel.add(new JLabel("Now editing meal: " + meal.getMealType()));
        subPanel.add(mealButton1);
        subPanel.add(mealButton2);
        subPanel.add(mealButton3);
        subPanel.add(mealButton4);
        subPanel.add(mealButton5);

        mealWindow.add(subPanel);
        mealWindow.setVisible(true);
        mealWindow.setLocationRelativeTo(null);

    }

    //EFFECTS: creates button for calculating total calories in meal
    private JButton createMealButton5(Meal meal) {
        JButton mealButton5 = new JButton("Total calories in meal");
        mealButton5.addActionListener(e -> JOptionPane.showMessageDialog(new JFrame(),
                ("The meal's calorie total is " + meal.totalCalories()),
                "Calorie Total",
                JOptionPane.PLAIN_MESSAGE));
        return mealButton5;
    }

    //EFFECTS: creates button for editing a food
    private JButton createMealButton4(Meal meal) {
        JButton mealButton4 = new JButton("Edit a food");
        mealButton4.addActionListener(e -> findFoodToEdit(meal));
        return mealButton4;
    }

    //EFFECTS: creates a button to show all foods
    private JButton createMealButton3(Meal meal) {
        JButton mealButton3 = new JButton("Show all foods");
        mealButton3.addActionListener(e -> {
            JFrame previousFoods = new JFrame();
            JTextArea foods = new JTextArea();
            foods.setEditable(false);
            foods.setFont(new Font("Ariel", Font.PLAIN, 40));
            if (meal.getMealSize() == 0) {
                foods.append("There are no foods in this meal");
            } else {
                for (Food f : meal.meal) {
                    foods.append(f.getFoodName() + " - " + f.getCalories() + " Calories \n");
                }
            }
            calendarButtonSetup(previousFoods, foods);
        });
        return mealButton3;
    }

    //EFFECTS: creates a button to remove a specified food
    private JButton createMealButton2(Meal meal) {
        JButton mealButton2 = new JButton("Remove food");
        mealButton2.addActionListener(e -> removeFood(meal));
        return mealButton2;
    }

    //EFFECTS: creates button for adding food
    private JButton createMealButton1(Meal meal) {
        JButton mealButton1 = new JButton("Add food");
        mealButton1.addActionListener(e -> {
            playSound("./data/beep.wav");
            addFood(meal);

        });
        return mealButton1;
    }

    //MODIFIES: food
    //EFFECTS: primary window for editing food, contains buttons for all methods
    public void foodWindow(Food food) {
        JFrame foodWindow = new JFrame();
        foodWindow.setLayout(new BorderLayout());
        foodWindow.setSize(300, 200);
        JPanel subPanel = new JPanel();
        subPanel.setBackground(new Color(125, 134, 156));
        subPanel.setLayout(new BoxLayout(subPanel, Y_AXIS));

        JButton foodButton1;
        foodButton1 = new JButton("Edit name");
        foodButton1.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(new JFrame(),
                    "What would you like to change the name to?", null);
            food.editName(newName);
        });

        JButton foodButton2 = new JButton("Edit calories ");
        foodButton2.addActionListener(e -> {
            String newCalories = JOptionPane.showInputDialog(new JFrame(),
                    "What would you like to change the calories to?", null);
            int tempNum = Integer.parseInt(newCalories);
            food.editCalories(tempNum);
        });

        foodWindowPanelSetup(food, foodWindow, subPanel, foodButton1, foodButton2);

    }

    //EFFECTS: sets up the buttons on the JFrame for the foodWindow
    private void foodWindowPanelSetup(Food food, JFrame foodWindow, JPanel subPanel, JButton foodButton1,
                                      JButton foodButton2) {
        subPanel.add(new JLabel("Now editing meal: " + food.getFoodName()));
        subPanel.add(foodButton1);
        subPanel.add(foodButton2);


        foodWindow.add(subPanel);
        foodWindow.setVisible(true);
        foodWindow.setLocationRelativeTo(null);
    }


    // Centres frame on desktop
    // modifies: this
    // effects:  location of frame is set so frame is centred on desktop
    private void centreOnScreen() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
    }

    //EFFECTS: attempts to save the JSON file, will have popup window indicating success/failure
    private void saveFileTryCatch() {
        try {
            writer.open();
            writer.write(calendar);
            writer.close();
            JOptionPane.showMessageDialog(new JFrame(),
                    "Successfully saved.");
        } catch (FileNotFoundException e) {
            createErrorWindow("Error while saving. Exiting without a save...");
        }
    }

    //EFFECTS: attempts to retrieve past saved data
    private void getSave() {
        try {
            calendar = reader.read();
            JOptionPane.showMessageDialog(new JFrame(),
                    "Previous save data loaded.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Could not find previously saved data. Creating new data.");
            calendar = new Calendar();
        }
    }

    //EFFECTS: attempts to retrieve past saved data
    private void addDay() {
        int dayNumber = calendar.getNumDays();
        dayNumber++;
        Day tempDay = new Day(dayNumber);
        calendar.addDay(tempDay);
        dayNumber++;

        JOptionPane.showMessageDialog(new JFrame(),
                "A new day was added.");
    }

    //EFFECTS: redirects to window for editing day
    public void editDay(Day day) {
        dayWindow(day);
    }

    //MODIFIES: day
    //EFFECTS: adds meal to list of meals
    public void addMeal(Day day) {
        String mealName = JOptionPane.showInputDialog(new JFrame(),
                "Please label the meal", null);
        if (mealName != null) {
            Meal tempMeal = new Meal(mealName);
            day.addMeal(tempMeal);
            mealWindow(tempMeal);
        } else {
            createErrorWindow("Invalid meal name");
        }

    }

    //EFFECTS: searches for meal to edit inside the day by name
    public void findMealToEdit(Day day) {
        final JFrame window = new JFrame();
        String searchTerm = JOptionPane.showInputDialog(window,
                "Which meal would you like to edit?", null);

        if (day.mealExistsInDay(searchTerm)) {
            for (int i = 0; i < day.getNumberOfMeals(); i++) {
                if (searchTerm.equals(day.getMeal(i).getMealType())) {
                    editMeal(day.getMeal(i));
                }
            }
        } else {
            createErrorWindow("That food does not exist.");
        }

    }


    //EFFECTS: redirects to window for editing a meal
    public void editMeal(Meal meal) {
        mealWindow(meal);
    }


    //MODIFIES: meal
    //EFFECTS: removes food from parameter meal based on name
    public void removeFood(Meal meal) {
        final JFrame window = new JFrame();
        String removeFood = JOptionPane.showInputDialog(window,
                "Which food would you like to remove? (enter name)", null);

        if (meal.removeFood(removeFood)) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Food was removed");
        } else {
            createErrorWindow("Food was not found");
        }
    }


    //EFFECTS: searches for food to edit inside meal by name
    private void findFoodToEdit(Meal meal) {
        final JFrame window = new JFrame();
        String searchTerm = JOptionPane.showInputDialog(window,
                "Which food would you like to edit? (enter name)", null);

        if (meal.foodExistsInMeal(searchTerm)) {
            for (int i = 0; i < meal.getMealSize(); i++) {
                if (searchTerm.equals(meal.getFood(i).getFoodName())) {
                    editFood(meal.getFood(i));
                }
            }
        } else {
            createErrorWindow("Food was not found");
        }
    }

    //MODIFIES: day
    //EFFECTS: removes meal from day based on name
    public void removeMeal(Day day) {
        final JFrame window = new JFrame();
        String removeMeal = JOptionPane.showInputDialog(window,
                "Which meal would you like to remove? (enter name)", null);

        if (day.removeMeal(removeMeal)) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Meal was removed");
        } else {
            createErrorWindow("Meal was not found");
        }
    }


    //used the two field JOptionPane code from here:
    //https://stackoverflow.com/questions/41904362/multiple-joptionpane-input-dialogs
    //MODIFIES: meal
    //EFFECTS: adds food to meal
    public void addFood(Meal meal) {
        final JFrame frame = new JFrame();
        JPanel pane;
        JTextField foodNameField;
        JTextField foodCaloField;
        pane = new JPanel();
        pane.setLayout(new GridLayout(0, 2, 2, 2));

        foodNameField = new JTextField(5);
        foodCaloField = new JTextField(5);

        pane.add(new JLabel("What is the food called?"));
        pane.add(foodNameField);

        pane.add(new JLabel("How many calories are in the food?"));
        pane.add(foodCaloField);

        int option = JOptionPane.showConfirmDialog(frame, pane, "Please fill all the fields",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            int tempNum = Integer.parseInt(foodCaloField.getText());
            Food tempFood = new Food(foodNameField.getText(), tempNum);
            meal.addFood(tempFood);
            editFood(tempFood);

        }
    }

    //EFFECTS: redirects to window for  editing food
    public void editFood(Food food) {
        foodWindow(food);
    }

    //EFFECTS: when main window is trying to be closed, prompt user to save;
    private void closeJFrame() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                String[] options = {"Yes", "No"};
                int promptResult = JOptionPane.showOptionDialog(null,
                        "Would you like to save before exiting?",
                        "Warning",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[0]);
                if (promptResult == JOptionPane.YES_OPTION) {
                    saveFileTryCatch();
                }
                System.exit(0);
            }
        });
    }

    //Used this youtube tutorial to figure out how to play sounds:
    //https://www.youtube.com/watch?v=3q4f6I5zi2w
    //EFFECTS: plays an audio sound effect at the parameter filePath
    public static void playSound(String filePath) {
        InputStream soundEffect;
        try {
            soundEffect = new FileInputStream(new File(filePath));
            AudioStream audio = new AudioStream(soundEffect);
            AudioPlayer.player.start(audio);
        } catch (FileNotFoundException e) {
            createErrorWindow("File was not found");
        } catch (IOException e) {
            createErrorWindow("IOException");
        }
    }



/*
    //CODE FROM PREVIOUS VERSIONS THAT IS NOW UNUSED:

//ui element
    public void editFoodPrint() {
        System.out.println("1. Edit food name");
        System.out.println("2. Edit food calories");
        System.out.println("3. Back");
    }

    //ui element
    public void editMealPrint() {
        System.out.println("1. Add food");
        System.out.println("2. Remove food");
        System.out.println("3. Print all foods");
        System.out.println("4. Edit a food");
        System.out.println("5. Meal calorie total");
        System.out.println("6. Back");
    }

    //ui element
    public void editDayPrint() {
        System.out.println("1. Add meal");
        System.out.println("2. Remove meal");
        System.out.println("3. Print all meals");
        System.out.println("4. Edit a meal");
        System.out.println("5. Daily calorie total");
        System.out.println("6. Back");
    }

     //EFFECTS: shows user options regarding food being viewed
    public void viewFood(Food food) {
        System.out.println("Now viewing " + food.getFoodName());
        System.out.println("1. Edit Food");
        System.out.println("2. Go back");

        int c = scanner.nextInt();
        if (c == 1) {
            editFood(food);
        }
    }

    //EFFECTS: prints all the meals
    public void printAllMeals(Day day) {
        if (day.getNumberOfMeals() == 0) {
            System.out.println("There are no meals for this day.");
        } else {
            for (int i = 0; i < day.getNumberOfMeals(); i++) {
                System.out.println("Meal: " + day.getMeal(i).getMealType());
                for (int j = 0; j < day.getMeal(i).getMealSize(); j++) {
                    System.out.println(" - " + day.getMeal(i).getFood(j).getFoodName());
                }
            }
        }
    }

    //EFFECTS: prints all the foods
    private void printAllFoods(Meal meal) {
        if (meal.getMealSize() == 0) {
            System.out.println("There is no food in this meal.");
        } else {
            System.out.println("Meal: " + meal.getMealType());
            for (int i = 0; i < meal.getMealSize(); i++) {
                System.out.println(" - " + meal.getFood(i).getFoodName());

            }
        }
    }

    //EFFECTS: menu that shows what meal is being interacted with
    public void viewMeal(Meal tempMeal) {
        System.out.println("Now viewing " + tempMeal.getMealType());
        System.out.println("1. Edit meal");
        System.out.println("2. Go back");
        int c = scanner.nextInt();
        if (c == 1) {
            editMeal(tempMeal);
        }
    }

    //EFFECTS: checks to see if a food can be removed from meal
    public void removeFoodCheck(Meal meal) {
        if (meal.getMealSize() > 0) {
            removeFood(meal);
        } else {
            System.out.println("No meals have been added yet.");
        }
    }
   //MODIFIES: day
    //EFFECTS: checks to see if there exists a meal to remove
    public void removeMealCheck(Day day) {
        if (day.getNumberOfMeals() > 0) {
            removeMeal(day);
        } else {
            System.out.println("No meals have been added yet.");
        }
    }

    //MODIFIES: this
    //EFFECTS: starts the application
    void run() {
        System.out.println("Welcome to the calorie tracker application.");
        while (flag) {
            printOptions();
            int c = scanner.nextInt();
            if (c == 1) {
                addDay();
            } else if (c == 2) {
                if (calendar.getNumDays() > 0) {
                    seePreviousDays();
                } else {
                    System.out.println("No days have been added yet.");
                }
            } else if (c == 3) {
                selectDayToEdit();
            } else if (c == 4) {
                endApplication();
            } else {
                System.out.println("Not a valid option.");
            }
        }
    }


    //EFFECTS: prints out all days within the calendar
    public void seePreviousDays() {
        for (Day listOfDay : calendar.days) {
            System.out.println("Day " + listOfDay.getDayNum());
        }
    }

    //ui element
    public void printOptions() {
        System.out.println("What would you like to do?");
        System.out.println("1. Add a day");
        System.out.println("2. See previous days");
        System.out.println("3. Edit a day");
        System.out.println("4. Exit");
    }

    //EFFECTS: ends application by setting for loop flag to false
    private void endApplication() {
        confirmQuit();
        System.out.println("Exiting application...");
        flag = false;
    }

    //EFFECTS: checks to see if user wants to save the data before quitting and saves if user answers yes
    private void confirmQuit() {
        boolean flag = true;
        while (flag) {
            System.out.println("Would you like to save before exiting?");
            System.out.println("1. Yes");
            System.out.println("2. No");

            int c = scanner.nextInt();
            if (c == 1) {
                try {
                    writer.open();
                    writer.write(calendar);
                    writer.close();
                    System.out.println("Successfully saved.");
                    flag = false;
                } catch (FileNotFoundException e) {
                    System.out.println("Error while saving. Exiting without a save...");
                    flag = false;
                }
            }
        }

    }

    //EFFECTS: ui to select which day to edit
    public void selectDayToEdit() {
        System.out.println("Which day would you like to edit? (enter number)");
        int c = scanner.nextInt();

        try {
            editDay(calendar.getDay(c - 1));
        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "This day does not exist.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

  */


}
