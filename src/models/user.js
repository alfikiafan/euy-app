const db2 = require('../database/sequelize')

const User = db2.define('users', {
    id_user: {
        type: db2.Sequelize.INTEGER,
        autoincrement: true,
        primaryKey: true
    },
    nama_user: {
        type: db2.Sequelize.STRING,
        allowNull: false
    }
}, {
    timestamps: false,
    createdAt: false,
    updatedAt: false
})

module.exports = User