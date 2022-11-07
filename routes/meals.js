let express = require("express");
let router = express.Router();

const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();
const mealDatabase = prisma.meal;
const likeDatabase = prisma.like;

router.get("/get_meals", async (req, res) => {
  const meals = await mealDatabase.findMany();
  res.send(meals);
});

router.get("/get_meals_by_id", async (req, res) => {
  let meal_id = req.query.meal_id;
  let meal = await mealDatabase.findFirst({
    where: {
      id: meal_id,
    },
  });
  res.send(meal);
});

router.put("/update_meal_like", async (req, res) => {
  let meal_id = req.query.meal_id;
  let user_id = req.query.user_id;
  let like = await likeDatabase.findFirst({ where: { meal_id: meal_id } });
  let meal = await mealDatabase.findFirst({ where: { id: meal_id } });
  if (like != null) {
    let new_meal = await mealDatabase.update({
      where: { id: meal.id },
      data: { likes: parseInt(meal.likes) - 1 },
    });
    await likeDatabase.delete({
      where: {id: like.id }
    })
    res.send(new_meal);
  } else {
    let new_meal = await mealDatabase.update({
      where: { id: meal.id },
      data: { likes: parseInt(meal.likes) + 1 },
    });
    await likeDatabase.create({
      data: {
        meal_id: meal.id,
        user_id: user_id
      }
    })
    res.send(new_meal)
  }
});

module.exports = router;
