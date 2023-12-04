const Sequelize = require('sequelize')
const { test, development, production } = require('../config/database')

const getDBConfig = () => {
  if (process.env.NODE_ENV === 'production') {
    return production
  } else if (process.env.NODE_ENV === 'test') {
    return test
  }
  return development
}

const { database, username, password, host, port, dialect } = getDBConfig()
const sequelize = new Sequelize(
  database,
  username,
  password,
  {
    host,
    port,
    dialect
  }
)

module.exports = sequelize
