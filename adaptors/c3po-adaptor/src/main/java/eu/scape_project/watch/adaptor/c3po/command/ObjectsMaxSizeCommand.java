package eu.scape_project.watch.adaptor.c3po.command;

import eu.scape_project.watch.domain.DataType;
import eu.scape_project.watch.domain.Property;
import eu.scape_project.watch.domain.PropertyValue;
import eu.scape_project.watch.domain.RenderingHint;
import eu.scape_project.watch.utils.exceptions.InvalidJavaClassForDataTypeException;
import eu.scape_project.watch.utils.exceptions.UnsupportedDataTypeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static eu.scape_project.watch.adaptor.c3po.common.C3POConstants.CP_OBJECTS_MAX_SIZE;

/**
 * Fetches the size of the largest object within the collection.
 * 
 * @author Petar Petrov <me@petarpetrov.org>
 * 
 * @author lfaria@keep.pt
 * 
 *         Changed at 2012-04-18: Catching new
 *         {@link PropertyValue#setValue(Object)} exceptions.
 * 
 */
public class ObjectsMaxSizeCommand extends Command {

  /**
   * Default Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(ObjectsMaxSizeCommand.class);

  /**
   * Retrieves the size (in bytes) of the largest object in the collection.
   * 
   * @return the value of the size.
   */
  @Override
  public PropertyValue execute() {
    PropertyValue pv = null;
    final String objectsMaxSize = this.getReader().getObjectsMaxSize();
    if (objectsMaxSize != null && !"cp_missing_value".equals(objectsMaxSize)) {
      try {
        final double dSize = Double.parseDouble(objectsMaxSize);
        final long size = Math.round(dSize);

        final Property property = this.getProperty(CP_OBJECTS_MAX_SIZE, "The size of the largest object");
        property.setRenderingHint(RenderingHint.STORAGE_VOLUME);
        property.setDatatype(DataType.LONG);

        pv = new PropertyValue();
        pv.setProperty(property);
        pv.setValue(size, Long.class);

      } catch (final NumberFormatException e) {
        LOG.error("Could not parse max size from profile", e);
      } catch (final UnsupportedDataTypeException e) {
        LOG.error("Data type is not supported. Could not set property value", e);
      } catch (final InvalidJavaClassForDataTypeException e) {
        LOG.error("Invalid Java Class. Could not set property value", e);
      }
    }

    return pv;
  }

}
