package eu.scape_project.watch.dao;

import java.util.Collection;
import java.util.List;

import eu.scape_project.watch.domain.EntityType;
import eu.scape_project.watch.domain.Property;
import eu.scape_project.watch.utils.KBUtils;

/**
 * 
 * @author Luis Faria <lfaria@keep.pt>
 * 
 */
public final class PropertyDAO extends AbstractDO<Property> {

  /**
   * The name of the relationship to {@link EntityType} in {@link Property}.
   */
  private static final String ENTITY_TYPE_REL = KBUtils.WATCH_PREFIX + "type";

  /**
   * Logger.
   */
  // private static final Logger LOG =
  // LoggerFactory.getLogger(PropertyDAO.class);

  /**
   * Get the complete Property RDF Id to use in SPARQL.
   * 
   * @param entityType
   *          The name of the entity type that contains this property.
   * @param propertyName
   *          The name of the property.
   * @return The complete Property RDF Id using namescape prefix
   */
  public static String getPropertyRDFId(final String entityType, final String propertyName) {
    return "<" + KBUtils.WATCH_NS + Property.class.getSimpleName() + "/" + Property.createId(entityType, propertyName)
      + ">";
  }

  /**
   * Singleton instance.
   */
  private static PropertyDAO instance = null;

  /**
   * Get singleton instance.
   * 
   * @return The singleton instance
   */
  public static PropertyDAO getInstance() {
    if (instance == null) {
      instance = new PropertyDAO();
    }
    return instance;
  }

  /**
   * No other instances can exist as this is a singleton.
   */
  private PropertyDAO() {

  }

  /**
   * Find a {@link Property} by the related {@link EntityType} and name.
   * 
   * @param entityTypeName
   *          The name of the related {@link EntityType}
   * @param name
   *          the name of the {@link Property}
   * @return the {@link Property} or <code>null</code> if not found
   */
  public Property findByEntityTypeAndName(final String entityTypeName, final String name) {
    final String id = Property.createId(entityTypeName, name);
    return super.findById(id, Property.class);
  }

  /**
   * Query for {@link Property}.
   * 
   * @see #query(Class, String, int, int)
   * 
   * @param bindings
   *          The query bindings, see
   *          {@link AbstractDO#query(Class, String, int, int)}
   * @param start
   *          The index of the first item to retrieve
   * @param max
   *          The maximum number of items to retrieve
   * @return A list of {@link Property} filtered by the above constraints
   */
  public List<Property> query(final String bindings, final int start, final int max) {
    return super.query(Property.class, bindings, start, max);
  }

  /**
   * Count the results of a query for {@link Property}.
   * 
   * @param bindings
   *          The query bindings, see {@link AbstractDO#count(Class, String)}
   * @return The number of results expected for the query
   */
  public int count(final String bindings) {
    return super.count(Property.class, bindings);
  }

  /**
   * List properties of a type.
   * 
   * @param type
   *          The related {@link EntityType}
   * @param start
   *          the index of the first item to retrieve
   * @param max
   *          the maximum number of items to retrieve
   * @return The list of properties filtered by the above constraints
   */
  public Collection<Property> listWithType(final String type, final int start, final int max) {
    final String bindings = String.format("?s %1$s %2$s", ENTITY_TYPE_REL, EntityTypeDAO.getEntityTypeRDFId(type));
    return this.query(bindings, start, max);
  }

}
