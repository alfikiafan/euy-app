from flask import Flask, jsonify, request
import pandas as pd
import tensorflow as tf
import tensorflow_recommenders as tfrs
import numpy as np
import re
from app.model import RecipeModel

app = Flask(__name__)

@app.route('/')
def index():
    return {
        "message": "Welcome to EUY Machine Learning API"
    }

@app.route('/predict', methods=['POST'])
def predict():
    try:
        data = request.json
        # mobile_recipes should have minimum 10 row
        mobile_recipes = pd.DataFrame(data['recipes'])
        mobile_recipes['recipe_title'] = mobile_recipes['recipe_title'].apply(lambda x: re.sub(r'[^A-Za-z\s]', '', x).lower())
        # clicked_recipes should have minimum 1 row
        clicked_recipes = pd.DataFrame(data['clicked_recipes'])
        clicked_recipes['recipe_title'] = clicked_recipes['recipe_title'].apply(lambda x: re.sub(r'[^A-Za-z\s]', '', x).lower())

        tensor_mobile_recipes = tf.data.Dataset.from_tensor_slices({
            'recipe_title': tf.constant(mobile_recipes['recipe_title'].values, dtype=tf.string),
        })
        tensor_clicked_recipes = tf.data.Dataset.from_tensor_slices({
            'user_id': tf.constant(clicked_recipes['user_id'].values, dtype=tf.string),
            'recipe_id': tf.constant(clicked_recipes['recipe_id'].values, dtype=tf.string),
            'recipe_title': tf.constant(clicked_recipes['recipe_title'].values, dtype=tf.string),
        })

        tensor_recipe_titles = tensor_mobile_recipes.map(lambda x: x['recipe_title'])
        tensor_user_ids = tensor_clicked_recipes.map(lambda x: x['user_id'])

        unique_user_ids = np.unique(np.concatenate(list(tensor_user_ids.batch(100))))

        shuffled_clicked_recipes = tensor_clicked_recipes.shuffle(100, seed=1, reshuffle_each_iteration=False)
        train = shuffled_clicked_recipes.take(100)
        cached_train = train.shuffle(100).batch(100).cache()

        model = RecipeModel(tensor_recipe_titles, tensor_user_ids)
        model.load_weights("model/weights")
        model.compile(optimizer=tf.keras.optimizers.Adagrad(learning_rate=0.1))
        model.fit(cached_train, epochs=1)

        index = tfrs.layers.factorized_top_k.BruteForce(model.user_model)
        index.index_from_dataset(
            tf.data.Dataset.zip((tensor_recipe_titles.batch(100), tensor_recipe_titles.batch(100).map(model.recipe_model)))
        )

        _, titles = index(tf.constant([unique_user_ids[0]]))
        byte_titles = titles.numpy()
        results = [x.decode('utf-8') for x in byte_titles[0]]
        return jsonify({
            "status": 200,
            "message": "Success to get predictions",
            "data": {
                "predictions": list(set(results))
            }
        })
    except Exception as e:
        return jsonify({
            "status": 500,
            "message": str(e),
            "data": None
        })

if __name__ == '__main__':
    app.run(host='127.0.0.1', port=5000)