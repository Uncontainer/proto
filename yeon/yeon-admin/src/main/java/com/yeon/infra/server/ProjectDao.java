package com.yeon.infra.server;

import com.yeon.YeonContext;
import com.yeon.YeonParameters;
import com.yeon.lang.ResourceService;
import com.yeon.lang.query.ResultSet;
import com.yeon.server.Project;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectDao implements InitializingBean {
	@Autowired
	YeonContext yeonContext;

	private ResourceService resourceService;

	@Override
	public void afterPropertiesSet() throws Exception {
		resourceService = YeonContext.getResourceContext().getResourceService();
	}

	public List<Project> selectAll() {
		List<Project> projects = new ArrayList<Project>();
		ResultSet rs = resourceService.select("SELECT * from @yeon.project");
		try {
			while (rs.next()) {
				projects.add((Project) rs.get());
			}
		} finally {
			rs.close();
		}

		return projects;
	}

	public void insert(Project project) {
		resourceService.add(project);
	}

	public void update(Project project) {
		resourceService.modify(project);
	}

	public void delete(String id) {
		resourceService.remove(id);
	}

	public Project select(String solutionName, String projectName) {
		String query = "SELECT * FROM @yeon.project WHERE " +
				YeonParameters.SOLUTION_NAME + "=" + solutionName +
				" AND " + YeonParameters.PROCESSOR_NAME + "=" + projectName;

		ResultSet rs = resourceService.select(query);
		try {
			return rs.next() ? (Project) rs.get() : null;
		} finally {
			rs.close();
		}
	}
}
