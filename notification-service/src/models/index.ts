export type ReminderEvent = {
  from: string;
  to: string[];
  title: string;
  content: string;
  reminderId: string;
};

export type MessageEvent = {};

export type MailEvent = {};

export type CallEvent = {};

enum NotificationType {
  MAIL,
  MESSAGE,
  REMINDER,
  CALL,
}

export type UnAckedNotification = {
  id: string;
  type: NotificationType;
  event: ReminderEvent | MailEvent | MessageEvent | CallEvent;
};
