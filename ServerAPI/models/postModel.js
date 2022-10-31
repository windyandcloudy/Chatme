const mongoose= require("mongoose")

const postSchema= mongoose.Schema({
  content: {
    type: String,
    requried: true,
    trim: true
  },
  img: {
    type: String
  },
  like: {
    type: Number,
    default: 0
  },
  id_acc: {
    type: mongoose.SchemaTypes.ObjectId,
    ref: "account"
  },
  createdAt: {
    type: String,
    default: new Date()
  }
}, {
  versionKey: false,
})

postSchema.set("toJSON", {
  transform: function (doc, ret, options) {
    let d= new Date(ret.createdAt)
    let now= new Date()
    let time= now.getTime()- d.getTime()
    let days= Math.floor(time/(24*60*60*1000)) 
    let hours= Math.floor(time/(60*60*1000))
    let minutes= Math.floor(time/(60*1000))
    let seconds= Math.floor(time/1000)
    if (days>=1){
      ret.createdAt= days + " ngày trước"
    }else if (hours>=1){
      ret.createdAt= hours + " giờ trước"
    }else if (minutes>=1){
      ret.createdAt= minutes + " phút trước"
    }else if (seconds>=1){
      ret.createdAt= seconds+ " giây trước"
    }
    return ret;
  }, 
})

postSchema.pre("findById", function(next){
  const _update = { ...this.getUpdate() };

  let d= new Date(_update.createdAt)
  let now= new Date()
  let time= now.getTime()- d.getTime()
  let days= Math.floor(time/(24*60*60*1000)) 
  let hours= Math.floor(time/(60*60*1000))
  let minutes= Math.floor(time/(60*1000))
  let seconds= Math.floor(time/1000)
  if (days>=1){
    _update.createdAt= days + " ngày trước"
  }else if (hours>=1){
    _update.createdAt= hours + " giờ trước"
  }else if (minutes>=1){
    _update.createdAt= minutes + " phút trước"
  }else if (seconds>=1){
    _update.createdAt= seconds+ " giây trước"
  }

  this.setUpdate(_update);
  next();
})

module.exports= mongoose.model("post", postSchema)