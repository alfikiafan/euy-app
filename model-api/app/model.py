import tensorflow as tf
import tensorflow_recommenders as tfrs
import numpy as np
from typing import Dict, Text

embedding_dimension = 64

class RecipeModel(tfrs.Model):
  def __init__(self, recipe_titles, user_ids):
    super().__init__()
    unique_recipe_titles = np.unique(np.concatenate(list(recipe_titles.batch(100))))
    unique_user_ids = np.unique(np.concatenate(list(user_ids.batch(100))))

    self.recipe_model: tf.keras.Model = tf.keras.Sequential([
        tf.keras.layers.StringLookup(mask_token=None),
        tf.keras.layers.Embedding(len(unique_recipe_titles) + 1, embedding_dimension)
        ])
    self.recipe_model.layers[0].adapt(unique_recipe_titles)

    self.user_model: tf.keras.Model = tf.keras.Sequential([
        tf.keras.layers.StringLookup(mask_token=None),
        tf.keras.layers.Embedding(len(unique_user_ids) + 1, embedding_dimension)
        ])
    self.user_model.layers[0].adapt(unique_user_ids)

    self.task: tf.keras.layers.Layer = tfrs.tasks.Retrieval(
        metrics=tfrs.metrics.FactorizedTopK(
            candidates=recipe_titles.batch(100).map(self.recipe_model))
        )

  def compute_loss(self, features: Dict[Text, tf.Tensor], training=False) -> tf.Tensor:
    user_embeddings = self.user_model(features["user_id"])
    positive_recipe_embeddings = self.recipe_model(features["recipe_title"])
    return self.task(user_embeddings, positive_recipe_embeddings)
