package com.yeon.monitor.log;

import com.yeon.monitor.common.MomString;

import java.io.IOException;

public interface ServerLogLineScanner {
	/**
	 * 텍스트에서 한 줄을 읽어온다. 한 줄은 '\r' 또는 '\n'으로 끝나는 것으로 가정한다.
	 *  
	 * @param buffer 읽을 줄을 저장할 buffer. '\n'을 포함하여 읽을 줄을 저장하며, '\r'은 제거된다. 
	 * @return 버퍼에 저장한 문자 수. 텍스트의 끝에 도착하거나 buffer의 용량을 넘어설 경우 -1.
	 * @throws IOException
	 */
	int nextLine(MomString buffer) throws IOException;

	/**
	 * 스캔한 파일의 전체 크기를 돌려준다. 중간에 스캔을 멈추었더라도 해당 파일의 전체 크기로 계산한다.  
	 * 
	 * @return
	 */
	long getScanFileSize();

	/**
	 * 스캔한 총 라인 수를 돌려준다.
	 * 
	 * @return
	 */
	long getScanLineCount();

	void close();
}