<script>
export default {
  props: {
    article: Object
  }
};
</script>

<template>
  <v-card class="card">
    <v-row>
      <v-col class="col-md-2">
        <img
          :src="'http://coverartarchive.org/release/' + article.musicbrainzId + '/front/'"
          alt="Album Cover"
          class="vertical-center img"
        />
      </v-col>
      <v-col>
        <h3 class="text-black">Title: {{ article.title }}</h3>
        <p>Type: {{ article.type }}</p>
        <p>Label: {{ article.label }}</p>
        <p>
          Artists: <i v-for="artist in article.artists">{{ artist.name }};</i>
        </p>
      </v-col>
      </v-row>
  </v-card>
</template>

<style>
/* .card {
  margin-bottom: 20px 20px 20px 20px;
  background-color: blue;
  border-color: blue;
} */

h2 {
  color: gold;
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
