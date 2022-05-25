<script setup>
import LineItem from "./LineItem.vue";
import axios from "axios";
</script>

<script>
export default {
  data() {
    return {
      invoiceID: "",
      loading: false,
      errored: false,
      lineItems: []
    };
  },
  methods: {
    search() {
      if (localStorage.getItem("token") == null) {
        alert("You are not logged in!");
        this.errored = true;
        return;
      }

      const config = {
        headers: {Authorization: `Bearer ${localStorage.getItem("token")}`}
      };

      const body = {
        mediumId: id
      };

      axios
          .post("http://localhost:8080/backend-1.0-SNAPSHOT/rest/shoppingcart/remove", body, config)
          .then((response) => {
            console.log(response);
            alert("Successfully removed from cart");
          })
          .catch((error) => {
            alert(error);
          });
    }
  }
};
</script>

<template>
  <div class="container-xxl container hero-header header">
    <div class="container">
      <div class="row g-5 align-items-center pa-5">
        <h1 class="text-white mb-4 animated slideInDown">Playlist</h1>
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
          <v-row v-for="lineItem in lineItems">
            <LineItem :lineItem="lineItem"/>
            <v-col cols="2">
              <v-chip
                  class="ma-2"
                  color="#ffd700"
                  text-color="white"
                  id="button"
                  @click="addToCart"
              >
                <v-avatar left>
                  <v-icon color="#ffd700"> mdi-basket</v-icon>
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
