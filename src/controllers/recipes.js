const Recipe = require('../models/recipe');
const { Op } = require('sequelize');

// Simulating user viewed recipes with an array
// let viewedRecipes = [];

async function getRecipes(req, res) {
  try {
    const { page, search } = req.query;
    let currentPage = 1;
    let limit = 10;
    let offset = 0;

    if (page) {
      currentPage = page;
      offset = (currentPage - 1) * limit;
    }

    let recipes;

    if (search) {
      const searchlist = search.split(',').map((ingredient) => ingredient.trim());

      recipes = await Recipe.findAll({
        where: {
          [Op.and]: searchlist.map((ingredient) => ({
            ingredients: {
              [Op.like]: `%${ingredient}%`,
            },
          })),
        },
        limit: limit,
        offset: offset,
      });
    } else {
      recipes = await Recipe.findAll({
        limit: limit,
        offset: offset,
      });
    }

    // Additional processing for splitting ingredients and steps
    recipes.forEach((recipe) => {
      recipe.ingredients = recipe.ingredients.split('--').filter((ingredient) => ingredient !== '');
      recipe.steps = recipe.steps.split('--').filter((step) => step !== '');
    });

    return res.json(recipes);
  } catch (error) {
    console.error(error);
    return res.status(500).json({ error: 'Internal Server Error' });
  }
}

// async function saveRecipe(data) {
//   try{
//     const { name } = data;

//     const saved = await Recipe.create({})
//   }
// }

// async function saveViewedRecipe(req, res) {
//   try {
//     const { recipeId } = req.body;

//     // Simulating saving viewed recipes
//     viewedRecipes.push(recipeId);

//     return res.json({ success: true });
//   } catch (error) {
//     console.error(error);
//     return res.status(500).json({ error: 'Internal Server Error' });
//   }
// }

// async function getViewedRecipes(req, res) {
//   try {
//     // Simulating retrieving viewed recipes
//     const uniqueViewedRecipes = Array.from(new Set(viewedRecipes));

//     // Retrieve full recipe details based on recipe IDs (Assuming recipe has an 'id' field)
//     const recipes = await Recipe.findAll({
//       where: {
//         id: {
//           [Op.in]: uniqueViewedRecipes,
//         },
//       },
//     });

//     return res.json(recipes);
//   } catch (error) {
//     console.error(error);
//     return res.status(500).json({ error: 'Internal Server Error' });
//   }
// }

module.exports = {
  getRecipes,
  // saveViewedRecipe,
  // getViewedRecipes,
};
