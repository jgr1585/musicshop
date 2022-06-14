<script setup>
import { DefaultApi as BackendApi } from "../rest/backend/index.js";
</script>

<script>
export default {
  data() {
    return {
      username: "",
      password: "",
      loading: false
    };
  },
  filters: {
    validater(p1, p2) {
      return p1.length > 0 && p2.length > 0;
    }
  },
  methods: {
    login() {
      if (this.$options.filters.validater(this.username, this.password)) {
        this.loading = true;

        const opts = {
          body: {
            username: this.username,
            password: this.password
          }
        };

        new BackendApi().authenticateUser(opts, (error, data, response) => {
          this.loading = false;
          if (error) {
            this.$notify({
              type: "error",
              title: error
            });
          } else {
            console.log(response);
            localStorage.setItem("token", response.text);
            console.log(localStorage.getItem("token"));
            this.$emit("addTokenToApiClient");
            this.$emit("updateParent", localStorage.getItem("tab"));
          }
        });
      } else {
        this.$notify({
          type: "warn",
          title: "Please fill in all the fields"
        });
      }
    },
    reset() {
      this.username = "";
      this.password = "";
    },
    tokenIsNull() {
      return localStorage.getItem("token") == null;
    }
  },
  created() {
    console.log("LOGIN: " + localStorage.getItem("token"));
    if (localStorage.getItem("token") != null) {
      this.$emit("addTokenToApiClient");
      this.$emit("updateParent", localStorage.getItem("tab"));
    }
  }
};
</script>

<template>
  <div class="container-xxl container hero-header header">
    <div class="container">
      <div class="row g-5 align-items-center pa-5">
        <h1 class="text-white mb-4 animated slideInDown">Login</h1>
      </div>
      <div v-if="tokenIsNull()" class="w-auto" id="search">
        <input
          class="v-col-lg-auto border-e rounded-pill w-33 input"
          type="text"
          :value="username"
          @input="username = $event.target.value"
          placeholder="Username"
        />
        <input
          class="v-col-lg-auto border-e rounded-pill w-33 input"
          type="password"
          :value="password"
          @input="password = $event.target.value"
          placeholder="Password"
        />
        <div class="w-33">
          <v-btn class="btn-primary rounded-pill w-33" id="button" @click="login" color="#FFD700">
            <v-icon size="25px"> mdi-login </v-icon>
          </v-btn>

          <v-btn class="btn-primary rounded-pill w-33" id="button" @click="reset" color="#FFD700">
            <v-icon size="25px"> mdi-filter-remove </v-icon>
          </v-btn>
        </div>
      </div>

      <div v-else>
        <div v-if="loading">Loading...</div>
        <h1 v-else>Welcome {{ username }}</h1>
      </div>
    </div>
  </div>
</template>

<style>
.input {
  background-color: #ffffff;
}
</style>
