<script setup>
import Article from "./Article.vue";
import axios from "axios";
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
      return p1.length > 0 || p2.length > 0;
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
    addToCart(article) {
      if (article.mediums == null) {
        alert("This article is not available");
        return;
      }

      if (localStorage.getItem("token") == null) {
        alert("You are not logged in!");
        this.errored = true;
        return;
      }

      const config = {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
      };

      let id;
      article.mediums.forEach((medium) => {
        console.log(medium);
        if (medium.type == "DIGITAL") {
          id = medium.id;
        }
      });

      const body = {
        mediumId: id
      };

      axios
        .post("http://localhost:8080/backend-1.0-SNAPSHOT/rest/shoppingcart/add", body, config)
        .then((response) => {
          console.log(response);
          alert("Successfully added to cart");
        }) // TODO: show message of 409
        .catch((error) => {
          alert(error);
        });
    },
    reset() {
      this.title = "";
      this.artist = "";
      this.errored = false;
    },
    tokenIsNull() {
      return localStorage.getItem("token") == null;
    }
  }
};
</script>

<template>
  <div class="container-xxl container hero-header header">
    <div class="container">
      <div class="row g-5 align-items-center">
        <h1 class="text-white mb-4 animated slideInDown">MP3-Music-Downloader</h1>
      </div>
      <div class="position-relative w-auto" id="search">
        <input
          class="v-col-lg-auto border-e rounded-pill w-33 input"
          type="text"
          :value="title"
          @input="title = $event.target.value"
          placeholder="Title"
        />
        <input
          class="v-col-lg-auto border-e rounded-pill w-33 input"
          type="text"
          :value="artist"
          @input="artist = $event.target.value"
          placeholder="Artist"
        />
        <button class="btn btn-primary rounded-pill w-25" id="button" @click="search">
          Search
        </button>
        <button class="btn btn-primary rounded-pill w-25" id="button" @click="reset">Reset</button>
      </div>

      <div v-if="errored">
        <p class="text">
          We're sorry, we're not able to retrieve this information at the moment, please try back
          later
        </p>
      </div>

      <div v-else>
        <div v-if="loading">Loading...</div>
        <v-container v-else>
          <v-row v-for="article in articles" style="margin-top: 30px">
            <v-col>
              <Article :article="article" />
            </v-col>
            <v-col cols="2">
              <v-chip
                class="ma-2"
                color="#ffd700"
                text-color="white"
                id="button"
                @click="addToCart(article)"
                :disabled="tokenIsNull()"
              >
                <v-avatar left>
                  <v-icon color="#ffd700"> mdi-basket </v-icon>
                </v-avatar>
                Add to cart
              </v-chip>
            </v-col>
          </v-row>
        </v-container>
      </div>
    </div>
  </div>

  <div class="card-img">
    <img class="bottom-img" alt="adele" src="/src/assets/adele.jpg" width="150" height="180" />
    <img class="bottom-img" alt="weeknd" src="/src/assets/weeknd.jpg" width="185" height="180" />
    <img class="bottom-img" alt="billie" src="/src/assets/billie.jpg" width="220" height="180" />
    <img class="bottom-img" alt="madonna" src="/src/assets/Madonna.jpg" width="200" height="180" />
    <img class="bottom-img" alt="manson" src="/src/assets/manson.jpg" width="190" height="180" />
    <img
      class="bottom-img"
      alt="postmalone"
      src="/src/assets/postmalone.jpg"
      width="200"
      height="180"
    />
  </div>
</template>

<style>
.header {
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
  margin-right: 20px;
}

.bottom-img {
  margin-left: 25px;
  border-radius: 100px;
  margin-bottom: 100px;
}
</style>
