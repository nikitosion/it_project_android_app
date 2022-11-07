-- RedefineTables
PRAGMA foreign_keys=OFF;
CREATE TABLE "new_Meal" (
    "id" TEXT NOT NULL PRIMARY KEY,
    "name" TEXT NOT NULL,
    "image" TEXT NOT NULL,
    "full_time" INTEGER NOT NULL,
    "difficulty" TEXT NOT NULL,
    "likes" INTEGER NOT NULL,
    "comments" INTEGER NOT NULL,
    "ingredients_ids" TEXT NOT NULL,
    "calories" INTEGER NOT NULL,
    "proteins" INTEGER NOT NULL,
    "fats" INTEGER NOT NULL,
    "carbohydrates" INTEGER NOT NULL,
    "diets" TEXT NOT NULL,
    "tags" TEXT NOT NULL DEFAULT '',
    "instruction_ids" TEXT NOT NULL
);
INSERT INTO "new_Meal" ("calories", "carbohydrates", "comments", "diets", "difficulty", "fats", "full_time", "id", "image", "ingredients_ids", "instruction_ids", "likes", "name", "proteins") SELECT "calories", "carbohydrates", "comments", "diets", "difficulty", "fats", "full_time", "id", "image", "ingredients_ids", "instruction_ids", "likes", "name", "proteins" FROM "Meal";
DROP TABLE "Meal";
ALTER TABLE "new_Meal" RENAME TO "Meal";
PRAGMA foreign_key_check;
PRAGMA foreign_keys=ON;
