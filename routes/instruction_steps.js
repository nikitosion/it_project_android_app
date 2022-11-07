let express = require("express");
let router = express.Router();

let { PrismaClient } = require("@prisma/client");
let prisma = new PrismaClient();
let instructionStepsDatabase = prisma.instructionStep;

router.use("/get_instruction_steps", async (req, res) => {
  let instuctionSteps = await instructionStepsDatabase.findMany();
  res.send(instuctionSteps);
});

router.use("/get_instruction_steps_by_ids", async (req, res) => {
  let instructionSteps_ids = req.query.instructionSteps_ids.split(",");
  let instructionSteps = await instructionStepsDatabase.findMany({
    where: { id: { in: instructionSteps_ids } },
  });
  console.log(instructionSteps)
  res.send(instructionSteps)
});

module.exports = router;
