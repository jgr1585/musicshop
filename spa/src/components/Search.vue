<script setup>
import Article from "./Article.vue";
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
    validate() {
      let p1 = this.title ? this.title : "";
      let p2 = this.artist ? this.artist : "";
      return p1.length > 0 || p2.length > 0 ? true : false;
    },
    searchArticles() {
      if (this.validate()) {
        this.loading = true;
        let p1 = this.title ? this.title : "";
        let p2 = this.artist ? this.artist : "";
        axios
          .get(
            "http://localhost:8080/backend-1.0-SNAPSHOT/rest/article/search?title=" +
              p1 +
              "&artist=" +
              p2
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
        alert("Error: PLease fill in at least one field");
      }
    },
  },
};
</script>

<template>
  <div id="header">
    <h1 class="text-white mb-4 animated slideInDown">MP3-Music-Downloader</h1>
  </div>
  <div class="position-relative w-100" id="search">
    <input class="col-form-label border-1 rounded-pill w-50 ps-4 pe-5" type="text" v-model="title" placeholder="Title"/>
    <input class="col-form-label border-1 rounded-pill w-50 ps-4 pe-5" type="text" v-model="artist" placeholder="Artist"/>
    <button class="btn btn-primary rounded-pill" id="button" @click="searchArticles">Search</button>
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
        <Article v-else v-for="article in articles" :article="article" />
      </section>
    </div>
</template>

<style>

#header{
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

</style>
