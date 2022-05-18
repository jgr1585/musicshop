<script>
export default {
  props: {
    article: Object,
  },
};
</script>

<template>
  <h2>Title: {{ article.title }}</h2>
  <p>Type: {{ article.type }}</p>
  <p>Label: {{ article.label }}</p>
  <p>Artists: <i v-for="artist in article.artists">{{ artist.name }}; </i></p>
  <button @click="addToCart">Add to cart</button>
</template>
