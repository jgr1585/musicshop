<script setup>
import LineItem from "./LineItem.vue";
import axios from "axios";
</script>

<script>
export default {
  data() {
    return {
      loading: false,
      errored: false,
      creditcardNo: "",
      totalAmount: 0,
      lineItems: []
    };
  },
  methods: {
    getShoppingCart() {
      if (localStorage.getItem("token") == null) {
        alert("You are not logged in!");
        this.errored = true;
        return;
      }

      const config = {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
      };

      axios
        .post("http://localhost:8080/backend-1.0-SNAPSHOT/rest/shoppingcart/get", {}, config)
        .then((response) => {
          console.log(response.data);

          this.lineItems = response.data.lineItems;
          this.totalAmount = response.data.totalAmount;
        })
        .catch((error) => {
          alert(error);
        });

      // new DefaultApi().getShoppingCart((error, data, response) => {
      //   if (error) {
      //     alert(error);
      //   } else {
      //     console.log(response);
      //     console.log("API called successfully. Returned data: " + data);
      //     console.log("API called successfully. Returned response: " + response);
      //   }
      // });
    },
    removeFromCart(id) {
      if (localStorage.getItem("token") == null) {
        alert("You are not logged in!");
        this.errored = true;
        return;
      }

      const config = {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
      };

      const body = {
        mediumId: id
      };

      axios
        .post("http://localhost:8080/backend-1.0-SNAPSHOT/rest/shoppingcart/remove", body, config)
        .then((response) => {
          console.log(response);
          alert("Successfully removed from cart");
          this.getShoppingCart();
        })
        .catch((error) => {
          alert(error);
        });
    },
    buyFromCart() {
      if (localStorage.getItem("token") == null) {
        alert("You are not logged in!");
        this.errored = true;
        return;
      }

      const config = {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
      };

      const body = {
        creditCardNo: this.creditcardNo
      };

      axios
        .post("http://localhost:8080/backend-1.0-SNAPSHOT/rest/shoppingcart/buy", body, config)
        .then((response) => {
          console.log(response);
          alert("Successfully purchased");
          this.getShoppingCart();
          this.creditcardNo = "";
        })
        .catch((error) => {
          alert(error);
        });
    },
    emptyCart() {
      if (localStorage.getItem("token") == null) {
        alert("You are not logged in!");
        this.errored = true;
        return;
      }

      const config = {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
      };

      axios
        .post("http://localhost:8080/backend-1.0-SNAPSHOT/rest/shoppingcart/empty", {}, config)
        .then((response) => {
          console.log(response);
          alert("Shopping Cart emptied");
          this.getShoppingCart();
          this.creditcardNo = "";
        })
        .catch((error) => {
          alert(error);
        });
    }
  },
  created() {
    this.getShoppingCart();
  }
};
</script>

<template>
  <div class="container-xxl container hero-header header">
    <div class="container">
      <div class="row align-items-center pa-5">
        <h1 class="text-white mb-4 animated slideInDown">Shopping Cart</h1>
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
            <LineItem :lineItem="lineItem" />
            <v-col class="col-1 row align-items-center">
              <v-btn
                class="ma-lg-10 bg-transparent rounded-pill pa-5"
                @click="removeFromCart(lineItem.medium.id)"
              >
                <v-icon color="#ffd700" size="40"> mdi-delete</v-icon>
              </v-btn>
            </v-col>
          </v-row>
        </v-container>
      </div>

      <v-row align="center" justify="center">
        <input
          class="v-col-lg-auto border-e rounded-pill w-33 input"
          type="text"
          :value="creditcardNo"
          @input="creditcardNo = $event.target.value"
          placeholder="Creditcard No"
        />
      </v-row>

      <v-col class="row justify-content-center mx-auto w-33">
        <v-btn
          class="btn-primary rounded-pill w-33"
          id="button"
          @click="buyFromCart"
          color="#FFD700"
        >
          <v-icon size="25px"> mdi-briefcase-check</v-icon>
        </v-btn>

        <v-btn class="btn-primary rounded-pill w-33" id="button" @click="emptyCart" color="#FFD700">
          <v-icon size="25px"> mdi-basket-unfill</v-icon>
        </v-btn>
      </v-col>
    </div>
  </div>
</template>

<style></style>
