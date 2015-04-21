package com.naver.mage4j.external.php;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Core {
	private static final Logger LOG = LoggerFactory.getLogger(Core.class);

	/**
	 * Sets a user-defined error handler function
	 * @link http://www.php.net/manual/en/function.set-error-handler.php
	 * @param error_handler callable <p>
	 * A callback with the following signature.
	 * &null; may be passed instead, to reset this handler to its default state.
	 * </p>
	 * <p>
	 * boolhandler
	 * interrno
	 * stringerrstr
	 * stringerrfile
	 * interrline
	 * arrayerrcontext
	 * errno
	 * The first parameter, errno, contains the
	 * level of the error raised, as an integer.
	 * @param error_types int[optional] <p>
	 * Can be used to mask the triggering of the
	 * error_handler function just like the error_reporting ini setting 
	 * controls which errors are shown. Without this mask set the
	 * error_handler will be called for every error
	 * regardless to the setting of the error_reporting setting.
	 * </p>
	 * @return mixed a string containing the previously defined error handler (if any). If
	 * the built-in error handler is used &null; is returned. &null; is also returned
	 * in case of an error such as an invalid callback. If the previous error handler
	 * was a class method, this function will return an indexed array with the class
	 * and the method name.
	 */
	public static void set_error_handler(Object error_handler, Object error_types) {
		LOG.error("[NO_IMPL] set_error_handler({}, {})", error_handler, error_types);
	}

	public static void set_error_handler(Object error_handler) {
		set_error_handler(error_handler, null);
	}

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
	public static void date_default_timezone_set(String timezone_identifier) {
		LOG.error("[NO_IMPL] date_default_timezone_set({})", timezone_identifier);
	}
}
