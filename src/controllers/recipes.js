const Sequelize = require('sequelize')
const Recipe = require('../models').recipe
const User = require('../models').user
const UserSeeRecipe = require('../models').userSeeRecipe
const { Op } = require('sequelize')
const { checkSavedRecipePayload } = require('../validator/recipes-validator')
const responses = require('../constants/responses')
const responseMaker = require('../utils/response-maker')

async function getRecipes (req, res) {
  try {
    const { page, ingredients, search } = req.query
    let currentPage = 1
    const limit = 10
    let offset = 0

    if (page) {
      currentPage = page
      offset = (currentPage - 1) * limit
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

    const recipes = await Recipe.findAll({
      ...whereClause,
      ...query
    })

    // Additional processing for splitting ingredients and steps
    recipes.forEach((recipe) => {
      recipe.ingredients = recipe.ingredients.split('--').filter((ingredient) => ingredient !== '')
      recipe.steps = recipe.steps.split('--').filter((step) => step !== '')
    })

    return responseMaker(res, recipes, {
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
    console.error(error)
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
