const Sequelize = require('sequelize')
const Recipe = require('../models').recipe
const User = require('../models').user
const UserSeeRecipe = require('../models').userSeeRecipe
const { Op } = require('sequelize')
const { checkSavedRecipePayload } = require('../validator/recipes-validator')
const { verifyToken, splitToken } = require('../utils/token-manager')
const responses = require('../constants/responses')
const responseMaker = require('../utils/response-maker')
const config = require('../config')
const fetch = require('node-fetch')

async function getRecipes (req, res) {
  try {
    const { page, ingredients, search } = req.query
    let currentPage = 1
    let limit = 10
    let offset = 0

    if (page) {
      currentPage = Number(page)
      offset = (currentPage - 1) * limit
    }

    if (currentPage === 1) {
      limit = 100
    }

    const query = {
      limit,
      offset
    }
    const whereClause = {
      where: {
        [Op.and]: []
      }
    }

    if (ingredients) {
      const ingredientList = ingredients.split(',').map((ingredient) => ingredient.trim())
      const ingredientQuery = ingredientList.map((ingredient) => ({
        ingredients: {
          [Op.like]: `%${ingredient}%`
        }
      }))
      whereClause.where[Op.and].push(ingredientQuery)
    }

    if (search) {
      whereClause.where[Op.and].push({
        name: {
          [Op.like]: `%${search}%`
        }
      })
    }

    let recipes = await Recipe.findAll({
      ...whereClause,
      ...query,
      order: Sequelize.literal('rand()')
    })

    // Additional processing for splitting ingredients and steps
    recipes.forEach((recipe) => {
      recipe.ingredients = recipe.ingredients.split('--').filter((ingredient) => ingredient !== '')
      recipe.steps = recipe.steps.split('--').filter((step) => step !== '')
    })

    if (recipes.length >= 10 && currentPage === 1) {
      const { token } = splitToken(req.headers['x-access-token'])
      const jwtToken = verifyToken(token)
      const user = await User.findByPk(jwtToken.id, {
        include: [
          {
            model: UserSeeRecipe,
            limit: 15,
            order: [['createdAt', 'DESC']],
            include: [
              {
                model: Recipe
              }
            ]
          }
        ]
      })

      try {
        if (user.userSeeRecipes.length > 0) {
          // Machine Learning Prediction
          const postData = {
            recipes: recipes.map(recipe => ({
              recipe_id: recipe.id,
              recipe_title: recipe.name.replace(/[^A-Za-z\s]/g, '').toLowerCase()
            })),
            clicked_recipes: user.userSeeRecipes.map(clicked => ({
              user_id: clicked.userId,
              recipe_id: clicked.recipeId,
              recipe_title: clicked.recipe.name.replace(/[^A-Za-z\s]/g, '').toLowerCase()
            }))
          }

          console.log(`${config.modelHostname}/predict`)
          const response = await fetch(`${config.modelHostname}/predict`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(postData)
          })

          const { data } = await response.json()
          const predictedRecipeTitles = data.predictions

          const reorderedRecipes = []
          predictedRecipeTitles.forEach((title) => {
            const recipe = recipes.find(recipe => recipe.name.replace(/[^A-Za-z\s]/g, '').toLowerCase() === title)
            if (recipe) {
              reorderedRecipes.push(recipe)
            }
          })

          recipes = reorderedRecipes
        } else {
          recipes = recipes.slice(0, 10)
        }
      } catch (error) {
        recipes = recipes.slice(0, 10)
      }
    }

    const data = {
      recipes
    }

    return responseMaker(res, data, {
      ...responses.success,
      message: 'Successfully retrieved recipes'
    })
  } catch (error) {
    return responseMaker(res, null, {
      ...responses.error,
      message: error.message
    })
  }
}

async function getOurRecipeChoices (req, res) {
  try {
    const searchList = ['ayam', 'sapi', 'kambing', 'ikan']
    const recipes = await Promise.all(searchList.map((search) => Recipe.findAll({
      where: {
        name: {
          [Op.like]: `%${search}%`
        }
      },
      order: Sequelize.literal('rand()'),
      limit: 10
    })))

    recipes.forEach((recipeCategory) => {
      recipeCategory.forEach((recipe) => {
        recipe.ingredients = recipe.ingredients.split('--').filter((ingredient) => ingredient !== '')
        recipe.steps = recipe.steps.split('--').filter((step) => step !== '')
      })
    })

    const [chicken, beef, lamb, fish] = recipes
    const data = {
      chicken,
      beef,
      lamb,
      fish
    }

    return responseMaker(res, data, {
      ...responses.success,
      message: 'Successfully retrieved recipes'
    })
  } catch (error) {
    return responseMaker(res, null, {
      ...responses.error,
      message: error.message
    })
  }
}

async function saveViewedRecipe (req, res) {
  try {
    const { valid, message } = await checkSavedRecipePayload(req.body)

    if (!valid) {
      return responseMaker(res, null, {
        ...responses.badRequest,
        message
      })
    }

    const { userId, recipeId } = req.body

    const viewedRecipe = await UserSeeRecipe.create({
      userId,
      recipeId
    })

    const userViewedRecipe = await UserSeeRecipe.findOne({
      where: {
        id: viewedRecipe.id
      },
      include: [
        {
          model: User,
          attributes: {
            exclude: ['password']
          }
        },
        {
          model: Recipe
        }
      ]
    })

    const data = {
      viewedRecipe: userViewedRecipe
    }

    return responseMaker(res, data, {
      ...responses.created,
      message: 'Successfully saved recipe view from user'
    })
  } catch (error) {
    return responseMaker(res, null, {
      ...responses.error,
      message: error.message
    })
  }
}

module.exports = {
  getRecipes,
  getOurRecipeChoices,
  saveViewedRecipe
}
