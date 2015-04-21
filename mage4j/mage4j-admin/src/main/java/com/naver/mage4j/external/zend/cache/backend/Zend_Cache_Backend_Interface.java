package com.naver.mage4j.external.zend.cache.backend;

import java.util.List;
import java.util.Map;

import com.naver.mage4j.external.zend.cache.CacheBackendInterface;

/**
 * Zend Framework
 *
 * LICENSE
 *
 * This source file is subject to the new BSD license that is bundled
 * with this package in the file LICENSE.txt.
 * It is also available through the world-wide-web at this URL:
 * http://framework.zend.com/license/new-bsd
 * If you did not receive a copy of the license and are unable to
 * obtain it through the world-wide-web, please send an email
 * to license@zend.com so we can send you a copy immediately.
 *
 * @category   Zend
 * @package    Zend_Cache
 * @subpackage Zend_Cache_Backend
 * @copyright  Copyright (c) 2005-2012 Zend Technologies USA Inc. (http://www.zend.com)
 * @license    http://framework.zend.com/license/new-bsd     New BSD License
 * @version    $Id: Interface.php 24593 2012-01-05 20:35:02Z matthew $
 */

/**
 * @package    Zend_Cache
 * @subpackage Zend_Cache_Backend
 * @copyright  Copyright (c) 2005-2012 Zend Technologies USA Inc. (http://www.zend.com)
 * @license    http://framework.zend.com/license/new-bsd     New BSD License
 */
public interface Zend_Cache_Backend_Interface extends CacheBackendInterface
{
	/**
	 * Set the frontend directives
	 *
	 * @param array $directives assoc of directives
	 */
	void setDirectives(Map<String, Object> directives);

	/**
	 * Test if a cache is available or not (for the given id)
	 *
	 * @param  string $id cache id
	 * @return mixed|false (a cache is not available) or "last modified" timestamp (int) of the available cache record
	 */
	Object test(String id);

	/**
	 * Clean some cache records
	 *
	 * Available modes are :
	 * Zend_Cache::CLEANING_MODE_ALL (default)    => remove all cache entries ($tags is not used)
	 * Zend_Cache::CLEANING_MODE_OLD              => remove too old cache entries ($tags is not used)
	 * Zend_Cache::CLEANING_MODE_MATCHING_TAG     => remove cache entries matching all given tags
	 *                                               ($tags can be an array of strings or a single string)
	 * Zend_Cache::CLEANING_MODE_NOT_MATCHING_TAG => remove cache entries not {matching one of the given tags}
	 *                                               ($tags can be an array of strings or a single string)
	 * Zend_Cache::CLEANING_MODE_MATCHING_ANY_TAG => remove cache entries matching any given tags
	 *                                               ($tags can be an array of strings or a single string)
	 *
	 * @param  string $mode Clean mode
	 * @param  array  $tags Array of tags
	 * @return boolean true if no problem
	 */
	boolean clean(String mode/* = Zend_Cache::CLEANING_MODE_ALL*/, List<String> tags);
}
