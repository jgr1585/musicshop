window.swaggerSpec={
  "openapi" : "3.0.1",
  "info" : {
    "title" : "Musicshop Download API",
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
    "url" : "http://20.39.221.88/",
    "variables" : { }
  } ],
  "paths" : {
    "/rest/media/download/album/{albumId}" : {
      "get" : {
        "summary" : "Retrieve the album download which comes as zipped archive",
        "operationId" : "downloadAlbum",
        "parameters" : [ {
          "name" : "albumId",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "integer",
            "format" : "int32"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "Returns binary zip of album",
            "content" : {
              "application/zip" : {
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
    },
    "/rest/media/download/song/{songId}" : {
      "get" : {
        "summary" : "Retrieve the song download which comes as audio filed",
        "operationId" : "downloadSong",
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
            "description" : "Returns binary song file",
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