const express= require("express")
const router= express.Router()

const asyncMiddleware= require("../middlewares/async.middleware")

const {
  getAllAcc,
  createAcc,
  updateInfor,
  deleteAcc,
  login
}= require("../controllers/account.ctrl")

const multer= require("multer")
const upload = multer({ dest: "uploads" })

router
  .route("/")
  .get(
    asyncMiddleware(getAllAcc)
  )
  .post(
    upload.none(),
    asyncMiddleware(login)
  )

router
  .route("/register")
  .post(
    upload.none(),
    asyncMiddleware(createAcc)
  )

router
  .route("/:id")
  .patch(
    upload.single("avt"),
    asyncMiddleware(updateInfor)
  )
  .delete(
    asyncMiddleware(deleteAcc)
  )

module.exports= router