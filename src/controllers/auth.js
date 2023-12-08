const User = require('../models').user
const SSO = require('../models').sso
const { Op } = require('sequelize')
const { checkRegisterPayload, checkLoginPayload, checkLoginSSOPayload } = require('../validator/auth-validator')
const responseMaker = require('../utils/response-maker')
const responses = require('../constants/responses')
const bycrypt = require('bcryptjs')
const { verifyToken } = require('../utils/token-manager')
const config = require('../config')

async function register (req, res) {
  try {
    const { valid, message } = await checkRegisterPayload(req.body)
    const { name, email, password } = req.body

    if (!valid) {
      return responseMaker(res, null, {
        ...responses.badRequest,
        message
      })
    }

    const newUser = await User.create({
      name,
      email,
      password: bycrypt.hashSync(password, 8)
    })

    const user = await User.findByPk(newUser.id, {
      attributes: {
        exclude: ['password']
      }
    })

    const data = {
      user
    }

    return responseMaker(res, data, {
      ...responses.created,
      message: 'User created'
    }, {
      includeJwt: true,
      includeRefreshToken: true
    })
  } catch (error) {
    return responseMaker(res, null, {
      ...responses.error,
      message: error.message
    })
  }
}

async function login (req, res) {
  try {
    const { valid, message } = checkLoginPayload(req.body)
    const { email, password } = req.body

    if (!valid) {
      return responseMaker(res, null, {
        ...responses.badRequest,
        message
      })
    }

    const data = await User.findOne({
      where: {
        email
      }
    })

    if (!data) {
      throw new Error('User not found')
    }

    const passwordIsValid = bycrypt.compareSync(password, data.password)

    if (!passwordIsValid) {
      throw new Error('Invalid password')
    }

    delete data.dataValues.password
    const user = {
      user: data
    }

    return responseMaker(res, user, {
      ...responses.success,
      message: 'User logged in'
    }, {
      includeJwt: true,
      includeRefreshToken: true
    })
  } catch (error) {
    return responseMaker(res, null, {
      ...responses.error,
      message: error.message
    })
  }
}

async function ssoLogin (req, res) {
  try {
    const { valid, message } = await checkLoginSSOPayload(req.body)
    const { uid, provider, email, name } = req.body

    if (!valid) {
      return responseMaker(res, null, {
        ...responses.badRequest,
        message
      })
    }

    let ssoData = null

    const sso = await SSO.findOne({
      where: {
        [Op.and]: [
          { uid },
          { provider }
        ]
      },
      include: [
        {
          model: User,
          attributes: {
            exclude: ['password']
          }
        }
      ]
    })

    if (!sso) {
      const user = await User.findOne({
        where: {
          email
        }
      })

      if (!user) {
        // Create new user and SSO (User not registered)
        const newUser = await User.create({
          name,
          email,
          password: ''
        })

        const newSSO = await SSO.create({
          uid,
          userId: newUser.id,
          provider: provider.toLowerCase()
        })

        ssoData = await SSO.findOne({
          where: {
            id: newSSO.id
          },
          include: [
            {
              model: User,
              attributes: {
                exclude: ['password']
              }
            }
          ]
        })
      } else {
        // Create SSO and link registered user (User registered but not have SSO)
        const newSSO = await SSO.create({
          uid,
          userId: user.id,
          provider: provider.toLowerCase()
        })

        ssoData = await SSO.findOne({
          where: {
            id: newSSO.id
          },
          include: {
            model: User,
            attributes: {
              exclude: ['password']
            }
          }
        })
      }
    } else {
      ssoData = sso
    }

    const data = {
      sso: ssoData
    }

    return responseMaker(res, data, {
      ...responses.success,
      message: 'User logged in'
    }, {
      includeJwt: true,
      includeRefreshToken: true
    })
  } catch (error) {
    console.log(error)
    return responseMaker(res, null, {
      ...responses.error,
      message: error.message
    })
  }
}

async function updateAccessToken (req, res) {
  try {
    const tokenHeader = req.headers['x-refresh-token']
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

    const data = verifyToken(token)
    if (data.signature) {
      const isSignatureValid = bycrypt.compareSync(config.keySignature, data.signature)
      if (isSignatureValid) {
        return responseMaker(res, null, {
          ...responses.success,
          message: 'Access token updated'
        }, {
          includeJwt: true
        })
      }
    }

    return responseMaker(res, null, {
      ...responses.unauthorized,
      message: 'Failed to authenticate token'
    })
  } catch (error) {
    return responseMaker(res, null, {
      ...responses.error,
      message: error.message
    })
  }
}

module.exports = {
  register,
  login,
  ssoLogin,
  updateAccessToken
}
