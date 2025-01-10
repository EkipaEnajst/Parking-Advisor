package api.v1;


import org.ekipaenajst.beans.SortLogic;
import org.ekipaenajst.entitete.Parkirisce;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationPath("v1")
public class parkingadvisorApplication extends Application {

//    private SortLogic sortlogic;
//
//    @GET
//    @Path("{params}")
//    // npr. blabla/advisor?params=oddaljenost,cena_default,cena_short
//    public Response vrniSortedParkirisca(@PathParam("params") List<String> sortparams){
//
//        Parkirisce[] parkirisca = sortlogic.getSortedParkirisca(sortparams.toArray(new String[0]));
//
//        return Response.status(Response.Status.OK).entity(parkirisca).build();
//    }

}
