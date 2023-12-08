const { Router } = require('express')
const verifyJwtToken = require('../middlewares/verify-jwt-token.js')
const { getRecipes, getOurRecipeChoices, saveViewedRecipe } = require('../controllers/recipes.js')
const { getUser } = require('../controllers/users')
const { login, ssoLogin, register, updateAccessToken } = require('../controllers/auth.js')

const router = Router()
router.get('/', (req, res) => {
  res.json({
    message: 'Welcome to the EUY API'
  })
})
router.get('/recipes', [verifyJwtToken], getRecipes)
router.get('/recipes/our-choices', [verifyJwtToken], getOurRecipeChoices)
router.post('/recipes/viewed', [verifyJwtToken], saveViewedRecipe)

router.post('/auth/register', register)
router.post('/auth/login', login)
router.post('/auth/sso', ssoLogin)
router.post('/token/update', updateAccessToken)

router.get('/user/:id', [verifyJwtToken], getUser)

module.exports = router
