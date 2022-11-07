-- RedefineTables
PRAGMA foreign_keys=OFF;
CREATE TABLE "new_Ingredient" (
    "id" TEXT NOT NULL PRIMARY KEY,
    "name" TEXT NOT NULL DEFAULT '',
    "measurement" TEXT NOT NULL DEFAULT '',
    "food_id" TEXT NOT NULL,
    "amount" REAL NOT NULL
);
INSERT INTO "new_Ingredient" ("amount", "food_id", "id") SELECT "amount", "food_id", "id" FROM "Ingredient";
DROP TABLE "Ingredient";
ALTER TABLE "new_Ingredient" RENAME TO "Ingredient";
PRAGMA foreign_key_check;
PRAGMA foreign_keys=ON;
