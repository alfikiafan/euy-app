require('dotenv').config()

module.exports = {
  appKey: process.env.APP_KEY,
  keySignature: 'euy-data-signature',
  keyExpirationTime: 5 * 60, // expires in 5 minutes
  modelHostname: 'http://127.0.0.1:5000'
}
