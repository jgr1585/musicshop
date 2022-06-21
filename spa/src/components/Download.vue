<script setup>
import Article from "./Article.vue";
import { DefaultApi as BackendApi } from "../rest/backend/index.js";
import { DefaultApi as DownloadApi } from "../rest/microservicedownload/index.js";
import { DefaultApi as PlaylistApi } from "../rest/microserviceplaylist";
</script>

<script>
export default {
  data() {
    return {
      loading: false,
      articles: [],
      songIDs: [],
      title: "",
      audio: new Audio(),
      currentPlaying: null
    };
  },
  methods: {
    play(id) {
      if (localStorage.getItem("token") == null) {
        this.$notify({
          type: "success",
          title: "You are not logged in!"
        });
        localStorage.setItem("tab", "Download");
        this.$emit("updateParent", "Login");
        return;
      }

      new PlaylistApi().streamSong(id, (error, data, response) => {
        if (error) {
          this.$notify({
            type: "error",
            title: error
          });
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
      let index = this.findNextID();
      this.play(index);
    },
    shuffle() {
      this.play(this.songIDs.at(Math.floor(Math.random() * this.songIDs.length)));
    },
    findNextID() {
      let index = this.songIDs.indexOf(this.currentPlaying);
      if (index === -1) {
        return this.songIDs[0];
      } else {
        return this.songIDs.at((index + 1) % this.songIDs.length);
      }
    },
    downloadAlbum(id) {
      if (localStorage.getItem("token") == null) {
        this.$notify({
          type: "success",
          title: "You are not logged in!"
        });
        localStorage.setItem("tab", "Download");
        this.$emit("updateParent", "Login");
        return;
      }

      new DownloadApi().downloadAlbum(id, (error, data, response) => {
        if (error) {
          this.$notify({
            type: "error",
            title: error
          });
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
      if (localStorage.getItem("token") == null) {
        this.$notify({
          type: "success",
          title: "You are not logged in!"
        });
        localStorage.setItem("tab", "Download");
        this.$emit("updateParent", "Login");
        return;
      }

      new DownloadApi().downloadSong(id, (error, data, response) => {
        if (error) {
          this.$notify({
            type: "error",
            title: error
          });
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
      if (localStorage.getItem("token") == null) {
        this.$notify({
          type: "success",
          title: "You are not logged in!"
        });
        localStorage.setItem("tab", "Download");
        this.$emit("updateParent", "Login");
        return;
      }

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
          this.articles.forEach((article) => {
            article.songs.forEach((song) => {
              this.songIDs.push(song.id);
            });
          });
          console.log(this.songIDs);
        }
      });
    }
  },
  created() {
    this.search();
    this.audio.addEventListener("ended", this.playNext);
  }
};
</script>

<template>
  <div class="container-xxl container hero-header header">
    <div class="container">
      <div class="row g-5 align-items-center pa-5">
        <h1 class="text-white mb-4 animated slideInDown">Playlist</h1>
      </div>
      <div class="row justify-content-center mx-auto">
        <div class="row justify-content-center mx-auto w-33">
          <v-btn
            class="btn-primary rounded-pill w-33"
            id="button"
            @click="playNext"
            color="#FFD700"
          >
            <v-icon size="25px"> mdi-arrow-right-thin </v-icon>
          </v-btn>

          <v-btn class="btn-primary rounded-pill w-33" id="button" @click="shuffle" color="#FFD700">
            <v-icon size="25px"> mdi-shuffle </v-icon>
          </v-btn>
        </div>
      </div>
      <div>
        <div v-if="loading">Loading...</div>
        <v-container v-else v-for="article in articles">
          <v-row>
            <v-col>
              <Article :article="article" />
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
              <Article :article="article" />
            </v-col>
            <v-col
              v-if="audio.played && currentPlaying === article.id"
              class="col-1 row align-items-center"
            >
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
