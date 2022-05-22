<script setup>
import axios from "axios";
</script>

<script>
export default {
  data() {
    return {
      username: "",
      password: "",
      token: null,
      loading: false,
      errored: false,
      requested: false
    };
  },
  filters: {
    validater(p1, p2) {
      return p1.length > 0 || p2.length > 0 ? true : false;
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
              console.log(response);
              this.token = response.data;
            })
            .catch((error) => {
              alert(error);
              this.errored = true;
            })
            .finally((this.loading = false), (this.requestd = true));
      } else {
        alert("Please fill in all the fields");
      }
    }
  }
};
</script>

<template>
  <div class="container-xxl hero-header" id="header">
    <div class="container">
      <div class="row g-5 align-items-center">
        <h1 class="text-white mb-4 animated slideInDown">Login</h1>
      </div>
      <div class="position-relative w-auto" id="search">
        <input
            class="v-col-lg-auto border-e rounded-2 w-33" id="input"
            type="text"
            :value="username"
            @input="username = $event.target.value"
            placeholder="Username"
        />
        <input
            class="v-col-lg-auto border-e rounded-2 w-33" id="input"
            type="password"
            :value="password"
            @input="password = $event.target.value"
            placeholder="Password"
        />
        <button class="btn btn-primary rounded-pill" id="button" @click="login">Search</button>
      </div>

      <div v-if="requested">
        <section v-if="errored">
          <p>
            We're sorry, we're not able to retrieve this information at the moment, please try
            back later
          </p>
        </section>

        <section v-else>
          <div v-if="loading">Loading...</div>
          <h1 v-else>Welcome {{ username }}</h1>
        </section>
      </div>
    </div>
  </div>
</template>

<style>
#header {
  background-color: #181818 !important;
  padding-bottom: 250px;
}

#input {
  background-color: #FFFFFF;
}
</style>