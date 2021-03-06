package eu.scape_project.watch.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.inject.Inject;

import eu.scape_project.watch.adaptor.AdaptorManager;
import eu.scape_project.watch.dao.DAO;
import eu.scape_project.watch.domain.Source;
import eu.scape_project.watch.domain.SourceAdaptor;
import eu.scape_project.watch.listener.ContextUtil;
import eu.scape_project.watch.plugin.PluginInfo;
import eu.scape_project.watch.plugin.PluginManager;
import eu.scape_project.watch.web.annotations.Controller;
import eu.scape_project.watch.web.annotations.HttpMethod;
import eu.scape_project.watch.web.annotations.Path;
import eu.scape_project.watch.web.annotations.TemplateSource;

@Path("/administration")
@TemplateSource("administration")
public class Administration extends TemplateContext {

  public List<SourceAdaptor> getSourceAdaptors() {
    return DAO.SOURCE_ADAPTOR.queryAll(0, getPageSize());
  }

  public List<PluginInfo> getPlugins() {
    return PluginManager.getDefaultPluginManager().getPluginInfo();
  }

  public String getPluginDirectory() {
    return PluginManager.getDefaultPluginManager().getPluginDirectory().getAbsolutePath();
  }

  @Inject
  private HttpServletResponse response;

  @Inject
  private HttpServletRequest request;

  @Controller
  boolean redirectPostData() throws IOException {
    boolean continueChain = true;
    final String operation = request.getParameter("operation");

    if (operation == null || operation.length() == 0) {
      continueChain = true;
    } else if (operation.equals("removeAdaptor")) {
      final String adaptorId = request.getParameter("adaptor");

      final SourceAdaptor adaptor = DAO.SOURCE_ADAPTOR.findById(adaptorId);

      final ServletContext context = ContextUtil.getServletContext(request);
      final AdaptorManager adaptorManager = ContextUtil.getAdaptorManager(context);

      if (adaptor != null) {
        DAO.delete(adaptor);
        // XXX reload and un-scheduling now done in ScoutManager (check before deleting next line)
        // adaptorManager.reloadKnownAdaptors();
        response.sendRedirect(getMustacheletPath() + "/administration");
      } else {
        response.sendError(404, "Source adaptor not found: " + StringEscapeUtils.escapeHtml(adaptorId));
      }
      continueChain = false;

    } else {
      response.sendError(400, "Unrecognized operation requested: " + StringEscapeUtils.escapeHtml(operation));
      continueChain = false;
    }

    return continueChain;
  }

}
