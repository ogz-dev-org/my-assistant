"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const mongoose_1 = require("mongoose");
const UnAckedNotification = new mongoose_1.Schema({
    id: mongoose_1.Types.ObjectId,
    ownerId: String,
    //type: NotificationType,
    eventId: String,
    //event: ReminderEvent || MailEvent || CallEvent || MessageEvent
});
exports.default = UnAckedNotification;
