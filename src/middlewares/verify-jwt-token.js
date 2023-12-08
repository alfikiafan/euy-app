const responses = require('../constants/responses')
const responseMaker = require('../utils/response-maker')
const { verifyToken } = require('../utils/token-manager')

function verifyJwtToken (req, res, next) {
  const tokenHeader = req.headers['x-access-token']
  if (!tokenHeader) {
    return responseMaker(res, null, {
      ...responses.unauthorized,
      message: 'Token not provided'
    })
  }

  const [format, token] = tokenHeader.split(' ')

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
    verifyToken(token)
    next()
  } catch {
    return responseMaker(res, null, {
      ...responses.unauthorized,
      message: 'Failed to authenticate token'
    })
  }
}

module.exports = verifyJwtToken
