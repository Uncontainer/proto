package com.naver.mage4j.core.util;

public class Flags {
	public static final int CONNECTION_ABORTED = 1;
	public static final int CONNECTION_NORMAL = 0;
	public static final int CONNECTION_TIMEOUT = 2;
	public static final int INI_USER = 1;
	public static final int INI_PERDIR = 2;
	public static final int INI_SYSTEM = 4;
	public static final int INI_ALL = 7;

	/**
	 * Normal INI scanner mode (since PHP 5.3).
	 * @link http://www.php.net/manual/en/filesystem.constants.php
	 */
	public static final int INI_SCANNER_NORMAL = 0;

	/**
	 * Raw INI scanner mode (since PHP 5.3).
	 * @link http://www.php.net/manual/en/filesystem.constants.php
	 */
	public static final int INI_SCANNER_RAW = 1;
	public static final int PHP_URL_SCHEME = 0;
	public static final int PHP_URL_HOST = 1;
	public static final int PHP_URL_PORT = 2;
	public static final int PHP_URL_USER = 3;
	public static final int PHP_URL_PASS = 4;
	public static final int PHP_URL_PATH = 5;
	public static final int PHP_URL_QUERY = 6;
	public static final int PHP_URL_FRAGMENT = 7;
	public static final int PHP_QUERY_RFC1738 = 1;
	public static final int PHP_QUERY_RFC3986 = 2;
	public static final double M_E = 2.718281828459;
	public static final double M_LOG2E = 1.442695040889;
	public static final double M_LOG10E = 0.43429448190325;
	public static final double M_LN2 = 0.69314718055995;
	public static final double M_LN10 = 2.302585092994;

	/**
	 * Round halves up
	 * @link http://www.php.net/manual/en/math.constants.php
	 */
	public static final double M_PI = 3.1415926535898;
	public static final double M_PI_2 = 1.5707963267949;
	public static final double M_PI_4 = 0.78539816339745;
	public static final double M_1_PI = 0.31830988618379;
	public static final double M_2_PI = 0.63661977236758;
	public static final double M_SQRTPI = 1.7724538509055;
	public static final double M_2_SQRTPI = 1.1283791670955;
	public static final double M_LNPI = 1.1447298858494;
	public static final double M_EULER = 0.57721566490153;
	public static final double M_SQRT2 = 1.4142135623731;
	public static final double M_SQRT1_2 = 0.70710678118655;
	public static final double M_SQRT3 = 1.7320508075689;
	public static final double INF = Double.POSITIVE_INFINITY;
	public static final double NAN = Double.NaN;
	public static final int PHP_ROUND_HALF_UP = 1;

	/**
	 * Round halves down
	 * @link http://www.php.net/manual/en/math.constants.php
	 */
	public static final int PHP_ROUND_HALF_DOWN = 2;

	/**
	 * Round halves to even numbers
	 * @link http://www.php.net/manual/en/math.constants.php
	 */
	public static final int PHP_ROUND_HALF_EVEN = 3;

	/**
	 * Round halves to odd numbers
	 * @link http://www.php.net/manual/en/math.constants.php
	 */
	public static final int PHP_ROUND_HALF_ODD = 4;
	public static final int INFO_GENERAL = 1;

	/**
	 * PHP Credits. See also phpcredits.
	 * @link http://www.php.net/manual/en/info.constants.php
	 */
	public static final int INFO_CREDITS = 2;

	/**
	 * Current Local and Master values for PHP directives. See
	 * also ini_get.
	 * @link http://www.php.net/manual/en/info.constants.php
	 */
	public static final int INFO_CONFIGURATION = 4;

	/**
	 * Loaded modules and their respective settings.
	 * @link http://www.php.net/manual/en/info.constants.php
	 */
	public static final int INFO_MODULES = 8;

	/**
	 * Environment Variable information that's also available in
	 * $_ENV.
	 * @link http://www.php.net/manual/en/info.constants.php
	 */
	public static final int INFO_ENVIRONMENT = 16;

	/**
	 * Shows all 
	 * predefined variables from EGPCS (Environment, GET,
	 * POST, Cookie, Server).
	 * @link http://www.php.net/manual/en/info.constants.php
	 */
	public static final int INFO_VARIABLES = 32;

	/**
	 * PHP License information. See also the license faq.
	 * @link http://www.php.net/manual/en/info.constants.php
	 */
	public static final int INFO_LICENSE = 64;

	/**
	 * Unused
	 * @link http://www.php.net/manual/en/info.constants.php
	 */
	public static final long INFO_ALL = 4294967295L;

	/**
	 * A list of the core developers
	 * @link http://www.php.net/manual/en/info.constants.php
	 */
	public static final int CREDITS_GROUP = 1;

	/**
	 * General credits: Language design and concept, PHP
	 * authors and SAPI module.
	 * @link http://www.php.net/manual/en/info.constants.php
	 */
	public static final int CREDITS_GENERAL = 2;

	/**
	 * A list of the server API modules for PHP, and their authors.
	 * @link http://www.php.net/manual/en/info.constants.php
	 */
	public static final int CREDITS_SAPI = 4;

	/**
	 * A list of the extension modules for PHP, and their authors.
	 * @link http://www.php.net/manual/en/info.constants.php
	 */
	public static final int CREDITS_MODULES = 8;

	/**
	 * The credits for the documentation team.
	 * @link http://www.php.net/manual/en/info.constants.php
	 */
	public static final int CREDITS_DOCS = 16;

	/**
	 * Usually used in combination with the other flags. Indicates
	 * that a complete stand-alone HTML page needs to be
	 * printed including the information indicated by the other
	 * flags.
	 * @link http://www.php.net/manual/en/info.constants.php
	 */
	public static final int CREDITS_FULLPAGE = 32;

	/**
	 * The credits for the quality assurance team.
	 * @link http://www.php.net/manual/en/info.constants.php
	 */
	public static final int CREDITS_QA = 64;

	/**
	 * The configuration line, &php.ini; location, build date, Web
	 * Server, System and more.
	 * @link http://www.php.net/manual/en/info.constants.php
	 */
	public static final long CREDITS_ALL = 4294967295L;
	public static final int HTML_SPECIALCHARS = 0;
	public static final int HTML_ENTITIES = 1;
	public static final int ENT_COMPAT = 2;
	public static final int ENT_QUOTES = 3;
	public static final int ENT_NOQUOTES = 0;
	public static final int ENT_IGNORE = 4;
	public static final int ENT_SUBSTITUTE = 8;
	public static final int ENT_DISALLOWED = 128;
	public static final int ENT_HTML401 = 0;
	public static final int ENT_XML1 = 16;
	public static final int ENT_XHTML = 32;
	public static final int ENT_HTML5 = 48;
	public static final int STR_PAD_LEFT = 0;
	public static final int STR_PAD_RIGHT = 1;
	public static final int STR_PAD_BOTH = 2;
	public static final int PATHINFO_DIRNAME = 1;
	public static final int PATHINFO_BASENAME = 2;
	public static final int PATHINFO_EXTENSION = 4;

	/**
	 * Since PHP 5.2.0.
	 * @link http://www.php.net/manual/en/filesystem.constants.php
	 */
	public static final int PATHINFO_FILENAME = 8;
	public static final int CHAR_MAX = 127;
	public static final int LC_CTYPE = 2;
	public static final int LC_NUMERIC = 4;
	public static final int LC_TIME = 5;
	public static final int LC_COLLATE = 1;
	public static final int LC_MONETARY = 3;
	public static final int LC_ALL = 0;
	public static final int LC_MESSAGES = 6;
	public static final int SEEK_SET = 0;
	public static final int SEEK_CUR = 1;
	public static final int SEEK_END = 2;
	public static final int LOCK_SH = 1;
	public static final int LOCK_EX = 2;
	public static final int LOCK_UN = 3;
	public static final int LOCK_NB = 4;

	/**
	 * A connection with an external resource has been established.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_NOTIFY_CONNECT = 2;

	/**
	 * Additional authorization is required to access the specified resource.
	 * Typical issued with severity level of
	 * STREAM_NOTIFY_SEVERITY_ERR.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_NOTIFY_AUTH_REQUIRED = 3;

	/**
	 * Authorization has been completed (with or without success).
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_NOTIFY_AUTH_RESULT = 10;

	/**
	 * The mime-type of resource has been identified,
	 * refer to message for a description of the
	 * discovered type.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_NOTIFY_MIME_TYPE_IS = 4;

	/**
	 * The size of the resource has been discovered.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_NOTIFY_FILE_SIZE_IS = 5;

	/**
	 * The external resource has redirected the stream to an alternate
	 * location. Refer to message.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_NOTIFY_REDIRECTED = 6;

	/**
	 * Indicates current progress of the stream transfer in
	 * bytes_transferred and possibly
	 * bytes_max as well.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_NOTIFY_PROGRESS = 7;

	/**
	 * A generic error occurred on the stream, consult
	 * message and message_code
	 * for details.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_NOTIFY_FAILURE = 9;

	/**
	 * There is no more data available on the stream.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_NOTIFY_COMPLETED = 8;

	/**
	 * A remote address required for this stream has been resolved, or the resolution
	 * failed. See severity for an indication of which happened.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_NOTIFY_RESOLVE = 1;

	/**
	 * Normal, non-error related, notification.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_NOTIFY_SEVERITY_INFO = 0;

	/**
	 * Non critical error condition. Processing may continue.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_NOTIFY_SEVERITY_WARN = 1;

	/**
	 * A critical error occurred. Processing cannot continue.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_NOTIFY_SEVERITY_ERR = 2;

	/**
	 * Used with stream_filter_append and
	 * stream_filter_prepend to indicate
	 * that the specified filter should only be applied when
	 * reading
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_FILTER_READ = 1;

	/**
	 * Used with stream_filter_append and
	 * stream_filter_prepend to indicate
	 * that the specified filter should only be applied when
	 * writing
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_FILTER_WRITE = 2;

	/**
	 * This constant is equivalent to 
	 * STREAM_FILTER_READ | STREAM_FILTER_WRITE
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_FILTER_ALL = 3;

	/**
	 * Client socket opened with stream_socket_client
	 * should remain persistent between page loads.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_CLIENT_PERSISTENT = 1;

	/**
	 * Open client socket asynchronously. This option must be used
	 * together with the STREAM_CLIENT_CONNECT flag.
	 * Used with stream_socket_client.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_CLIENT_ASYNC_CONNECT = 2;

	/**
	 * Open client socket connection. Client sockets should always
	 * include this flag. Used with stream_socket_client.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_CLIENT_CONNECT = 4;
	public static final int STREAM_CRYPTO_METHOD_SSLv2_CLIENT = 0;
	public static final int STREAM_CRYPTO_METHOD_SSLv3_CLIENT = 1;
	public static final int STREAM_CRYPTO_METHOD_SSLv23_CLIENT = 2;
	public static final int STREAM_CRYPTO_METHOD_TLS_CLIENT = 3;
	public static final int STREAM_CRYPTO_METHOD_SSLv2_SERVER = 4;
	public static final int STREAM_CRYPTO_METHOD_SSLv3_SERVER = 5;
	public static final int STREAM_CRYPTO_METHOD_SSLv23_SERVER = 6;
	public static final int STREAM_CRYPTO_METHOD_TLS_SERVER = 7;

	/**
	 * Used with stream_socket_shutdown to disable
	 * further receptions. Added in PHP 5.2.1.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_SHUT_RD = 0;

	/**
	 * Used with stream_socket_shutdown to disable
	 * further transmissions. Added in PHP 5.2.1.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_SHUT_WR = 1;

	/**
	 * Used with stream_socket_shutdown to disable
	 * further receptions and transmissions. Added in PHP 5.2.1.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_SHUT_RDWR = 2;

	/**
	 * Internet Protocol Version 4 (IPv4).
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_PF_INET = 2;

	/**
	 * Internet Protocol Version 6 (IPv6).
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_PF_INET6 = 30;

	/**
	 * Unix system internal protocols.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_PF_UNIX = 1;

	/**
	 * Provides a IP socket.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_IPPROTO_IP = 0;

	/**
	 * Provides a TCP socket.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_IPPROTO_TCP = 6;

	/**
	 * Provides a UDP socket.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_IPPROTO_UDP = 17;

	/**
	 * Provides a ICMP socket.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_IPPROTO_ICMP = 1;

	/**
	 * Provides a RAW socket.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_IPPROTO_RAW = 255;

	/**
	 * Provides sequenced, two-way byte streams with a transmission mechanism
	 * for out-of-band data (TCP, for example).
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_SOCK_STREAM = 1;

	/**
	 * Provides datagrams, which are connectionless messages (UDP, for
	 * example).
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_SOCK_DGRAM = 2;

	/**
	 * Provides a raw socket, which provides access to internal network
	 * protocols and interfaces. Usually this type of socket is just available
	 * to the root user.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_SOCK_RAW = 3;

	/**
	 * Provides a sequenced packet stream socket.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_SOCK_SEQPACKET = 5;

	/**
	 * Provides a RDM (Reliably-delivered messages) socket.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_SOCK_RDM = 4;
	public static final int STREAM_PEEK = 2;
	public static final int STREAM_OOB = 1;

	/**
	 * Tells a stream created with stream_socket_server
	 * to bind to the specified target. Server sockets should always include this flag.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_SERVER_BIND = 4;

	/**
	 * Tells a stream created with stream_socket_server
	 * and bound using the STREAM_SERVER_BIND flag to start
	 * listening on the socket. Connection-orientated transports (such as TCP)
	 * must use this flag, otherwise the server socket will not be enabled.
	 * Using this flag for connect-less transports (such as UDP) is an error.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_SERVER_LISTEN = 8;

	/**
	 * Search for filename in
	 * include_path (since PHP 5).
	 * @link http://www.php.net/manual/en/filesystem.constants.php
	 */
	public static final int FILE_USE_INCLUDE_PATH = 1;

	/**
	 * Strip EOL characters (since PHP 5).
	 * @link http://www.php.net/manual/en/filesystem.constants.php
	 */
	public static final int FILE_IGNORE_NEW_LINES = 2;

	/**
	 * Skip empty lines (since PHP 5).
	 * @link http://www.php.net/manual/en/filesystem.constants.php
	 */
	public static final int FILE_SKIP_EMPTY_LINES = 4;

	/**
	 * Append content to existing file.
	 * @link http://www.php.net/manual/en/filesystem.constants.php
	 */
	public static final int FILE_APPEND = 8;
	public static final int FILE_NO_DEFAULT_CONTEXT = 16;

	/**
	 * <p>
	 * Text mode (since PHP 5.2.7).
	 * <p>
	 * This constant has no effect, and is only available for 
	 * forward compatibility.
	 * </p>
	 * </p>
	 * @link http://www.php.net/manual/en/filesystem.constants.php
	 */
	public static final int FILE_TEXT = 0;

	/**
	 * <p>
	 * Binary mode (since PHP 5.2.7).
	 * <p>
	 * This constant has no effect, and is only available for 
	 * forward compatibility.
	 * </p>
	 * </p>
	 * @link http://www.php.net/manual/en/filesystem.constants.php
	 */
	public static final int FILE_BINARY = 0;

	/**
	 * Disable backslash escaping.
	 * @link http://www.php.net/manual/en/filesystem.constants.php
	 */
	public static final int FNM_NOESCAPE = 1;

	/**
	 * Slash in string only matches slash in the given pattern.
	 * @link http://www.php.net/manual/en/filesystem.constants.php
	 */
	public static final int FNM_PATHNAME = 2;

	/**
	 * Leading period in string must be exactly matched by period in the given pattern.
	 * @link http://www.php.net/manual/en/filesystem.constants.php
	 */
	public static final int FNM_PERIOD = 4;

	/**
	 * Caseless match. Part of the GNU extension.
	 * @link http://www.php.net/manual/en/filesystem.constants.php
	 */
	public static final int FNM_CASEFOLD = 16;

	/**
	 * Return Code indicating that the
	 * userspace filter returned buckets in $out.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int PSFS_PASS_ON = 2;

	/**
	 * Return Code indicating that the
	 * userspace filter did not return buckets in $out
	 * (i.e. No data available).
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int PSFS_FEED_ME = 1;

	/**
	 * Return Code indicating that the
	 * userspace filter encountered an unrecoverable error
	 * (i.e. Invalid data received).
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int PSFS_ERR_FATAL = 0;

	/**
	 * Regular read/write.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int PSFS_FLAG_NORMAL = 0;

	/**
	 * An incremental flush.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int PSFS_FLAG_FLUSH_INC = 1;

	/**
	 * Final flush prior to closing.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int PSFS_FLAG_FLUSH_CLOSE = 2;

	/**
	 * <p>
	 * The default algorithm to use for hashing if no algorithm is provided.
	 * This may change in newer PHP releases when newer, stronger hashing
	 * algorithms are supported.
	 * </p>
	 * <p>
	 * It is worth noting that over time this constant can (and likely will)
	 * change. Therefore you should be aware that the length of the resulting
	 * hash can change. Therefore, if you use PASSWORD_DEFAULT
	 * you should store the resulting hash in a way that can store more than 60
	 * characters (255 is the recomended width).
	 * </p>
	 * <p>
	 * Values for this constant:
	 * </p>
	 * PHP 5.5.0 - PASSWORD_BCRYPT
	 * @link http://www.php.net/manual/en/password.constants.php
	 */
	public static final int PASSWORD_DEFAULT = 1;

	/**
	 * <p>
	 * PASSWORD_BCRYPT is used to create new password
	 * hashes using the CRYPT_BLOWFISH algorithm.
	 * </p>
	 * <p>
	 * This will always result in a hash using the "$2y$" crypt format, 
	 * which is always 60 characters wide.
	 * </p>
	 * <p>
	 * Supported Options:
	 * </p>
	 * <p>
	 * salt - to manually provide a salt to use when hashing the password.
	 * Note that this will override and prevent a salt from being automatically generated.
	 * </p>
	 * <p>
	 * If omitted, a random salt will be generated by password_hash for
	 * each password hashed. This is the intended mode of operation.
	 * </p>
	 * @link http://www.php.net/manual/en/password.constants.php
	 */
	public static final int PASSWORD_BCRYPT = 1;
	public static final int PASSWORD_BCRYPT_DEFAULT_COST = 10;
	public static final int ABDAY_1 = 14;
	public static final int ABDAY_2 = 15;
	public static final int ABDAY_3 = 16;
	public static final int ABDAY_4 = 17;
	public static final int ABDAY_5 = 18;
	public static final int ABDAY_6 = 19;
	public static final int ABDAY_7 = 20;
	public static final int DAY_1 = 7;
	public static final int DAY_2 = 8;
	public static final int DAY_3 = 9;
	public static final int DAY_4 = 10;
	public static final int DAY_5 = 11;
	public static final int DAY_6 = 12;
	public static final int DAY_7 = 13;
	public static final int ABMON_1 = 33;
	public static final int ABMON_2 = 34;
	public static final int ABMON_3 = 35;
	public static final int ABMON_4 = 36;
	public static final int ABMON_5 = 37;
	public static final int ABMON_6 = 38;
	public static final int ABMON_7 = 39;
	public static final int ABMON_8 = 40;
	public static final int ABMON_9 = 41;
	public static final int ABMON_10 = 42;
	public static final int ABMON_11 = 43;
	public static final int ABMON_12 = 44;
	public static final int MON_1 = 21;
	public static final int MON_2 = 22;
	public static final int MON_3 = 23;
	public static final int MON_4 = 24;
	public static final int MON_5 = 25;
	public static final int MON_6 = 26;
	public static final int MON_7 = 27;
	public static final int MON_8 = 28;
	public static final int MON_9 = 29;
	public static final int MON_10 = 30;
	public static final int MON_11 = 31;
	public static final int MON_12 = 32;
	public static final int AM_STR = 5;
	public static final int PM_STR = 6;
	public static final int D_T_FMT = 1;
	public static final int D_FMT = 2;
	public static final int T_FMT = 3;
	public static final int T_FMT_AMPM = 4;
	public static final int ERA = 45;
	public static final int ERA_D_T_FMT = 47;
	public static final int ERA_D_FMT = 46;
	public static final int ERA_T_FMT = 48;
	public static final int ALT_DIGITS = 49;
	public static final int CRNCYSTR = 56;
	public static final int RADIXCHAR = 50;
	public static final int THOUSEP = 51;
	public static final int YESEXPR = 52;
	public static final int NOEXPR = 53;
	public static final int YESSTR = 54;
	public static final int NOSTR = 55;
	public static final int CODESET = 0;
	public static final int CRYPT_SALT_LENGTH = 123;
	public static final int CRYPT_STD_DES = 1;
	public static final int CRYPT_EXT_DES = 1;
	public static final int CRYPT_MD5 = 1;
	public static final int CRYPT_BLOWFISH = 1;
	public static final int CRYPT_SHA256 = 1;
	public static final int CRYPT_SHA512 = 1;
	public static final String DIRECTORY_SEPARATOR = "/";

	/**
	 * Available since PHP 4.3.0. Semicolon on Windows, colon otherwise.
	 * @link http://www.php.net/manual/en/dir.constants.php
	 */
	public static final String PATH_SEPARATOR = ":";

	/**
	 * Available since PHP 5.4.0.
	 * @link http://www.php.net/manual/en/dir.constants.php
	 */
	public static final int SCANDIR_SORT_ASCENDING = 0;

	/**
	 * Available since PHP 5.4.0.
	 * @link http://www.php.net/manual/en/dir.constants.php
	 */
	public static final int SCANDIR_SORT_DESCENDING = 1;

	/**
	 * Available since PHP 5.4.0.
	 * @link http://www.php.net/manual/en/dir.constants.php
	 */
	public static final int SCANDIR_SORT_NONE = 2;
	public static final int GLOB_BRACE = 128;
	public static final int GLOB_MARK = 8;
	public static final int GLOB_NOSORT = 32;
	public static final int GLOB_NOCHECK = 16;
	public static final int GLOB_NOESCAPE = 8192;
	public static final int GLOB_ERR = 4;
	public static final int GLOB_ONLYDIR = 1073741824;
	public static final int GLOB_AVAILABLE_FLAGS = 1073750204;

	/**
	 * system is unusable
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_EMERG = 0;

	/**
	 * action must be taken immediately
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_ALERT = 1;

	/**
	 * critical conditions
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_CRIT = 2;

	/**
	 * error conditions
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_ERR = 3;

	/**
	 * warning conditions
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_WARNING = 4;

	/**
	 * normal, but significant, condition
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_NOTICE = 5;

	/**
	 * informational message
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_INFO = 6;

	/**
	 * debug-level message
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_DEBUG = 7;

	/**
	 * kernel messages
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_KERN = 0;

	/**
	 * generic user-level messages
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_USER = 8;

	/**
	 * mail subsystem
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_MAIL = 16;

	/**
	 * other system daemons
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_DAEMON = 24;

	/**
	 * security/authorization messages (use LOG_AUTHPRIV instead
	 * in systems where that constant is defined)
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_AUTH = 32;

	/**
	 * messages generated internally by syslogd
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_SYSLOG = 40;

	/**
	 * line printer subsystem
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_LPR = 48;

	/**
	 * USENET news subsystem
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_NEWS = 56;

	/**
	 * UUCP subsystem
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_UUCP = 64;

	/**
	 * clock daemon (cron and at)
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_CRON = 72;

	/**
	 * security/authorization messages (private)
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_AUTHPRIV = 80;
	public static final int LOG_LOCAL0 = 128;
	public static final int LOG_LOCAL1 = 136;
	public static final int LOG_LOCAL2 = 144;
	public static final int LOG_LOCAL3 = 152;
	public static final int LOG_LOCAL4 = 160;
	public static final int LOG_LOCAL5 = 168;
	public static final int LOG_LOCAL6 = 176;
	public static final int LOG_LOCAL7 = 184;

	/**
	 * include PID with each message
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_PID = 1;

	/**
	 * if there is an error while sending data to the system logger,
	 * write directly to the system console
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_CONS = 2;

	/**
	 * (default) delay opening the connection until the first
	 * message is logged
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_ODELAY = 4;

	/**
	 * open the connection to the logger immediately
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_NDELAY = 8;
	public static final int LOG_NOWAIT = 16;

	/**
	 * print log message also to standard error
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int LOG_PERROR = 32;
	public static final int EXTR_OVERWRITE = 0;
	public static final int EXTR_SKIP = 1;
	public static final int EXTR_PREFIX_SAME = 2;
	public static final int EXTR_PREFIX_ALL = 3;
	public static final int EXTR_PREFIX_INVALID = 4;
	public static final int EXTR_PREFIX_IF_EXISTS = 5;
	public static final int EXTR_IF_EXISTS = 6;
	public static final int EXTR_REFS = 256;

	/**
	 * SORT_ASC is used with
	 * array_multisort to sort in ascending order.
	 * @link http://www.php.net/manual/en/array.constants.php
	 */
	public static final int SORT_ASC = 4;

	/**
	 * SORT_DESC is used with
	 * array_multisort to sort in descending order.
	 * @link http://www.php.net/manual/en/array.constants.php
	 */
	public static final int SORT_DESC = 3;

	/**
	 * SORT_REGULAR is used to compare items normally.
	 * @link http://www.php.net/manual/en/array.constants.php
	 */
	public static final int SORT_REGULAR = 0;

	/**
	 * SORT_NUMERIC is used to compare items numerically.
	 * @link http://www.php.net/manual/en/array.constants.php
	 */
	public static final int SORT_NUMERIC = 1;

	/**
	 * SORT_STRING is used to compare items as strings.
	 * @link http://www.php.net/manual/en/array.constants.php
	 */
	public static final int SORT_STRING = 2;

	/**
	 * SORT_LOCALE_STRING is used to compare items as
	 * strings, based on the current locale. Added in PHP 4.4.0 and 5.0.2.
	 * @link http://www.php.net/manual/en/array.constants.php
	 */
	public static final int SORT_LOCALE_STRING = 5;

	/**
	 * SORT_NATURAL is used to compare items as
	 * strings using "natural ordering" like natsort. Added in PHP 5.4.0.
	 * @link http://www.php.net/manual/en/array.constants.php
	 */
	public static final int SORT_NATURAL = 6;

	/**
	 * SORT_FLAG_CASE can be combined
	 * (bitwise OR) with
	 * SORT_STRING or
	 * SORT_NATURAL to sort strings case-insensitively. Added in PHP 5.4.0.
	 * @link http://www.php.net/manual/en/array.constants.php
	 */
	public static final int SORT_FLAG_CASE = 8;

	/**
	 * CASE_LOWER is used with
	 * array_change_key_case and is used to convert array
	 * keys to lower case. This is also the default case for
	 * array_change_key_case.
	 * @link http://www.php.net/manual/en/array.constants.php
	 */
	public static final int CASE_LOWER = 0;

	/**
	 * CASE_UPPER is used with
	 * array_change_key_case and is used to convert array
	 * keys to upper case.
	 * @link http://www.php.net/manual/en/array.constants.php
	 */
	public static final int CASE_UPPER = 1;
	public static final int COUNT_NORMAL = 0;
	public static final int COUNT_RECURSIVE = 1;
	public static final int ASSERT_ACTIVE = 1;
	public static final int ASSERT_CALLBACK = 2;
	public static final int ASSERT_BAIL = 3;
	public static final int ASSERT_WARNING = 4;
	public static final int ASSERT_QUIET_EVAL = 5;

	/**
	 * Flag indicating if the stream
	 * used the include path.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_USE_PATH = 1;
	public static final int STREAM_IGNORE_URL = 2;

	/**
	 * Flag indicating if the wrapper
	 * is responsible for raising errors using trigger_error 
	 * during opening of the stream. If this flag is not set, you
	 * should not raise any errors.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_REPORT_ERRORS = 8;

	/**
	 * This flag is useful when your extension really must be able to randomly
	 * seek around in a stream. Some streams may not be seekable in their
	 * native form, so this flag asks the streams API to check to see if the
	 * stream does support seeking. If it does not, it will copy the stream
	 * into temporary storage (which may be a temporary file or a memory
	 * stream) which does support seeking.
	 * Please note that this flag is not useful when you want to seek the
	 * stream and write to it, because the stream you are accessing might
	 * not be bound to the actual resource you requested.
	 * If the requested resource is network based, this flag will cause the
	 * opener to block until the whole contents have been downloaded.
	 * @link http://www.php.net/manual/en/internals2.ze1.streams.constants.php
	 */
	public static final int STREAM_MUST_SEEK = 16;
	public static final int STREAM_URL_STAT_LINK = 1;
	public static final int STREAM_URL_STAT_QUIET = 2;
	public static final int STREAM_MKDIR_RECURSIVE = 1;
	public static final int STREAM_IS_URL = 1;
	public static final int STREAM_OPTION_BLOCKING = 1;
	public static final int STREAM_OPTION_READ_TIMEOUT = 4;
	public static final int STREAM_OPTION_READ_BUFFER = 2;
	public static final int STREAM_OPTION_WRITE_BUFFER = 3;
	public static final int STREAM_BUFFER_NONE = 0;
	public static final int STREAM_BUFFER_LINE = 1;
	public static final int STREAM_BUFFER_FULL = 2;

	/**
	 * Stream casting, when stream_cast is called 
	 * otherwise (see above).
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_CAST_AS_STREAM = 0;

	/**
	 * Stream casting, for when stream_select is 
	 * calling stream_cast.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_CAST_FOR_SELECT = 3;

	/**
	 * Used with stream_metadata, to specify touch call.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_META_TOUCH = 1;

	/**
	 * Used with stream_metadata, to specify chown call.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_META_OWNER = 3;

	/**
	 * Used with stream_metadata, to specify chown call.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_META_OWNER_NAME = 2;

	/**
	 * Used with stream_metadata, to specify chgrp call.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_META_GROUP = 5;

	/**
	 * Used with stream_metadata, to specify chgrp call.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_META_GROUP_NAME = 4;

	/**
	 * Used with stream_metadata, to specify chmod call.
	 * @link http://www.php.net/manual/en/stream.constants.php
	 */
	public static final int STREAM_META_ACCESS = 6;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_GIF = 1;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_JPEG = 2;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_PNG = 3;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_SWF = 4;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_PSD = 5;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_BMP = 6;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_TIFF_II = 7;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_TIFF_MM = 8;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_JPC = 9;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_JP2 = 10;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_JPX = 11;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_JB2 = 12;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_SWC = 13;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_IFF = 14;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_WBMP = 15;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_JPEG2000 = 9;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_XBM = 16;

	/**
	 * Image type constant used by the
	 * image_type_to_mime_type and
	 * image_type_to_extension functions.
	 * (Available as of PHP 5.3.0)
	 * @link http://www.php.net/manual/en/image.constants.php
	 */
	public static final int IMAGETYPE_ICO = 17;
	public static final int IMAGETYPE_UNKNOWN = 0;
	public static final int IMAGETYPE_COUNT = 18;

	/**
	 * IPv4 Address Resource
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int DNS_A = 1;

	/**
	 * Authoritative Name Server Resource
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int DNS_NS = 2;

	/**
	 * Alias (Canonical Name) Resource
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int DNS_CNAME = 16;

	/**
	 * Start of Authority Resource
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int DNS_SOA = 32;

	/**
	 * Pointer Resource
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int DNS_PTR = 2048;

	/**
	 * Host Info Resource (See IANA's
	 * Operating System Names
	 * for the meaning of these values)
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int DNS_HINFO = 4096;

	/**
	 * Mail Exchanger Resource
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int DNS_MX = 16384;

	/**
	 * Text Resource
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int DNS_TXT = 32768;
	public static final int DNS_SRV = 33554432;
	public static final int DNS_NAPTR = 67108864;

	/**
	 * IPv6 Address Resource
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int DNS_AAAA = 134217728;
	public static final int DNS_A6 = 16777216;

	/**
	 * Any Resource Record. On most systems
	 * this returns all resource records, however
	 * it should not be counted upon for critical
	 * uses. Try DNS_ALL instead.
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int DNS_ANY = 268435456;

	/**
	 * Iteratively query the name server for
	 * each available record type.
	 * @link http://www.php.net/manual/en/network.constants.php
	 */
	public static final int DNS_ALL = 251713587;
}
