import express from "express"
import {Server} from "socket.io"
import * as http from "http";
import {instrument} from "@socket.io/admin-ui";
import {REMINDER_EVENT} from "./src/constants/endpoints";
const eurekaHelper = require('./src/eureka-helper');



const app = express();
app.use(express.json())
const server = http.createServer(app)

// io.in("room") bu odadadaki herkez
// Bizim icin sadece kullanici olucak cunku sadece kullanici kendi id'li odasina baglanabilir.

// Emit in son argumani olarak bir callback fonksiyonu verebiliriz bu sekilde yolladigimiz mesajlarin kullaniciya
// varip varmadigini gorebiliriz.

const io = new Server(server,{
    // path: '/api/v1/notification',
    cors: {
        origin: ["https://admin.socket.io"],
        credentials: true
    }
});

app.post(REMINDER_EVENT,(req, res)=>{
    console.log("TO:",req.body.to)
    // console.log(res)
    io.to(req.body.to).emit("message","Deneme mesaji");
    res.send("Hello word from reminder event");
})

io.on("connection",(socket)=>{
    //console.log("A user connected", socket)
    // @ts-ignore
    socket.join(socket.handshake.query.sessionID)
    console.log("Connected To:" ,socket.handshake.query.sessionID)
    // socket.on("message",(message)=>{
    //     console.log("DENEME",message)
    // })
    socket.on("disconnect",(disconnectReason)=>{
        socket.leave(<string>socket.handshake.query.sessionID);
        console.log(socket.handshake.query.sessionID)
    })
})

instrument(io, {
    auth: false,
    mode: "development",
});
server.listen(3000,()=>{
    console.log("Server starts in port 3000")
})

eurekaHelper.registerWithEureka("notification-service",3000)
