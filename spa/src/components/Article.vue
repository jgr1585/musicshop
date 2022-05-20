<script>
export default {
  props: {
    article: Object,
  },
};
</script>

<template>
  <div class="container-xxl bg-primary hero-header">
    <div class="container">
      <div class="row g-5 align-items-center">
        <div class="container-xxl py-5">
          <div class="container">
            <div style="max-width: 500px;">
              <h3 class="text-light mt-0">Title: {{ article.title }}</h3>
              <p class="mb-2">Type: {{ article.type }}</p>
              <p class="mb-2">Label: {{ article.label }}</p>
              <p class="mb-2">Artists: <i v-for="artist in article.artists">{{ artist.name }};</i></p>
              <button class="btn btn-primary rounded-pill" id="button" @click="addToCart">Add to cart</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style>

h2 {
  color: gold;
}

#button {
  color: #000000;
  margin-right: 10px;
  padding-right: 30px;
  padding-left: 30px;
}
</style>
