const db = require('../database/sequelize')

const Recipe = db.define('recipes', {
    id: {
        type: db.Sequelize.INTEGER,
        autoincrement: true,
        primaryKey: true
    },
    name: {
        type: db.Sequelize.STRING,
        allowNull: false
    },
    ingredients: {
        type: db.Sequelize.TEXT,
        allowNull: false
    },
    steps: {
        type: db.Sequelize.TEXT,
        allowNull: false
    },
    loves: {
        type: db.Sequelize.INTEGER,
        allowNull: false
    },
    description: {
        type: db.Sequelize.TEXT,
        allowNull: false
    },
    image: {
        type: db.Sequelize.STRING,
        allowNull: false
    }
}, {
    timestamps: false,
    createdAt: false,
    updatedAt: false
})

module.exports = Recipe
