package at.fhv.teamd.musicshop.backend.application;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "Musicshop API",
                version = "3.0.1",
                contact = @Contact(
                        name = "Musicshop API Support",
                        url = "http://musicshop.com/contact",
                        email = "techsupport@musicshop.com"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html")),
        servers = {
                @Server(url = "http://localhost:8080/backend-1.0-SNAPSHOT"),
                @Server(url = "http://10.0.40.166:8080/")
        }
)

// TODO: switch servers before push to master!

@ApplicationPath("/rest")
public class RestWebservice extends Application {
}