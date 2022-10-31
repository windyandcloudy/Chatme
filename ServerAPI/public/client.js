let btn= document.getElementById("btn")
let room= document.getElementById("room")
let message= document.getElementById("message")
let btnSend= document.getElementById("btnSend")
let socket= io()
socket.on("connect", ()=>{
  console.log("client connet to server")
})
btn.addEventListener("click", ()=>{
  socket.emit("join", room.value)
})

btnSend.addEventListener("click", ()=>{
  socket.emit("message", message.value)
})

socket.on("message", (data)=>{
  console.log("Message nef: ", data)
})

