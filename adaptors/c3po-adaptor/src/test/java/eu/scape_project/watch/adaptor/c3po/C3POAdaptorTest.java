package eu.scape_project.watch.adaptor.c3po;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.scape_project.watch.domain.Entity;
import eu.scape_project.watch.domain.Property;
import eu.scape_project.watch.interfaces.ResultInterface;
import eu.scape_project.watch.utils.ConfigParameter;
import eu.scape_project.watch.utils.exceptions.InvalidParameterException;
import eu.scape_project.watch.utils.exceptions.PluginException;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class C3POAdaptorTest {

  private C3POAdaptor adaptor;

  @Before
  public void setup() {
    this.adaptor = new C3POAdaptor();
    this.adaptor.init();
  }

  @Test
  public void onParameterValuesSetupSuccess() throws Exception {
    final List<ConfigParameter> parameters = this.adaptor.getParameters();
    Map<String, String> values = new HashMap<String, String>();

    for (final ConfigParameter cp : parameters) {
      values.put(cp.getKey(), cp.getValue());
    }

    this.adaptor.setParameterValues(values);

    values = this.adaptor.getParameterValues();
    Assert.assertEquals(parameters.size(), values.keySet().size());
    Assert.assertFalse(values.keySet().isEmpty());
  }

  @Test(expected = InvalidParameterException.class)
  public void onParameterValuesSetupFailure1() throws Exception {
    final Map<String, String> values = new HashMap<String, String>();
    this.adaptor.setParameterValues(values);

    Assert.fail("This code should not have been reached.");
  }
  
  @Test(expected = InvalidParameterException.class)
  public void onParameterValuesSetupFailure2() throws Exception {
    final List<ConfigParameter> parameters = this.adaptor.getParameters();
    final Map<String, String> values = new HashMap<String, String>();
    
    for (final ConfigParameter cp : parameters) {
      values.put(cp.getKey(), null);
    }
    
    this.adaptor.setParameterValues(values);

    Assert.fail("This code should not have been reached.");
  }
  
  @Test
  public void onExecute() throws Exception {
    final List<ConfigParameter> parameters = this.adaptor.getParameters();
    final Map<String, String> values = new HashMap<String, String>();

    for (final ConfigParameter cp : parameters) {
      values.put(cp.getKey(), cp.getValue());
    }

    this.adaptor.setParameterValues(values);
    final ResultInterface result = this.adaptor.execute();
    
    Assert.assertNotNull(result.getPropertyValues());
    Assert.assertFalse(result.getPropertyValues().isEmpty());
    
  }
  
  @Test(expected = PluginException.class)
  public void onExecuteWithContext() throws Exception {
    final List<ConfigParameter> parameters = this.adaptor.getParameters();
    final Map<String, String> values = new HashMap<String, String>();

    for (final ConfigParameter cp : parameters) {
      values.put(cp.getKey(), cp.getValue());
    }

    this.adaptor.setParameterValues(values);
    this.adaptor.execute(new HashMap<Entity, List<Property>>());
    
    Assert.fail("This code should not have been reached");
  }
  
  @Test
  public void onShutDown() throws Exception {
    this.adaptor.shutdown();
    
    final List<ConfigParameter> parameters = this.adaptor.getParameters();
    final Map<String, String> values = this.adaptor.getParameterValues();
    
    Assert.assertTrue(parameters.isEmpty());
    Assert.assertTrue(values.isEmpty());
  }
}
