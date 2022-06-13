<script setup>
import Article from "./Article.vue";
import {DefaultApi as BackendApi} from "../rest/backend/index.js";
import {DefaultApi as DownloadApi} from "../rest/microservicedownload/index.js";
import {DefaultApi as PlaylistApi} from "../rest/microserviceplaylist";
</script>

<script>

export default {
  data() {
    return {
      loading: false,
      errored: false,
      articles: [],
      title: "",
      audio: new Audio(),
      currentPlaying: null
    };
  },
  methods: {
    play(id) {
      new PlaylistApi().streamSong(id, (error, data, response) => {
        if (error) {
          this.$notify({
            type: "error",
            title: error
          });
          this.errored = true;
        } else {
          console.log(response);
          var url = window.URL.createObjectURL(response.body);
          this.audio.src = url;
          this.audio.play();
          this.currentPlaying = id;
          this.$forceUpdate();
        }
      });
    },
    pause() {
      this.audio.pause();
      this.currentPlaying = null;
      this.$forceUpdate();
    },
    playNext() {
      let index = this.articles.findIndex(article => article.id === this.currentPlaying);
      this.play(this.articles.at((index + 1) % this.articles.length).id);
    },
    downloadAlbum(id) {
      new DownloadApi().downloadAlbum(id, (error, data, response) => {
        if (error) {
          this.$notify({
            type: "error",
            title: error
          });
          this.errored = true;
        } else {
          this.$notify({
            type: "success",
            title: "Download started"
          });
          console.log(response);
          const url = window.URL.createObjectURL(response.body);
          const link = document.createElement("a");
          link.href = url;
          link.setAttribute("download", "album.zip");
          document.body.appendChild(link);
          link.click();
          link.remove();
        }
      });
    },
    downloadSong(id) {
      new DownloadApi().downloadSong(id, (error, data, response) => {
        if (error) {
          this.$notify({
            type: "error",
            title: error
          });
          this.errored = true;
        } else {
          this.$notify({
            type: "success",
            title: "Download started"
          });
          console.log(response);
          const url = window.URL.createObjectURL(response.body);
          const link = document.createElement("a");
          link.href = url;
          link.setAttribute("download", "song.mp3");
          document.body.appendChild(link);
          link.click();
          link.remove();
        }
      });
    },
    search() {
      new BackendApi().getUserPlaylist((error, data, response) => {
        if (error) {
          this.$notify({
            type: "error",
            title: error
          });
          this.errored = true;
        } else {
          console.log(response);
          console.log(response.body);
          this.articles = response.body;
        }
      });
    },
    reset() {
      this.errored = false;
      this.articles = [];
      this.title = "";
    }
  },
  created() {
    this.search();
    //this.audio.addEventListener("ended", this.playNext);
  }
};
</script>

<template>
  <div class="container-xxl container hero-header header">
    <div class="container">
      <div class="row g-5 align-items-center pa-5">
        <h1 class="text-white mb-4 animated slideInDown">Playlist</h1>
      </div>
      <div class="w-auto align-center" id="search">
        <input
            class="v-col-lg-auto border-e rounded-pill w-66 input"
            type="text"
            :value="title"
            @input="title = $event.target.value"
            placeholder="Title"
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
        <v-container v-else v-for="article in articles">
          <v-row>
            <v-col>
              <Article :article="article"/>
            </v-col>
            <v-col class="col-1 row align-items-center">
              <v-btn class="hidden bg-transparent"/>
            </v-col>
            <v-col class="col-1 row align-items-center">
              <v-btn
                  class="ma-lg-10 bg-transparent rounded-pill pa-5"
                  @click="downloadAlbum(article.id)"
              >
                <v-icon color="#ffd700" size="40"> mdi-download</v-icon>
              </v-btn>
            </v-col>
          </v-row>
          <v-row v-for="article in article.songs">
            <v-col>
              <Article :article="article"/>
            </v-col>
            <v-col v-if="this.audio.played && this.currentPlaying === article.id" class="col-1 row align-items-center">
              <v-btn class="ma-lg-10 bg-transparent rounded-pill pa-5" @click="pause()">
                <v-icon color="#ffd700" size="40"> mdi-pause</v-icon>
              </v-btn>
            </v-col>
            <v-col v-else class="col-1 row align-items-center">
              <v-btn class="ma-lg-10 bg-transparent rounded-pill pa-5" @click="play(article.id)">
                <v-icon color="#ffd700" size="40"> mdi-play-circle-outline</v-icon>
              </v-btn>
            </v-col>
            <v-col class="col-1 row align-items-center">
              <v-btn
                  class="ma-lg-10 bg-transparent rounded-pill pa-5"
                  @click="downloadSong(article.id)"
              >
                <v-icon color="#ffd700" size="40"> mdi-download</v-icon>
              </v-btn>
            </v-col>
          </v-row>
        </v-container>
      </div>
    </div>
  </div>
</template>

<style></style>
