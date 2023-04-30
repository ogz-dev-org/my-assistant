"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const mongoose_1 = require("mongoose");
const ReminderEvent = new mongoose_1.Schema({
    id: mongoose_1.Types.ObjectId,
    from: String,
    to: [{ type: String }],
    title: String,
    content: String,
    reminderId: String,
});
exports.default = ReminderEvent;
