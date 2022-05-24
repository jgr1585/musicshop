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
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
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
  <div class="container-xxl hero-header header">
    <div class="container">
      <div class="row g-5 align-items-center">
        <h1 class="text-white mb-4 animated slideInDown">Download</h1>
      </div>

      <div class="position-relative w-auto" id="search">
        <input
          class="v-col-lg-auto border-e rounded-pill w-33 input"
          type="text"
          :value="invoiceID"
          @input="invoiceID = $event.target.value"
          placeholder="Invoice Number"
        />
        <button class="btn btn-primary rounded-pill w-25" id="button" @click="search">
          Search
        </button>
        <button class="btn btn-primary rounded-pill w-25" id="button" @click="reset">Reset</button>
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
            <LineItem :lineItem="lineItem" />
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
