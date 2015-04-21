package com.pulsarang.infra.server;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "nm_svr_info")
public class ServerEntity {
	private Long id;
	private Date lastStartupDate;
	private Date lastShutdownDate;
	private String runningStatus;
	private String registStatus;

	private Server server;

	public enum RegistStatus {
		WAIT, IGNORE, NORMAL
	}

	public enum RunningStatus {
		RUNNING, SHUTDOWN
	}

	public ServerEntity() {
		this.server = new Server();
	}

	public ServerEntity(Server server) {
		this.server = server;
	}

	@Transient
	public Server getServer() {
		return server;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "lst_strt_ymdt")
	public Date getLastStartupDate() {
		return lastStartupDate;
	}

	public void setLastStartupDate(Date lastStartupDate) {
		this.lastStartupDate = lastStartupDate;
	}

	@Column(name = "lst_shtdn_ymdt")
	public Date getLastShutdownDate() {
		return lastShutdownDate;
	}

	public void setLastShutdownDate(Date lastShutdownDate) {
		this.lastShutdownDate = lastShutdownDate;
	}

	@Column(name = "runng_stat_cd")
	public String getRunningStatus() {
		return runningStatus;
	}

	public void setRunningStatus(String runningStatus) {
		this.runningStatus = runningStatus;
	}

	@Column(name = "reg_stat_cd")
	public String getRegistStatus() {
		return registStatus;
	}

	public void setRegistStatus(String registStatus) {
		this.registStatus = registStatus;
	}

	@Column(name = "sln_nm", length = 20)
	public String getSolutionName() {
		return server.getSolutionName();
	}

	public void setSolutionName(String solutionName) {
		server.setSolutionName(solutionName);
	}

	@Column(name = "pjt_nm", length = 20)
	public String getProjectName() {
		return server.getProjectName();
	}

	public void setProjectName(String projectName) {
		server.setProjectName(projectName);
	}

	@Column(name = "svr_nm", length = 50)
	public String getName() {
		return server.getName();
	}

	public void setName(String name) {
		server.setName(name);
	}

	@Column(name = "svr_ip", length = 15)
	public String getIp() {
		return server.getIp();
	}

	public void setIp(String ip) {
		server.setIp(ip);
	}

	@Column(name = "svr_prt")
	public int getPort() {
		return server.getPort();
	}

	public void setPort(int port) {
		server.setPort(port);
	}

	@Transient
	public boolean isRunning() {
		return RunningStatus.RUNNING.toString().equals(getRunningStatus());
	}
}
