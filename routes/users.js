let express = require("express");
let router = express.Router();

let { PrismaClient } = require("@prisma/client");
let prisma = new PrismaClient();
let usersDatabase = prisma.user;

router.get("/get_user_info", async (req, res) => {
  let user_id = req.query.user_id;
  let user = await usersDatabase.findFirst({
    where: { id: user_id },
  });
  res.send(user);
});

router.get("/login_user", async (req, res) => {
  let email = req.query.email;
  let password = req.query.password;
  let user = await usersDatabase.findFirst({
    where: { email: email, password: password },
  });
  res.send(user);
});

router.post("/registrate_user", async (req, res) => {
  let user = req.body;
  console.log(user);
  try {
    await usersDatabase.create({
      data: {
        name: user.name,
        image: "",
        email: user.email,
        password: user.password,
        liked_meal_ids: "",
        comments_ids: "",
      },
    });
    res.send(user);
  } catch (error) {
    console.log(error);
  }
});

router.post("/edit_user_password", async (req, res) => {
  let user_id = req.query.id;
  let new_password = req.query.new_password;
  let user = await usersDatabase.update({
    where: {id : user_id},
    data: {
      password: new_password
    },
  });
  res.send(user)
});

module.exports = router;
