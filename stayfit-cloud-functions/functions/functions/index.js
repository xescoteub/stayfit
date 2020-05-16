const functions = require('firebase-functions');
const admin = require('firebase-admin');
const express = require('express');
const bodyParser = require('body-parser');

var serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://stayfit-87c1a.firebaseio.com"
});


const cors = require('cors')({origin: true});

var db = admin.firestore();

// ============================================================================
// Serverless api architecture
// ============================================================================
const app = express();
// https://expressjs.com/en/advanced/best-practice-security.html#at-a-minimum-disable-x-powered-by-header
app.disable("x-powered-by");

// api is your functions name, and you will pass main as
// a parameter
exports.api = functions.https.onRequest(app);

// ============================================================================
// Get user blogs
// ============================================================================
app.get('/blogs/user/:uid', (req, res) => {
    const uid = req.params.uid;

    // Variable used to hold user blogs
    let blogs = [];
    let completedDailyWorkouts = 0;

    // Get blogs database collection
    let blogsRef         = db.collection('blogs');
    let dailyWorkoutRef  = db.collection('daily_exercises');

    dailyWorkoutRef.where('user_id', '==', uid).get()
      .then(snapshot => {
        if (snapshot.empty) {
          console.log('No matching documents.');
          return;
        }
        completedDailyWorkouts = snapshot.size;

        // Push motivation blog if completed daily workouts is less than 5
        if (completedDailyWorkouts < 5) {
            // Get welcome blog
            blogsRef.doc('motivation').get().then(doc => {
            if (doc.exists) {
               console.log("Document data:", doc.data());
               blogs.push(doc.data());
            } else {
               // doc.data() will be undefined in this case
               console.log("No such document!");
            }
            }).catch(error => res.status(400).send(`Cannot get welcome blog: ${error}`));
        }
      });

    // Query blogs that where user_id equals the one from request(uid)
    blogsRef.where('user_id', '==', uid).get()
      .then(snapshot => {
        if (snapshot.empty) {
          console.log('No matching documents.');
          return;
        }

        // Push snapshot documents into blogs array, and send a 200 response to client
        snapshot.forEach(doc => {
          blogs.push(doc.data());
          console.log(doc.id, '=>', doc.data().date);
        });

      }).then(() => {
            // Get welcome blog
            blogsRef.doc('welcome').get().then(doc => {
            if (doc.exists) {
               console.log("Document data:", doc.data());
               blogs.push(doc.data());
            } else {
               // doc.data() will be undefined in this case
               console.log("No such document!");
            }
            }).then(() => {
                res.status(200).send(blogs);
            }).catch(error => res.status(400).send(`Cannot get welcome blog: ${error}`));

      }).catch(error => res.status(400).send(`Cannot get user blogs: ${error}`));
});

// ============================================================================
// Get user analytics
// - Calories
// ============================================================================
app.get('/analytics/user/:uid', async (req, res) => {
    const uid = req.params.uid;

    // Get daily workouts database collection
    let dailyWorkoutsRef = db.collection('daily_exercises');

    dailyWorkoutsRef.where('user_id', '==', uid)
        .get()
        .then(snapshot => {
            if (snapshot.empty) {
                console.log('No matching documents.');
                return;
            }

            // Array that holds response object containing:
            // - BMI
            // - Calories burned
            // - Total workout minutes (for a given day)
            let data = {};

            // Array that holds the total workout time records for a given day
            let totalWorkoutTimeOfToday = [];

            snapshot.forEach(doc => {
                console.log(doc.id, '=>', doc.data());
                let documentDate = new Date(doc.data().date);

                // Filter only documents that have been created today
                // and store it's workout time (in minutes -> total_time / 60) in totalWorkoutTimeOfToday
                if (documentDate.setHours(0,0,0,0) === new Date().setHours(0,0,0,0)) {
                    totalWorkoutTimeOfToday.push(parseInt(doc.data().total_time / 60));
                }
            });

            // Store promises
            const promises = [];
            promises.push(calculateUserCaloriesBurned(uid));
            promises.push(calculateUserBMI(uid));

            // Full-fill all promises
            Promise.all(promises).then((results) => {
                console.log("results : ", results)
                data.calories_burned = results[0];
                data.bmi             = results[1];
                data.total_time      = totalWorkoutTimeOfToday.reduce((a, b) => a + b, 0); // Sum of all workout time for a given day (in minutes)
                res.status(200).send(data);
            });
        })
});

/**
 * Get the personal information for a given user.
 *
 * @param {string} uid The UID of the user.
 */
async function getUserData(uid) {
    const snap = await db.collection('users').doc(uid).get();

    if (snap.exists) {
     //console.log("Document data:", doc.data());
     return snap.data();
    } else {
        // doc.data() will be undefined in this case
        console.log("No such document!");
    }
}

/**
 *  Returns the activity factor for a given number of exercises.
 *
 *  Sedentary i.e. little or no exercise : Activity Factor = 1.2
 *  Lightly active i.e.light exercise 1-3 days/week : Activity Factor = 1.375
 *  Moderately active i.e. exercise 3-5 days/week : Activity Factor = 1.55
 *  Very active i.e.exercise/sports 6-7 days a week) : Activity Factor = 1.725
 *  Extra active i.e hard exercise & physical job : Activity Factor = 1.9
 */
function getActivityFactor(numExercises) {
    switch(numExercises) {
        case numExercises < 1:
            return 1.2;
        case numExercises >= 3:
            return 1.375;
        case numExercises >= 3 && numExercises <= 5:
            return 1.55;
        case numExercises >= 6 && numExercises <= 7:
            return 1.725;
        case numExercises > 7:
            return 1.9;
    }
}

/**
 *  Calculates Body Mass Index (BMI)
 *  Weight in kg
 *  Height in cm
 *
 *  if(bmi < 18.5) {
 *      You are too thin
 *  }
 *  if(bmi > 18.5 && finalBmi < 25) {
 *      You are healthy
 *  }
 *  if(bmi > 25) {
 *      You have overweight
 *  }
 *
 */
async function calculateUserBMI(uid) {
    const user = await getUserData(uid);
    const weight = user.weight;
    const height = user.height;

    var bmi = weight/(height/100*height/100);
    return bmi;
}

/**
 *  ------------------------------------------------
 *  Total calorie expenditure = BMR × ActivityFactor
 *  ------------------------------------------------
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
    const user = await getUserData(uid);

    const activityFactor = 1.375;

    if (user.gender == "man") {
        BMR = 10 * user.weight + 6.25 * user.height - 5 * user.age + 5;
    } else {
      BMR = 10 * user.weight + 6.25 * user.height - 5 * user.age -161;
    }

    var calories = BMR * activityFactor;

    return calories;
}

function calculateRecommendedWaterIntake() {

}

