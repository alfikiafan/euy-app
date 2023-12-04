const User = require('../models/user');
const Recipe = require('../models/recipe');
const { Op } = require('sequelize');

async function getUsers(req, res) {
    try {
        const { id_user } = req.query;
        
        if (id_user) {
            // If idUser is provided, fetch a specific user
            const user = await User.findByPk(id_user);
            if (!user) {
                return res.status(404).json({error: 'User not found'});
            }
            console.log('User: ', JSON.stringify(user, null, 2));
            return res.json(user);
        } else {
            // If idUser is not provided, fetch all users
            const users = await User.findAll();
            console.log(users.every(user => user instanceof User)); // true
            console.log("All users:", JSON.stringify(users, null, 2));
            return res.json(users);
        }

    } catch (error) {
      console.log(error);
      return res.status(500).json({error: 'Belom bisa akses memank'});
    }
}

module.exports = {
    getUsers
}