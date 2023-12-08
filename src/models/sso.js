'use strict'
const {
  Model
} = require('sequelize')

module.exports = (sequelize, DataTypes) => {
  class sso extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate (models) {
      sso.belongsTo(models.user)
    }
  }
  sso.init({
    id: {
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true
    },
    uid: DataTypes.STRING,
    userId: DataTypes.UUID,
    provider: DataTypes.STRING
  }, {
    sequelize,
    modelName: 'sso'
  })

  return sso
}
