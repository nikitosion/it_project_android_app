let express = require("express");
let router = express.Router();

let { PrismaClient } = require("@prisma/client");
let prisma = new PrismaClient();
let commentsDatabase = prisma.comment;
let mealDatabase = prisma.meal;

router.get("/get_comment_by_meal_id", async (req, res) => {
  let mealId = req.query.meal_id;
  let comments = await commentsDatabase.findMany({
    where: { meal_id: mealId },
  });
  res.send(comments);
});

router.post("/add_comment", async (req, res) => {
  let comment = req.body;
  let new_comment = await commentsDatabase.create({
    data: {
      meal_id: comment.meal_id,
      user_id: comment.user_id,
      user_name: comment.user_name,
      user_image: comment.user_image,
      date: comment.date,
      text: comment.text
    }
  });
  let meal = await mealDatabase.findFirst({where: {id: comment.meal_id}})
  await mealDatabase.update({where: {id: comment.meal_id}, data: {comments: meal.comments + 1}})
  res.send(await commentsDatabase.findMany());
});

// router.get("/get_comment_by_user_id", async (req, res) => {
//   let email = req.query.email;
//   let password = req.query.password;
//   let user = await usersDatabase.findFirst({
//     where: { email: email, password: password },
//   });
//   res.send(user);
// });

// router.post("/registrate_user", async (req, res) => {
//   let user = req.body;
//   console.log(user);
//   try {
//     await usersDatabase.create({
//       data: {
//         name: user.name,
//         image: "",
//         email: user.email,
//         password: user.password,
//         liked_meal_ids: "",
//         comments_ids: "",
//       },
//     });
//     res.send(user);
//   } catch (error) {
//     console.log(error);
//   }
// });

// router.post("/edit_user_password", async (req, res) => {
//   let userId = req.query.id;
//   let newPassword = req.query.new_password;
//   let user = await usersDatabase.update({
//     where: { id: userId },
//     data: {
//       password: newPassword,
//     },
//   });
//   res.send(user);
// });

module.exports = router;
