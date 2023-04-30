"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const mongoose_1 = __importDefault(require("mongoose"));
module.exports = () => {
    mongoose_1.default.connect("mongodb://127.0.0.1:27017/my-assistant-db", {
        auth: { username: "oguzmuay", password: "22558800" },
        authMechanism: "DEFAULT",
    }).then(r => {
        console.log(r);
        return r;
    });
    mongoose_1.default.connection.on('open', () => {
        console.log('MongoDB: Connected');
    });
    mongoose_1.default.connection.on('error', (err) => {
        console.log('MongoDB: Error', err);
    });
    mongoose_1.default.Promise = global.Promise;
};
