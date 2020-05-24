const functions = require('firebase-functions');
const admin = require('firebase-admin');
const express = require('express');
const bodyParser = require('body-parser');

// Get service account key
var serviceAccount = require("./serviceAccountKey.json");

// Initialize express app
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://stayfit-87c1a.firebaseio.com"
});

const cors = require('cors')({origin: true});

// Get the firestore database instance reference
var db = admin.firestore();

// ============================================================================
// Server-less api architecture
// ============================================================================
const app = express();
// https://expressjs.com/en/advanced/best-practice-security.html#at-a-minimum-disable-x-powered-by-header
app.disable("x-powered-by");

// api is your functions name
exports.api = functions.https.onRequest(app);

// ============================================================================
//
// USER BLOG'S
//
// ============================================================================
app.get('/blogs/user/:uid', (req, res) => {
    const uid = req.params.uid;

    // Get blogs database collection
    let blogsRef = db.collection('blogs');

    // Array used to hold user recommended blogs
    let blogs = [];

    // The user completed workouts
    let completedDailyWorkouts = 0;

    // Get user completed daily workouts count
    db.collection('daily_exercises').where('user_id', '==', uid).get()
      .then(snapshot => {
        if (snapshot.empty) {
          console.log('No matching documents.');
          return;
        }
        completedDailyWorkouts = snapshot.size;
      }).then(() => {
          // Query blogs that where user_id equals the one from request(uid)
          blogsRef.get()
            .then(async snapshot => {
              if (snapshot.empty) {
                console.log('No matching documents.');
                return;
              }

              // Get recommended blogs
              const recommendations = await handleUserBlogRecommendation(completedDailyWorkouts)
              //console.log("recommendations: ", recommendations);
              res.status(200).send(recommendations)
            }).catch(error => res.status(400).send(`Cannot get user blogs: ${error}`));
      });
});

/**
 * Utility function to handle user blogs recommendation based on user data
 */
async function handleUserBlogRecommendation(completedWorkouts) {
    // Get daily workouts database collection
    let blogsRef = db.collection('blogs');

    // Array to hold list of blog objects
    const blogs = [];

    if (completedWorkouts == 0) {
        // Get welcome blog
        await blogsRef.doc('welcome').get().then(doc => {
            if (doc.exists) {
               //console.log("Document data:", doc.data());
               blogs.push(doc.data());
            } else {
               // doc.data() will be undefined in this case
               console.log("No such document!");
            }
        });
    }
    else if (completedWorkouts <= 2) {
        // Get motivation blog
        await blogsRef.doc('motivation').get().then(doc => {
            if (doc.exists) {
               //console.log("Document data:", doc.data());
               blogs.push(doc.data());
            } else {
               // doc.data() will be undefined in this case
               console.log("No such document!");
            }
        }).catch(error => res.status(400).send(`Cannot get welcome blog: ${error}`));
    }
    else if (completedWorkouts >= 4){
        await blogsRef.doc('healthy_food').get().then(doc => {
            if (doc.exists) {
               //console.log("Document data:", doc.data());
               blogs.push(doc.data());
            } else {
               // doc.data() will be undefined in this case
               console.log("No such document!");
            }
        }).catch(error => res.status(400).send(`Cannot get welcome blog: ${error}`));
    }
    else {
        //
    }
    return blogs;
}

// ============================================================================
//  USER ANALYTICS
//      - BMI
//      - Calories burned
//      - Total workout minutes (for a given day)
// ============================================================================
app.get('/analytics/user/:uid', async (req, res) => {
    const uid = req.params.uid;

    // Get daily workouts database collection
    let dailyWorkoutsRef = db.collection('daily_exercises');

    // Get all daily workouts for a given user
    dailyWorkoutsRef.where('user_id', '==', uid)
        .get()
        .then(async snapshot => {
            // If snapshot is empty (meaning there are no daily workouts associated for that user)
            // send the user analytics data object with the user available data (BMI), the rest (calories burned/total workout time)
            // are set to zero, since the user has no data to compute those values.
            if (snapshot.empty) {
                console.log('User: ' + uid + " has no daily workouts registered.");
                const bmi = await calculateUserBMI(uid)
                const data = {
                    calories_burned : "0",
                    bmi             : bmi,
                    total_time      : 0
                }
                // Send data object
                res.status(200).send(data);
                return;
            }

            // Array that holds response object containing:
            // - BMI
            // - Calories burned
            // - Total workout minutes (for a given day)
            let data = {};

            // Array that holds the total workout time records for a given day
            let totalWorkoutTimeOfToday = [];

            // Iterate throughout all user workouts and compute the total
            // workout hours for the current day
            snapshot.forEach(doc => {
                console.log(doc.id, '=>', doc.data());
                let documentDate = new Date(doc.data().date);

                // Filter only documents that have been created today
                // and store it's workout time (in minutes -> total_time / 60) in totalWorkoutTimeOfToday
                if (documentDate.setHours(0,0,0,0) === new Date().setHours(0,0,0,0)) {
                    totalWorkoutTimeOfToday.push(parseInt(doc.data().total_time / 60));
                }
            });

            // --------------------------------------------------------------
            // Fetch user analytics:
            //
            // - BMI
            // - Calories
            // - Water intake
            // - Total workout time (in minutes)
            // --------------------------------------------------------------
            const bmi           = await calculateUserBMI(uid);
            const calories      = await calculateUserCaloriesBurned(uid);
            const water_intake  = await calculateRecommendedWaterIntake(uid);

            /*
            console.log("bmi: ", bmi);
            console.log("calories: ", calories);
            console.log("water_intake: ", water_intake);
            */

            // Append the user BMI
            data.bmi = bmi;

            // The calories
            data.calories = calories;

            // Append the user recommended water intake
            data.water_intake = water_intake;

            // Sum of all workout time for a given day (in minutes)
            data.total_time = totalWorkoutTimeOfToday.reduce((a, b) => a + b, 0);

            // Send data object
            res.status(200).send(data);
        });
});

/**
 * Get the personal information for a given user.
 *      - User name
 *      - User email
 *      - User gender
 *      - User age
 *      - User weight
 *      - User height
 *
 * @param {string} uid The UID of the user.
 */
async function getUserData(uid) {
    // Get user document from users db collection
    const snap = await db.collection('users').doc(uid).get();

    // If a document exists for the provided uid, send the data object
    if (snap.exists) {
        //console.log("Document data:", doc.data());
        return snap.data();
    } else {
        // doc.data() will be undefined in this case
        console.log("No such document for uid: ", uid);
    }
}

/**
 *  Returns the activity factor for a given number of exercises.
 *
 *      - Sedentary i.e. little or no exercise : Activity Factor = 1.2
 *
 *      - Lightly active i.e.light exercise 1-3 days/week : Activity Factor = 1.375
 *
 *      - Moderately active i.e. exercise 3-5 days/week : Activity Factor = 1.55
 *
 *      - Very active i.e.exercise/sports 6-7 days a week) : Activity Factor = 1.725
 *
 *      - Extra active i.e hard exercise & physical job : Activity Factor = 1.9
 */
function getActivityFactor(numExercises) {
    if (numExercises >= 1 && numExercises < 3) {
        return 1.2;
    }
    else if (numExercises >= 3 && numExercises < 5) {
        return 1.375;
    }
    else if (numExercises > 3 && numExercises < 5) {
        return 1.55;
    }
    else if(numExercises >= 5 && numExercises < 7) {
        return 1.725;
    }
    else if (numExercises > 7) {
        return 1.9;
    }
    else {
        return 0;
    }
}

/**
 *  Calculates Body Mass Index (BMI) and returns object with BMI value + advice message
 *  (The weight is in kg and the height in cm)
 */
async function calculateUserBMI(uid) {
    // Get user data (weight / height)
    const user = await getUserData(uid);

    const weight = user.user_weight;
    const height = user.user_height;

    // The response object that will hold the actual BMI value plus an informative message
    const obj = {};

    // Compute BMI
    const bmi = parseInt(weight/(height/100*height/100));

    // Append bmi to response object
    obj.bmi = bmi;

    // Underweight message
    const under_weight_message       = "For your height, a normal weight range would be from 129 to 174 pounds. Talk with your healthcare provider to determine possible causes of underweight and if you need to gain weight."

    // Normal weight message
    const normal_weight_message      = "Maintaining a healthy weight may reduce the risk of chronic diseases associated with overweight and obesity."
                                     + "For information about the importance of a healthy diet and physical activity in maintaining a healthy weight, visit Preventing Weight Gain."
    // Overweight message
    const over_weight_advice_message = "Anyone who is overweight should try to avoid gaining additional weight."
                                     + "Additionally, if you are overweight with other risk factors (such as high LDL cholesterol, low HDL cholesterol, or high blood pressure), you should try to lose weight."
                                     + "Even a small weight loss (just 10% of your current weight) may help lower the risk of disease. Talk with your healthcare provider to determine appropriate ways to lose weight."
                                     + "For information about the importance of a healthy diet and physical activity in reaching a healthy weight, visit Healthy Weight."

    // Append the corresponding BMI result/advice message according to the previously computed value.
    if (bmi < 18.5) {
        obj.result_message = "Your BMI is " + bmi + " indicating your weight is in the Underweight category for your height.";
        obj.advice_message = under_weight_message;
    }
    if (bmi > 18.5 && bmi < 25) {
        obj.result_message = "Your BMI is " + bmi + " indicating your weight is in the Normal category for your height.";
        obj.advice_message = normal_weight_message;
    }
    if (bmi > 25) {
        obj.result_message = "Your BMI is " + bmi + " indicating your weight is in the Overweight category for your height.";
        obj.advice_message = over_weight_advice_message;
    }
    return obj;
}

/**
 * Returns the total calories expenditure for a given user
 *
 *  Total calories expenditure = BMR × ActivityFactor
 *
 *  BMR:
 *  - Women : BMR=655+9.6×weight+1.8×height−4.7×age
 *  - Men   : BMR=66+13.7×weight+5×height−6.8×age
 *  where weight is in kilograms, height is in centimeters and age is in years
 *
 *  Sedentary i.e. little or no exercise : Activity Factor = 1.2
 *  Lightly active i.e.light exercise 1-3 days/week : Activity Factor = 1.375
 *  Moderately active i.e. exercise 3-5 days/week : Activity Factor = 1.55
 *  Very active i.e.exercise/sports 6-7 days a week) : Activity Factor = 1.725
 *  Extra active i.e hard exercise & physical job : Activity Factor = 1.9
 *
 *  Example:
 *  activityFactor = 1.55 ( Moderate exercise )
 *  BMR = 1200
 *  Calories -> 1860
 *
 *  The total calories needed to maintain the current weight must be the same as the total calorie expenditure i.e. 1860
 *  If the total daily calories consumed is higher than the total calories needed, the person will gain weight.
 *  On the other hand, if the total daily calories consumed is lower than the total calories needed, the person will lose weight.
 *
 */
async function calculateUserCaloriesBurned(uid) {
    // Get user data
    const user = await getUserData(uid);

    // The user completed workouts
    let completedDailyWorkouts = 0;

    // Get user completed daily workouts count
    await db.collection('daily_exercises').where('user_id', '==', uid).get()
      .then(snapshot => {
        if (snapshot.empty) {
          console.log('No matching documents for uid: ', uid);
          return;
        }
        completedDailyWorkouts = snapshot.size;
    });

    // Get user activity factor (computed accordingly the daily completed workouts)
    const activityFactor = await getActivityFactor(completedDailyWorkouts);

    // Compute the BMR according to user data
    if (user.user_gender == "man") {
        BMR = 10 * user.user_weight + 6.25 * user.user_height - 5 * user.user_age + 5;
    } else {
      BMR = 10 * user.user_weight + 6.25 * user.user_height - 5 * user.user_age -161;
    }

    // Compute calories needed to maintain current user weight
    let calories = BMR * activityFactor;

    // Parse the value as a float value
    calories = parseFloat(calories);

    // Format the value w/ the specified number
    // of decimal places and return it.
    return calories.toFixed(1);
}

/**
 * Returns an estimation of the recommended daily water intake based on user data
 */
async function calculateRecommendedWaterIntake(uid) {
    // TODO
    const user = await getUserData(uid);
    return 100;
}