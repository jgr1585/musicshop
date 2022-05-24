<script setup>
import Search from "./components/Search.vue";
import ShoppingCart from "./components/ShoppingCart.vue";
import Login from "./components/Login.vue";
</script>

<script>
export default {
  components: {
    Search,
    ShoppingCart,
    Login
  },
  data() {
    return {
      loggedIn: localStorage.getItem("token") != null,
      currentTab: "Search"
    };
  },
  methods: {
    setTab(tab) {
      this.currentTab = tab;
      localStorage.getItem("token") == null ? (this.loggedIn = false) : (this.loggedIn = true);
      this.$forceUpdate();
    },
    logout() {
      localStorage.removeItem("token");
      location.reload();
    }
  },
  created() {
    console.log(localStorage.getItem("token"));
  }
};
</script>

<template>
  <div class="container-xxl bg-white p-0">
    <nav class="navbar navbar-expand navbar-light px-4 px-lg-5 py-3 py-lg-0" id="home">
      <a href="index.html">
        <img alt="logo" src="./assets/logo.png" width="250" height="180"/>
      </a>
      <div>
        <button
            class="btn btn-primary rounded-pill"
            id="button"
            @click="setTab('Search')"
        >
          Search
        </button>
        <button
            class="btn btn-primary rounded-pill"
            id="button"
            @click="setTab('ShoppingCart')"
        >
          Shopping Cart
        </button>
        <button
            v-if="!loggedIn"
            class="btn btn-primary rounded-pill"
            id="button"
            @click="setTab('Login')"
        >
          Login
        </button>
        <button
            v-else
            class="btn btn-primary rounded-pill default-button"
            id="button"
            @click="logout"
        >
          Logout
        </button>
      </div>
    </nav>

    <component :is="currentTab"></component>

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
