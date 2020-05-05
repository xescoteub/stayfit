const functions = require("firebase-functions");

const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

const cors = require('cors')({origin: true});

// ============================================================================
// Inserts welcome blog when user signs up to application
// ============================================================================

exports.insertWelcomeBlog = functions.https.onRequest((req, res) => {
    var db = admin.firestore();

    var user_id = req.body.user_id

    db.collection('blogs').doc(user_id).set({
        name        : 'Welcome to StayFit!',
        description : 'StayFit is an effortless way to track all your  physical activities including sleep, heart rate and calories burned. Learn more about',
        photo       : "https://firebasestorage.googleapis.com/v0/b/stayfit-87c1a.appspot.com/o/gym.jpg?alt=media&token=16fc3018-6586-402e-8f11-4bd8d88de137"
    });

    cors(req, res, () => {
        res.status(200).send("Successfully inserted welcome blog.");
	});
});

// ============================================================================
// Return user blogs
// ============================================================================

// Unsplash workout images
const data = [
    {
        name        : "Blog1",
        description : "Blog 1 description from cloud functions"
        //photo       : "https://images.unsplash.com/photo-1541694458248-5aa2101c77df?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=800&q=60"
    },
    {
        name        : "Blog2",
        description : "Blog 2 description"
        //photo       : "https://images.unsplash.com/photo-1517344884509-a0c97ec11bcc?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1234&q=80"
    },
    {
        name        : "Blog3",
        description : "Blog 3 description"
        //photo       : "https://images.unsplash.com/photo-1570655565594-9a0161dd16eb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1600&q=80"
    },
    {
        name        : "Blog4",
        description : "Blog 4 description"
        //photo       : "https://images.unsplash.com/photo-1539798488725-7387f3229c49?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=900&q=60"
    }
];

exports.getUserBlogs = functions.https.onRequest((req, res) => {
    if (req.method === "PUT") {
		res.status(403).send("Forbidden!");
		return;
    }
    cors(req, res, () => {
        var object = data[0];
        res.status(200).send(object);
	});
});
