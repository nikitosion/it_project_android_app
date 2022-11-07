let express = require("express");
let router = express.Router();

const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();
const ingredientsDatabase = prisma.ingredient;
const foodsDatabase = prisma.food;

router.get("/get_ingredients", async (req, res) => {
  let ingredients = await ingredientsDatabase.findMany();
  res.send(ingredients);
});

router.get("/get_ingredients_by_ids", async (req, res) => {
  let ingredients_ids = req.query.ingredients_ids.split(",");
  let ingredients = await ingredientsDatabase.findMany({
    where: { id: { in: ingredients_ids } },
  });
  res.send(ingredients)
});
// router.get("/load", async(req, res) => {
//   let ingredients = await ingredientsDatabase.findMany()
//   for (let i = 0; i < ingredients.length; i++) {
//     console.log(ingredients[i]);
//     let food_name = await foodsDatabase.findFirst({
//       where: {id: ingredients[i].food_id}
//     })
//     let measurement = await foodsDatabase.findFirst({
//       where: {id : ingredients[i].food_id}
//     })
//     let ingr = await ingredientsDatabase.update({
//       where: { id: ingredients[i].id },
//       data: { name: food_name.name, measurement: measurement.measurement}
//     })
//     console.log(ingr)
//   }
// })

module.exports = router;
