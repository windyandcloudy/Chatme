const cloudinary= require("../configs/cloudinary")
const postModel= require("../models/postModel")

module.exports= {
  getAllPost: async(req, res, next)=>{
    let post= await postModel.find().populate("id_acc").sort("-createdAt")
    return res.status(200).json(post)
  },
  createPost: async(req, res, next)=>{
    let {...body}= req.body
    if (req.file){
      let avt= await cloudinary.uploader.upload(req.file.path)
      body.img= avt.secure_url
    }
    body.createdAt= new Date()
    let post= await postModel.create(body)
    let p= await postModel.findById(post._id).populate("id_acc")

    let d= new Date(p.createdAt)
    let now= new Date()
    let time= now.getTime()- d.getTime()
    let days= Math.floor(time/(24*60*60*1000)) 
    let hours= Math.floor(time/(60*60*1000))
    let minutes= Math.floor(time/(60*1000))
    let seconds= Math.floor(time/1000)
    if (days>=1){
      p.createdAt= days + " ngày trước"
    }else if (hours>=1){
      p.createdAt= hours + " giờ trước"
    }else if (minutes>=1){
      p.createdAt= minutes + " phút trước"
    }else {
      p.createdAt= seconds+ " giây trước"
    }

    return res.status(201).json(p)
  },
  updatePost: async(req, res, next)=>{
    let id= req.params.id
    let {...body}= req.body
    let post= await postModel.findByIdAndUpdate(id, body, {new: true}).populate("id_acc")
    return res.status(200).json(post)
  },
  deletePost: async(req, res, next)=>{
    let id= req.params.id
    let post= await postModel.findByIdAndDelete(id).populate("id_acc")
    return res.status(200).json(post)
  }
}