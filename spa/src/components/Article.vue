<script>
export default {
  props: {
    article: Object
  }
  // created() {
  //   console.log(this.article);
  // }
};
</script>

<template>
  <v-card class="flex-card" height="100%">
    <v-row class="pa-2">
      <v-col class="col-md-2 ma-2">
        <img
          :src="'http://coverartarchive.org/release/' + article.musicbrainzId + '/front/'"
          alt="Album Cover"
          class="vertical-center"
          id="img"
        />
      </v-col>
      <v-col>
        <h4 class="text-amber-accent-3">{{ article.title }}</h4>
        <p>Genre: {{ article.genre }}</p>
        <p>Label: {{ article.label }}</p>
        <p>
          Artists: <i v-for="artist in article.artists">{{ artist.name }};</i>
        </p>
        <p v-if="article.type === 'album'">
          Price: €
          {{
            article.mediums
              .filter((medium) => {
                return medium.type === "DIGITAL";
              })
              .at(0).price
          }}
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

#img {
  height: 150px;
  width: 150px;
}

.vertical-center {
  margin: 0;
  position: absolute;
  top: 50%;
  -ms-transform: translateY(-50%);
  transform: translateY(-50%);
}
</style>
