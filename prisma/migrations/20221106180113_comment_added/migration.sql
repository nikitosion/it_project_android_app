-- CreateTable
CREATE TABLE "Comment" (
    "id" TEXT NOT NULL PRIMARY KEY,
    "user_name" TEXT NOT NULL,
    "user_image" TEXT NOT NULL,
    "date" DATETIME NOT NULL,
    "text" TEXT NOT NULL
);
