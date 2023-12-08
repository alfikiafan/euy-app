const { Router } = require('express')
const apiRouter = require('./api.js')
const router = Router()

router.use('/api', apiRouter)
router.get('/', (req, res) => {
  res.redirect('/api')
})

module.exports = router
