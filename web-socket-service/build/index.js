"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const socket_io_1 = require("socket.io");
const http = __importStar(require("http"));
const admin_ui_1 = require("@socket.io/admin-ui");
const endpoints_1 = require("./src/constant/endpoints");
const api_1 = require("./src/api");
const model_1 = require("./src/model");
const eurekaHelper = require("./src/eureka-helper");
const app = (0, express_1.default)();
app.use(express_1.default.json());
const server = http.createServer(app);
// io.in("room") bu odadadaki herkez
// Bizim icin sadece kullanici olucak cunku sadece kullanici kendi id'li odasina baglanabilir.
// Emit in son argumani olarak bir callback fonksiyonu verebiliriz bu sekilde yolladigimiz mesajlarin kullaniciya
// varip varmadigini gorebiliriz.
// Maillerin sonradan yollamak icin db'ye kaydetmeye gerek yok egerki mail o an kullaniciya ulasamassa internete
// reconnect oldugunda direkt olarak kaldigi date'ten itibaren istesin.
const io = new socket_io_1.Server(server, {
    cors: {
        origin: ["https://admin.socket.io"],
        credentials: true,
    },
});
app.post(endpoints_1.REMINDER_EVENT, (req, res) => {
    console.log("body:", req.body);
    let body = Object.assign({}, req.body);
    console.log("Body:", body);
    io.to(body.to).timeout(10000).emit("reminder", body, (err, response) => {
        if (err) {
            //TODO save unAckedReminderEvent
        }
        if (response === null || response === undefined) {
        }
        else {
            (0, api_1.checkReminder)(body.reminderId).then((r) => console.log(r));
        }
    });
    res.send(req.body);
});
app.post(endpoints_1.MAIL_EVENT, (req, res) => {
    console.log("TO:", req.body.to);
    // console.log(res)
    io.to(req.body.to).emit("message", "Deneme mesaji", (response) => {
        res.send("Hello word from mail event");
    });
});
app.post(endpoints_1.MESSAGE_EVENT, (req, res) => {
    console.log("TO:", req.body.to);
    // console.log(res)
    io.to(req.body.to).emit("message", "Deneme mesaji", (response) => {
        res.send("Hello word from message event");
    });
});
app.post(endpoints_1.CALL_EVENT, (req, res) => {
    console.log("TO:", req.body.to);
    // console.log(res)
    io.to(req.body.to).emit("message", "Deneme mesaji", (response) => {
        res.send("Hello word from call event");
    });
});
app.get("/api/v1/notification/", (req, res) => __awaiter(void 0, void 0, void 0, function* () {
    // id: Types.ObjectId
    // type: NotificationType
    // eventId:string
    // ownerId:string
    // event: typeof MailEvent | typeof MessageEvent | typeof CallEvent | typeof ReminderEvent
    let notifciation = new model_1.NotificationMongoDB({ ownerId: "test" });
    yield notifciation.save();
    console.log(notifciation);
    res.send("Hello word from notification service");
}));
io.on("connection", (socket) => {
    //console.log("A user connected", socket)
    // @ts-ignore
    socket.join(socket.handshake.query.sessionID);
    console.log("Connected To:", socket.handshake.query.sessionID);
    //TODO: When user re-connect to websocket server needs to fetch all missed notifications and send to user;
    // let unAckedNotifications = UnAckedNotification.find({ownerId:socket.handshake.query.userId})
    // unAckedNotifications?.forEach((notification:any)=>{
    //   console.log(notification)
    // })
    socket.on("disconnect", (disconnectReason) => {
        socket.leave(socket.handshake.query.sessionID);
    });
});
const saveUnAckedNotifications = () => {
    //TODO: Save all missed notifications for re connection
};
(0, admin_ui_1.instrument)(io, {
    auth: false,
    mode: "development",
});
server.listen(3000, () => {
    console.log("Server starts in port 3000");
});
eurekaHelper.registerWithEureka("notification-service", 3000);
