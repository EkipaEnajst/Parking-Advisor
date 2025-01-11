package api.v1;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.ekipaenajst.beans.SortLogic;
import org.ekipaenajst.entitete.Parkirisce;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;

@ApplicationPath("/advisor")
public class parkingadvisorApplication extends Application {

    private SortLogic sortlogic;

    private String frontendURL;

    @PostConstruct
    public void init() {

        Map<String,String> env = System.getenv();

        frontendURL = env.get("FRONTEND_URL");
    }

    @GET
    @Path("{params}")
    // npr. blabla/advisor?params=oddaljenost,cena_default,cena_short
    public Response vrniSortedParkirišča(@PathParam("params") List<String> sortparams){

        Parkirisce[] parkirisca = sortlogic.getSortedParkirisca(sortparams.toArray(new String[0]));

        return Response.status(Response.Status.OK)
                .header("Access-Control-Allow-Origin", frontendURL)
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .entity(parkirisca).build();
    }

}
