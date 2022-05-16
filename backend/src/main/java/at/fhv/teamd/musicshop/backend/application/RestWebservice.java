package at.fhv.teamd.musicshop.backend.application;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.Servers;

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
                @Server(url = "/backend-1.0-SNAPSHOT"),
                @Server(url = "/")
        }
)

@ApplicationPath("/rest")
public class RestWebservice extends Application {
}