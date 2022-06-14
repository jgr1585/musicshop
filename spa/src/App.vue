<script setup>
import Search from "./components/Search.vue";
import ShoppingCart from "./components/ShoppingCart.vue";
import Login from "./components/Login.vue";
import Download from "./components/Download.vue";

import { DefaultApi as BackendApi } from "./rest/backend/index.js";
import { DefaultApi as DownloadApi } from "./rest/microservicedownload/index.js";
import { DefaultApi as PlaylistApi } from "./rest/microserviceplaylist/index.js";
</script>

<script>
export default {
  components: {
    Search,
    ShoppingCart,
    Login,
    Download
  },
  data() {
    return {
      currentTab: "Search"
    };
  },
  methods: {
    setTab(tab) {
      this.currentTab = tab;
    },
    logout() {
      localStorage.removeItem("token");
      location.reload();
    },
    tokenIsNull() {
      return localStorage.getItem("token") == null;
    },
    setCorrectTab(tab) {
      localStorage.setItem("tab", tab);
      if (localStorage.getItem("token") == null) {
        this.setTab("Login");
        this.$forceUpdate();
      } else {
        this.setTab(tab);
      }
    },
    addTokenToApiClient() {
      new BackendApi().apiClient.authentications = {
        Authentication: {
          type: "oauth2",
          accessToken: localStorage.getItem("token")
        }
      };
      new DownloadApi().apiClient.authentications = {
        Authentication: {
          type: "oauth2",
          accessToken: localStorage.getItem("token")
        }
      };
      new PlaylistApi().apiClient.authentications = {
        Authentication: {
          type: "oauth2",
          accessToken: localStorage.getItem("token")
        }
      };
    }
  },
  created() {
    console.log(localStorage.getItem("token"));
    if (localStorage.getItem("token") != null) {
      this.addTokenToApiClient();
    }
    if (localStorage.getItem("tab") != null) {
      this.setTab(localStorage.getItem("tab"));
    }
  }
};
</script>

<template>
  <div class="container-xxl bg-white p-0">
    <nav class="navbar navbar-expand navbar-light px-4 px-lg-5 py-4" id="home">
      <a href="index.html">
        <img alt="logo" src="./assets/logo.png" width="200" height="120" />
      </a>
      <div>
        <v-btn class="btn btn-primary rounded-pill" id="button" @click="setTab('Search')">
          <v-icon size="25px"> mdi-magnify </v-icon>
          Search
        </v-btn>
        <v-btn
          class="btn btn-primary rounded-pill"
          id="button"
          @click="setCorrectTab('ShoppingCart')"
        >
          <v-icon size="25px"> mdi-cart </v-icon>
          Shopping Cart
        </v-btn>
        <v-btn class="btn btn-primary rounded-pill" id="button" @click="setCorrectTab('Download')">
          <v-icon size="25px"> mdi-music </v-icon>
          Playlist
        </v-btn>
        <!-- <v-btn
          v-if="tokenIsNull()"
          class="btn btn-primary rounded-pill"
          id="button"
          @click="setTab('Login')"
        >
          <v-icon size="25px"> mdi-login </v-icon>
          Login
        </v-btn>
        <v-btn v-else class="btn btn-primary rounded-pill" id="button" @click="logout">
          <v-icon size="25px"> mdi-logout </v-icon>
          Logout
        </v-btn> -->
        <v-btn
          v-if="!tokenIsNull()"
          class="btn btn-primary rounded-pill"
          id="button"
          @click="logout"
        >
          <v-icon size="25px"> mdi-logout </v-icon>
          Logout
        </v-btn>
      </div>
    </nav>

    <notifications position="center" />
    <keep-alive :exclude="'ShoppingCart'">
      <component
        v-on:updateParent="setTab"
        v-on:addTokenToApiClient="addTokenToApiClient"
        :is="currentTab"
      ></component>
    </keep-alive>

    <div class="container-xxl bg-white p-0">
      <div class="container text-center modal-body">
        <h4>Contact us:</h4>
        <p>Team D</p>
        <p>Hochschulstraße 1</p>
        <p>6850 Dornbirn</p>
        <p>E-Mail: teamD@fhv.at</p>
        <p>Tel: +43 (0) 5572 / 623541</p>
      </div>
    </div>
  </div>
</template>

<style>
@import "./assets/css/bootstrap.min.css";
@import "./assets/css/style.css";

#home {
  position: sticky;
  background-color: #181818;
  display: flex;
  justify-content: space-between;
}
</style>
