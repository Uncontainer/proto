package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * SalesFlatOrderPayment generated by hbm2java
 */
@Entity
@Table(name = "sales_flat_order_payment"
	, catalog = "magento")
public class SalesFlatOrderPayment implements java.io.Serializable {

	private Integer entityId;
	private SalesFlatOrder salesFlatOrder;
	private BigDecimal baseShippingCaptured;
	private BigDecimal shippingCaptured;
	private BigDecimal amountRefunded;
	private BigDecimal baseAmountPaid;
	private BigDecimal amountCanceled;
	private BigDecimal baseAmountAuthorized;
	private BigDecimal baseAmountPaidOnline;
	private BigDecimal baseAmountRefundedOnline;
	private BigDecimal baseShippingAmount;
	private BigDecimal shippingAmount;
	private BigDecimal amountPaid;
	private BigDecimal amountAuthorized;
	private BigDecimal baseAmountOrdered;
	private BigDecimal baseShippingRefunded;
	private BigDecimal shippingRefunded;
	private BigDecimal baseAmountRefunded;
	private BigDecimal amountOrdered;
	private BigDecimal baseAmountCanceled;
	private Integer quotePaymentId;
	private String additionalData;
	private String ccExpMonth;
	private String ccSsStartYear;
	private String echeckBankName;
	private String method;
	private String ccDebugRequestBody;
	private String ccSecureVerify;
	private String protectionEligibility;
	private String ccApproval;
	private String ccLast4;
	private String ccStatusDescription;
	private String echeckType;
	private String ccDebugResponseSerialized;
	private String ccSsStartMonth;
	private String echeckAccountType;
	private String lastTransId;
	private String ccCidStatus;
	private String ccOwner;
	private String ccType;
	private String poNumber;
	private String ccExpYear;
	private String ccStatus;
	private String echeckRoutingNumber;
	private String accountStatus;
	private String anetTransMethod;
	private String ccDebugResponseBody;
	private String ccSsIssue;
	private String echeckAccountName;
	private String ccAvsStatus;
	private String ccNumberEnc;
	private String ccTransId;
	private String payboxRequestNumber;
	private String addressStatus;
	private String additionalInformation;
	private Set<SalesPaymentTransaction> salesPaymentTransactions = new HashSet<SalesPaymentTransaction>(0);

	public SalesFlatOrderPayment() {
	}

	public SalesFlatOrderPayment(SalesFlatOrder salesFlatOrder) {
		this.salesFlatOrder = salesFlatOrder;
	}

	public SalesFlatOrderPayment(SalesFlatOrder salesFlatOrder, BigDecimal baseShippingCaptured, BigDecimal shippingCaptured, BigDecimal amountRefunded, BigDecimal baseAmountPaid, BigDecimal amountCanceled, BigDecimal baseAmountAuthorized, BigDecimal baseAmountPaidOnline, BigDecimal baseAmountRefundedOnline, BigDecimal baseShippingAmount, BigDecimal shippingAmount, BigDecimal amountPaid, BigDecimal amountAuthorized, BigDecimal baseAmountOrdered, BigDecimal baseShippingRefunded,
		BigDecimal shippingRefunded, BigDecimal baseAmountRefunded, BigDecimal amountOrdered, BigDecimal baseAmountCanceled, Integer quotePaymentId, String additionalData, String ccExpMonth, String ccSsStartYear, String echeckBankName, String method, String ccDebugRequestBody, String ccSecureVerify, String protectionEligibility, String ccApproval, String ccLast4, String ccStatusDescription, String echeckType, String ccDebugResponseSerialized, String ccSsStartMonth, String echeckAccountType,
		String lastTransId, String ccCidStatus, String ccOwner, String ccType, String poNumber, String ccExpYear, String ccStatus, String echeckRoutingNumber, String accountStatus, String anetTransMethod, String ccDebugResponseBody, String ccSsIssue, String echeckAccountName, String ccAvsStatus, String ccNumberEnc, String ccTransId, String payboxRequestNumber, String addressStatus, String additionalInformation, Set<SalesPaymentTransaction> salesPaymentTransactions) {
		this.salesFlatOrder = salesFlatOrder;
		this.baseShippingCaptured = baseShippingCaptured;
		this.shippingCaptured = shippingCaptured;
		this.amountRefunded = amountRefunded;
		this.baseAmountPaid = baseAmountPaid;
		this.amountCanceled = amountCanceled;
		this.baseAmountAuthorized = baseAmountAuthorized;
		this.baseAmountPaidOnline = baseAmountPaidOnline;
		this.baseAmountRefundedOnline = baseAmountRefundedOnline;
		this.baseShippingAmount = baseShippingAmount;
		this.shippingAmount = shippingAmount;
		this.amountPaid = amountPaid;
		this.amountAuthorized = amountAuthorized;
		this.baseAmountOrdered = baseAmountOrdered;
		this.baseShippingRefunded = baseShippingRefunded;
		this.shippingRefunded = shippingRefunded;
		this.baseAmountRefunded = baseAmountRefunded;
		this.amountOrdered = amountOrdered;
		this.baseAmountCanceled = baseAmountCanceled;
		this.quotePaymentId = quotePaymentId;
		this.additionalData = additionalData;
		this.ccExpMonth = ccExpMonth;
		this.ccSsStartYear = ccSsStartYear;
		this.echeckBankName = echeckBankName;
		this.method = method;
		this.ccDebugRequestBody = ccDebugRequestBody;
		this.ccSecureVerify = ccSecureVerify;
		this.protectionEligibility = protectionEligibility;
		this.ccApproval = ccApproval;
		this.ccLast4 = ccLast4;
		this.ccStatusDescription = ccStatusDescription;
		this.echeckType = echeckType;
		this.ccDebugResponseSerialized = ccDebugResponseSerialized;
		this.ccSsStartMonth = ccSsStartMonth;
		this.echeckAccountType = echeckAccountType;
		this.lastTransId = lastTransId;
		this.ccCidStatus = ccCidStatus;
		this.ccOwner = ccOwner;
		this.ccType = ccType;
		this.poNumber = poNumber;
		this.ccExpYear = ccExpYear;
		this.ccStatus = ccStatus;
		this.echeckRoutingNumber = echeckRoutingNumber;
		this.accountStatus = accountStatus;
		this.anetTransMethod = anetTransMethod;
		this.ccDebugResponseBody = ccDebugResponseBody;
		this.ccSsIssue = ccSsIssue;
		this.echeckAccountName = echeckAccountName;
		this.ccAvsStatus = ccAvsStatus;
		this.ccNumberEnc = ccNumberEnc;
		this.ccTransId = ccTransId;
		this.payboxRequestNumber = payboxRequestNumber;
		this.addressStatus = addressStatus;
		this.additionalInformation = additionalInformation;
		this.salesPaymentTransactions = salesPaymentTransactions;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "entity_id", unique = true, nullable = false)
	public Integer getEntityId() {
		return this.entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", nullable = false)
	public SalesFlatOrder getSalesFlatOrder() {
		return this.salesFlatOrder;
	}

	public void setSalesFlatOrder(SalesFlatOrder salesFlatOrder) {
		this.salesFlatOrder = salesFlatOrder;
	}

	@Column(name = "base_shipping_captured", precision = 12, scale = 4)
	public BigDecimal getBaseShippingCaptured() {
		return this.baseShippingCaptured;
	}

	public void setBaseShippingCaptured(BigDecimal baseShippingCaptured) {
		this.baseShippingCaptured = baseShippingCaptured;
	}

	@Column(name = "shipping_captured", precision = 12, scale = 4)
	public BigDecimal getShippingCaptured() {
		return this.shippingCaptured;
	}

	public void setShippingCaptured(BigDecimal shippingCaptured) {
		this.shippingCaptured = shippingCaptured;
	}

	@Column(name = "amount_refunded", precision = 12, scale = 4)
	public BigDecimal getAmountRefunded() {
		return this.amountRefunded;
	}

	public void setAmountRefunded(BigDecimal amountRefunded) {
		this.amountRefunded = amountRefunded;
	}

	@Column(name = "base_amount_paid", precision = 12, scale = 4)
	public BigDecimal getBaseAmountPaid() {
		return this.baseAmountPaid;
	}

	public void setBaseAmountPaid(BigDecimal baseAmountPaid) {
		this.baseAmountPaid = baseAmountPaid;
	}

	@Column(name = "amount_canceled", precision = 12, scale = 4)
	public BigDecimal getAmountCanceled() {
		return this.amountCanceled;
	}

	public void setAmountCanceled(BigDecimal amountCanceled) {
		this.amountCanceled = amountCanceled;
	}

	@Column(name = "base_amount_authorized", precision = 12, scale = 4)
	public BigDecimal getBaseAmountAuthorized() {
		return this.baseAmountAuthorized;
	}

	public void setBaseAmountAuthorized(BigDecimal baseAmountAuthorized) {
		this.baseAmountAuthorized = baseAmountAuthorized;
	}

	@Column(name = "base_amount_paid_online", precision = 12, scale = 4)
	public BigDecimal getBaseAmountPaidOnline() {
		return this.baseAmountPaidOnline;
	}

	public void setBaseAmountPaidOnline(BigDecimal baseAmountPaidOnline) {
		this.baseAmountPaidOnline = baseAmountPaidOnline;
	}

	@Column(name = "base_amount_refunded_online", precision = 12, scale = 4)
	public BigDecimal getBaseAmountRefundedOnline() {
		return this.baseAmountRefundedOnline;
	}

	public void setBaseAmountRefundedOnline(BigDecimal baseAmountRefundedOnline) {
		this.baseAmountRefundedOnline = baseAmountRefundedOnline;
	}

	@Column(name = "base_shipping_amount", precision = 12, scale = 4)
	public BigDecimal getBaseShippingAmount() {
		return this.baseShippingAmount;
	}

	public void setBaseShippingAmount(BigDecimal baseShippingAmount) {
		this.baseShippingAmount = baseShippingAmount;
	}

	@Column(name = "shipping_amount", precision = 12, scale = 4)
	public BigDecimal getShippingAmount() {
		return this.shippingAmount;
	}

	public void setShippingAmount(BigDecimal shippingAmount) {
		this.shippingAmount = shippingAmount;
	}

	@Column(name = "amount_paid", precision = 12, scale = 4)
	public BigDecimal getAmountPaid() {
		return this.amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	@Column(name = "amount_authorized", precision = 12, scale = 4)
	public BigDecimal getAmountAuthorized() {
		return this.amountAuthorized;
	}

	public void setAmountAuthorized(BigDecimal amountAuthorized) {
		this.amountAuthorized = amountAuthorized;
	}

	@Column(name = "base_amount_ordered", precision = 12, scale = 4)
	public BigDecimal getBaseAmountOrdered() {
		return this.baseAmountOrdered;
	}

	public void setBaseAmountOrdered(BigDecimal baseAmountOrdered) {
		this.baseAmountOrdered = baseAmountOrdered;
	}

	@Column(name = "base_shipping_refunded", precision = 12, scale = 4)
	public BigDecimal getBaseShippingRefunded() {
		return this.baseShippingRefunded;
	}

	public void setBaseShippingRefunded(BigDecimal baseShippingRefunded) {
		this.baseShippingRefunded = baseShippingRefunded;
	}

	@Column(name = "shipping_refunded", precision = 12, scale = 4)
	public BigDecimal getShippingRefunded() {
		return this.shippingRefunded;
	}

	public void setShippingRefunded(BigDecimal shippingRefunded) {
		this.shippingRefunded = shippingRefunded;
	}

	@Column(name = "base_amount_refunded", precision = 12, scale = 4)
	public BigDecimal getBaseAmountRefunded() {
		return this.baseAmountRefunded;
	}

	public void setBaseAmountRefunded(BigDecimal baseAmountRefunded) {
		this.baseAmountRefunded = baseAmountRefunded;
	}

	@Column(name = "amount_ordered", precision = 12, scale = 4)
	public BigDecimal getAmountOrdered() {
		return this.amountOrdered;
	}

	public void setAmountOrdered(BigDecimal amountOrdered) {
		this.amountOrdered = amountOrdered;
	}

	@Column(name = "base_amount_canceled", precision = 12, scale = 4)
	public BigDecimal getBaseAmountCanceled() {
		return this.baseAmountCanceled;
	}

	public void setBaseAmountCanceled(BigDecimal baseAmountCanceled) {
		this.baseAmountCanceled = baseAmountCanceled;
	}

	@Column(name = "quote_payment_id")
	public Integer getQuotePaymentId() {
		return this.quotePaymentId;
	}

	public void setQuotePaymentId(Integer quotePaymentId) {
		this.quotePaymentId = quotePaymentId;
	}

	@Column(name = "additional_data", length = 65535)
	public String getAdditionalData() {
		return this.additionalData;
	}

	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	@Column(name = "cc_exp_month")
	public String getCcExpMonth() {
		return this.ccExpMonth;
	}

	public void setCcExpMonth(String ccExpMonth) {
		this.ccExpMonth = ccExpMonth;
	}

	@Column(name = "cc_ss_start_year")
	public String getCcSsStartYear() {
		return this.ccSsStartYear;
	}

	public void setCcSsStartYear(String ccSsStartYear) {
		this.ccSsStartYear = ccSsStartYear;
	}

	@Column(name = "echeck_bank_name")
	public String getEcheckBankName() {
		return this.echeckBankName;
	}

	public void setEcheckBankName(String echeckBankName) {
		this.echeckBankName = echeckBankName;
	}

	@Column(name = "method")
	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Column(name = "cc_debug_request_body")
	public String getCcDebugRequestBody() {
		return this.ccDebugRequestBody;
	}

	public void setCcDebugRequestBody(String ccDebugRequestBody) {
		this.ccDebugRequestBody = ccDebugRequestBody;
	}

	@Column(name = "cc_secure_verify")
	public String getCcSecureVerify() {
		return this.ccSecureVerify;
	}

	public void setCcSecureVerify(String ccSecureVerify) {
		this.ccSecureVerify = ccSecureVerify;
	}

	@Column(name = "protection_eligibility")
	public String getProtectionEligibility() {
		return this.protectionEligibility;
	}

	public void setProtectionEligibility(String protectionEligibility) {
		this.protectionEligibility = protectionEligibility;
	}

	@Column(name = "cc_approval")
	public String getCcApproval() {
		return this.ccApproval;
	}

	public void setCcApproval(String ccApproval) {
		this.ccApproval = ccApproval;
	}

	@Column(name = "cc_last4")
	public String getCcLast4() {
		return this.ccLast4;
	}

	public void setCcLast4(String ccLast4) {
		this.ccLast4 = ccLast4;
	}

	@Column(name = "cc_status_description")
	public String getCcStatusDescription() {
		return this.ccStatusDescription;
	}

	public void setCcStatusDescription(String ccStatusDescription) {
		this.ccStatusDescription = ccStatusDescription;
	}

	@Column(name = "echeck_type")
	public String getEcheckType() {
		return this.echeckType;
	}

	public void setEcheckType(String echeckType) {
		this.echeckType = echeckType;
	}

	@Column(name = "cc_debug_response_serialized")
	public String getCcDebugResponseSerialized() {
		return this.ccDebugResponseSerialized;
	}

	public void setCcDebugResponseSerialized(String ccDebugResponseSerialized) {
		this.ccDebugResponseSerialized = ccDebugResponseSerialized;
	}

	@Column(name = "cc_ss_start_month")
	public String getCcSsStartMonth() {
		return this.ccSsStartMonth;
	}

	public void setCcSsStartMonth(String ccSsStartMonth) {
		this.ccSsStartMonth = ccSsStartMonth;
	}

	@Column(name = "echeck_account_type")
	public String getEcheckAccountType() {
		return this.echeckAccountType;
	}

	public void setEcheckAccountType(String echeckAccountType) {
		this.echeckAccountType = echeckAccountType;
	}

	@Column(name = "last_trans_id")
	public String getLastTransId() {
		return this.lastTransId;
	}

	public void setLastTransId(String lastTransId) {
		this.lastTransId = lastTransId;
	}

	@Column(name = "cc_cid_status")
	public String getCcCidStatus() {
		return this.ccCidStatus;
	}

	public void setCcCidStatus(String ccCidStatus) {
		this.ccCidStatus = ccCidStatus;
	}

	@Column(name = "cc_owner")
	public String getCcOwner() {
		return this.ccOwner;
	}

	public void setCcOwner(String ccOwner) {
		this.ccOwner = ccOwner;
	}

	@Column(name = "cc_type")
	public String getCcType() {
		return this.ccType;
	}

	public void setCcType(String ccType) {
		this.ccType = ccType;
	}

	@Column(name = "po_number")
	public String getPoNumber() {
		return this.poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	@Column(name = "cc_exp_year")
	public String getCcExpYear() {
		return this.ccExpYear;
	}

	public void setCcExpYear(String ccExpYear) {
		this.ccExpYear = ccExpYear;
	}

	@Column(name = "cc_status")
	public String getCcStatus() {
		return this.ccStatus;
	}

	public void setCcStatus(String ccStatus) {
		this.ccStatus = ccStatus;
	}

	@Column(name = "echeck_routing_number")
	public String getEcheckRoutingNumber() {
		return this.echeckRoutingNumber;
	}

	public void setEcheckRoutingNumber(String echeckRoutingNumber) {
		this.echeckRoutingNumber = echeckRoutingNumber;
	}

	@Column(name = "account_status")
	public String getAccountStatus() {
		return this.accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	@Column(name = "anet_trans_method")
	public String getAnetTransMethod() {
		return this.anetTransMethod;
	}

	public void setAnetTransMethod(String anetTransMethod) {
		this.anetTransMethod = anetTransMethod;
	}

	@Column(name = "cc_debug_response_body")
	public String getCcDebugResponseBody() {
		return this.ccDebugResponseBody;
	}

	public void setCcDebugResponseBody(String ccDebugResponseBody) {
		this.ccDebugResponseBody = ccDebugResponseBody;
	}

	@Column(name = "cc_ss_issue")
	public String getCcSsIssue() {
		return this.ccSsIssue;
	}

	public void setCcSsIssue(String ccSsIssue) {
		this.ccSsIssue = ccSsIssue;
	}

	@Column(name = "echeck_account_name")
	public String getEcheckAccountName() {
		return this.echeckAccountName;
	}

	public void setEcheckAccountName(String echeckAccountName) {
		this.echeckAccountName = echeckAccountName;
	}

	@Column(name = "cc_avs_status")
	public String getCcAvsStatus() {
		return this.ccAvsStatus;
	}

	public void setCcAvsStatus(String ccAvsStatus) {
		this.ccAvsStatus = ccAvsStatus;
	}

	@Column(name = "cc_number_enc")
	public String getCcNumberEnc() {
		return this.ccNumberEnc;
	}

	public void setCcNumberEnc(String ccNumberEnc) {
		this.ccNumberEnc = ccNumberEnc;
	}

	@Column(name = "cc_trans_id")
	public String getCcTransId() {
		return this.ccTransId;
	}

	public void setCcTransId(String ccTransId) {
		this.ccTransId = ccTransId;
	}

	@Column(name = "paybox_request_number")
	public String getPayboxRequestNumber() {
		return this.payboxRequestNumber;
	}

	public void setPayboxRequestNumber(String payboxRequestNumber) {
		this.payboxRequestNumber = payboxRequestNumber;
	}

	@Column(name = "address_status")
	public String getAddressStatus() {
		return this.addressStatus;
	}

	public void setAddressStatus(String addressStatus) {
		this.addressStatus = addressStatus;
	}

	@Column(name = "additional_information", length = 65535)
	public String getAdditionalInformation() {
		return this.additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesFlatOrderPayment")
	public Set<SalesPaymentTransaction> getSalesPaymentTransactions() {
		return this.salesPaymentTransactions;
	}

	public void setSalesPaymentTransactions(Set<SalesPaymentTransaction> salesPaymentTransactions) {
		this.salesPaymentTransactions = salesPaymentTransactions;
	}

}
