<script setup>
import Search from "./components/Search.vue";
import ShoppingCart from "./components/ShoppingCart.vue";
import Login from "./components/Login.vue";
</script>

<script>
export default {
  data() {
    return {
      currentTab: Search,
      tabs: [Search, ShoppingCart, Login],
    };
  },
  methods: {
    extractName(tab) {
      return tab.__file.split("/").pop().split(".")[0];
    },
  }
};
</script>

<template>
  <header>
    <img alt="Musicshop24 logo" src="./assets/logo.png" width="320" height="230" />

    <div class="demo">
      <button
        v-for="tab in tabs"
        :key="tab"
        :class="['tab-button', { active: currentTab === tab }]"
        @click="currentTab = tab"
      >
        {{ extractName(tab) }}
      </button>
      <component :is="currentTab"></component>
    </div>
  </header>

  <main></main>
</template>

<style>
.demo {
  font-family: sans-serif;
  border: 1px solid #eee;
  border-radius: 2px;
  padding: 20px 30px;
  margin-top: 1em;
  margin-bottom: 40px;
  user-select: none;
  overflow-x: auto;
}

.tab-button {
  padding: 6px 10px;
  border-top-left-radius: 3px;
  border-top-right-radius: 3px;
  border: 1px solid #ccc;
  cursor: pointer;
  background: #f0f0f0;
  margin-bottom: -1px;
  margin-right: -1px;
}
.tab-button:hover {
  background: #e0e0e0;
}
.tab-button.active {
  background: #e0e0e0;
}
.tab {
  border: 1px solid #ccc;
  padding: 10px;
}
</style>
