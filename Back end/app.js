const express = require('express');
const app = express();
app.use(express.static('public'));


app.listen(3000, () => {
    console.log('server started on port 3000');
});

var MongoClient = require('mongodb').MongoClient;
var stemmer = require('porter-stemmer').stemmer;
var url = "mongodb://localhost:27017/SearchAbbas";

MongoClient.connect(url, function (err, db) {
    if (err) throw err;
    console.log("Database Connected!");
});

const findTitle = async function(element){
    const title;
     await db.collection("URLTitle").findOne({ URL: element },
             (err, title) => {
                if (err) throw err;
                title = title;
      });
    return title;
}

app.get('/search/:data?', (req, res) => {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE'); 
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type'); 
    res.setHeader('Access-Control-Allow-Credentials', true);
    
    const stem = stemmer(req.params.data);
    MongoClient.connect(url, function (err, db) {
        if (err) throw err;
        db.collection("Indexer").find({ Word: stem },
             (err, urls) => {
                if (err) throw err;
                const searchResults = [];
                if (urls) 
                    urls.URLs.forEach((url) => findTitle(url).then(title=>searchResults.push(title)));
            
               res.send({result:searchResults});
            });
    });
});
