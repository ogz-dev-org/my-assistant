"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const mongoose_1 = require("mongoose");
const CallEvent = new mongoose_1.Schema({
    id: mongoose_1.Types.ObjectId,
    from: String,
    callDate: Date,
});
exports.default = CallEvent;
