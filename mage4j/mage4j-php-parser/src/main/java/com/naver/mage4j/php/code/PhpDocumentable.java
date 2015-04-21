package com.naver.mage4j.php.code;

import java.util.List;

public interface PhpDocumentable {
	List<String> getDocumentComments();

	void setDocumentComments(List<String> documentComments);
}
