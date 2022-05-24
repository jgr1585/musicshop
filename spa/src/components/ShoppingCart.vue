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
      totalAmount: 0,
      lineItems: []
    };
  },
  methods: {
    getShoppingCart() {
      if (localStorage.getItem("token") == null) {
        alert("You are not logged in!");
        this.errored = true;
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

          this.lineItems = response.data.lineItems;
          this.totalAmount = response.data.totalAmount;
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
  <div class="container-xxl hero-header header">
    <div class="container">
      <div class="row g-5 align-items-center">
        <h1 class="text-white mb-4 animated slideInDown">Shopping Cart</h1>
      </div>
      <div v-if="errored">
        <p class="text">
          We're sorry, we're not able to retrieve this information at the moment, please try back
          later
        </p>
      </div>

      <!-- TODO: uncomment after fix of shoppingcart assignment error -->

      <div v-else>
        <div v-if="loading">Loading...</div>
        <v-container v-else>
          <v-row v-for="lineItem in lineItems">
            <v-col>
              <LineItem :lineItem="lineItem" />
            </v-col>
            <v-col cols="2">
              <v-chip
                class="ma-2"
                color="#ffd700"
                text-color="white"
                id="button"
                @click="addToCart"
              >
                <v-avatar left>
                  <v-icon color="#ffd700"> mdi-basket </v-icon>
                </v-avatar>
                Remove
              </v-chip>
            </v-col>
          </v-row>
        </v-container>
      </div>
    </div>
  </div>
</template>

<style></style>
