-- RedefineTables
PRAGMA foreign_keys=OFF;
CREATE TABLE "new_Comment" (
    "id" TEXT NOT NULL PRIMARY KEY,
    "meal_id" TEXT NOT NULL,
    "user_id" TEXT NOT NULL,
    "user_name" TEXT NOT NULL,
    "user_image" TEXT NOT NULL,
    "date" TEXT NOT NULL,
    "text" TEXT NOT NULL
);
INSERT INTO "new_Comment" ("date", "id", "meal_id", "text", "user_id", "user_image", "user_name") SELECT "date", "id", "meal_id", "text", "user_id", "user_image", "user_name" FROM "Comment";
DROP TABLE "Comment";
ALTER TABLE "new_Comment" RENAME TO "Comment";
PRAGMA foreign_key_check;
PRAGMA foreign_keys=ON;
