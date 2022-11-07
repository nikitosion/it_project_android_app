let express = require('express');
let path = require('path');
let cookieParser = require('cookie-parser');
let logger = require('morgan');


let indexRouter = require('./routes/index');
let mealsRouter = require('./routes/meals');
let foodsRouter = require('./routes/foods');
let ingredientsRouter = require('./routes/ingredients')
let instructionStepsRouter = require("./routes/instruction_steps");
let usersRouter = require("./routes/users")
let commentsRouter = require("./routes/comments");
let app = express();

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/meals', mealsRouter);
app.use('/foods', foodsRouter);
app.use('/ingredients', ingredientsRouter)
app.use('/instruction_steps', instructionStepsRouter)
app.use('/users', usersRouter)
app.use("/comments", commentsRouter);

module.exports = app;
