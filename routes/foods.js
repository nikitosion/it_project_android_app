let express = require("express");
let router = express.Router();

const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();
const foodsDatabase = prisma.food;

router.get("/get_foods", async (req, res) => {
  let foods = await foodsDatabase.findMany();
  res.send(foods);
});

router.get("/get_foods_by_ids", async (req, res) => {
  let foods_ids = req.query.foods_ids.split(",");
  let foods = await foodsDatabase.findMany({ where: { id: {in: foods_ids}}})
  console.log(foods);
});

module.exports = router;
