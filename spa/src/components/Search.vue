<script setup>
import Article from "./Article.vue";
import axios from "axios";
</script>

<script>
export default {
  data() {
    return {
      title: "",
      artist: "",
      loading: false,
      errored: false,
      articles: []
    };
  },
  filters: {
    validater(p1, p2) {
      return p1.length > 0 || p2.length > 0 ? true : false;
    }
  },
  methods: {
    searchArticles() {
      if (this.$options.filters.validater(this.title, this.artist)) {
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
            alert(error);
            this.errored = true;
          })
          .finally((this.loading = false));
      } else {
        alert("Please fill in at least one field");
      }
    }
  }
};
</script>

<template>
  <div id="header">
    <h1 class="text-white mb-4 animated slideInDown">MP3-Music-Downloader</h1>
  </div>
  <div class="position-relative w-100" id="search">
    <input
      class="col-form-label border-1 rounded-pill w-50 ps-4 pe-5"
      type="text"
      :value="title"
      @input="title = $event.target.value"
      placeholder="Title"
    />
    <input
      class="col-form-label border-1 rounded-pill w-50 ps-4 pe-5"
      type="text"
      :value="artist"
      @input="artist = $event.target.value"
      placeholder="Artist"
    />
    <button class="btn btn-primary rounded-pill" id="button" @click="searchArticles">Search</button>
  </div>

  <div>
    <section v-if="errored">
      <p>
        We're sorry, we're not able to retrieve this information at the moment, please try back
        later
      </p>
    </section>

    <section v-else>
      <div v-if="loading">Loading...</div>
      <Article v-else v-for="article in articles" :article="article" />
    </section>
  </div>

  <div class="card-img">
    <img id="img" alt="adele" src="src/assets/adele.jpg" width="150" height="180" />
    <img id="img" alt="weeknd" src="src/assets/weeknd.jpg" width="185" height="180" />
    <img id="img" alt="billie" src="src/assets/billie.jfif" width="220" height="180" />
    <img id="img" alt="madonna" src="src/assets/Madonna.jpg" width="200" height="180" />
    <img id="img" alt="manson" src="src/assets/manson.jfif" width="190" height="180" />
    <img id="img" alt="postmalone" src="src/assets/postmalone.jfif" width="200" height="180" />
  </div>
</template>

<style>
#header {
  margin-bottom: 30px;
}
h1 {
  color: gold;
  text-align: center;
}

#search {
  display: flex;
  justify-content: space-between;
}

#img {
  margin-left: 25px;
  border-radius: 100px;
  margin-bottom: 100px;
}
</style>
