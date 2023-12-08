const { Router } = require('express')
const apiRouter = require('./api.js')
const router = Router()

router.use('/api', apiRouter)

module.exports = router
