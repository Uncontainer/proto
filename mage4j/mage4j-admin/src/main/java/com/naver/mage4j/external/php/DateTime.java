package com.naver.mage4j.external.php;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateTime {
	private static final Logger LOG = LoggerFactory.getLogger(Core.class);

	/**
	 * Sets the default timezone used by all date/time functions in a script
	 * @link http://www.php.net/manual/en/function.date-default-timezone-set.php
	 * @param timezone_identifier string <p>
	 * The timezone identifier, like UTC or
	 * Europe/Lisbon. The list of valid identifiers is
	 * available in the .
	 * </p>
	 * @return bool This function returns false if the
	 * timezone_identifier isn't valid, or true
	 * otherwise.
	 */
	public static boolean date_default_timezone_set(String timezone_identifier) {
		LOG.error("[NO_IMPL] date_default_timezone_set({})", timezone_identifier);
		return true;
	}
}
