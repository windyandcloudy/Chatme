const mongoose= require("mongoose")

const accountSchema= mongoose.Schema({
  username: {
    type: String,
    required: true,
    unique: true,
    trim: true
  },
  password: {
    type: String,
    required: true,
    trim: true
  },
  fullname: {
    type: String,
    required: true
  },
  status: {
    type: Number,
    default: 0
  },
  phone: {
    type: String
  }, 
  avt: {
    type: String,
    default: "https://www.dmarge.com/wp-content/uploads/2021/01/dwayne-the-rock--480x320.jpg"
  }
}, {
  versionKey: false,
  timestamps: true
})

module.exports= mongoose.model("account", accountSchema)