require('dotenv').config()

module.exports = {
  appKey: process.env.APP_KEY,
  keySignature: process.env.APP_SIGNATURE,
  keyExpirationTime: 5 * 60, // expires in 5 minutes
  modelHostname: process.env.MODEL_HOSTNAME,
}
