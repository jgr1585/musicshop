window.swaggerSpec={
  "openapi" : "3.0.1",
  "info" : {
    "title" : "Musicshop API",
    "contact" : {
      "name" : "Musicshop API Support",
      "url" : "http://musicshop.com/contact",
      "email" : "techsupport@musicshop.com"
    },
    "license" : {
      "name" : "Apache 2.0",
      "url" : "https://www.apache.org/licenses/LICENSE-2.0.html"
    },
    "version" : "3.0.1"
  },
  "servers" : [ {
    "url" : "http://localhost:8080/",
    "variables" : { }
  } ],
  "paths" : {
    "/rest/article/search" : {
      "get" : {
        "summary" : "Search Articles",
        "description" : "Search Articles by Attributes (Title, Artist)",
        "operationId" : "searchArticlesByAttributes",
        "parameters" : [ {
          "name" : "title",
          "in" : "query",
          "schema" : {
            "type" : "string",
            "default" : ""
          }
        }, {
          "name" : "artist",
          "in" : "query",
          "schema" : {
            "type" : "string",
            "default" : ""
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "Articles found"
          },
          "204" : {
            "description" : "No Articles found"
          },
          "400" : {
            "description" : "No search params provided"
          }
        }
      }
    },
    "/rest/playlist" : {
      "get" : {
        "summary" : "Retrieve the user playlist which contains all bought mediums",
        "operationId" : "getUserPlaylist",
        "responses" : {
          "200" : {
            "description" : "Returns List<AlbumDTO>"
          },
          "401" : {
            "description" : "Unauthorized"
          }
        },
        "security" : [ {
          "Authentication" : [ ]
        } ]
      }
    },
    "/rest/shoppingcart/add" : {
      "post" : {
        "summary" : "Add an item to your ShoppingCart",
        "operationId" : "addToShoppingCart",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/AddToShoppingCartForm"
              }
            }
          }
        },
        "responses" : {
          "401" : {
            "description" : "Not Authorized"
          },
          "404" : {
            "description" : "Article not found"
          },
          "406" : {
            "description" : "Already in Cart"
          }
        },
        "security" : [ {
          "Authentication" : [ ]
        } ]
      }
    },
    "/rest/shoppingcart/buy" : {
      "post" : {
        "summary" : "Buy Items from ShoppingCart",
        "description" : "Returns the InvoiceNo. on success?",
        "operationId" : "buyFromShoppingCart",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/BuyFromShoppingCartForm"
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "Successfully bought Items"
          },
          "401" : {
            "description" : "Not Authorized"
          },
          "404" : {
            "description" : "Customer not found"
          },
          "406" : {
            "description" : "Only digital mediums allowed"
          }
        },
        "security" : [ {
          "Authentication" : [ ]
        } ]
      }
    },
    "/rest/shoppingcart/empty" : {
      "post" : {
        "summary" : "Empty your ShoppingCart",
        "operationId" : "emptyShoppingCart",
        "responses" : {
          "401" : {
            "description" : "Not Authorized"
          }
        },
        "security" : [ {
          "Authentication" : [ ]
        } ]
      }
    },
    "/rest/shoppingcart/get" : {
      "post" : {
        "summary" : "Query your ShoppingCart",
        "operationId" : "getShoppingCart",
        "responses" : {
          "200" : {
            "description" : "Returns ShoppingCartDTO"
          },
          "401" : {
            "description" : "Not Authorized"
          }
        },
        "security" : [ {
          "Authentication" : [ ]
        } ]
      }
    },
    "/rest/shoppingcart/remove" : {
      "post" : {
        "summary" : "Remove  an item to your ShoppingCart",
        "operationId" : "removeFromShoppingCart",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/RemoveFromShoppingCartForm"
              }
            }
          }
        },
        "responses" : {
          "400" : {
            "description" : "No params provided"
          },
          "401" : {
            "description" : "Not Authorized"
          }
        },
        "security" : [ {
          "Authentication" : [ ]
        } ]
      }
    },
    "/rest/authentication" : {
      "post" : {
        "operationId" : "authenticateUser",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/Credentials"
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "Authentication succeeded"
          },
          "403" : {
            "description" : "Wrong credentials"
          }
        }
      }
    }
  },
  "components" : {
    "schemas" : {
      "AddToShoppingCartForm" : {
        "type" : "object",
        "properties" : {
          "mediumId" : {
            "type" : "integer",
            "format" : "int64"
          }
        }
      },
      "BuyFromShoppingCartForm" : {
        "type" : "object",
        "properties" : {
          "creditCardNo" : {
            "type" : "string"
          }
        }
      },
      "RemoveFromShoppingCartForm" : {
        "type" : "object",
        "properties" : {
          "mediumId" : {
            "type" : "integer",
            "format" : "int64"
          }
        }
      },
      "Credentials" : {
        "type" : "object",
        "properties" : {
          "username" : {
            "type" : "string"
          },
          "password" : {
            "type" : "string"
          }
        }
      }
    },
    "securitySchemes" : {
      "Authentication" : {
        "type" : "http",
        "description" : "JWT token",
        "in" : "header",
        "scheme" : "bearer",
        "bearerFormat" : "JWT"
      }
    }
  }
}