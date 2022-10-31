const express= require("express")
const { getAllCmt, createCmt, deleteCmt } = require("../controllers/comment.ctrl")
const asyncMiddleware = require("../middlewares/async.middleware")
const router= express.Router()

const multer= require("multer")
const upload = multer({ dest: "uploads" })


router
  .route("/:id")
  .get(
    asyncMiddleware(getAllCmt)
  )
  .post(
    upload.none(),
    asyncMiddleware(createCmt)
  )
  .delete(
    asyncMiddleware(deleteCmt)
  )



module.exports= router