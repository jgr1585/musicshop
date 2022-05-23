<script setup>
import Article from "./Article.vue";
import axios from "axios";
import SwaggerClient from "swagger-client";
</script>

<script>
export default {
  props: {
    token: String
  },
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
    search() {
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
            this.loading = false;
            this.articles = response.data;
          })
          .catch((error) => {
            this.loading = false;
            this.errored = true;
            alert(error);
          })
          .finally(() => {
            this.loading = false;
          });

        // const opts = {
        //   title: this.title,
        //   artist: this.artist
        // };
        // new DefaultApi().searchArticlesByAttributes(opts, (error, data, response) => {
        //   this.loading = false;
        //   if (error) {
        //     alert(error);
        //     this.errored = true;
        //   } else {
        //     console.log(response);
        //     this.articles = response.body;
        //     console.log("API called successfully. Returned data: " + data);
        //     console.log("API called successfully. Returned response: " + response);
        //   }
        // });
      } else {
        alert("Please fill in at least one field");
      }
    },
    addToCart() {},
    reset() {
      this.title = "";
      this.artist = "";
      this.errored = false;
    }
  }
};
</script>

<template>
  <div class="container-xxl container hero-header" id="header">
    <div class="container">
      <div class="row g-5 align-items-center">
        <h1 class="text-white mb-4 animated slideInDown">MP3-Music-Downloader</h1>
      </div>
      <div class="position-relative w-auto" id="search">
        <input
          class="v-col-lg-auto border-e rounded-2 w-33 input"
          type="text"
          :value="title"
          @input="title = $event.target.value"
          placeholder="Title"
        />
        <input
          class="v-col-lg-auto border-e rounded-2 w-33 input"
          type="text"
          :value="artist"
          @input="artist = $event.target.value"
          placeholder="Artist"
        />
        <button class="btn btn-primary rounded-pill" id="button" @click="search">Search</button>
        <button class="btn btn-primary rounded-pill" id="button" @click="reset">Reset</button>
      </div>

      <div v-if="errored">
        <p>
          We're sorry, we're not able to retrieve this information at the moment, please try back
          later
        </p>
      </div>

      <div v-else>
        <div v-if="loading">Loading...</div>
        <v-container v-else>
          <v-row v-for="article in articles">
            <v-col>
              <Article :article="article" />
              <div class="col-md-2">
                <button
                  class="btn btn-primary rounded-pill vertical-center"
                  id="button"
                  @click="addToCart"
                >
                  Add to cart
                </button>
              </div>
            </v-col>
          </v-row>
        </v-container>
      </div>
    </div>
  </div>

  <div class="card-img">
    <img id="img" alt="adele" src="/src/assets/adele.jpg" width="150" height="180" />
    <img id="img" alt="weeknd" src="/src/assets/weeknd.jpg" width="185" height="180" />
    <!-- <img id="img" alt="billie" src="src/assets/billie.jfif" width="220" height="180"/> -->
    <img id="img" alt="madonna" src="/src/assets/Madonna.jpg" width="200" height="180" />
    <!-- <img id="img" alt="manson" src="src/assets/manson.jfif" width="190" height="180"/> -->
    <!-- <img id="img" alt="postmalone" src="src/assets/postmalone.jfif" width="200" height="180"/> -->
  </div>
</template>

<style>
#header {
  background-color: #181818 !important;
  padding-bottom: 250px;
}

h1 {
  color: gold;
  text-align: center;
}

#search {
  display: flex;
  justify-content: space-between;
  margin-top: 30px;
}

.input {
  background-color: #ffffff;
}

#img {
  margin-left: 25px;
  border-radius: 100px;
  margin-bottom: 100px;
}
</style>
