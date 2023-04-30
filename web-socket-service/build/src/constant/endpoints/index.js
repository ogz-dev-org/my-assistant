"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.CALL_EVENT = exports.MESSAGE_EVENT = exports.MAIL_EVENT = exports.REMINDER_EVENT = void 0;
const MAIN_URL = "/api/v1/notification/";
exports.REMINDER_EVENT = MAIN_URL + "reminderEvent";
exports.MAIL_EVENT = MAIN_URL + "mailEvent";
exports.MESSAGE_EVENT = MAIN_URL + "messageEvent";
exports.CALL_EVENT = MAIN_URL + "callEvent";
