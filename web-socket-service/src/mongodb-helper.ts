import mongoose from "mongoose";


module.exports = () => {

    mongoose.connect("mongodb://127.0.0.1:27017/my-assistant-db",
        {
            auth: {username: "oguzmuay", password: "22558800"},
            authMechanism:"DEFAULT",

        }
    ).then( r => {
        console.log(r)
        return r;
    });

    mongoose.connection.on('open', () => {
        console.log('MongoDB: Connected');
    });
    mongoose.connection.on('error', (err) => {
        console.log('MongoDB: Error', err);
    });
    mongoose.Promise = global.Promise;
}