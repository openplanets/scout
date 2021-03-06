package eu.scape_project.watch.adaptor.c3po.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the profile parser.
 * 
 * @author Petar Petrov <me@petarpetrov.org>
 * 
 */
public class DummyReaderTest {

  /**
   * The object under test.
   */
  private DummyReader reader;

  @Before
  public void setup() {
    try {
      this.reader = new DummyReader();
      this.reader.setStream(new FileInputStream(new File("src/test/resources/profiles/dummy_profile.xml")));
    } catch (final FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @After
  public void tearDown() {
    this.reader = null;
  }

  @Test
  public void shouldObtainCollectionName() throws Exception {
    final String name = this.reader.getCollectionName();
    Assert.assertEquals("coll-0-test", name);
  }

  @Test
  public void shouldObtainObjectsCount() throws Exception {
    final String count = this.reader.getObjectsCount();
    Assert.assertEquals("502", count);
  }

  @Test
  public void shouldObtainCollectionSize() throws Exception {
    final String size = this.reader.getCollectionSize();
    Assert.assertEquals("42", size);
  }

  @Test
  public void shouldObtainObjectsMinSize() throws Exception {
    final String size = this.reader.getObjectsMinSize();
    Assert.assertEquals("42", size);
  }

  @Test
  public void shouldObtainObjectsMaxSize() throws Exception {
    final String size = this.reader.getObjectsMaxSize();
    Assert.assertEquals("10000000", size);
  }

  @Test
  public void shouldObtainObjectsAvgSize() throws Exception {
    final String size = this.reader.getObjectsAvgSize();
    Assert.assertEquals("35000.42", size);
  }
  
  @Test
  public void shouldObtainFormatDistribution() throws Exception {
    final Map<String, String> distribution = this.reader.getDistribution("format");
    Assert.assertNotNull(distribution);
    Assert.assertFalse(distribution.keySet().isEmpty());
    
  }
  
  @Test
  public void shouldObtainMissingDistribution() throws Exception {
    final Map<String, String> distribution = this.reader.getDistribution("blah");
    Assert.assertNull(distribution);
  }
  
  @Test(expected = FileNotFoundException.class)
  public void shouldTestMissingDocument() throws Exception {
    this.reader = new DummyReader();
    this.reader.setStream(new FileInputStream(new File("src/test/resources/missing.xml")));
    
    Assert.fail("This code should not have been reached");
  }
  
  @Test
  public void shouldTestFalseDocument() throws Exception {
    this.reader = new DummyReader();
    this.reader.setStream(new FileInputStream(new File("src/test/resources/profiles/wrong.xml")));
    
    final String name = this.reader.getCollectionName();
    Assert.assertEquals(DummyReader.MISSING_VALUE, name);
  }
}
