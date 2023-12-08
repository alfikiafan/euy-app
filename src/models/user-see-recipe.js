/* eslint-disable camelcase */
'use strict'
const {
  Model
} = require('sequelize')
module.exports = (sequelize, DataTypes) => {
  class userSeeRecipe extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate (models) {
      userSeeRecipe.belongsTo(models.user)
      userSeeRecipe.belongsTo(models.recipe)
    }
  }
  userSeeRecipe.init({
    id: {
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    userId: DataTypes.UUID,
    recipeId: DataTypes.UUID
  }, {
    sequelize,
    tableName: 'user_see_recipes',
    modelName: 'userSeeRecipe'
  })
  return userSeeRecipe
}
