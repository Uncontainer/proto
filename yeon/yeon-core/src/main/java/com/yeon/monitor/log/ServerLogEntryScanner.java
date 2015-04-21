package com.yeon.monitor.log;

import java.io.IOException;

public interface ServerLogEntryScanner<T extends ServerLogEntry> {
	/**
	 * 다음 logEntry를 파라미터에 셋팅한다. 끝 도달 여부는 응답값으로 판단한다. 
	 * 
	 * @param logEntry 다음 logEntry를 저장할 객체. null 값은 넘어오지 않는다.
	 * @return 끝에 도달했을 경우 false, 그렇지 않을 경우 true
	 * @throws IOException 
	 */
	int next(T logEntry) throws IOException;

	/**
	 * buffer에서 마지막 logEntry를 제거한다.
	 * 
	 * @param logEntry 제거할 마지막 logEntry
	 * @throws IllegalArgumentException 파라미터로 넘어온 logEntry가 마지막 logEntry가 아닐 경우.
	 */
	void back(T logEntry);

	/**
	 * 서버로그 파일 type을 돌려준다.
	 * 
	 * @return 서버로그 파일 type
	 */
	ServerLogCollectOption.FileType getFileType();

	/**
	 * 스캔한 로그 갯수를 돌려준다.
	 * 
	 * @return 스캔한 로그 갯수
	 */
	long getScanLogCount();

	/**
	 * 스캔한 로그 라인 수를 돌려준다.
	 * 
	 * @return 스캔한 로그 라인 수
	 */
	long getScanLineCount();

	/**
	 * 스캔한 로그 파일의 크기를 돌려준다.
	 * 
	 * @return 스캔한 로그 파일 크기
	 */
	long getScanFileSize();

	/**
	 * 버퍼에 임시로 복사되어 있는 로그들을 제거한다.
	 */
	void clearReady();

	/**
	 * 스캔을 위해 사용한 리소스 해제.
	 */
	void close();
}
