/**
 * 
 */
package eu.scape_project.watch.core.rest.resource;

import com.wordnik.swagger.core.ApiError;
import com.wordnik.swagger.core.ApiErrors;
import com.wordnik.swagger.core.ApiOperation;
import com.wordnik.swagger.core.ApiParam;
import com.wordnik.swagger.core.JavaHelp;
import eu.scape_project.watch.core.KB;
import eu.scape_project.watch.core.dao.EntityDAO;
import eu.scape_project.watch.core.dao.PropertyDAO;
import eu.scape_project.watch.core.dao.PropertyValueDAO;
import eu.scape_project.watch.core.model.Entity;
import eu.scape_project.watch.core.model.Property;
import eu.scape_project.watch.core.model.PropertyValue;
import eu.scape_project.watch.core.rest.exception.ApiException;
import eu.scape_project.watch.core.rest.exception.NotFoundException;

import java.util.Collection;

import javax.persistence.EntityExistsException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.xml.bind.PropertyException;

import org.omg.CosNaming.NamingContextPackage.NotFound;

/**
 * REST API for {@link PropertyValue} operations.
 * 
 * @author Luis Faria <lfaria@keep.pt>
 * 
 */
public class PropertyValueResource extends JavaHelp {

  /**
   * The logger.
   */
  // private static final Logger LOG =
  // LoggerFactory.getLogger(PropertyValueResource.class);

  /**
   * Get an existing {@link PropertyValue}.
   * 
   * @param entityName
   *          The name of the {@link Entity} this property value refers to.
   * @param propertyName
   *          The name of the {@link Property} this property value refers to.
   * @return The {@link PropertyValue} or throws {@link NotFoundException} if
   *         not found.
   */
  @GET
  @Path("/{entity}/{property}")
  @ApiOperation(value = "Find property value by entity and property", notes = "")
  @ApiErrors(value = {@ApiError(code = NotFoundException.CODE, reason = "Property value not found")})
  public Response getPropertyValueByName(
    @ApiParam(value = "Name of the entity type", required = true) @PathParam("entity") final String entityName,
    @ApiParam(value = "Name of the property", required = true) @PathParam("property") final String propertyName) {
    final PropertyValue propertyValue = PropertyValueDAO.findByEntityAndName(entityName, propertyName);

    if (propertyValue != null) {
      return Response.ok().entity(propertyValue).build();
    } else {
      throw new NotFoundException("Property value not found, entity=" + entityName + ", property=" + propertyName);
    }
  }

  /**
   * Ge a list with all existing {@link PropertyValue}, independently of the
   * {@link Entity} or {@link Property} they refer to.
   * 
   * @return The complete list of {@link PropertyValue} in the KB
   */
  @GET
  @Path("/list")
  @ApiOperation(value = "List all property values", notes = "")
  public Response listPropertyValue() {
    // TODO list property values of an entity
    // TODO list property values of an entity and property
    final Collection<PropertyValue> list = KB.getInstance().getReader().load(PropertyValue.class);
    return Response.ok().entity(new GenericEntity<Collection<PropertyValue>>(list) {
    }).build();
  }

  /**
   * Create a new property value.
   * 
   * @param entityName
   *          The name of the related {@link Entity}
   * @param propertyName
   *          The name of the related {@link Property}
   * @param value
   *          The value of the {@link Property} for the {@link Entity}
   * @return The newly created {@link PropertyValue}.
   */
  @POST
  @Path("/{entity}/{property}")
  @ApiOperation(value = "Create property value", notes = "This can only be done by a logged user (TODO)")
  @ApiErrors(value = {@ApiError(code = NotFoundException.CODE, reason = "Entity or property does not exist")})
  public Response createPropertyValue(
    @ApiParam(value = "Entity name (must exist)", required = true) @PathParam("entity") final String entityName,
    @ApiParam(value = "Property name (must exist)", required = true) @PathParam("property") final String propertyName,
    @ApiParam(value = "Property value", required = false) final String value) {

    final Entity entity = EntityDAO.findById(entityName);

    if (entity != null) {
      final String typeName = entity.getEntityType().getName();
      final Property property = PropertyDAO.findByEntityTypeAndName(typeName, propertyName);

      if (property != null) {
        final PropertyValue propertyValue = new PropertyValue(entity, property, value);
        propertyValue.save();
        return Response.ok().entity(propertyValue).build();
      } else {
        throw new NotFoundException("Property not found type=" + typeName + ", name=" + propertyName);
      }
    } else {
      throw new NotFoundException("Entity not found: " + entityName);
    }

  }

  /**
   * Update an existing {@link PropertyValue}.
   * 
   * @param entityName
   *          The name of the related {@link Entity}.
   * @param propertyName
   *          The name of the related {@link Property}.
   * @param value
   *          The new value of the {@link Property} for the {@link Entity}
   * @return The updated {@link PropertyValue}.
   */
  @PUT
  @Path("/{entity}/{property}")
  @ApiOperation(value = "Update property value", notes = "This can only be done by a logged user (TODO)")
  @ApiErrors(value = {@ApiError(code = NotFoundException.CODE, reason = "Entity or property not found")})
  public Response updatePropertyValue(
    @ApiParam(value = "Entity related to the property value", required = true) @PathParam("entity") final String entityName,
    @ApiParam(value = "Property related to the property value", required = true) @PathParam("property") final String propertyName,
    @ApiParam(value = "Updated value", required = true) final String value) {
    // TODO support several property values, by versioning
    final PropertyValue propertyValue = PropertyValueDAO.findByEntityAndName(entityName, propertyName);

    if (propertyValue != null) {
      propertyValue.setValue(value);
      propertyValue.save();
      return Response.ok().entity(propertyName).build();
    } else {
      throw new NotFoundException("Property value not found entity=" + entityName + ", property=" + propertyName);
    }
  }

  /**
   * Delete an existing {@link PropertyValue}.
   * 
   * @param entityName
   *          The name of the related {@link Entity}.
   * @param propertyName
   *          The name of the related {@link Property}.
   * @return The deleted {@link PropertyValue} or throws
   *         {@link NotFoundException} if not found.
   */
  @DELETE
  @Path("/{entity}/{property}")
  @ApiOperation(value = "Delete property value", notes = "This can only be done by an admin user (TODO)")
  @ApiErrors(value = {@ApiError(code = NotFoundException.CODE, reason = "Property value not found")})
  public Response deletePropertyValue(
    @ApiParam(value = "Entity related with the property value", required = true) @PathParam("entity") final String entityName,
    @ApiParam(value = "Property related with the property value", required = true) @PathParam("property") final String propertyName) {
    final PropertyValue propertyValue = PropertyValueDAO.findByEntityAndName(entityName, propertyName);

    if (propertyValue != null) {
      propertyValue.delete();
      return Response.ok().entity(propertyValue).build();
    } else {
      throw new NotFoundException("Property value not found entity=" + entityName + ", property=" + propertyName);
    }
  }
}