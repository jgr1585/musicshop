<script setup>
import LineItem from "./LineItem.vue";
</script>

<script>
export default {
  data() {
    return {
      invoiceID: "",
      loading: false,
      errored: false,
      lineItems: [],
      title: ""
    };
  },
  methods: {
    play() {

    },
    download() {

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
      <div class="w-auto align-center" id="search">
        <input
            class="v-col-lg-auto border-e rounded-pill w-66 input"
            type="text"
            :value="title"
            @input="title = $event.target.value"
            placeholder="Title"
        />
        <div class="w-33">
          <v-btn class="btn-primary rounded-pill w-33" id="button" @click="search" color="#FFD700">
            <v-icon size="25px"> mdi-magnify</v-icon>
          </v-btn>

          <v-btn class="btn-primary rounded-pill w-33" id="button" @click="reset" color="#FFD700">
            <v-icon size="25px"> mdi-filter-remove</v-icon>
          </v-btn>
        </div>
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
            <v-col class="col-1 row align-items-center">
              <v-btn
                  class="ma-lg-10 bg-transparent rounded-pill pa-5"
                  @click="play"
              >
                <v-icon color="#ffd700" size="40"> mdi-play-circle-outline</v-icon>
              </v-btn>
              <v-btn
                  class="ma-lg-10 bg-transparent rounded-pill pa-5"
                  @click="download"
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
