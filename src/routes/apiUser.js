const { Router } = require('express');
const { getUsers } = require('../controllers/users')

const router2 = Router();
router2.get('/user/viewUsers', getUsers);

module.exports = router2;