<script setup>
import Article from "./Article.vue";
</script>

<script>
export default {
  props: {
    lineItem: Object
  }
};
</script>

<template>
  <v-col>
    <Article :article="lineItem.article" />
  </v-col>
  <v-col cols="2">
    <p>{{ lineItem.id }}</p>
    <p>{{ lineItem.price }}</p>
   </v-col>
</template>

<style>
.v-card {
  margin-bottom: 10px;
}

h2 {
  color: gold;
}

#button {
  color: #000000;
  margin-right: 10px;
  margin-left: 10px;
  padding-right: 30px;
  padding-left: 30px;
}

.img {
  height: 175px;
  width: 175px;
  margin: 10px 10px 10px 10px;
  padding: 10px 10px 10px 10px;
}

.vertical-center {
  margin: 0;
  position: absolute;
  top: 50%;
  -ms-transform: translateY(-50%);
  transform: translateY(-50%);
}
</style>
