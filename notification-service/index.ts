import express from "express";
import { Server } from "socket.io";
import * as http from "http";
import { instrument } from "@socket.io/admin-ui";
import {
  CALL_EVENT,
  MAIL_EVENT,
  MESSAGE_EVENT,
  REMINDER_EVENT,
} from "./src/constants/endpoints";
import { checkReminder } from "./src/api";
import { log } from "util";
import { ReminderEvent } from "./src/models";
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
  console.log("body:", req.body);
  let body: ReminderEvent = {
    ...req.body,
  };
  console.log("Body:", body);
  io.to(body.to).emit("reminder", body, (response: any) => {
    if (response === null || response === undefined) {
    } else {
      checkReminder(body.reminderId).then((r) => console.log(r));
    }
  });
  res.send(req.body);
});

app.post(MAIL_EVENT, (req, res) => {
  console.log("TO:", req.body.to);
  // console.log(res)
  io.to(req.body.to).emit("message", "Deneme mesaji", (response: any) => {
    res.send("Hello word from mail event");
  });
});

app.post(MESSAGE_EVENT, (req, res) => {
  console.log("TO:", req.body.to);
  // console.log(res)
  io.to(req.body.to).emit("message", "Deneme mesaji", (response: any) => {
    res.send("Hello word from message event");
  });
});

app.post(CALL_EVENT, (req, res) => {
  console.log("TO:", req.body.to);
  // console.log(res)
  io.to(req.body.to).emit("message", "Deneme mesaji", (response: any) => {
    res.send("Hello word from call event");
  });
});

app.get("/api/v1/notification/", (req, res) => {
  res.send("Hello word from notification service");
});

io.on("connection", (socket) => {
  //console.log("A user connected", socket)
  // @ts-ignore
  socket.join(socket.handshake.query.sessionID);
  console.log("Connected To:", socket.handshake.query.sessionID);

  //TODO: When user re-connect to websocket server needs to fetch all missed notifications and send to user;

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
