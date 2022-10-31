const ErrorResponse = require("../helpers/ErrorResponse")
const accountRouter= require("./account.router")
const errorHandle= require("../middlewares/error.handle")
const postRouter= require("./post.router")
const commentRouter= require("./comment.router")
module.exports= (app)=>{
  app.use("/api/accounts", accountRouter)
  app.use("/api/posts", postRouter)
  app.use("/api/comments", commentRouter)

  app.use("*", (req, res)=>{
    throw new ErrorResponse(404, "Not found API")
  })
  app.use(errorHandle)
}