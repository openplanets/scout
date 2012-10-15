package eu.scape_project.watch.rest.resource;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sun.jersey.spi.resource.Singleton;
import com.wordnik.swagger.core.Api;

/**
 * {@link SourceAdaptorEventResource} with JSON output.
 * 
 * @author Luis Faria <lfaria@keep.pt>
 * 
 */
@Path("/sourceadaptorevent.json")
@Api(value = "/sourceadaptorevent", description = "Operations about Source Adaptor Events")
@Singleton
@Produces({"application/json"})
public class SourceAdaptorEventResourceXML extends SourceAdaptorEventResource {

}
