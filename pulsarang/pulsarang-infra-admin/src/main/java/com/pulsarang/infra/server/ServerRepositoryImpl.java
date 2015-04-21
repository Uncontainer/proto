package com.pulsarang.infra.server;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import com.pulsarang.core.data.BaseCustomRepository;
import com.pulsarang.infra.server.ServerEntity.RegistStatus;

public class ServerRepositoryImpl extends BaseCustomRepository<ServerEntity, Long> implements ServerRepositoryCustom {
	QServerEntity qItem = QServerEntity.serverEntity;

	@Override
	public List<String> findSolutionNames() {
		JPAQuery query = createJPAQuery().from(qItem).distinct();

		return query.list(qItem.solutionName);
	}

	@Override
	public List<String> findProjectNames(String solutionName) {
		JPAQuery query = createJPAQuery().from(qItem).where(qItem.solutionName.eq(solutionName)).distinct();
		return query.list(qItem.projectName);
	}

	@Override
	public List<ServerEntity> findNormalServers(String solutionName, String projectName) {
		BooleanExpression whereClauses = qItem.registStatus.eq(RegistStatus.NORMAL.name());
		if (StringUtils.isNotEmpty(solutionName)) {
			whereClauses = whereClauses.and(qItem.solutionName.eq(solutionName));
			if (StringUtils.isNotEmpty(projectName)) {
				whereClauses = whereClauses.and(qItem.projectName.eq(projectName));
			}
		}

		JPAQuery query = createJPAQuery().from(qItem).where(whereClauses);
		return query.list(qItem);
	}

	@Override
	public ServerEntity findServer(ServerEntity server) {
		JPAQuery query = createJPAQuery().from(qItem).where(
				qItem.solutionName.eq(server.getSolutionName()).and(qItem.projectName.eq(server.getProjectName())).and(qItem.ip.eq(server.getIp())));

		return query.uniqueResult(qItem);
	}
}
