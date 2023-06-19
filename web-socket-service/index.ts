import express, { query } from "express";
import { Server } from "socket.io";
import * as http from "http";
import { instrument } from "@socket.io/admin-ui";
import {
  CALL_EVENT,
  MAIL_EVENT,
  MESSAGE_EVENT,
  REMINDER_EVENT,
} from "./src/constant/endpoints";
import { checkReminder } from "./src/api";
import {
  MailEvent,
  MessageEvent,
  NotificationMongoDB,
  ReminderEvent,
} from "./src/model";
import axios from "axios";
import { NotificationType } from "./src/constant/type";

const eurekaHelper = require("./src/eureka-helper");

const app = express();
app.use(express.json());
const server = http.createServer(app);

// io.in("room") bu odadadaki herkez
// Bizim icin sadece kullanici olucak cunku sadece kullanici kendi id'li odasina baglanabilir.

// Emit in son argumani olarak bir callback fonksiyonu verebiliriz bu sekilde yolladigimiz mesajlarin kullaniciya
// varip varmadigini gorebiliriz.

// Maillerin sonradan yollamak icin db'ye kaydetmeye gerek yok egerki mail o an kullaniciya ulasamassa internete
// reconnect oldugunda direkt olarak kaldigi date'ten itibaren istesin.

const io = new Server(server, {
  cors: {
    origin: ["https://admin.socket.io"],
    credentials: true,
  },
});

app.post(REMINDER_EVENT, (req, res) => {
  let body: ReminderEvent = {
    ...req.body,
  };
  console.log("Body:", body);
  io.to(body.to)
    .timeout(10000)
    .emit("reminder", body, async (err: any, response: any) => {
      if (err) {
        //TODO: LocalHost belirlenmis subdomain ile degistirilecek
        // let event = await axios.post(
        //   "http://localhost:8080/api/v1/event/reminder",
        //   { body },
        //   {}
        // );
        // //TODO: LocalHost belirlenmis subdomain ile degistirilecek
        // let unAckedReminder = await axios.post(
        //   "http://localhost:8080/api/v1/unAckedNotification",
        //   {
        //     eventId: event.data.id,
        //     ownerId: body.userList[0],
        //     eventType: NotificationType.REMINDER_EVENT,
        //   }
        // );
      }
      if (response === null || response === undefined) {
      } else {
        // checkReminder(body.reminderId).then((r) => console.log(r));
      }
    });
  res.send(req.body);
});

app.post(MAIL_EVENT, (req, res) => {
  let body: MailEvent = {
    ...req.body,
  };

  io.to(body.toUser)
    .timeout(10000)
    .emit("mail", body, (err: any, response: any) => {
      if (err) {
        //TODO save unAckedReminderEvent
      }
      if (response === null || response === undefined) {
      } else {
        res.send(body);
      }
    });
});

app.post(MESSAGE_EVENT, (req, res) => {
  let body: MessageEvent = {
    ...req.body,
  };
  console.log("Message Event: ");
  console.log({ ...body });
  console.log(body.toUser);
  io.to(body.toUser)
    .timeout(10000)
    .emit("message", body, (err: any, response: any) => {
      if (err) {
        //TODO save unAckedReminderEvent
      }
      if (response === null || response === undefined) {
      } else {
        res.send(body);
        //console.log("Body:", { ...body });
        //checkReminder(body.id).then((r) => console.log(r));
      }
    });
});

// app.post(CALL_EVENT, (req, res) => {
//   let body: ReminderEvent = {
//     ...req.body,
//   };
//   console.log("Body:", body);
//   io.to(body.to).timeout(10000).emit("reminder", body, (err:any,response: any) => {
//     if (err) {
//       //TODO save unAckedReminderEvent
//     }
//     if (response === null || response === undefined) {
//     } else {
//       checkReminder(body.reminderId).then((r) => console.log(r));
//     }
//   });
// });

app.get("/api/v1/notification/", async (req, res) => {
  // id: Types.ObjectId
  // type: NotificationType
  // eventId:string
  // ownerId:string
  // event: typeof MailEvent | typeof MessageEvent | typeof CallEvent | typeof ReminderEvent
  let notifciation = new NotificationMongoDB({ ownerId: "test" });
  await notifciation.save();
  console.log(notifciation);
  res.send("Hello word from notification service");
});

io.on("connection", (socket) => {
  //console.log("A user connected", socket)
  // @ts-ignore
  socket.join(socket.handshake.query.sessionID);
  console.log("Connected To:" + socket.handshake.query.sessionID?.toString());

  //TODO: When user re-connect to websocket server needs to fetch all missed notifications and send to user;

  // let unAckedNotifications = UnAckedNotification.find({ownerId:socket.handshake.query.userId})
  // unAckedNotifications?.forEach((notification:any)=>{
  //   console.log(notification)
  // })

  socket.on("disconnect", (disconnectReason) => {
    socket.leave(<string>socket.handshake.query.sessionID);
  });
});

const saveUnAckedNotifications = () => {
  //TODO: Save all missed notifications for re connection
};

instrument(io, {
  auth: false,
  mode: "development",
});
server.listen(3000, () => {
  console.log("Server starts in port 3000");
});

eurekaHelper.registerWithEureka("notification-service", 3000);
