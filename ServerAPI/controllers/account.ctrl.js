const accountModel= require("../models/accountModel")
const cloudinary= require("../configs/cloudinary");
const ErrorResponse = require("../helpers/ErrorResponse");

module.exports= {
  getAllAcc: async(req, res, next)=>{
    let accs= await accountModel.find()
    return res.status(200).json(accs)
  },
  login: async(req, res, next)=>{
    let {username, password}= req.body
    let acc= await accountModel.findOne({username: username, password: password})
    if (acc){
      return res.status(200).json(acc)
    }
    throw new ErrorResponse(400, "Sai tài khoản hoặc mật khẩu")
  },
  createAcc: async(req, res, next)=>{
    let {...body}= req.body
    let acc= await accountModel.create(body)
    return res.status(201).json(acc)
  },
  updateInfor: async(req, res, next)=>{
    let id= req.params.id
    let bd= req.body
    if (req.file){
      let avt= await cloudinary.uploader.upload(req.file.path)
      bd.avt= avt.secure_url
    }
    let acc= await accountModel.findByIdAndUpdate(id, bd, {new: true})
    return res.status(200).json(acc)
  },
  deleteAcc: async(req, res, next)=>{
    let id= req.params.id
    let acc= await accountModel.findByIdAndDelete(id)
    return res.status(200).json(acc)
  }
}