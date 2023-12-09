const responses = require('../constants/responses')
const responseMaker = require('../utils/response-maker')
const { verifyToken, splitToken } = require('../utils/token-manager')
const User = require('../models').user

async function verifyJwtToken (req, res, next) {
  const tokenHeader = req.headers['x-access-token']
  if (!tokenHeader) {
    return responseMaker(res, null, {
      ...responses.unauthorized,
      message: 'Token not provided'
    })
  }

  const { format, token } = splitToken(tokenHeader)

  if (format !== 'Bearer') {
    return responseMaker(res, null, {
      ...responses.unauthorized,
      message: 'Invalid token format'
    })
  }

  if (!token) {
    return responseMaker(res, null, {
      ...responses.unauthorized,
      message: 'Token not provided'
    })
  }

  try {
    const data = verifyToken(token)
    const userId = data.id
    const user = await User.findByPk(userId)
    if (user) {
      next()
    } else {
      return responseMaker(res, null, {
        ...responses.unauthorized,
        message: 'Invalid token'
      })
    }
  } catch {
    return responseMaker(res, null, {
      ...responses.unauthorized,
      message: 'Failed to authenticate token'
    })
  }
}

module.exports = verifyJwtToken
