import {NotificationType} from "../constant/type";
import {model} from "mongoose";
import UnAckedNotification, {UnAckedNotificationType} from "../schema/UnAckedNotification";
export type ReminderEvent = {
  from: string;
  to: string[]
  title: string
  content: string
  reminderId: string
};

export type MessageEvent = {
  from:string
  summaryContent:string
  sendDate:Date
  messageId:string
};

export type MailEvent = {
  title:string
  sendDate:Date
  mailId:string
};

export type CallEvent = {
  from:string
  callDate:Date
};

export type UnAckedNotification = {
  id: string
  type: NotificationType
  eventId:String
};

export const NotificationMongoDB = model<UnAckedNotificationType>('Notification', UnAckedNotification);
