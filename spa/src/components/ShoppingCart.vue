<script setup>
import LineItem from "./LineItem.vue";
import axios from "axios";
import { DefaultApi } from "../rest/index.js";
</script>

<script>
export default {
  data() {
    return {
      loading: false,
      errored: false,
      shoppingcart: null
    };
  },
  methods: {
    getShoppingCart() {
      if (localStorage.getItem("token") == null) {
        alert("You are not logged in!");
        return;
      }

      const config = {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
      };

      axios
        .post("http://localhost:8080/backend-1.0-SNAPSHOT/rest/shoppingcart/get", {}, config)
        .then((response) => {
          console.log(response.data);

          // TODO: fix shoppingcart reference error; only a proxy is assigned to this.shoppingcart

          this.shoppingcart = response.data;
          console.log(this.shoppingcart);
        })
        .catch((error) => {
          alert(error);
        });

      // new DefaultApi().getShoppingCart((error, data, response) => {
      //   if (error) {
      //     alert(error);
      //   } else {
      //     console.log(response);
      //     console.log("API called successfully. Returned data: " + data);
      //     console.log("API called successfully. Returned response: " + response);
      //   }
      // });
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
      <div v-if="errored">
        <p>
          We're sorry, we're not able to retrieve this information at the moment, please try back
          later
        </p>
      </div>

      <!-- TODO: uncomment after fix of shoppingcart assignment error -->

      <!-- <div v-else>
        <div v-if="loading">Loading...</div>
        <v-container v-else>
          <v-row v-for="lineItem in this.shoppingcart.lineItems">
            <v-col>
              <LineItem :lineItem="lineItem" />
            </v-col>
            <div class="col-md-2">
              <button
                class="btn btn-primary rounded-pill vertical-center"
                id="button"
                @click="removeFromCart"
              >
                Remove
              </button>
            </div>
          </v-row>
        </v-container>
      </div> -->
    </div>
  </div>
</template>

<style>
#header {
  background-color: #181818 !important;
  padding-bottom: 250px;
}
</style>
