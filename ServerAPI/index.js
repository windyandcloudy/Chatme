const express = require("express");
const app = express();
const http = require("http");
const server = http.createServer(app);
const { Server } = require("socket.io");
const io = new Server(server, { cors: { origin: "*"}});

const connectDB = require("./configs/database");
const router = require("./routers");


app.use(express.json())
app.use(express.urlencoded({ extended: false }));

io.on("connect", function (client) {
  console.log("client connected...");
  let room;
  client.on("join", function (data) {
    room = data;
    console.log(data)
    client.join(room);
  });
  
  client.on("message", function(data){
    io.to(room).emit("message", data)
  })
});



app.set("views", "./views")
app.set("view engine", "ejs")
app.use(express.static("public"))

app.get("/client", (req, res)=>{
  res.render("index.ejs")
})

connectDB();
router(app);

server.listen(process.env.PORT || 3000, () => {
  console.log("Server run at port: " + 3000);
});