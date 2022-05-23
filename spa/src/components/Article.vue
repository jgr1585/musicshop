<script>
export default {
  props: {
    article: Object
  }
};
</script>

<template>
  <v-card class="v-card">
    <div class="row">
      <div class="col-md-2">
        <img
          :src="'http://coverartarchive.org/release/' + article.musicbrainzId + '/front/'"
          class="vertical-center img"
        />
      </div>
      <div class="col">
        <h3 class="text-light">Title: {{ article.title }}</h3>
        <p>Type: {{ article.type }}</p>
        <p>Label: {{ article.label }}</p>
        <p>
          Artists: <i v-for="artist in article.artists">{{ artist.name }};</i>
        </p>
      </div>
    </div>
  </v-card>
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
