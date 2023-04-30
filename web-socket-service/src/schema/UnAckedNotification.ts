import {Schema,Types} from "mongoose";
import {NotificationType} from "../constant/type";


export type UnAckedNotificationType = {
    id: Types.ObjectId
    //type: NotificationType
    eventId:string
    ownerId:string
    //event: typeof MailEvent | typeof MessageEvent | typeof CallEvent | typeof ReminderEvent
};


const UnAckedNotification = new Schema<UnAckedNotificationType>({
    id: Types.ObjectId,
    ownerId: String,
    //type: NotificationType,
    eventId: String,
    //event: ReminderEvent || MailEvent || CallEvent || MessageEvent
});

export default UnAckedNotification;