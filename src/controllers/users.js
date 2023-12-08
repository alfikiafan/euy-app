const User = require('../models').user
const responses = require('../constants/responses')
const responseMaker = require('../utils/response-maker')

async function getUser (req, res) {
  try {
    const { id } = req.params

    const user = await User.findByPk(id, {
      attributes: {
        exclude: ['password']
      }
    })

    if (!user) {
      return responseMaker(res, null, {
        ...responses.notFound,
        message: 'User not found'
      })
    }

    const data = {
      user
    }

    return responseMaker(res, data, {
      ...responses.success,
      message: 'User retrieved successfully'
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
  getUser
}
