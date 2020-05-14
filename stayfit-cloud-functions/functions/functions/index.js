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
// Inserts welcome blog when user signs up to application
// ============================================================================
exports.insertWelcomeBlog = functions.https.onRequest((req, res) => {

    var user_id = req.body.user_id

    // Add a new document in collection "cities"
    db.collection("blogs").doc(user_id).collection("docs").set({
        name: "Los Angeles",
        state: "CA",
        country: "USA"
    })

    cors(req, res, () => {
        res.status(200).send("Successfully inserted welcome blog.");
	});
});

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
// Get welcome blog
// ============================================================================
app.get('/blogs/welcome', (req, res) => {
    db.collection("blogs").doc('welcome').get().then(doc => {
        if (doc.exists) {
           console.log("Document data:", doc.data());
           res.status(200).send(doc.data());
       } else {
           // doc.data() will be undefined in this case
           console.log("No such document!");
       }
     })
     .catch(error => res.status(400).send(`Cannot get user blogs: ${error}`));
});
// ============================================================================
// Get user blogs
// ============================================================================
app.get('/blogs2/user/:uid', async (req, res) => {

});




// ============================================================================
// Get user blogs
// ============================================================================
app.get('/blogs/user/:uid', (req, res) => {
    const uid = req.params.uid;

    // Variable used to hold user blogs
    let blogs = [];
    let completedDailyWorkouts = 0;

    // Get blogs database collection
    let blogsRef        = db.collection('blogs');
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
