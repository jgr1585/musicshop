<script setup>
import RestService from "../services/restservice.js";
</script>

<script>
export default {
  data() {
    return {};
  },
  methods: {
    getShoppingCart() {
      RestService.getShoppingCart((error, data, response) => {
        if (error) {
          alert(error);
        } else {
          console.log(response);
          console.log("API called successfully. Returned data: " + data);
          console.log("API called successfully. Returned response: " + response);
        }
      });
    }
  },
  created() {
    this.getShoppingCart();
  }
};
</script>

<template>
  <div class="container-xxl hero-header" id="header">
    <div class="container">
      <div class="row g-5 align-items-center">
        <h1 class="text-white mb-4 animated slideInDown">Shopping Cart</h1>
      </div>
    </div>
  </div>
</template>

<style>
#header {
  background-color: #181818 !important;
  padding-bottom: 250px;
}
</style>