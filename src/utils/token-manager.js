const jwt = require('jsonwebtoken')
const appConfig = require('../config')

function generateAccessToken (data) {
  return `Bearer ${jwt.sign(data, appConfig.appKey)}`
  // return `Bearer ${jwt.sign(data, appConfig.appKey, {
  //       expiresIn: appConfig.keyExpirationTime
  //     })}`
}

function generateRefreshToken (data) {
  return `Bearer ${jwt.sign(data, appConfig.appKey)}`
}

function verifyToken (token) {
  let data = null
  jwt.verify(token, appConfig.appKey, (err, decoded) => {
    if (err) {
      throw Error('Invalid token')
    }
    data = decoded
  })
  return data
}

module.exports = {
  generateAccessToken,
  generateRefreshToken,
  verifyToken
}
