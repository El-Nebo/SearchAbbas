const express = require('express');
const app = express();
app.use(express.static('public'));
// listen for requests
app.listen(3000, () => {
    console.log('server started on port 3000');
});

var MongoClient = require('mongodb').MongoClient;
var stemmer = require('porter-stemmer').stemmer;
var url = "mongodb://localhost:27017/SearchAbbas";

MongoClient.connect(url, function (err, db) {
    if (err) throw err;
    console.log("Database gamela!");
    // db.close();
});

app.get('/search/:data?', (req, res) => {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE'); // If needed
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type'); // If needed
    res.setHeader('Access-Control-Allow-Credentials', true); // If needed
    
    let arr = []
    var abb = stemmer(req.params.data);
    console.log(abb);
    MongoClient.connect(url, function (err, db) {
        if (err) throw err;
        db.collection("Indexer").findOne({ Word: abb },
            function (err, fout) {
                if (err) throw err;
                if (fout != null) {
                    fout.URLs.forEach((element, idx) => {ز
                        db.collection("URLTitle").findOne({ URL: element },
                            function (err2, fout2) {
                                if (err2) throw err2;
                                if(fout2)
                                    arr.push(fout2);
                                if (idx === fout.URLs.length - 1) {
                                    let ob ={result: arr};
                                    console.log(ob);
                                    res.send(ob);
                                }
                            });
                    });
                }else{
                    console.log({result:[]});
                    res.send({result:[]});
                }
            });
    });
});
