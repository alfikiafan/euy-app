const User = require('../models').user

async function checkRegisterPayload (body) {
  let valid = false
  let message = ''
  const { name, email, password } = body

  const emailExists = await User.findOne({
    where: {
      email
    }
  })

  if (name == null) {
    message = 'name is required'
  } else if (email == null) {
    message = 'email is required'
    // eslint-disable-next-line no-useless-escape
  } else if (!email.match(/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/)) {
    message = 'invalid email address'
  } else if (password == null) {
    message = 'password is required'
  } else if (emailExists) {
    message = 'email already exists'
  } else {
    valid = true
  }

  return {
    valid,
    message
  }
}

function checkLoginPayload (body) {
  let valid = false
  let message = ''
  const { email, password } = body

  if (email == null) {
    message = 'email is required'
  } else if (password == null) {
    message = 'password is required'
  } else {
    valid = true
  }

  return {
    valid,
    message
  }
}

async function checkLoginSSOPayload (body) {
  let valid = false
  let message = ''
  const { uid, provider, email, name } = body

  if (uid == null) {
    message = 'uid is required'
  } else if (provider == null) {
    message = 'provider is required'
  } else if (email == null) {
    message = 'email is required'
    // eslint-disable-next-line no-useless-escape
  } else if (!email.match(/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/)) {
    message = 'invalid email address'
  } else if (name == null) {
    message = 'name is required'
  } else {
    valid = true
  }

  return {
    valid,
    message
  }
}

module.exports = {
  checkRegisterPayload,
  checkLoginPayload,
  checkLoginSSOPayload
}
