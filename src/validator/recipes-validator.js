const User = require('../models').user
const Recipe = require('../models').recipe

async function checkSavedRecipePayload (body) {
  let valid = false
  let message = ''
  const { userId, recipeId } = body

  const user = await User.findOne({
    where: {
      id: userId
    }
  })

  const recipe = await Recipe.findOne({
    where: {
      id: recipeId
    }
  })

  if (!user) {
    message = 'user not found'
  } else if (!recipe) {
    message = 'recipe not found'
  } else if (userId == null) {
    message = 'userId is required'
  } else if (recipeId == null) {
    message = 'recipeId is required'
  } else {
    valid = true
  }

  return {
    valid,
    message
  }
}

module.exports = {
  checkSavedRecipePayload
}
