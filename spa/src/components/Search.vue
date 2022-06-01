<script setup>
import Article from "./Article.vue";
import { DefaultApi } from "../rest";
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

        const opts = {
          title: this.title,
          artist: this.artist
        };
        new DefaultApi().searchArticlesByAttributes(opts, (error, data, response) => {
          this.loading = false;
          if (error) {
            alert(error);
            this.errored = true;
          } else {
            console.log(response);
            this.articles = response.body;
            console.log("API called successfully. Returned data: " + response.body);
          }
        });
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
        this.$emit('updateParent', 'Login')
        this.errored = true;
        return;
      }

      let id;
      article.mediums.forEach((medium) => {
        console.log(medium);
        if (medium.type === "DIGITAL") {
          id = medium.id;
        }
      });

      const opts = {
        body: {
          mediumId: id
        }
      };

      new DefaultApi().addToShoppingCart(opts, (error, data, response) => {
        if (error) {
          alert(error);
        } else {
          console.log(response);
          alert("Successfully added to cart");
        }
      });
    },
    reset() {
      this.title = "";
      this.artist = "";
      this.errored = false;
      this.articles = [];
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
      <div class="row align-items-center pa-5">
        <h1 class="text-white mb-4 animated slideInDown">MP3-Music-Downloader</h1>
      </div>
      <div class="w-auto" id="search">
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
        <div class="w-33">
          <v-btn class="btn-primary rounded-pill w-33" id="button" @click="search" color="#FFD700">
            <v-icon size="25px"> mdi-magnify</v-icon>
          </v-btn>

          <v-btn class="btn-primary rounded-pill w-33" id="button" @click="reset" color="#FFD700">
            <v-icon size="25px"> mdi-filter-remove</v-icon>
          </v-btn>
        </div>
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
          <v-row v-for="article in articles" style="margin-top: 30px" class="align-center">
            <v-col>
              <Article :article="article" />
            </v-col>
            <div>
              <v-col class="col-1">
                <v-btn
                    v-if="article.mediums"
                    class="ma-lg-10 bg-transparent rounded-pill pa-5"
                    @click="addToCart(article)"
                >
                  <v-icon color="#ffd700" size="40"> mdi-cart</v-icon>
                </v-btn>
                <v-btn
                    v-else
                    class="ma-lg-10 bg-transparent rounded-pill pa-5"
                    @click="addToCart(article.albums.at(0))"
                >
                  <v-icon color="#ffd700" size="40"> mdi-cart</v-icon>
                </v-btn>
              </v-col>
            </div>
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
    <img class="bottom-img" alt="postmalone" src="/src/assets/postmalone.jpg" width="200" height="180"/>
  </div>
</template>

<style>
.header {
  background-color: #181818 !important;
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
