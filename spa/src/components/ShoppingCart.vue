<script setup>
import LineItem from "./LineItem.vue";
import { DefaultApi as BackendApi } from "../rest/backend/index.js";
</script>

<script>
export default {
  name: "ShoppingCart",
  data() {
    return {
      loading: false,
      creditcardNo: "",
      totalAmount: 0,
      lineItems: []
    };
  },
  methods: {
    getShoppingCart() {
      if (localStorage.getItem("token") == null) {
        this.$notify({
          type: "success",
          title: "You are not logged in!"
        });
        localStorage.setItem("tab", "ShoppingCart");
        this.$emit("updateParent", "Login");
        return;
      }

      new BackendApi().getShoppingCart((error, data, response) => {
        if (error) {
          this.$notify({
            type: "error",
            title: error
          });
        } else {
          console.log(response.body);
          this.lineItems = response.body.lineItems;
          this.totalAmount = response.body.totalAmount;
        }
      });
    },
    removeFromCart(id) {
      if (localStorage.getItem("token") == null) {
        this.$notify({
          type: "success",
          tile: "You are not logged in!"
        });
        localStorage.setItem("tab", "ShoppingCart");
        this.$emit("updateParent", "Login");
        return;
      }

      const opts = {
        body: {
          mediumId: id
        }
      };

      new BackendApi().removeFromShoppingCart(opts, (error, data, response) => {
        if (error) {
          this.$notify({
            type: "error",
            title: error
          });
        } else {
          console.log(response);
          this.$notify({
            type: "success",
            title: "Successfully removed from cart"
          });
          this.getShoppingCart();
        }
      });
    },
    buyFromCart() {
      if (localStorage.getItem("token") == null) {
        this.$notify({
          type: "success",
          title: "You are not logged in!"
        });
        localStorage.setItem("tab", "ShoppingCart");
        this.$emit("updateParent", "Login");
        return;
      }

      const opts = {
        body: {
          creditCardNo: this.creditcardNo
        }
      };

      new BackendApi().buyFromShoppingCart(opts, (error, data, response) => {
        if (error) {
          this.$notify({
            type: "error",
            title: error
          });
        } else {
          console.log(response);
          this.creditcardNo = "";
          this.$notify({
            type: "success",
            title: "Successfully purchased",
            text: response.text
          });
          this.getShoppingCart();
        }
      });
    },
    emptyCart() {
      if (localStorage.getItem("token") == null) {
        this.$notify({
          type: "success",
          title: "You are not logged in!"
        });
        localStorage.setItem("tab", "ShoppingCart");
        this.$emit("updateParent", "Login");
        return;
      }

      new BackendApi().emptyShoppingCart((error, data, response) => {
        if (error) {
          this.$notify({
            type: "error",
            title: error
          });
        } else {
          console.log(response);
          this.$notify({
            type: "success",
            title: "Shopping Cart emptied"
          });
          this.getShoppingCart();
          this.creditcardNo = "";
        }
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
      <div>
        <div v-if="loading">Loading...</div>
        <v-container v-else>
          <v-row v-for="lineItem in lineItems">
            <LineItem :lineItem="lineItem" />
            <v-col class="col-1 row align-items-center">
              <v-btn
                class="ma-lg-10 bg-transparent rounded-pill pa-5"
                @click="removeFromCart(lineItem.medium.id)"
              >
                <v-icon color="#ffd700" size="40"> mdi-file-excel-box</v-icon>
              </v-btn>
            </v-col>
          </v-row>
        </v-container>
      </div>

      <v-row align="center" justify="center">
        <input
          class="v-col-lg-auto border-e rounded-pill w-66 input"
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
          <v-icon size="25px"> mdi-delete</v-icon>
        </v-btn>
      </v-col>
    </div>
  </div>
</template>

<style></style>
