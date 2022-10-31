const mongoose= require("mongoose")

const connectDB= async()=>{
  try{
    const conn= await mongoose.connect("mongodb+srv://root:123@cluster0.70cd3.mongodb.net/app_chat_vuvu?retryWrites=true&w=majority", {
      useNewUrlParser: true,
      useUnifiedTopology: true,
    })
    console.log("Kết nối db thành công");
  }catch(error){
    console.log(error)
  }
}

module.exports= connectDB