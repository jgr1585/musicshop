<script setup>
import axios from "axios";
import {DefaultApi} from "../rest/index.js";
</script>

<script>
export default {
  props: {
    token: String
  },
  data() {
    return {
      username: "",
      password: "",
      loading: false,
      errored: false
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

        axios
            .post("http://localhost:8080/backend-1.0-SNAPSHOT/rest/authentication", {
              username: this.username,
              password: this.password
            })
            .then((response) => {
              this.loading = false;
              this.errored = false;
              localStorage.setItem("token", response.data);
              console.log(localStorage.getItem("token"));
              location.reload();
            })
            .catch((error) => {
              this.loading = false;
              this.errored = true;
              alert(error);
            });

        // TODO: solve problem about of setting JWT in header
        // const opts = {
        //   body: {
        //     username: this.username,
        //     password: this.password
        //   }
        // };
        // new DefaultApi().authenticateUser(opts, (error, data, response) => {
        //   this.loading = false;
        //   if (error) {
        //     this.errored = true;
        //     alert(error);
        //   } else {
        //     this.token = response.text;
        //     console.log("Token: " + response.text);
        //   }
        // });
      } else {
        alert("Please fill in all the fields");
      }
    },
    reset() {
      this.username = "";
      this.password = "";
      this.errored = false;
    },
    tokenIsNull() {
      return localStorage.getItem("token") == null;
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
          <v-btn
              class="btn-primary rounded-pill w-33"
              id="button"
              @click="login"
              color="#FFD700"
          >
            <v-icon
                size="25px"
            >
              mdi-login
            </v-icon>
          </v-btn>

          <v-btn
              class="btn-primary rounded-pill w-33"
              id="button"
              @click="reset"
              color="#FFD700"
          >
            <v-icon
                size="25px"
            >
              mdi-replay
            </v-icon>
          </v-btn>
        </div>
      </div>

      <div v-else>
        <div v-if="loading">Loading...</div>
        <h1 v-else>Welcome {{ username }}</h1>
      </div>

      <div v-if="errored">
        <p class="text">
          We're sorry, we're not able to retrieve this information at the moment, please try back
          later
        </p>
      </div>
    </div>
  </div>
</template>

<style>
#header {
  background-color: #181818 !important;
  padding-bottom: 250px;
}

.input {
  background-color: #ffffff;
}
</style>
