const mongoose= require("mongoose")

const commentSchema= mongoose.Schema({
  content: {
    type: String,
    required: true,
    trim: true
  },
  id_post: {
    type: mongoose.SchemaTypes.ObjectId,
    ref: "post"
  },
  id_acc: {
    type: mongoose.SchemaTypes.ObjectId,
    ref: "account"
  }
}, {
  versionKey: false,
  timestamps: true
})

module.exports= mongoose.model("comment", commentSchema)