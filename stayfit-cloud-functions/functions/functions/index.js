const functions = require("firebase-functions");

const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

const cors = require('cors')({origin: true});

// ----------------------------------------------------------------
// Return account information
// ----------------------------------------------------------------

// 
// Unsplash workout images
//
const data = [
    {
        name        : "Blog1",
        description : "Blog 1 description"
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

//const arrLength = data.length;

//const randomPick = Math.floor(Math.random() * arrLength);

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
