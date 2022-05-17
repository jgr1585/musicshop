<script setup>
import article from "./Article.vue";
import axios from "axios";
</script>

<script>
export default {
  data() {
    return {
      loading: false,
      errored: false,
      articles: [],
      title: "",
      artist: "",
    };
  },
  filters: {},
  methods: {
    searchArticles() {
      this.loading = true;
      axios
        .get(
          "http://localhost:8080/backend-1.0-SNAPSHOT/rest/article/search?title=" +
            this.title +
            "&artist=" +
            this.artist
        )
        .then((response) => {
          console.log(response);
          this.articles = response.data;
        })
        .catch((error) => {
          console.log(error);
          this.errored = true;
        })
        .finally((this.loading = false));
    },
  },
};
</script>

<template>
  <header>
    <h1>Search articles</h1>
  </header>

  <main>
    <div>
      <input type="text" v-model="title" placeholder="Title" />
      <input type="text" v-model="artist" placeholder="Artist" />
      <button @click="searchArticles">Search</button>
    </div>

    <div>
      <section v-if="errored">
        <p>
          We're sorry, we're not able to retrieve this information at the moment, please
          try back later
        </p>
      </section>

      <section v-else>
        <div v-if="loading">Loading...</div>
        <div v-else v-for="article in articles">
          <h2>Title: {{ article.title }}</h2>
          <p>Type: {{ article.type }}</p>
          <p>Label: {{ article.label }}</p>
          <p>Artists: <i v-for="artist in article.artists">{{ artist.name }}; </i></p>
          <button @click="addToCart">Add to cart</button>
          <!-- <article :article="article"></article> -->
        </div>
      </section>
    </div>
  </main>
</template>
