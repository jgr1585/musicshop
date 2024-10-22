window.swaggerSpec={
  "openapi" : "3.0.1",
  "info" : {
    "title" : "Musicshop Streaming API",
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
    "url" : "http://localhost:8083/",
    "variables" : { }
  } ],
  "paths" : {
    "/rest/media/stream/song/{songId}" : {
      "get" : {
        "summary" : "Retrieve a song stream",
        "operationId" : "streamSong",
        "parameters" : [ {
          "name" : "songId",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "integer",
            "format" : "int32"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "Returns binary song stream",
            "content" : {
              "application/mp3" : {
                "schema" : {
                  "type" : "string",
                  "format" : "binary"
                }
              }
            }
          },
          "401" : {
            "description" : "Unauthorized"
          },
          "500" : {
            "description" : "Server Error"
          }
        },
        "security" : [ {
          "Authentication" : [ ]
        } ]
      }
    }
  },
  "components" : {
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