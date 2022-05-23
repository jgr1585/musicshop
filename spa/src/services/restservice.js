// import { DefaultApi } from "../rest/api/DefaultApi";

// let api = new DefaultApi();
// let token = null;

// export default {
//   login(username, password, callback) {
//     const body = {
//       body: {
//         username: username,
//         password: password
//       }
//     };
//     let callback1 = (error, data, response) => {
//       token = response.text;
//       callback(error, data, response);
//     };
//     api.authenticateUser(body, callback1);
//   },
//   getShoppingCart() {
//     return api.getShoppingCart();
//   },
//   searchArticlesByAttributes(title, artist, callback) {
//     const config = {
//       title: title,
//       artist: artist
//     };
//     console.log(api);
//     api.searchArticlesByAttributes(config, callback);
//   }
// };
