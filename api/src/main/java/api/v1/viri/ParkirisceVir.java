package api.v1.viri;

import org.ekipaenajst.beans.SortLogic;
import org.ekipaenajst.entitete.Parkirisce;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Path("parkirisca")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class ParkirisceVir {

    @Inject
    SortLogic sortLogic;


    private String frontendURL;

    @PostConstruct
    public void init() {

        Map<String,String> env = System.getenv();

        frontendURL = env.get("FRONTEND_URL");
    }

    @Context
    UriInfo uriInfo;

    @GET
    public Response getParkirisca(@QueryParam("params") List<String> params) {
        if (params != null && params.size() >= 2) {
            // Split the comma-separated string into a list
            List<String> paramList = Arrays.asList(params.get(0).split(","));


            Parkirisce[] parkirisca = sortLogic.getSortedParkirisca(paramList.toArray(new String[0]), params.get(1));


            return Response.status(Response.Status.OK)
                    .header("Access-Control-Allow-Origin", frontendURL)
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .entity(parkirisca).build();


        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("Access-Control-Allow-Origin", frontendURL)
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .entity("Query parameter 'params' is not filled out correctly (missing sort params of location)").build();
        }
    }
}
