<script setup>
import Login from "./components/Login.vue";
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
      currentTab: "Search",
      tabs: ["Search", "ShoppingCart", "Login"]
    };
  }
};

</script>

<template>
  <div class="container-xxl bg-white p-0">
    <nav class="navbar navbar-expand navbar-light px-4 px-lg-5 py-3 py-lg-0" id="home">
      <img alt="logo" src="./assets/logo.png" width="250" height="180" />
      <div>
        <button
          class="btn btn-primary rounded-pill"
          id="button"
          v-for="tab in tabs"
          :key="tab"
          :class="['tab-button', { active: currentTab === tab }]"
          @click="currentTab = tab"
        >
          {{ tab }}
        </button>
      </div>
    </nav>
    <div class="container-xxl bg-primary hero-header">
      <div class="container">
        <div class="row g-5 align-items-center">
          <component :is="currentTab"></component>
        </div>
      </div>
    </div>
    <div class="container-xxl bg-white p-0">
      <div class="container text-center">
        <h4 >Contact us:</h4>
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
@import "assets/css/bootstrap.min.css";
@import "assets/css/style.css";

#home {
  position: sticky;
  background-color: #181818;
  display: flex;
  justify-content: space-between;
}

#button {
  color: #000000;
  margin-right: 10px;
  padding-right: 30px;
  padding-left: 30px;
}
</style>
