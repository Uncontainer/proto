package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Salesrule generated by hbm2java
 */
@Entity
@Table(name = "salesrule"
	, catalog = "magento")
public class Salesrule implements java.io.Serializable {

	private Integer ruleId;
	private String name;
	private String description;
	private Date fromDate;
	private Date toDate;
	private int usesPerCustomer;
	private short isActive;
	private String conditionsSerialized;
	private String actionsSerialized;
	private short stopRulesProcessing;
	private short isAdvanced;
	private String productIds;
	private int sortOrder;
	private String simpleAction;
	private BigDecimal discountAmount;
	private BigDecimal discountQty;
	private int discountStep;
	private short simpleFreeShipping;
	private short applyToShipping;
	private int timesUsed;
	private short isRss;
	private short couponType;
	private short useAutoGeneration;
	private int usesPerCoupon;
	private Set<CoreWebsite> coreWebsites = new HashSet<CoreWebsite>(0);
	private Set<SalesruleCoupon> salesruleCoupons = new HashSet<SalesruleCoupon>(0);
	private Set<CustomerGroup> customerGroups = new HashSet<CustomerGroup>(0);
	private Set<SalesruleLabel> salesruleLabels = new HashSet<SalesruleLabel>(0);
	private Set<SalesruleCustomer> salesruleCustomers = new HashSet<SalesruleCustomer>(0);
	private Set<SalesruleProductAttribute> salesruleProductAttributes = new HashSet<SalesruleProductAttribute>(0);

	public Salesrule() {
	}

	public Salesrule(int usesPerCustomer, short isActive, short stopRulesProcessing, short isAdvanced, int sortOrder, BigDecimal discountAmount, int discountStep, short simpleFreeShipping, short applyToShipping, int timesUsed, short isRss, short couponType, short useAutoGeneration, int usesPerCoupon) {
		this.usesPerCustomer = usesPerCustomer;
		this.isActive = isActive;
		this.stopRulesProcessing = stopRulesProcessing;
		this.isAdvanced = isAdvanced;
		this.sortOrder = sortOrder;
		this.discountAmount = discountAmount;
		this.discountStep = discountStep;
		this.simpleFreeShipping = simpleFreeShipping;
		this.applyToShipping = applyToShipping;
		this.timesUsed = timesUsed;
		this.isRss = isRss;
		this.couponType = couponType;
		this.useAutoGeneration = useAutoGeneration;
		this.usesPerCoupon = usesPerCoupon;
	}

	public Salesrule(String name, String description, Date fromDate, Date toDate, int usesPerCustomer, short isActive, String conditionsSerialized, String actionsSerialized, short stopRulesProcessing, short isAdvanced, String productIds, int sortOrder, String simpleAction, BigDecimal discountAmount, BigDecimal discountQty, int discountStep, short simpleFreeShipping, short applyToShipping, int timesUsed, short isRss, short couponType, short useAutoGeneration, int usesPerCoupon,
		Set<CoreWebsite> coreWebsites, Set<SalesruleCoupon> salesruleCoupons, Set<CustomerGroup> customerGroups, Set<SalesruleLabel> salesruleLabels, Set<SalesruleCustomer> salesruleCustomers, Set<SalesruleProductAttribute> salesruleProductAttributes) {
		this.name = name;
		this.description = description;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.usesPerCustomer = usesPerCustomer;
		this.isActive = isActive;
		this.conditionsSerialized = conditionsSerialized;
		this.actionsSerialized = actionsSerialized;
		this.stopRulesProcessing = stopRulesProcessing;
		this.isAdvanced = isAdvanced;
		this.productIds = productIds;
		this.sortOrder = sortOrder;
		this.simpleAction = simpleAction;
		this.discountAmount = discountAmount;
		this.discountQty = discountQty;
		this.discountStep = discountStep;
		this.simpleFreeShipping = simpleFreeShipping;
		this.applyToShipping = applyToShipping;
		this.timesUsed = timesUsed;
		this.isRss = isRss;
		this.couponType = couponType;
		this.useAutoGeneration = useAutoGeneration;
		this.usesPerCoupon = usesPerCoupon;
		this.coreWebsites = coreWebsites;
		this.salesruleCoupons = salesruleCoupons;
		this.customerGroups = customerGroups;
		this.salesruleLabels = salesruleLabels;
		this.salesruleCustomers = salesruleCustomers;
		this.salesruleProductAttributes = salesruleProductAttributes;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "rule_id", unique = true, nullable = false)
	public Integer getRuleId() {
		return this.ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", length = 65535)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "from_date", length = 10)
	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "to_date", length = 10)
	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	@Column(name = "uses_per_customer", nullable = false)
	public int getUsesPerCustomer() {
		return this.usesPerCustomer;
	}

	public void setUsesPerCustomer(int usesPerCustomer) {
		this.usesPerCustomer = usesPerCustomer;
	}

	@Column(name = "is_active", nullable = false)
	public short getIsActive() {
		return this.isActive;
	}

	public void setIsActive(short isActive) {
		this.isActive = isActive;
	}

	@Column(name = "conditions_serialized", length = 16777215)
	public String getConditionsSerialized() {
		return this.conditionsSerialized;
	}

	public void setConditionsSerialized(String conditionsSerialized) {
		this.conditionsSerialized = conditionsSerialized;
	}

	@Column(name = "actions_serialized", length = 16777215)
	public String getActionsSerialized() {
		return this.actionsSerialized;
	}

	public void setActionsSerialized(String actionsSerialized) {
		this.actionsSerialized = actionsSerialized;
	}

	@Column(name = "stop_rules_processing", nullable = false)
	public short getStopRulesProcessing() {
		return this.stopRulesProcessing;
	}

	public void setStopRulesProcessing(short stopRulesProcessing) {
		this.stopRulesProcessing = stopRulesProcessing;
	}

	@Column(name = "is_advanced", nullable = false)
	public short getIsAdvanced() {
		return this.isAdvanced;
	}

	public void setIsAdvanced(short isAdvanced) {
		this.isAdvanced = isAdvanced;
	}

	@Column(name = "product_ids", length = 65535)
	public String getProductIds() {
		return this.productIds;
	}

	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}

	@Column(name = "sort_order", nullable = false)
	public int getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Column(name = "simple_action", length = 32)
	public String getSimpleAction() {
		return this.simpleAction;
	}

	public void setSimpleAction(String simpleAction) {
		this.simpleAction = simpleAction;
	}

	@Column(name = "discount_amount", nullable = false, precision = 12, scale = 4)
	public BigDecimal getDiscountAmount() {
		return this.discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	@Column(name = "discount_qty", precision = 12, scale = 4)
	public BigDecimal getDiscountQty() {
		return this.discountQty;
	}

	public void setDiscountQty(BigDecimal discountQty) {
		this.discountQty = discountQty;
	}

	@Column(name = "discount_step", nullable = false)
	public int getDiscountStep() {
		return this.discountStep;
	}

	public void setDiscountStep(int discountStep) {
		this.discountStep = discountStep;
	}

	@Column(name = "simple_free_shipping", nullable = false)
	public short getSimpleFreeShipping() {
		return this.simpleFreeShipping;
	}

	public void setSimpleFreeShipping(short simpleFreeShipping) {
		this.simpleFreeShipping = simpleFreeShipping;
	}

	@Column(name = "apply_to_shipping", nullable = false)
	public short getApplyToShipping() {
		return this.applyToShipping;
	}

	public void setApplyToShipping(short applyToShipping) {
		this.applyToShipping = applyToShipping;
	}

	@Column(name = "times_used", nullable = false)
	public int getTimesUsed() {
		return this.timesUsed;
	}

	public void setTimesUsed(int timesUsed) {
		this.timesUsed = timesUsed;
	}

	@Column(name = "is_rss", nullable = false)
	public short getIsRss() {
		return this.isRss;
	}

	public void setIsRss(short isRss) {
		this.isRss = isRss;
	}

	@Column(name = "coupon_type", nullable = false)
	public short getCouponType() {
		return this.couponType;
	}

	public void setCouponType(short couponType) {
		this.couponType = couponType;
	}

	@Column(name = "use_auto_generation", nullable = false)
	public short getUseAutoGeneration() {
		return this.useAutoGeneration;
	}

	public void setUseAutoGeneration(short useAutoGeneration) {
		this.useAutoGeneration = useAutoGeneration;
	}

	@Column(name = "uses_per_coupon", nullable = false)
	public int getUsesPerCoupon() {
		return this.usesPerCoupon;
	}

	public void setUsesPerCoupon(int usesPerCoupon) {
		this.usesPerCoupon = usesPerCoupon;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "salesrule_website", catalog = "magento", joinColumns = {
		@JoinColumn(name = "rule_id", nullable = false, updatable = false)}, inverseJoinColumns = {
		@JoinColumn(name = "website_id", nullable = false, updatable = false)})
	public Set<CoreWebsite> getCoreWebsites() {
		return this.coreWebsites;
	}

	public void setCoreWebsites(Set<CoreWebsite> coreWebsites) {
		this.coreWebsites = coreWebsites;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesrule")
	public Set<SalesruleCoupon> getSalesruleCoupons() {
		return this.salesruleCoupons;
	}

	public void setSalesruleCoupons(Set<SalesruleCoupon> salesruleCoupons) {
		this.salesruleCoupons = salesruleCoupons;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "salesrule_customer_group", catalog = "magento", joinColumns = {
		@JoinColumn(name = "rule_id", nullable = false, updatable = false)}, inverseJoinColumns = {
		@JoinColumn(name = "customer_group_id", nullable = false, updatable = false)})
	public Set<CustomerGroup> getCustomerGroups() {
		return this.customerGroups;
	}

	public void setCustomerGroups(Set<CustomerGroup> customerGroups) {
		this.customerGroups = customerGroups;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesrule")
	public Set<SalesruleLabel> getSalesruleLabels() {
		return this.salesruleLabels;
	}

	public void setSalesruleLabels(Set<SalesruleLabel> salesruleLabels) {
		this.salesruleLabels = salesruleLabels;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesrule")
	public Set<SalesruleCustomer> getSalesruleCustomers() {
		return this.salesruleCustomers;
	}

	public void setSalesruleCustomers(Set<SalesruleCustomer> salesruleCustomers) {
		this.salesruleCustomers = salesruleCustomers;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesrule")
	public Set<SalesruleProductAttribute> getSalesruleProductAttributes() {
		return this.salesruleProductAttributes;
	}

	public void setSalesruleProductAttributes(Set<SalesruleProductAttribute> salesruleProductAttributes) {
		this.salesruleProductAttributes = salesruleProductAttributes;
	}

}