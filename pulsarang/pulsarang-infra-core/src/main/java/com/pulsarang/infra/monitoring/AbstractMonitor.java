package com.pulsarang.infra.monitoring;

import java.util.Arrays;
import java.util.List;

import com.pulsarang.infra.InfraContext;
import com.pulsarang.infra.InfraContextFactory;
import com.pulsarang.infra.config.Config;
import com.pulsarang.infra.config.ConfigId;
import com.pulsarang.infra.config.ConfigListener;
import com.pulsarang.infra.remote.reload.AbstractReloadService;

/**
 * 
 * @author pulsarang
 */
public abstract class AbstractMonitor extends AbstractReloadService implements ConfigListener {
	protected volatile int count;
	protected volatile int duration;

	protected AbstractMonitor() {
		InfraContext infraContext = InfraContextFactory.getInfraContext();
		infraContext.getConfigContext().addListener(this);

		// TODO 실제 설정을 가져와서 처리.
		changed(getIds().get(0), new ProjectInfo());
	}

	public int getCount() {
		return count;
	}

	public int getDuration() {
		return duration;
	}

	@Override
	public List<ConfigId> getIds() {
		InfraContext infraContext = InfraContextFactory.getInfraContext();
		// TODO naming 규칙 변경.
		String configName = infraContext.getProjectName() + "-monitor";
		return Arrays.asList(new ConfigId(ProjectInfo.CATEGORY, configName));
	}

	@Override
	public void validate(ConfigId configId, Config config) throws Exception {
		ProjectInfo projectInfo = (ProjectInfo) config;
		int newCount = projectInfo.getMonitoringCount();
		int newDuration = projectInfo.getMonitoringDuration();

		if (newCount < 10 || newCount > 100) {
			throw new IllegalArgumentException();
		}

		// 5초 ~ 10분
		if (newDuration < 5000 || newDuration > 600000) {
			throw new IllegalArgumentException();
		}

		validateLocal(projectInfo);
	}

	@Override
	public void changed(ConfigId configId, Config config) {
		ProjectInfo projectInfo = (ProjectInfo) config;

		int newCount = projectInfo.getMonitoringCount();
		int newDuration = projectInfo.getMonitoringDuration();

		boolean changed = false;
		if (newCount < 10) {
			if (count != 10) {
				changed = true;
			}

			count = 10;
		} else if (newCount > 100) {
			if (count != 100) {
				changed = true;
			}

			count = 100;
		} else {
			if (count != newCount) {
				changed = true;
			}

			count = newCount;
		}

		// 10초 ~ 10분
		if (newDuration < 10000) {
			if (duration != 10000) {
				changed = true;
			}

			duration = 10000;
		} else if (newDuration > 600000) {
			if (duration != 600000) {
				changed = true;
			}

			duration = 600000;
		} else {
			if (duration != newDuration) {
				changed = true;
			}

			duration = newDuration;
		}

		if (changed) {
			initializeCompletedLocal(count, duration, projectInfo);
		} else {
			initializeCompletedLocal(projectInfo);
		}
	}

	protected abstract void validateLocal(ProjectInfo ticket);

	protected abstract void initializeCompletedLocal(ProjectInfo ticket);

	protected abstract void initializeCompletedLocal(int count, int duration, ProjectInfo ticket);
}
