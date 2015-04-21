//package com.yeon.infra.server;
//
//import java.util.Date;
//
//import javax.persistence.Transient;
//
//import com.yeon.server.Server;
//import com.yeon.server.ServerRunningStatus;
//
//public class ServerEntity extends Server {
//	private Date lastStartupDate;
//	private Date lastShutdownDate;
//
//	public Date getLastStartupDate() {
//		return lastStartupDate;
//	}
//
//	public void setLastStartupDate(Date lastStartupDate) {
//		this.lastStartupDate = lastStartupDate;
//	}
//
//	public Date getLastShutdownDate() {
//		return lastShutdownDate;
//	}
//
//	public void setLastShutdownDate(Date lastShutdownDate) {
//		this.lastShutdownDate = lastShutdownDate;
//	}
//
//	@Transient
//	public boolean isRunning() {
//		return getRunningStatus() == ServerRunningStatus.RUNNING;
//	}
//}
