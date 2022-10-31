const express= require("express")
const { 
  getAllPost, createPost, deletePost, updatePost 
} = require("../controllers/post.ctrl")
const asyncMiddleware = require("../middlewares/async.middleware")
const router= express.Router()

const multer= require("multer")
const upload = multer({ dest: "uploads" })


router
  .route("/")
  .get(
    asyncMiddleware(getAllPost)
  )
  .post(
    upload.single("img"),
    asyncMiddleware(createPost)
  )

router
  .route("/:id")
  .delete(
    asyncMiddleware(deletePost)
  )
  .patch(
    upload.none(),
    asyncMiddleware(updatePost)
  )

module.exports= router