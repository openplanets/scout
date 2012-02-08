package eu.scape_project.watch.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.tdb.TDBFactory;

public class KB {

  private static final Logger LOGGER = LoggerFactory.getLogger(KB.class);

  private static final String DATA_FOLDER = "./data/tdb";

  private static KB UNIQUE_INSTANCE;

  private static Dataset dataset;

  public static synchronized KB getInstance() {
    if (KB.UNIQUE_INSTANCE == null) {
      KB.UNIQUE_INSTANCE = new KB();

      dataset = TDBFactory.createDataset(DATA_FOLDER);

      Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
        @Override
        public void run() {
          dataset.close();
          KB.LOGGER.info("closing dataset");
        }
      }));
      
      KB.LOGGER.info("KB manager created");
    }

    return KB.UNIQUE_INSTANCE;
  }

  public Dataset getDataset() {
    return KB.dataset;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException("Singletons cannot be cloned");
  }

  private KB() {

  }
}
