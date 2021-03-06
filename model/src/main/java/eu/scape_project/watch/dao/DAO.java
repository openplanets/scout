package eu.scape_project.watch.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thewebsemantic.binding.RdfBean;
import eu.scape_project.watch.domain.AsyncRequest;
import eu.scape_project.watch.domain.Entity;
import eu.scape_project.watch.domain.EntityType;
import eu.scape_project.watch.domain.Measurement;
import eu.scape_project.watch.domain.Property;
import eu.scape_project.watch.domain.PropertyValue;
import eu.scape_project.watch.domain.QuestionTemplate;
import eu.scape_project.watch.domain.QuestionTemplateParameter;
import eu.scape_project.watch.domain.Source;
import eu.scape_project.watch.domain.SourceAdaptor;
import eu.scape_project.watch.domain.SourceAdaptorEvent;

/**
 * Data Access Object to access all resources.
 * 
 * @author Luis Faria <lfaria@keep.pt>
 * 
 */
public final class DAO {

  /**
   * No instances can exist.
   */
  private DAO() {

  }

  /**
   * {@link AsyncRequest} Data Access Object.
   */
  public static final EntityTypeDAO ENTITY_TYPE = new EntityTypeDAO();

  /**
   * {@link Property} Data Access Object.
   */
  public static final PropertyDAO PROPERTY = new PropertyDAO();

  /**
   * {@link Entity} Data Access Object.
   */
  public static final EntityDAO ENTITY = new EntityDAO();

  /**
   * {@link PropertyValue} Data Access Object.
   */
  public static final PropertyValueDAO PROPERTY_VALUE = new PropertyValueDAO();

  /**
   * {@link Measurement} Data Access Object.
   */
  public static final MeasurementDAO MEASUREMENT = new MeasurementDAO();

  /**
   * {@link Source} Data Access Object.
   */
  public static final SourceDAO SOURCE = new SourceDAO();

  /**
   * {@link SourceAdaptor} Data Access Object.
   */
  public static final SourceAdaptorDAO SOURCE_ADAPTOR = new SourceAdaptorDAO();

  /**
   * {@link SourceAdaptorEvents} Data Access Object.
   */
  public static final SourceAdaptorEventDAO SOURCE_ADAPTOR_EVENT = new SourceAdaptorEventDAO();

  /**
   * {@link AsyncRequest} Data Access Object.
   */
  public static final AsyncRequestDAO ASYNC_REQUEST = new AsyncRequestDAO();

  /**
   * Sync request fake DAO.
   */
  public static final RequestDAO REQUEST = new RequestDAO();

  /**
   * {@link QuestionTemplate} Data Access Object.
   */
  public static final QuestionTemplateDAO QUESTION_TEMPLATE = new QuestionTemplateDAO();
  
  /**
   * {@link QuestionTemplateParameter} Data Access Object.
   */
  public static final QuestionTemplateParameterDAO QUESTION_TEMPLATE_PARAMETER = new QuestionTemplateParameterDAO();

  /**
   * Map of listeners for each class.
   */
  private static final Map<Class<? extends RdfBean<?>>, List<DOListener<? extends RdfBean<?>>>> LISTENERS = new HashMap<Class<? extends RdfBean<?>>, List<DOListener<? extends RdfBean<?>>>>();

  public static void clearDOListeners() {
    LISTENERS.clear();
  }

  public static void clearDOListeners(final Class<? extends RdfBean<?>> classToListen) {
    List<DOListener<? extends RdfBean<?>>> listeners = LISTENERS.get(classToListen);

    if (listeners != null) {
      for (DOListener<?> listener : listeners) {
        LISTENERS.remove(listener);
      }
    }
  }

  /**
   * Add a listeners to data object events.
   * 
   * 
   * @param classTolisten
   *          The class for which the listener will be registered to.
   * 
   * @param listener
   *          The listener handler.
   */
  public static void addDOListener(final Class<? extends RdfBean<?>> classTolisten,
    final DOListener<? extends RdfBean<?>> listener) {
    List<DOListener<? extends RdfBean<?>>> classListeners = LISTENERS.get(classTolisten);
    if (classListeners != null) {
      classListeners.add(listener);
    } else {
      classListeners = new ArrayList<DOListener<? extends RdfBean<?>>>();
      classListeners.add(listener);
      LISTENERS.put(classTolisten, classListeners);
    }
  }

  /**
   * Remove existing listener of data object events.
   * 
   * 
   * @param classTolisten
   *          The class for which the listener will be registered to.
   * 
   * @param listener
   *          The listener to remove.
   */
  public static void removeDOListener(final Class<? extends RdfBean<?>> classTolisten,
    final DOListener<? extends RdfBean<?>> listener) {
    final List<DOListener<? extends RdfBean<?>>> classListeners = LISTENERS.get(classTolisten);

    if (classListeners != null) {
      classListeners.remove(listener);

      if (classListeners.isEmpty()) {
        LISTENERS.remove(classTolisten);
      }
    }
  }

  /**
   * Fire an on create or update event.
   * 
   * @param <T>
   *          The generic type of the resource class.
   * @param object
   *          The created or updated resource.
   */
  public static <T extends RdfBean<T>> void fireOnUpdated(final T object) {
    final List<DOListener<? extends RdfBean<?>>> classListeners = LISTENERS.get(object.getClass());

    if (classListeners != null) {
      for (DOListener<? extends RdfBean<?>> listener : classListeners) {
        @SuppressWarnings("unchecked")
        final DOListener<T> tListener = (DOListener<T>) listener;
        tListener.onUpdated(object);
      }
    }
  }

  /**
   * Fire an on remove event.
   * 
   * @param <T>
   *          The generic type of the resource class.
   * @param object
   *          The removed object.
   */
  public static <T extends RdfBean<T>> void fireOnRemoved(final T object) {
    final List<DOListener<? extends RdfBean<?>>> classListeners = LISTENERS.get(object.getClass());

    if (classListeners != null) {
      for (DOListener<? extends RdfBean<?>> listener : classListeners) {
        @SuppressWarnings("unchecked")
        final DOListener<T> tListener = (DOListener<T>) listener;
        tListener.onRemoved(object);
      }
    }
  }

  /**
   * Delegate object saving to the correct DAO.
   * 
   * @param <T>
   *          The type of the generic class.
   * 
   * @param object
   *          The object to create or update.
   */
  public static <T extends RdfBean<T>> void save(final T object) {

    if (EntityType.class.isInstance(object)) {
      ENTITY_TYPE.save(EntityType.class.cast(object));
    } else if (Property.class.isInstance(object)) {
      PROPERTY.save(Property.class.cast(object));
    } else if (Entity.class.isInstance(object)) {
      ENTITY.save(Entity.class.cast(object));
    } else if (PropertyValue.class.isInstance(object)) {
      throw new IllegalArgumentException("To save PropertyValue, "
        + "specialized PropertyValueDAO.save(PropertyValue,SourceAdaptor) must be used.");
    } else if (AsyncRequest.class.isInstance(object)) {
      ASYNC_REQUEST.save(AsyncRequest.class.cast(object));
    } else if (Source.class.isInstance(object)) {
      SOURCE.save(Source.class.cast(object));
    } else if (SourceAdaptor.class.isInstance(object)) {
      SOURCE_ADAPTOR.save(SourceAdaptor.class.cast(object));
    } else if (SourceAdaptorEvent.class.isInstance(object)) {
      SOURCE_ADAPTOR_EVENT.save(SourceAdaptorEvent.class.cast(object));
    } else if (QuestionTemplate.class.isInstance(object)) {
      QUESTION_TEMPLATE.save(QuestionTemplate.class.cast(object));
    } else {
      throw new IllegalArgumentException(object.getClass().getSimpleName());
    }

  }

  /**
   * Save several objects at the same time, calling {@link #save(RdfBean)} on
   * each.
   * 
   * @param <T>
   *          The type of the generic class.
   * @param objects
   *          The objects to save
   */
  public static <T extends RdfBean<T>> void save(final T... objects) {
    for (T object : objects) {
      save(object);
    }
  }

  /**
   * Delegate object saving to the correct DAO.
   * 
   * @param <T>
   *          The type of the generic class.
   * @param object
   *          The object to delete.
   */
  public static <T extends RdfBean<T>> void delete(final T object) {

    if (EntityType.class.isInstance(object)) {
      ENTITY_TYPE.delete(EntityType.class.cast(object));
    } else if (Property.class.isInstance(object)) {
      PROPERTY.delete(Property.class.cast(object));
    } else if (Entity.class.isInstance(object)) {
      ENTITY.delete(Entity.class.cast(object));
    } else if (PropertyValue.class.isInstance(object)) {
      PROPERTY_VALUE.delete(PropertyValue.class.cast(object));
    } else if (AsyncRequest.class.isInstance(object)) {
      ASYNC_REQUEST.delete(AsyncRequest.class.cast(object));
    } else if (Source.class.isInstance(object)) {
      SOURCE.delete(Source.class.cast(object));
    } else if (SourceAdaptor.class.isInstance(object)) {
      SOURCE_ADAPTOR.delete(SourceAdaptor.class.cast(object));
    } else if (QuestionTemplate.class.isInstance(object)) {
      QUESTION_TEMPLATE.delete(QuestionTemplate.class.cast(object));
    } else {
      throw new IllegalArgumentException(object.getClass().getSimpleName());
    }
  }

  /**
   * Delete several objects at the same time, calling {@link #delete(RdfBean)}
   * on each.
   * 
   * @param <T>
   *          The type of the generic class.
   * @param objects
   *          The objects to delete
   */
  public static <T extends RdfBean<T>> void delete(final T... objects) {
    for (T object : objects) {
      delete(object);
    }
  }
}
