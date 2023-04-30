"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const mongoose_1 = require("mongoose");
const MailEvent = new mongoose_1.Schema({
    id: mongoose_1.Types.ObjectId,
    from: String,
    title: String,
    mailId: String,
});
exports.default = MailEvent;
