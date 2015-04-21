package com.naver.mage4j.external.zend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.naver.mage4j.core.mage.core.AppContext;
import com.naver.mage4j.core.util.PhpStringUtils;
import com.naver.mage4j.core.util.RegexUtils;
import com.naver.mage4j.external.zend.cache.Zend_Cache_Core;
import com.naver.mage4j.external.zend.locale.Zend_Locale_Data;
import com.naver.mage4j.external.zend.locale.Zend_Locale_Exception;
import com.naver.mage4j.external.zend.locale.data.Zend_Locale_Data_Translation;
import com.naver.mage4j.php.mage.MageAtomArray;

/**
 * Base class for localization
 */
public class Zend_Locale {
	private static final Logger LOG = LoggerFactory.getLogger(Zend_Locale.class);

	/**
	 * Class wide Locale Constants
	 */
	private static Map<String, Boolean> _localeData = MageAtomArray.createMap(new Object[] {"root", true}, new Object[] {"aa_DJ", true},
		new Object[] {"aa_ER", true}, new Object[] {"aa_ET", true}, new Object[] {"aa", true}, new Object[] {"af_NA", true}, new Object[] {"af_ZA", true},
		new Object[] {"af", true}, new Object[] {"ak_GH", true}, new Object[] {"ak", true}, new Object[] {"am_ET", true},
		new Object[] {"am", true}, new Object[] {"ar_AE", true}, new Object[] {"ar_BH", true}, new Object[] {"ar_DZ", true},
		new Object[] {"ar_EG", true}, new Object[] {"ar_IQ", true}, new Object[] {"ar_JO", true}, new Object[] {"ar_KW", true},
		new Object[] {"ar_LB", true}, new Object[] {"ar_LY", true}, new Object[] {"ar_MA", true}, new Object[] {"ar_OM", true},
		new Object[] {"ar_QA", true}, new Object[] {"ar_SA", true}, new Object[] {"ar_SD", true}, new Object[] {"ar_SY", true},
		new Object[] {"ar_TN", true}, new Object[] {"ar_YE", true}, new Object[] {"ar", true}, new Object[] {"as_IN", true}, new Object[] {"as", true},
		new Object[] {"az_AZ", true}, new Object[] {"az", true}, new Object[] {"be_BY", true}, new Object[] {"be", true}, new Object[] {"bg_BG", true},
		new Object[] {"bg", true}, new Object[] {"bn_BD", true}, new Object[] {"bn_IN", true}, new Object[] {"bn", true}, new Object[] {"bo_CN", true},
		new Object[] {"bo_IN", true}, new Object[] {"bo", true}, new Object[] {"bs_BA", true}, new Object[] {"bs", true}, new Object[] {"byn_ER", true},
		new Object[] {"byn", true}, new Object[] {"ca_ES", true}, new Object[] {"ca", true}, new Object[] {"cch_NG", true}, new Object[] {"cch", true},
		new Object[] {"cop", true}, new Object[] {"cs_CZ", true}, new Object[] {"cs", true}, new Object[] {"cy_GB", true}, new Object[] {"cy", true},
		new Object[] {"da_DK", true}, new Object[] {"da", true}, new Object[] {"de_AT", true}, new Object[] {"de_BE", true}, new Object[] {"de_CH", true},
		new Object[] {"de_DE", true}, new Object[] {"de_LI", true}, new Object[] {"de_LU", true}, new Object[] {"de", true}, new Object[] {"dv_MV", true},
		new Object[] {"dv", true}, new Object[] {"dz_BT", true}, new Object[] {"dz", true}, new Object[] {"ee_GH", true}, new Object[] {"ee_TG", true},
		new Object[] {"ee", true}, new Object[] {"el_CY", true}, new Object[] {"el_GR", true}, new Object[] {"el", true}, new Object[] {"en_AS", true},
		new Object[] {"en_AU", true}, new Object[] {"en_BE", true}, new Object[] {"en_BW", true}, new Object[] {"en_BZ", true}, new Object[] {"en_CA", true},
		new Object[] {"en_GB", true}, new Object[] {"en_GU", true}, new Object[] {"en_HK", true}, new Object[] {"en_IE", true}, new Object[] {"en_IN", true},
		new Object[] {"en_JM", true}, new Object[] {"en_MH", true}, new Object[] {"en_MP", true}, new Object[] {"en_MT", true}, new Object[] {"en_NA", true},
		new Object[] {"en_NZ", true}, new Object[] {"en_PH", true}, new Object[] {"en_PK", true}, new Object[] {"en_SG", true}, new Object[] {"en_TT", true},
		new Object[] {"en_UM", true}, new Object[] {"en_US", true}, new Object[] {"en_VI", true}, new Object[] {"en_ZA", true}, new Object[] {"en_ZW", true},
		new Object[] {"en", true}, new Object[] {"eo", true}, new Object[] {"es_AR", true}, new Object[] {"es_BO", true}, new Object[] {"es_CL", true},
		new Object[] {"es_CO", true}, new Object[] {"es_CR", true}, new Object[] {"es_DO", true}, new Object[] {"es_EC", true}, new Object[] {"es_ES", true},
		new Object[] {"es_GT", true}, new Object[] {"es_HN", true}, new Object[] {"es_MX", true}, new Object[] {"es_NI", true}, new Object[] {"es_PA", true},
		new Object[] {"es_PE", true}, new Object[] {"es_PR", true}, new Object[] {"es_PY", true}, new Object[] {"es_SV", true}, new Object[] {"es_US", true},
		new Object[] {"es_UY", true}, new Object[] {"es_VE", true}, new Object[] {"es", true}, new Object[] {"et_EE", true}, new Object[] {"et", true},
		new Object[] {"eu_ES", true}, new Object[] {"eu", true}, new Object[] {"fa_AF", true}, new Object[] {"fa_IR", true}, new Object[] {"fa", true},
		new Object[] {"fi_FI", true}, new Object[] {"fi", true}, new Object[] {"fil_PH", true}, new Object[] {"fil", true}, new Object[] {"fo_FO", true},
		new Object[] {"fo", true}, new Object[] {"fr_BE", true}, new Object[] {"fr_CA", true}, new Object[] {"fr_CH", true}, new Object[] {"fr_FR", true},
		new Object[] {"fr_LU", true}, new Object[] {"fr_MC", true}, new Object[] {"fr_SN", true}, new Object[] {"fr", true}, new Object[] {"fur_IT", true},
		new Object[] {"fur", true}, new Object[] {"ga_IE", true}, new Object[] {"ga", true}, new Object[] {"gaa_GH", true}, new Object[] {"gaa", true},
		new Object[] {"gez_ER", true}, new Object[] {"gez_ET", true}, new Object[] {"gez", true}, new Object[] {"gl_ES", true}, new Object[] {"gl", true},
		new Object[] {"gsw_CH", true}, new Object[] {"gsw", true}, new Object[] {"gu_IN", true}, new Object[] {"gu", true}, new Object[] {"gv_GB", true},
		new Object[] {"gv", true}, new Object[] {"ha_GH", true}, new Object[] {"ha_NE", true}, new Object[] {"ha_NG", true}, new Object[] {"ha_SD", true},
		new Object[] {"ha", true}, new Object[] {"haw_US", true}, new Object[] {"haw", true}, new Object[] {"he_IL", true}, new Object[] {"he", true},
		new Object[] {"hi_IN", true}, new Object[] {"hi", true}, new Object[] {"hr_HR", true}, new Object[] {"hr", true}, new Object[] {"hu_HU", true},
		new Object[] {"hu", true}, new Object[] {"hy_AM", true}, new Object[] {"hy", true}, new Object[] {"ia", true}, new Object[] {"id_ID", true},
		new Object[] {"id", true}, new Object[] {"ig_NG", true}, new Object[] {"ig", true}, new Object[] {"ii_CN", true}, new Object[] {"ii", true},
		new Object[] {"in", true}, new Object[] {"is_IS", true}, new Object[] {"is", true}, new Object[] {"it_CH", true}, new Object[] {"it_IT", true},
		new Object[] {"it", true}, new Object[] {"iu", true}, new Object[] {"iw", true}, new Object[] {"ja_JP", true}, new Object[] {"ja", true},
		new Object[] {"ka_GE", true}, new Object[] {"ka", true}, new Object[] {"kaj_NG", true}, new Object[] {"kaj", true}, new Object[] {"kam_KE", true},
		new Object[] {"kam", true}, new Object[] {"kcg_NG", true}, new Object[] {"kcg", true}, new Object[] {"kfo_CI", true}, new Object[] {"kfo", true},
		new Object[] {"kk_KZ", true}, new Object[] {"kk", true}, new Object[] {"kl_GL", true}, new Object[] {"kl", true}, new Object[] {"km_KH", true},
		new Object[] {"km", true}, new Object[] {"kn_IN", true}, new Object[] {"kn", true}, new Object[] {"ko_KR", true}, new Object[] {"ko", true},
		new Object[] {"kok_IN", true}, new Object[] {"kok", true}, new Object[] {"kpe_GN", true}, new Object[] {"kpe_LR", true}, new Object[] {"kpe", true},
		new Object[] {"ku_IQ", true}, new Object[] {"ku_IR", true}, new Object[] {"ku_SY", true}, new Object[] {"ku_TR", true}, new Object[] {"ku", true},
		new Object[] {"kw_GB", true}, new Object[] {"kw", true}, new Object[] {"ky_KG", true},
		new Object[] {"ky", true}, new Object[] {"ln_CD", true}, new Object[] {"ln_CG", true}, new Object[] {"ln", true}, new Object[] {"lo_LA", true},
		new Object[] {"lo", true}, new Object[] {"lt_LT", true}, new Object[] {"lt", true}, new Object[] {"lv_LV", true}, new Object[] {"lv", true},
		new Object[] {"mk_MK", true}, new Object[] {"mk", true}, new Object[] {"ml_IN", true}, new Object[] {"ml", true}, new Object[] {"mn_CN", true},
		new Object[] {"mn_MN", true}, new Object[] {"mn", true}, new Object[] {"mo", true}, new Object[] {"mr_IN", true}, new Object[] {"mr", true}, new Object[] {"ms_BN", true},
		new Object[] {"ms_MY", true}, new Object[] {"ms", true}, new Object[] {"mt_MT", true}, new Object[] {"mt", true}, new Object[] {"my_MM", true}, new Object[] {"my", true},
		new Object[] {"nb_NO", true}, new Object[] {"nb", true}, new Object[] {"nds_DE", true}, new Object[] {"nds", true}, new Object[] {"ne_IN", true}, new Object[] {"ne_NP", true},
		new Object[] {"ne", true}, new Object[] {"nl_BE", true}, new Object[] {"nl_NL", true}, new Object[] {"nl", true}, new Object[] {"nn_NO", true},
		new Object[] {"nn", true}, new Object[] {"no", true}, new Object[] {"nr_ZA", true}, new Object[] {"nr", true}, new Object[] {"nso_ZA", true},
		new Object[] {"nso", true}, new Object[] {"ny_MW", true}, new Object[] {"ny", true}, new Object[] {"oc_FR", true}, new Object[] {"oc", true},
		new Object[] {"om_ET", true}, new Object[] {"om_KE", true}, new Object[] {"om", true}, new Object[] {"or_IN", true}, new Object[] {"or", true},
		new Object[] {"pa_IN", true}, new Object[] {"pa_PK", true}, new Object[] {"pa", true}, new Object[] {"pl_PL", true}, new Object[] {"pl", true},
		new Object[] {"ps_AF", true}, new Object[] {"ps", true}, new Object[] {"pt_BR", true}, new Object[] {"pt_PT", true}, new Object[] {"pt", true},
		new Object[] {"ro_MD", true}, new Object[] {"ro_RO", true}, new Object[] {"ro", true}, new Object[] {"ru_RU", true}, new Object[] {"ru_UA", true},
		new Object[] {"ru", true}, new Object[] {"rw_RW", true}, new Object[] {"rw", true}, new Object[] {"sa_IN", true}, new Object[] {"sa", true},
		new Object[] {"se_FI", true}, new Object[] {"se_NO", true}, new Object[] {"se", true}, new Object[] {"sh_BA", true}, new Object[] {"sh_CS", true},
		new Object[] {"sh_YU", true}, new Object[] {"sh", true}, new Object[] {"si_LK", true}, new Object[] {"si", true}, new Object[] {"sid_ET", true},
		new Object[] {"sid", true}, new Object[] {"sk_SK", true}, new Object[] {"sk", true}, new Object[] {"sl_SI", true}, new Object[] {"sl", true},
		new Object[] {"so_DJ", true}, new Object[] {"so_ET", true}, new Object[] {"so_KE", true}, new Object[] {"so_SO", true}, new Object[] {"so", true},
		new Object[] {"sq_AL", true}, new Object[] {"sq", true}, new Object[] {"sr_BA", true}, new Object[] {"sr_CS", true}, new Object[] {"sr_ME", true},
		new Object[] {"sr_RS", true}, new Object[] {"sr_YU", true}, new Object[] {"sr", true}, new Object[] {"ss_SZ", true}, new Object[] {"ss_ZA", true},
		new Object[] {"ss", true}, new Object[] {"st_LS", true}, new Object[] {"st_ZA", true}, new Object[] {"st", true}, new Object[] {"sv_FI", true},
		new Object[] {"sv_SE", true}, new Object[] {"sv", true}, new Object[] {"sw_KE", true}, new Object[] {"sw_TZ", true}, new Object[] {"sw", true},
		new Object[] {"syr_SY", true}, new Object[] {"syr", true}, new Object[] {"ta_IN", true}, new Object[] {"ta", true}, new Object[] {"te_IN", true},
		new Object[] {"te", true}, new Object[] {"tg_TJ", true}, new Object[] {"tg", true}, new Object[] {"th_TH", true}, new Object[] {"th", true},
		new Object[] {"ti_ER", true}, new Object[] {"ti_ET", true}, new Object[] {"ti", true}, new Object[] {"tig_ER", true}, new Object[] {"tig", true},
		new Object[] {"tl", true}, new Object[] {"tn_ZA", true}, new Object[] {"tn", true}, new Object[] {"to_TO", true}, new Object[] {"to", true},
		new Object[] {"tr_TR", true}, new Object[] {"tr", true}, new Object[] {"trv_TW", true}, new Object[] {"trv", true}, new Object[] {"ts_ZA", true},
		new Object[] {"ts", true}, new Object[] {"tt_RU", true}, new Object[] {"tt", true}, new Object[] {"ug_CN", true}, new Object[] {"ug", true},
		new Object[] {"uk_UA", true}, new Object[] {"uk", true}, new Object[] {"ur_IN", true}, new Object[] {"ur_PK", true}, new Object[] {"ur", true},
		new Object[] {"uz_AF", true}, new Object[] {"uz_UZ", true}, new Object[] {"uz", true}, new Object[] {"ve_ZA", true}, new Object[] {"ve", true},
		new Object[] {"vi_VN", true}, new Object[] {"vi", true}, new Object[] {"wal_ET", true}, new Object[] {"wal", true}, new Object[] {"wo_SN", true},
		new Object[] {"wo", true}, new Object[] {"xh_ZA", true}, new Object[] {"xh", true}, new Object[] {"yo_NG", true}, new Object[] {"yo", true},
		new Object[] {"zh_CN", true}, new Object[] {"zh_HK", true}, new Object[] {"zh_MO", true}, new Object[] {"zh_SG", true}, new Object[] {"zh_TW", true},
		new Object[] {"zh", true}, new Object[] {"zu_ZA", true}, new Object[] {"zu", true});

	private static Map<String, String> _territoryData = MageAtomArray.createMap(new Object[] {"AD", "ca_AD"}, new Object[] {"AE", "ar_AE"}, new Object[] {"AF", "fa_AF"},
		new Object[] {"AG", "en_AG"}, new Object[] {"AI", "en_AI"}, new Object[] {"AL", "sq_AL"}, new Object[] {"AM", "hy_AM"}, new Object[] {"AN", "pap_AN"},
		new Object[] {"AO", "pt_AO"}, new Object[] {"AQ", "und_AQ"}, new Object[] {"AR", "es_AR"}, new Object[] {"AS", "sm_AS"}, new Object[] {"AT", "de_AT"},
		new Object[] {"AU", "en_AU"}, new Object[] {"AW", "nl_AW"}, new Object[] {"AX", "sv_AX"}, new Object[] {"AZ", "az_Latn_AZ"}, new Object[] {"BA", "bs_BA"},
		new Object[] {"BB", "en_BB"}, new Object[] {"BD", "bn_BD"}, new Object[] {"BE", "nl_BE"}, new Object[] {"BF", "mos_BF"}, new Object[] {"BG", "bg_BG"},
		new Object[] {"BH", "ar_BH"}, new Object[] {"BI", "rn_BI"}, new Object[] {"BJ", "fr_BJ"}, new Object[] {"BL", "fr_BL"}, new Object[] {"BM", "en_BM"}, new Object[] {"BN", "ms_BN"},
		new Object[] {"BO", "es_BO"}, new Object[] {"BR", "pt_BR"}, new Object[] {"BS", "en_BS"}, new Object[] {"BT", "dz_BT"}, new Object[] {"BV", "und_BV"},
		new Object[] {"BW", "en_BW"}, new Object[] {"BY", "be_BY"}, new Object[] {"BZ", "en_BZ"}, new Object[] {"CA", "en_CA"}, new Object[] {"CC", "ms_CC"},
		new Object[] {"CD", "sw_CD"}, new Object[] {"CF", "fr_CF"}, new Object[] {"CG", "fr_CG"}, new Object[] {"CH", "de_CH"}, new Object[] {"CI", "fr_CI"}, new Object[] {"CK", "en_CK"},
		new Object[] {"CL", "es_CL"}, new Object[] {"CM", "fr_CM"}, new Object[] {"CN", "zh_Hans_CN"}, new Object[] {"CO", "es_CO"}, new Object[] {"CR", "es_CR"},
		new Object[] {"CU", "es_CU"}, new Object[] {"CV", "kea_CV"}, new Object[] {"CX", "en_CX"}, new Object[] {"CY", "el_CY"}, new Object[] {"CZ", "cs_CZ"},
		new Object[] {"DE", "de_DE"}, new Object[] {"DJ", "aa_DJ"}, new Object[] {"DK", "da_DK"}, new Object[] {"DM", "en_DM"}, new Object[] {"DO", "es_DO"}, new Object[] {"DZ", "ar_DZ"},
		new Object[] {"EC", "es_EC"}, new Object[] {"EE", "et_EE"}, new Object[] {"EG", "ar_EG"}, new Object[] {"EH", "ar_EH"}, new Object[] {"ER", "ti_ER"},
		new Object[] {"ES", "es_ES"}, new Object[] {"ET", "en_ET"}, new Object[] {"FI", "fi_FI"}, new Object[] {"FJ", "hi_FJ"}, new Object[] {"FK", "en_FK"},
		new Object[] {"FM", "chk_FM"}, new Object[] {"FO", "fo_FO"}, new Object[] {"FR", "fr_FR"}, new Object[] {"GA", "fr_GA"}, new Object[] {"GB", "en_GB"}, new Object[] {"GD", "en_GD"},
		new Object[] {"GE", "ka_GE"}, new Object[] {"GF", "fr_GF"}, new Object[] {"GG", "en_GG"}, new Object[] {"GH", "ak_GH"}, new Object[] {"GI", "en_GI"},
		new Object[] {"GL", "iu_GL"}, new Object[] {"GM", "en_GM"}, new Object[] {"GN", "fr_GN"}, new Object[] {"GP", "fr_GP"}, new Object[] {"GQ", "fan_GQ"},
		new Object[] {"GR", "el_GR"}, new Object[] {"GS", "und_GS"}, new Object[] {"GT", "es_GT"}, new Object[] {"GU", "en_GU"}, new Object[] {"GW", "pt_GW"}, new Object[] {"GY", "en_GY"},
		new Object[] {"HK", "zh_Hant_HK"}, new Object[] {"HM", "und_HM"}, new Object[] {"HN", "es_HN"}, new Object[] {"HR", "hr_HR"}, new Object[] {"HT", "ht_HT"},
		new Object[] {"HU", "hu_HU"}, new Object[] {"ID", "id_ID"}, new Object[] {"IE", "en_IE"}, new Object[] {"IL", "he_IL"}, new Object[] {"IM", "en_IM"},
		new Object[] {"IN", "hi_IN"}, new Object[] {"IO", "und_IO"}, new Object[] {"IQ", "ar_IQ"}, new Object[] {"IR", "fa_IR"}, new Object[] {"IS", "is_IS"}, new Object[] {"IT", "it_IT"},
		new Object[] {"JE", "en_JE"}, new Object[] {"JM", "en_JM"}, new Object[] {"JO", "ar_JO"}, new Object[] {"JP", "ja_JP"}, new Object[] {"KE", "en_KE"},
		new Object[] {"KG", "ky_Cyrl_KG"}, new Object[] {"KH", "km_KH"}, new Object[] {"KI", "en_KI"}, new Object[] {"KM", "ar_KM"}, new Object[] {"KN", "en_KN"},
		new Object[] {"KP", "ko_KP"}, new Object[] {"KR", "ko_KR"}, new Object[] {"KW", "ar_KW"}, new Object[] {"KY", "en_KY"}, new Object[] {"KZ", "ru_KZ"}, new Object[] {"LA", "lo_LA"},
		new Object[] {"LB", "ar_LB"}, new Object[] {"LC", "en_LC"}, new Object[] {"LI", "de_LI"}, new Object[] {"LK", "si_LK"}, new Object[] {"LR", "en_LR"},
		new Object[] {"LS", "st_LS"}, new Object[] {"LT", "lt_LT"}, new Object[] {"LU", "fr_LU"}, new Object[] {"LV", "lv_LV"}, new Object[] {"LY", "ar_LY"},
		new Object[] {"MA", "ar_MA"}, new Object[] {"MC", "fr_MC"}, new Object[] {"MD", "ro_MD"}, new Object[] {"ME", "sr_Latn_ME"}, new Object[] {"MF", "fr_MF"}, new Object[] {"MG", "mg_MG"},
		new Object[] {"MH", "mh_MH"}, new Object[] {"MK", "mk_MK"}, new Object[] {"ML", "bm_ML"}, new Object[] {"MM", "my_MM"}, new Object[] {"MN", "mn_Cyrl_MN"},
		new Object[] {"MO", "zh_Hant_MO"}, new Object[] {"MP", "en_MP"}, new Object[] {"MQ", "fr_MQ"}, new Object[] {"MR", "ar_MR"}, new Object[] {"MS", "en_MS"},
		new Object[] {"MT", "mt_MT"}, new Object[] {"MU", "mfe_MU"}, new Object[] {"MV", "dv_MV"}, new Object[] {"MW", "ny_MW"}, new Object[] {"MX", "es_MX"},
		new Object[] {"MY", "ms_MY"}, new Object[] {"MZ", "pt_MZ"}, new Object[] {"NA", "kj_NA"}, new Object[] {"NC", "fr_NC"},
		new Object[] {"NE", "ha_Latn_NE"}, new Object[] {"NF", "en_NF"}, new Object[] {"NG", "en_NG"}, new Object[] {"NI", "es_NI"}, new Object[] {"NL", "nl_NL"},
		new Object[] {"NO", "nb_NO"}, new Object[] {"NP", "ne_NP"}, new Object[] {"NR", "en_NR"}, new Object[] {"NU", "niu_NU"}, new Object[] {"NZ", "en_NZ"},
		new Object[] {"OM", "ar_OM"}, new Object[] {"PA", "es_PA"}, new Object[] {"PE", "es_PE"}, new Object[] {"PF", "fr_PF"}, new Object[] {"PG", "tpi_PG"},
		new Object[] {"PH", "fil_PH"}, new Object[] {"PK", "ur_PK"}, new Object[] {"PL", "pl_PL"}, new Object[] {"PM", "fr_PM"}, new Object[] {"PN", "en_PN"},
		new Object[] {"PR", "es_PR"}, new Object[] {"PS", "ar_PS"}, new Object[] {"PT", "pt_PT"}, new Object[] {"PW", "pau_PW"}, new Object[] {"PY", "gn_PY"},
		new Object[] {"QA", "ar_QA"}, new Object[] {"RE", "fr_RE"}, new Object[] {"RO", "ro_RO"}, new Object[] {"RS", "sr_Cyrl_RS"}, new Object[] {"RU", "ru_RU"},
		new Object[] {"RW", "rw_RW"}, new Object[] {"SA", "ar_SA"}, new Object[] {"SB", "en_SB"}, new Object[] {"SC", "crs_SC"}, new Object[] {"SD", "ar_SD"},
		new Object[] {"SE", "sv_SE"}, new Object[] {"SG", "en_SG"}, new Object[] {"SH", "en_SH"}, new Object[] {"SI", "sl_SI"}, new Object[] {"SJ", "nb_SJ"},
		new Object[] {"SK", "sk_SK"}, new Object[] {"SL", "kri_SL"}, new Object[] {"SM", "it_SM"}, new Object[] {"SN", "fr_SN"}, new Object[] {"SO", "sw_SO"},
		new Object[] {"SR", "srn_SR"}, new Object[] {"ST", "pt_ST"}, new Object[] {"SV", "es_SV"}, new Object[] {"SY", "ar_SY"}, new Object[] {"SZ", "en_SZ"},
		new Object[] {"TC", "en_TC"}, new Object[] {"TD", "fr_TD"}, new Object[] {"TF", "und_TF"}, new Object[] {"TG", "fr_TG"}, new Object[] {"TH", "th_TH"},
		new Object[] {"TJ", "tg_Cyrl_TJ"}, new Object[] {"TK", "tkl_TK"}, new Object[] {"TL", "pt_TL"}, new Object[] {"TM", "tk_TM"}, new Object[] {"TN", "ar_TN"},
		new Object[] {"TO", "to_TO"}, new Object[] {"TR", "tr_TR"}, new Object[] {"TT", "en_TT"}, new Object[] {"TV", "tvl_TV"}, new Object[] {"TW", "zh_Hant_TW"},
		new Object[] {"TZ", "sw_TZ"}, new Object[] {"UA", "uk_UA"}, new Object[] {"UG", "sw_UG"}, new Object[] {"UM", "en_UM"}, new Object[] {"US", "en_US"},
		new Object[] {"UY", "es_UY"}, new Object[] {"UZ", "uz_Cyrl_UZ"}, new Object[] {"VA", "it_VA"}, new Object[] {"VC", "en_VC"}, new Object[] {"VE", "es_VE"},
		new Object[] {"VG", "en_VG"}, new Object[] {"VI", "en_VI"}, new Object[] {"VU", "bi_VU"}, new Object[] {"WF", "wls_WF"}, new Object[] {"WS", "sm_WS"},
		new Object[] {"YE", "ar_YE"}, new Object[] {"YT", "swb_YT"}, new Object[] {"ZA", "en_ZA"}, new Object[] {"ZM", "en_ZM"}, new Object[] {"ZW", "sn_ZW"});

	/**
	 * Autosearch constants
	 */
	public static final String BROWSER = "browser";
	public static final String ENVIRONMENT = "environment";
	public static final String ZFDEFAULT = "default";

	/**
	 * Defines if old behaviour should be supported
	 * Old behaviour throws notices and will be deleted in future releases
	 */
	@Deprecated
	public static boolean compatibilityMode = false;

	/**
	 * Internal variable
	 */
	private static boolean _breakChain = false;

	/**
	 * Actual set locale
	 */
	protected String _locale;

	/**
	 * Automatic detected locale
	 */
	protected static Map<String, Float> _auto;

	/**
	 * Browser detected locale
	 */
	protected static Map<String, Float> _browser;

	/**
	 * Environment detected locale
	 */
	protected static Map<String, Float> _environment;

	/**
	 * Default locale
	 */
	protected static Map<String, Float> _default = Collections.singletonMap("en", 1f);

	/**
	 * Generates a locale objectIf no locale is given a automatic search is doneThen the most probable locale will be automatically setSearch order is1. Given Locale2. HTTP Client3. Server Environment4. Framework Standard
	 * 
	 * @param locale input 
	 */
	public Zend_Locale(String locale/* = null */) {
		this.setLocale(locale);
	}

	/**
	 * Serialization Interface
	 * 
	 * @return string
	 */
	//	public String serialize() {
	//		return serialize(this);
	//	}

	/**
	 * Returns a string representation of the object
	 * 
	 * @return string
	 */
	@Override
	public String toString() {
		return this._locale;
	}

	/**
	 * Returns a string representation of the objectAlias for toString
	 * 
	 * @return string
	 */
	public String __toString() {
		return this.toString();
	}

	/**
	 * Return the default locale
	 * 
	 * @return array  Returns an array of all locale string
	 */
	public static Map<String, Float> getDefault(Object... params) {
		if (compatibilityMode || params.length > 0) {
			if (!(_breakChain)) {
				_breakChain = true;
				LOG.info("You are running Zend_Locale in compatibility mode... please migrate your scripts");
				String param = null;
				if (params.length > 0) {
					param = (String)params[0];
				}

				return getOrder(param);
			}

			_breakChain = false;
		}

		return _default;
	}

	/**
	 * Sets a new default locale which will be used when no locale can be detectedIf provided you can set a quality between 0 and 1 (or 2 and 100)which represents the percent of quality the browserrequested within HTTP
	 * 
	 * @param locale set 
	 * @param  1 
	 * @return void
	 */
	public static void setDefault(String locale, float quality/* = 1 */) {
		String[] elocale;
		if ("auto".equals(locale) || "root".equals(locale) || "default".equals(locale) || "environment".equals(locale) || "browser".equals(locale)) {
			throw new Zend_Locale_Exception("Only full qualified locales can be used as default!");
		}

		if ((quality < 0.1) || (quality > 100)) {
			throw new Zend_Locale_Exception("Quality must be between 0.1 and 100");
		}

		if (quality > 1) {
			quality /= 100;
		}

		locale = _prepareLocale(locale, false);
		if (_localeData.get(locale) != null) {
			_default = Collections.singletonMap(locale, quality);
		} else {
			elocale = locale.split("_");
			if (_localeData.get(elocale[0]) != null) {
				_default = Collections.singletonMap(elocale[0], quality);
			} else {
				throw new Zend_Locale_Exception("Unknown locale '" + (locale) + "' can not be set as default!");
			}

		}

		_auto = MageAtomArray.merge(getBrowser(), getEnvironment(), getDefault());
	}

	/**
	 * Expects the Systems standard localeFor Windows:f.e.: LC_COLLATE=C;LC_CTYPE=German_Austria.1252;LC_MONETARY=Cwould be recognised as de_AT
	 * 
	 * @return array
	 */
	public static Map<String, Float> getEnvironment() {
		if (_environment != null) {
			return _environment;
		}

		//		String language = setlocale(Flags.LC_ALL, 0);
		String language = Locale.getDefault().getLanguage();
		String[] languages = language.split(";");
		Map<String, Float> languagearray = new HashMap<String, Float>();
		for (String locale : languages) {
			if (locale.contains("=")) {
				language = locale.substring(locale.indexOf("=") + 1);
			}

			if (!"C".equals(language)) {
				if (language.contains(".")) {
					language = language.substring(0, language.indexOf("."));
				} else {
					if (language.contains("@")) {
						language = language.substring(0, language.indexOf("@"));
					}
				}

				language = PhpStringUtils.replaceEachIgnoreCase(language, Zend_Locale_Data_Translation.languageTranslation);
				language = PhpStringUtils.replaceEachIgnoreCase(language, Zend_Locale_Data_Translation.regionTranslation);
				if ((_localeData.get(language) != null) == true) {
					languagearray.put(language, 1.0f);
					if (language.contains("_")) {
						languagearray.put(language.substring(0, language.indexOf("_")), 1.0f);
					}
				}
			}
		}

		_environment = languagearray;
		return languagearray;
	}

	/**
	 * Return an array of all accepted languages of the client
	 * Expects RFC compilant Header !!
	 * The notation can be :de,en-UK-US;q=0.5,fr-FR;q=0.2
	 * 
	 * @return array  - list of accepted languages including quality
	 */
	public static Map<String, Float> getBrowser() {
		if (_browser != null) {
			return _browser;
		}

		String httplanguages = System.getenv("HTTP_ACCEPT_LANGUAGE");
		if (httplanguages == null) {
			String httpAcceptLanguage = AppContext.getCurrent().getRequest().getServer("HTTP_ACCEPT_LANGUAGE", null);
			if (httpAcceptLanguage != null) {
				httplanguages = httpAcceptLanguage;
			}
		}

		Map<String, Float> languages = new HashMap<String, Float>();
		if (StringUtils.isEmpty(httplanguages)) {
			return languages;
		}

		String[] accepted = httplanguages.split(",\\s*");
		for (String accept : accepted) {
			List<String> match = new ArrayList<String>();
			if (!RegexUtils.match("(?i)^([a-z]{1,8}(?:[-_][a-z]{1,8})*)(?:;\\s*q=(0(?:\\.[0-9]{1,3})?|1(?:\\.0{1,3})?))?$", accept, match)) {
				continue;
			}

			float quality;
			if (match.size() > 2 && match.get(2) != null) {
				quality = Float.parseFloat(match.get(2));
			} else {
				quality = 1.0f;
			}

			String[] countrys = match.get(1).split("-");
			String region = countrys[0];
			String[] country2 = region.split("_");
			region = country2[0];
			for (int i = 1; i < countrys.length; i++) {
				languages.put(region + "_" + countrys[i].toUpperCase(), quality);
			}

			for (int i = 1; i < country2.length; i++) {
				languages.put(region + "_" + country2[i].toUpperCase(), quality);
			}

			Float regionQuality = languages.get(region);

			if (regionQuality == null || regionQuality < quality) {
				languages.put(region, quality);
			}

		}

		_browser = languages;
		return languages;
	}

	/**
	 * Sets a new locale
	 * 
	 * @param locale set 
	 * @return void
	 */
	public void setLocale(String locale/* = null */) {
		locale = _prepareLocale(locale, false);
		if (_localeData.get(locale) == null) {
			String region;
			if (locale.length() > 2) {
				region = locale.substring(0, 3);
				if ((region.charAt(2) == '_') || (region.charAt(2) == '-')) {
					region = region.substring(0, 2);
				}
			} else {
				region = locale;
			}

			if (_localeData.get(region) != null) {
				this._locale = region;
			} else {
				this._locale = "root";
			}
		} else {
			this._locale = locale;
		}

	}

	/**
	 * Returns the language part of the locale
	 * 
	 * @return string
	 */
	public String getLanguage() {
		String[] locale = this._locale.split("_");
		return locale[0];
	}

	/**
	 * Returns the region part of the locale if available
	 * 
	 * @return string|false  - Regionstring
	 */
	public String getRegion() {
		String[] locale = this._locale.split("_");
		if (locale.length > 1) {
			return locale[1];
		}

		return null;
	}

	/**
	 * Return the accepted charset of the client
	 * 
	 * @return string
	 */
	public static Map<String, Float> getHttpCharset() {
		String httpcharsets = System.getenv("HTTP_ACCEPT_CHARSET");
		Map<String, Float> charsets = new HashMap<String, Float>();
		if (httpcharsets == null) {
			return charsets;
		}

		String[] accepted = httpcharsets.split(",\\s*");
		for (String accept : accepted) {
			if (accept == null) {
				continue;
			}

			if (accept.contains(";")) {
				float quality = Float.parseFloat(accept.substring(accept.indexOf("=") + 1));
				String pos = accept.substring(0, accept.indexOf(";"));
				charsets.put(pos, quality);
			} else {
				charsets.put(accept, 1.0f);
			}
		}

		return charsets;
	}

	/**
	 * Returns true if both locales are equal
	 * 
	 * @param object equality 
	 * @return boolean
	 */
	public boolean equals(Zend_Locale object) {
		return toString().equals(object.toString());
	}

	/**
	 * Returns localized informations as array, supported are severaltypes of informations.For detailed information about the types look into the documentation
	 * 
	 * @param  return 
	 * @param locale returned 
	 * @param  list 
	 * @return array  Array with the wished information in the given language
	 */
	public static <T> Map<String, T> getTranslationList(String path/* = null */, String locale/* = null */, Object value/* = null */) {
		locale = findLocale(locale);
		return Zend_Locale_Data.getList(locale, path, value);
	}

	public static <T> Map<String, T> getTranslationList(String path/* = null */, Zend_Locale locale/* = null */, Object value/* = null */) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns an array with the name of all languages translated to the given language
	 * 
	 * @param locale translation 
	 * @return array
	 */
	public static <T> Map<String, T> getLanguageTranslationList(String locale/* = null */) {
		LOG.info("The method getLanguageTranslationList is deprecated. Use getTranslationList('language', $locale) instead");
		return getTranslationList("language", locale, null);
	}

	/**
	 * Returns an array with the name of all scripts translated to the given language
	 * 
	 * @param locale translation 
	 * @return array
	 */
	public static <T> Map<String, T> getScriptTranslationList(String locale/* = null */) {
		LOG.info("The method getScriptTranslationList is deprecated. Use getTranslationList('script', $locale) instead");
		return getTranslationList("script", locale, null);
	}

	/**
	 * Returns an array with the name of all countries translated to the given language
	 * 
	 * @param locale translation 
	 * @return array
	 */
	public static <T> Map<String, T> getCountryTranslationList(String locale/* = null */) {
		LOG.info("The method getCountryTranslationList is deprecated. Use getTranslationList('territory', $locale, 2) instead");
		return getTranslationList("territory", locale, 2);
	}

	/**
	 * Returns an array with the name of all territories translated to the given languageAll territories contains other countries.
	 * 
	 * @param locale translation 
	 * @return array
	 */
	public static <T> Map<String, T> getTerritoryTranslationList(String locale/* = null */) {
		LOG.info("The method getTerritoryTranslationList is deprecated. Use getTranslationList('territory', $locale, 1) instead");
		return getTranslationList("territory", locale, 1);
	}

	/**
	 * Returns a localized information string, supported are several types of informations.
	 * For detailed information about the types look into the documentation
	 * 
	 * @param  about 
	 * @param  return 
	 * @param locale returned 
	 * @return string|false  The wished information in the given language
	 */
	public static String getTranslation(Object value/* = null */, String path/* = null */, String locale/* = null */) {
		locale = findLocale(locale);
		String result = Zend_Locale_Data.getContent(locale, path, value);
		if (StringUtils.isEmpty(result) && !"0".equals(result)) {
			return null;
		}

		return result;
	}

	public static String getTranslation(Object value/* = null */, String path/* = null */, Zend_Locale locale/* = null */) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the localized language name
	 * 
	 * @param value about 
	 * @param locale translation 
	 * @return array
	 */
	public static String getLanguageTranslation(Object value, String locale/* = null */) {
		LOG.info("The method getLanguageTranslation is deprecated. Use getTranslation($value, 'language', $locale) instead");
		return getTranslation(value, "language", locale);
	}

	/**
	 * Returns the localized script name
	 * 
	 * @param value about 
	 * @param locale translation 
	 * @return array
	 */
	public static String getScriptTranslation(Object value, String locale/* = null */) {
		LOG.info("The method getScriptTranslation is deprecated. Use getTranslation($value, 'script', $locale) instead");
		return getTranslation(value, "script", locale);
	}

	/**
	 * Returns the localized country name
	 * 
	 * @param  about 
	 * @param locale translation 
	 * @return array
	 */
	public static String getCountryTranslation(Object value, String locale/* = null */) {
		LOG.info("The method getCountryTranslation is deprecated. Use getTranslation($value, 'country', $locale) instead");
		return getTranslation(value, "country", locale);
	}

	/**
	 * Returns the localized territory nameAll territories contains other countries.
	 * 
	 * @param  about 
	 * @param locale translation 
	 * @return array
	 */
	public static String getTerritoryTranslation(Object value, String locale/* = null */) {
		LOG.info("The method getTerritoryTranslation is deprecated. Use getTranslation($value, 'territory', $locale) instead");
		return getTranslation(value, "territory", locale);
	}

	/**
	 * Returns an array with translated yes strings
	 * 
	 * @param locale locale) 
	 * @return array
	 */
	public static Map<String, Object> getQuestion(String locale/* = null */) {
		locale = findLocale(locale);
		Map<String, Object> quest = Zend_Locale_Data.getList(locale, "question", false);
		String[] yes = StringUtils.split((String)quest.get("yes"), ":");
		String[] no = StringUtils.split((String)quest.get("no"), ":");
		quest.put("yes", yes[0]);
		quest.put("yesarray", yes);
		quest.put("no", no[0]);
		quest.put("noarray", no);
		quest.put("yesexpr", _prepareQuestionString(yes));
		quest.put("noexpr", _prepareQuestionString(no));
		return quest;
	}

	/**
	 * Internal function for preparing the returned question regex string
	 * 
	 * @param input parse 
	 * @return string
	 */
	private static String _prepareQuestionString(String[] input) {
		StringBuilder regex = new StringBuilder(input.length * 3);
		regex.append("^");
		boolean start = true;
		for (String row : input) {
			if (start == false) {
				regex.append("|");
			}

			start = false;
			regex.append("(");
			boolean one = false;
			if (row.length() > 2) {
				one = true;
			}

			for (char ch : row.toCharArray()) {
				regex.append("[" + ch);
				regex.append(Character.toUpperCase(ch) + "]");
				if (one == true) {
					one = false;
					regex.append("(");
				}

			}

			if (one == false) {
				regex.append(")");
			}

			regex.append("?)");
		}

		return regex.toString();
	}

	/**
	 * Checks if a locale identifier is a real locale or notExamples:"en_XX" refers to "en", which returns true"XX_yy" refers to "root", which returns false
	 * 
	 * @param locale for 
	 * @param  checking 
	 * @param  mode 
	 * @return boolean  If the locale is known dependend on the settings
	 */
	public static boolean isLocale(String locale, boolean strict/* = false */, boolean compatible/* = true */) {
		if (locale == null) {
			return false;
		}

		if (_localeData.containsKey(locale)) {
			return true;
		}

		try {
			locale = _prepareLocale(locale, strict);
		} catch (Zend_Locale_Exception e) {
			return false;
		}

		//		if (compatible && compatibilityMode) {
		//			LOG.info("You are running Zend_Locale in compatibility mode... please migrate your scripts");
		//			if (_localeData.containsKey(locale)) {
		//				return locale;
		//			} else {
		//				if (!(strict)) {
		//					String[] tokens = locale.split("_");
		//					if (_localeData.containsKey(tokens[0])) {
		//						return tokens[0];
		//					}
		//				}
		//			}
		//		} else {
		if (_localeData.containsKey(locale)) {
			return true;
		} else {
			if (!(strict)) {
				String[] tokens = locale.split("_");
				if (_localeData.containsKey(tokens[0])) {
					return true;
				}
			}
		}
		//		}

		return false;
	}

	/**
	 * Finds the proper locale based on the input
	 * Checks if it exists, degrades it when necessary
	 * Detects registry locale and when all fails tries to detect a automatic locale
	 * Returns the found locale as string
	 * 
	 * @param locale
	 * @return string
	 */
	public static String findLocale(String locale/* = null */) {
		//		if (locale == null) {
		//			if (Zend_Registry.isRegistered("Zend_Locale")) {
		//				locale = Zend_Registry.get("Zend_Locale");
		//			}
		//		}

		//		if (locale == null) {
		//			locale = new Zend_Locale();
		//		}
		Zend_Locale result = new Zend_Locale(null);

		if (!Zend_Locale.isLocale(locale, true, false)) {
			if (!Zend_Locale.isLocale(locale, false, false)) {
				String territory = Zend_Locale.getLocaleToTerritory(locale);
				if (territory == null) {
					throw new Zend_Locale_Exception("The locale '$locale' is no known locale");
				}
			} else {
				result = new Zend_Locale(locale);
			}
		}

		return result.toString();
	}

	/**
	 * Returns the expected locale for a given territory
	 * 
	 * @param territory searched 
	 * @return string|null  Locale string or null when no locale has been found
	 */
	public static String getLocaleToTerritory(String territory) {
		return _territoryData.get(territory.toUpperCase());
	}

	/**
	 * Returns a list of all known locales where the locale is the keyOnly real locales are returned, the internal locales 'root', 'auto', 'browser'and 'environment' are suppressed
	 * 
	 * @return array  List of all Locales
	 */
	public static Map<String, ?> getLocaleList() {
		Map<String, ?> list = new HashMap<>(_localeData);
		list.remove("root");
		list.remove("auto");
		list.remove("browser");
		list.remove("environment");

		return list;
	}

	/**
	 * Returns the set cache
	 * 
	 * @return Zend_Cache_Core  The set cache
	 */
	public static Zend_Cache_Core getCache() {
		return Zend_Locale_Data.getCache();
	}

	/**
	 * Sets a cache
	 * 
	 * @param cache set 
	 * @return void
	 */
	public static void setCache(Zend_Cache_Core cache) {
		Zend_Locale_Data.setCache(cache);
	}

	/**
	 * Returns true when a cache is set
	 * 
	 * @return boolean
	 */
	public static boolean hasCache() {
		return Zend_Locale_Data.hasCache();
	}

	/**
	 * Removes any set cache
	 * 
	 * @return void
	 */
	public static void removeCache() {
		Zend_Locale_Data.removeCache();
	}

	/**
	 * Clears all set cache data
	 * 
	 * @param tag used 
	 * @return void
	 */
	public static void clearCache(String tag/* = null */) {
		Zend_Locale_Data.clearCache(/*tag*/);
	}

	/**
	 * Disables the set cache
	 * 
	 * @param flag false 
	 * @return void
	 */
	public static void disableCache(boolean flag) {
		Zend_Locale_Data.disableCache(flag);
	}

	/**
	 * Internal function, returns a single locale on detection
	 * 
	 * @param locale on 
	 * @param  preparation 
	 * @return string
	 */
	private static String _prepareLocale(String locale, boolean strict/* = false */) {
		//		if (locale instanceof Zend_Locale) {
		//			locale = locale.toString();
		//		}
		//
		//		if (is_array(locale)) {
		//			return "";
		//		}

		if (_auto.isEmpty()) {
			_browser = getBrowser();
			_environment = getEnvironment();
			_breakChain = true;

			_auto = MageAtomArray.merge(getBrowser(), getEnvironment(), getDefault());
		}

		if (!strict) {
			Map<String, ?> map = null;
			if (locale == null) {
				map = _auto;
			} else {
				switch (locale) {
					case "browser":
						map = _browser;
						break;
					case "environment":
						map = _environment;
						break;
					case "default":
						map = _default;
						break;
					case "auto":
						map = _auto;
						break;
				}
			}

			if (map != null) {
				locale = map.keySet().iterator().next();
			}
		}

		if (locale == null) {
			throw new Zend_Locale_Exception("Autodetection of Locale has been failed!");
		}

		if (locale.contains("-")) {
			locale = locale.replace('-', '_');
		}

		String[] parts = locale.split("_");
		if (_localeData.get(parts[0]) == null) {
			if (parts.length == 1 && _territoryData.containsKey(parts[0])) {
				return _territoryData.get(parts[0]);
			}

			return "";
		}

		List<String> list = new ArrayList<String>(parts.length);
		for (String part : parts) {
			if ((part.length() < 2) || (part.length() > 3)) {
				continue;
			} else {
				list.add(part);
			}
		}

		return StringUtils.join(list, "_");
	}

	/**
	 * Search the locale automatically and return all used localesordered by qualityStandard Searchorder is Browser, Environment, Default
	 * 
	 * @param  Searchorder 
	 * @return array  Returns an array of all detected locales
	 */
	public static Map<String, Float> getOrder(String order/* = null */) {
		Map<String, Float> languages = new LinkedHashMap<String, Float>();
		switch (order) {
			case ENVIRONMENT: {
				_breakChain = true;
				languages.putAll(getEnvironment());
				languages.putAll(getBrowser());
				languages.putAll(getDefault());
				break;
			}
			case ZFDEFAULT: {
				_breakChain = true;
				languages.putAll(getDefault());
				languages.putAll(getEnvironment());
				languages.putAll(getBrowser());
				break;
			}
			default: {
				_breakChain = true;
				languages.putAll(getBrowser());
				languages.putAll(getEnvironment());
				languages.putAll(getDefault());
				break;
			}
		}

		return languages;
	}
}