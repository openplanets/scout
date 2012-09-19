package eu.scape_project.watch.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

import eu.scape_project.watch.adaptor.AdaptorManager;
import eu.scape_project.watch.dao.DAO;
import eu.scape_project.watch.domain.Source;
import eu.scape_project.watch.domain.SourceAdaptor;
import eu.scape_project.watch.interfaces.PluginType;
import eu.scape_project.watch.listener.ContextUtil;
import eu.scape_project.watch.plugin.PluginInfo;
import eu.scape_project.watch.plugin.PluginManager;
import eu.scape_project.watch.web.annotations.Controller;
import eu.scape_project.watch.web.annotations.HttpMethod;
import eu.scape_project.watch.web.annotations.Path;
import eu.scape_project.watch.web.annotations.Template;

@Path("/adaptor/new")
@Template("createSourceAdaptor.html")
@HttpMethod({HttpMethod.Type.GET, HttpMethod.Type.POST})
public class CreateSourceAdaptor extends Mustachelet {

  boolean page_administration() {
    return true;
  }

  public List<PluginInfo> plugin() {
    return PluginManager.getDefaultPluginManager().getPluginInfo(PluginType.ADAPTOR);
  }

  public List<Source> source() {
    return DAO.SOURCE.query("", 0, PAGE_SIZE);
  }

  @Inject
  private HttpServletResponse response;

  @Inject
  private HttpServletRequest request;

  @Controller(HttpMethod.Type.POST)
  boolean redirectPostData() throws IOException {
    final String instance = request.getParameter("instance");
    final String plugin = request.getParameter("plugin");
    final String pluginName = plugin.substring(0, plugin.indexOf('|'));
    final String pluginVersion = plugin.substring(plugin.indexOf('|') + 1);
    final String sourceName = request.getParameter("source");

    final Source source = DAO.SOURCE.findById(sourceName);

    if (source != null) {
      final ServletContext context = ContextUtil.getServletContext(request);
      final AdaptorManager adaptorManager = ContextUtil.getAdaptorManager(context);
      final SourceAdaptor adaptor = adaptorManager.createAdaptor(pluginName, pluginVersion, instance, source);

      if (adaptor != null) {
        response.sendRedirect(basePath + "/administration.html");
      } else {
        // TODO send error of plugin does not exist.
      }

    } else {
      // TODO send error source does not exist.
    }

    return false;
  }

}
