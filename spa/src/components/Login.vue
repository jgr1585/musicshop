<script setup></script>

<script>
export default {
  data() {
    return {
      username: "",
      password: "",
      loading: false,
      errored: false
    };
  },
  methods: {
    login() {
      if (this.$options.filters.validater(this.title, this.artist)) {
        this.loading = true;
        axios
          .get("http://localhost:8080/backend-1.0-SNAPSHOT/")
          .then((response) => {
            console.log(response);
            this.articles = response.data;
          })
          .catch((error) => {
            alert(error);
            this.errored = true;
          })
          .finally((this.loading = false));
      } else {
        alert("Please fill in all the fields");
      }
    }
  }
};
</script>

<template>
  <div id="header">
    <h1 class="text-white mb-4 animated slideInDown">Login</h1>
  </div>
  <div class="position-relative w-100" id="search">
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
    <button class="btn btn-primary rounded-pill" id="button" @click="login">Search</button>
  </div>
</template>
