const commentModel= require("../models/commentModel")

module.exports= {
  getAllCmt: async(req, res, next)=>{
    let id_post= req.params.id
    let cmts= await commentModel.find({id_post: id_post}).populate("id_acc")
    return res.status(200).json(cmts)
  },
  createCmt: async(req, res, next)=>{
    let {...body}= req.body
    body.id_post= req.params.id
    let cmt= await commentModel.create(body)
    let c= await commentModel.findById(cmt._id).populate("id_acc")
    return res.status(201).json(c)
  },
  deleteCmt: async(req, res, next)=>{
    let id_cmt= req.params.id
    let cmt= await commentModel.findByIdAndDelete(id_cmt).populate("id_acc")
    return res.status(200).json(cmt)
  }
}