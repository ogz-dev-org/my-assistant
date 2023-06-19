import { NotificationType } from "../constant/type";
import { model } from "mongoose";
import UnAckedNotification, {
  UnAckedNotificationType,
} from "../schema/UnAckedNotification";
export type ReminderEvent = {
  reminderId: string;
  title: string;
  content: string;
  creatorId: string;
  to: string[];
};

export type MessageEvent = {
  from: string;
  toUser: string;
  summaryContent: string;
  sendDate: Date;
  messageId: string;
};

export type MailEvent = {
  title: string;
  sendDate: Date;
  mailId: string;
  toUser: string;
};

export type CallEvent = {
  from: string;
  callDate: Date;
};

export type UnAckedNotification = {
  id: string;
  type: NotificationType;
  eventId: String;
};

export const NotificationMongoDB = model<UnAckedNotificationType>(
  "Notification",
  UnAckedNotification
);
