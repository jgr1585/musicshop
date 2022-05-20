<script setup>
import RestService from "../services/restservice.js";
import Article from "./Article.vue";
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

        RestService.searchArticlesByAttributes(this.title, this.artist, (error, data, response) => {
          this.loading = false;
          if (error) {
            alert(error);
            this.errored = true;
          } else {
            console.log(response);
            this.articles = response.body;
            console.log("API called successfully. Returned data: " + data);
            console.log("API called successfully. Returned response: " + response);
          }
        });
      } else {
        alert("Please fill in at least one field");
      }
    }
  }
};
</script>

<template>
  <div class="container-xxl bg-primary hero-header">
    <div class="container">
      <div class="row g-5 align-items-center">
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
          <button class="btn btn-primary rounded-pill" id="button" @click="searchArticles">
            Search
          </button>
        </div>

        <div>
          <section v-if="errored">
            <p>
              We're sorry, we're not able to retrieve this information at the moment, please try
              back later
            </p>
          </section>

          <section v-else>
            <div v-if="loading">Loading...</div>
            <v-container v-else>
              <v-row v-for="(article, index) in articles">
                <v-col>
                  <Article :article="article" />
                </v-col>
              </v-row>
            </v-container>
          </section>
        </div>
      </div>
    </div>
  </div>
  <div class="card-img">
    <img id="img" alt="adele" src="/src/assets/adele.jpg" width="150" height="180" />
    <img id="img" alt="weeknd" src="/src/assets/weeknd.jpg" width="185" height="180" />
    <!-- <img id="img" alt="billie" src="/src/assets/billie.jfif" width="220" height="180" /> -->
    <img id="img" alt="madonna" src="/src/assets/Madonna.jpg" width="200" height="180" />
    <!-- <img id="img" alt="manson" src="/src/assets/manson.jfif" width="190" height="180" /> -->
    <!-- <img id="img" alt="postmalone" src="/src/assets/postmalone.jfif" width="200" height="180" /> -->
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
