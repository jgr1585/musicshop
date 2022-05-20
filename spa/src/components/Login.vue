<script setup>
import RestService from "../services/restservice.js";
</script>

<script>
export default {
  data() {
    return {
      username: "",
      password: "",
      loading: false,
      errored: false,
      requested: false
    };
  },
  filters: {
    validater(p1, p2) {
      return p1.length > 0 && p2.length > 0 ? true : false;
    }
  },
  methods: {
    login() {
      if (this.$options.filters.validater(this.username, this.password)) {
        this.requested = true;
        this.loading = true;

        RestService.login(this.username, this.password, (error, data, response) => {
          this.loading = false;
          if (error) {
            this.errored = true;
            alert(error);
          } else {
            console.log("Token: " + response.text);
          }
        });
      } else {
        alert("Please fill in all the fields");
      }
    },
    reset() {
      this.errored = false;
      this.requested = false;
    }
  }
};
</script>

<template>
  <div class="container-xxl bg-primary hero-header">
    <div class="container">
      <div class="row g-5 align-items-center">
        <div id="header">
          <h1 class="text-white mb-4 animated slideInDown">Login</h1>
        </div>
        <div v-if="!requested" class="position-relative w-100" id="search">
          <input
            class="col-form-label border-1 rounded-pill w-50 ps-4 pe-5"
            type="text"
            :value="username"
            @input="username = $event.target.value"
            placeholder="Username"
          />
          <input
            class="col-form-label border-1 rounded-pill w-50 ps-4 pe-5"
            type="password"
            :value="password"
            @input="password = $event.target.value"
            placeholder="Password"
          />
          <button class="btn btn-primary rounded-pill" id="button" @click="login">Login</button>
        </div>

        <div v-else>
          <section v-if="errored">
            <p>
              We're sorry, we're not able to retrieve this information at the moment, please try
              back later
            </p>
            <button class="btn btn-primary rounded-pill" id="button" @click="reset">Reset</button>
          </section>

          <section v-else>
            <div v-if="loading">Loading...</div>
            <h1 v-else>Welcome {{ username }}</h1>
          </section>
        </div>
      </div>
    </div>
  </div>
</template>
