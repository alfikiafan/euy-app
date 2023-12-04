const { Router } = require('express');
const { getRecipes } = require('../controllers/recipes.js')

const router = Router();
router.get('/recipes', getRecipes)

module.exports = router;