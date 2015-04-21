package com.pulsarang.infra.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		return list(model);
	}

	@RequestMapping("/list")
	public String list(ModelMap model) {
		List<ProjectEntity> projects = projectService.listAll();

		model.addAttribute("projects", projects);

		return "/project/list";
	}

	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public String read(@ModelAttribute ProjectEntity project, ModelMap model) {
		ProjectEntity savedProject = projectService.getProject(project.getId());

		model.addAttribute("mode", "read");
		model.addAttribute("project", savedProject);

		return "/project/item";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createForm(ModelMap model) {
		ProjectEntity project = new ProjectEntity();

		model.addAttribute("mode", "create");
		model.addAttribute("project", project);

		return "/project/item";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createSubmit(@ModelAttribute ProjectEntity project, ModelMap model) throws Exception {
		projectService.addProject(project);

		return expireAndRedirectToRead(model, project);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public String modifyForm(@ModelAttribute ProjectEntity project, ModelMap model) {
		ProjectEntity savedProject = projectService.getProject(project.getId());
		model.addAttribute("mode", "modify");
		model.addAttribute("project", savedProject);

		return "/project/item";
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modifySubmit(@ModelAttribute ProjectEntity project, ModelMap model) throws Exception {
		if (project.getId() == null) {
			throw new IllegalArgumentException("Empty id.");
		}

		ProjectEntity savedProject = projectService.getProject(project.getId());
		project.setPortPrefix(savedProject.getPortPrefix());

		projectService.modifyProject(project);
		return expireAndRedirectToRead(model, project);
	}

	@RequestMapping(value = "/remove"/* , method = RequestMethod.POST */)
	public String remove(@ModelAttribute ProjectEntity project) {
		projectService.removeProject(project);

		return "redirect:/project/list";
	}

	private String expireAndRedirectToRead(ModelMap model, ProjectEntity project) throws Exception {
		// TODO 반영 코드 추가.
		// ReloadService reloadService = RemoteServiceBulkProxy.newAllSolutionProxy(ReloadService.class, ProjectCache.NAME, 5000);
		// reloadService.expire(null);
		//
		// RemoteServiceResponseList response = RemoteServiceResponseListHolder.getAndClear();

		// model.addAttribute("success", response.isSuccess());
		// model.addAttribute("result", response.getEntries());

		model.addAttribute("success", true);
		model.addAttribute("callbackUrl", "/project/read?id=" + project.getId());

		return "/update/refreshResult";
	}
}
