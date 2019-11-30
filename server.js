var express =require('express');
var mongoose =require('mongoose');
var bodyParser =require('body-parser');


//mongoose.connect("mongodb+srv://mycluster123:123@mycluster-hdjwd.mongodb.net/test?retryWrites=true");

mongoose.connect("mongodb+srv://mycluster123:123@mycluster-hdjwd.mongodb.net/test?retryWrites=true", { useNewUrlParser: true }, function (err) {
    if (err) {
        console.log(err);
        process.exit(-1);
    }
    console.log('Connected to MongoDB....');
});

var app =express();
app.use(bodyParser.urlencoded({extended:true}));
app.use(bodyParser.json);

app.use('/api',require('./routes/api'));

app.listen(1000);
console.log('server is running on port 1000');
