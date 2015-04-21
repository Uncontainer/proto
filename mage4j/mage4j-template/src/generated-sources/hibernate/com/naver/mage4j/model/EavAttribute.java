package com.naver.mage4j.model;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * EavAttribute generated by hbm2java
 */
@Entity
@Table(name = "eav_attribute"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = {"entity_type_id", "attribute_code"}))
public class EavAttribute implements java.io.Serializable {

	private Short attributeId;
	private EavEntityType eavEntityType;
	private String attributeCode;
	private String attributeModel;
	private String backendModel;
	private String backendType;
	private String backendTable;
	private String frontendModel;
	private String frontendInput;
	private String frontendLabel;
	private String frontendClass;
	private String sourceModel;
	private short isRequired;
	private short isUserDefined;
	private String defaultValue;
	private short isUnique;
	private String note;
	private Set<CustomerEntityText> customerEntityTexts = new HashSet<CustomerEntityText>(0);
	private Set<CustomerEntityDatetime> customerEntityDatetimes = new HashSet<CustomerEntityDatetime>(0);
	private Set<EavEntityAttribute> eavEntityAttributes = new HashSet<EavEntityAttribute>(0);
	private Set<CustomerAddressEntityInt> customerAddressEntityInts = new HashSet<CustomerAddressEntityInt>(0);
	private Set<CatalogProductEntityDatetime> catalogProductEntityDatetimes = new HashSet<CatalogProductEntityDatetime>(0);
	private Set<CatalogProductEntityMediaGallery> catalogProductEntityMediaGalleries = new HashSet<CatalogProductEntityMediaGallery>(0);
	private Set<CustomerEavAttributeWebsite> customerEavAttributeWebsites = new HashSet<CustomerEavAttributeWebsite>(0);
	private Set<CatalogCategoryEntityDecimal> catalogCategoryEntityDecimals = new HashSet<CatalogCategoryEntityDecimal>(0);
	private Set<CatalogProductIndexEav> catalogProductIndexEavs = new HashSet<CatalogProductIndexEav>(0);
	private Set<CustomerAddressEntityVarchar> customerAddressEntityVarchars = new HashSet<CustomerAddressEntityVarchar>(0);
	private Set<WeeeTax> weeeTaxes = new HashSet<WeeeTax>(0);
	private Set<CatalogCategoryEntityVarchar> catalogCategoryEntityVarchars = new HashSet<CatalogCategoryEntityVarchar>(0);
	private Set<CatalogProductEntityText> catalogProductEntityTexts = new HashSet<CatalogProductEntityText>(0);
	private Set<CustomerEntityDecimal> customerEntityDecimals = new HashSet<CustomerEntityDecimal>(0);
	private Set<EavAttributeLabel> eavAttributeLabels = new HashSet<EavAttributeLabel>(0);
	private Set<CustomerAddressEntityDatetime> customerAddressEntityDatetimes = new HashSet<CustomerAddressEntityDatetime>(0);
	private Set<CustomerAddressEntityDecimal> customerAddressEntityDecimals = new HashSet<CustomerAddressEntityDecimal>(0);
	private Set<EavAttributeOption> eavAttributeOptions = new HashSet<EavAttributeOption>(0);
	private Set<CatalogProductEntityDecimal> catalogProductEntityDecimals = new HashSet<CatalogProductEntityDecimal>(0);
	private Set<CatalogCategoryEntityInt> catalogCategoryEntityInts = new HashSet<CatalogCategoryEntityInt>(0);
	private Set<CatalogCategoryEntityDatetime> catalogCategoryEntityDatetimes = new HashSet<CatalogCategoryEntityDatetime>(0);
	private CatalogEavAttribute catalogEavAttribute;
	private Set<CustomerEntityVarchar> customerEntityVarchars = new HashSet<CustomerEntityVarchar>(0);
	private Set<CatalogCategoryEntityText> catalogCategoryEntityTexts = new HashSet<CatalogCategoryEntityText>(0);
	private Set<CatalogProductIndexEavDecimal> catalogProductIndexEavDecimals = new HashSet<CatalogProductIndexEavDecimal>(0);
	private Set<CustomerEntityInt> customerEntityInts = new HashSet<CustomerEntityInt>(0);
	private Set<CatalogProductEntityGallery> catalogProductEntityGalleries = new HashSet<CatalogProductEntityGallery>(0);
	private Set<CustomerFormAttribute> customerFormAttributes = new HashSet<CustomerFormAttribute>(0);
	private CustomerEavAttribute customerEavAttribute;
	private Set<CustomerAddressEntityText> customerAddressEntityTexts = new HashSet<CustomerAddressEntityText>(0);
	private Set<SalesruleProductAttribute> salesruleProductAttributes = new HashSet<SalesruleProductAttribute>(0);
	private Set<CatalogProductEntityInt> catalogProductEntityInts = new HashSet<CatalogProductEntityInt>(0);
	private Set<CatalogProductEntityVarchar> catalogProductEntityVarchars = new HashSet<CatalogProductEntityVarchar>(0);
	private Set<EavFormElement> eavFormElements = new HashSet<EavFormElement>(0);

	public EavAttribute() {
	}

	public EavAttribute(EavEntityType eavEntityType, String backendType, short isRequired, short isUserDefined, short isUnique) {
		this.eavEntityType = eavEntityType;
		this.backendType = backendType;
		this.isRequired = isRequired;
		this.isUserDefined = isUserDefined;
		this.isUnique = isUnique;
	}

	public EavAttribute(EavEntityType eavEntityType, String attributeCode, String attributeModel, String backendModel, String backendType, String backendTable, String frontendModel, String frontendInput, String frontendLabel, String frontendClass, String sourceModel, short isRequired, short isUserDefined, String defaultValue, short isUnique, String note, Set<CustomerEntityText> customerEntityTexts, Set<CustomerEntityDatetime> customerEntityDatetimes, Set<EavEntityAttribute> eavEntityAttributes,
		Set<CustomerAddressEntityInt> customerAddressEntityInts, Set<CatalogProductEntityDatetime> catalogProductEntityDatetimes, Set<CatalogProductEntityMediaGallery> catalogProductEntityMediaGalleries, Set<CustomerEavAttributeWebsite> customerEavAttributeWebsites, Set<CatalogCategoryEntityDecimal> catalogCategoryEntityDecimals, Set<CatalogProductIndexEav> catalogProductIndexEavs, Set<CustomerAddressEntityVarchar> customerAddressEntityVarchars, Set<WeeeTax> weeeTaxes,
		Set<CatalogCategoryEntityVarchar> catalogCategoryEntityVarchars, Set<CatalogProductEntityText> catalogProductEntityTexts, Set<CustomerEntityDecimal> customerEntityDecimals, Set<EavAttributeLabel> eavAttributeLabels, Set<CustomerAddressEntityDatetime> customerAddressEntityDatetimes, Set<CustomerAddressEntityDecimal> customerAddressEntityDecimals, Set<EavAttributeOption> eavAttributeOptions, Set<CatalogProductEntityDecimal> catalogProductEntityDecimals,
		Set<CatalogCategoryEntityInt> catalogCategoryEntityInts, Set<CatalogCategoryEntityDatetime> catalogCategoryEntityDatetimes, CatalogEavAttribute catalogEavAttribute, Set<CustomerEntityVarchar> customerEntityVarchars, Set<CatalogCategoryEntityText> catalogCategoryEntityTexts, Set<CatalogProductIndexEavDecimal> catalogProductIndexEavDecimals, Set<CustomerEntityInt> customerEntityInts, Set<CatalogProductEntityGallery> catalogProductEntityGalleries,
		Set<CustomerFormAttribute> customerFormAttributes, CustomerEavAttribute customerEavAttribute, Set<CustomerAddressEntityText> customerAddressEntityTexts, Set<SalesruleProductAttribute> salesruleProductAttributes, Set<CatalogProductEntityInt> catalogProductEntityInts, Set<CatalogProductEntityVarchar> catalogProductEntityVarchars, Set<EavFormElement> eavFormElements) {
		this.eavEntityType = eavEntityType;
		this.attributeCode = attributeCode;
		this.attributeModel = attributeModel;
		this.backendModel = backendModel;
		this.backendType = backendType;
		this.backendTable = backendTable;
		this.frontendModel = frontendModel;
		this.frontendInput = frontendInput;
		this.frontendLabel = frontendLabel;
		this.frontendClass = frontendClass;
		this.sourceModel = sourceModel;
		this.isRequired = isRequired;
		this.isUserDefined = isUserDefined;
		this.defaultValue = defaultValue;
		this.isUnique = isUnique;
		this.note = note;
		this.customerEntityTexts = customerEntityTexts;
		this.customerEntityDatetimes = customerEntityDatetimes;
		this.eavEntityAttributes = eavEntityAttributes;
		this.customerAddressEntityInts = customerAddressEntityInts;
		this.catalogProductEntityDatetimes = catalogProductEntityDatetimes;
		this.catalogProductEntityMediaGalleries = catalogProductEntityMediaGalleries;
		this.customerEavAttributeWebsites = customerEavAttributeWebsites;
		this.catalogCategoryEntityDecimals = catalogCategoryEntityDecimals;
		this.catalogProductIndexEavs = catalogProductIndexEavs;
		this.customerAddressEntityVarchars = customerAddressEntityVarchars;
		this.weeeTaxes = weeeTaxes;
		this.catalogCategoryEntityVarchars = catalogCategoryEntityVarchars;
		this.catalogProductEntityTexts = catalogProductEntityTexts;
		this.customerEntityDecimals = customerEntityDecimals;
		this.eavAttributeLabels = eavAttributeLabels;
		this.customerAddressEntityDatetimes = customerAddressEntityDatetimes;
		this.customerAddressEntityDecimals = customerAddressEntityDecimals;
		this.eavAttributeOptions = eavAttributeOptions;
		this.catalogProductEntityDecimals = catalogProductEntityDecimals;
		this.catalogCategoryEntityInts = catalogCategoryEntityInts;
		this.catalogCategoryEntityDatetimes = catalogCategoryEntityDatetimes;
		this.catalogEavAttribute = catalogEavAttribute;
		this.customerEntityVarchars = customerEntityVarchars;
		this.catalogCategoryEntityTexts = catalogCategoryEntityTexts;
		this.catalogProductIndexEavDecimals = catalogProductIndexEavDecimals;
		this.customerEntityInts = customerEntityInts;
		this.catalogProductEntityGalleries = catalogProductEntityGalleries;
		this.customerFormAttributes = customerFormAttributes;
		this.customerEavAttribute = customerEavAttribute;
		this.customerAddressEntityTexts = customerAddressEntityTexts;
		this.salesruleProductAttributes = salesruleProductAttributes;
		this.catalogProductEntityInts = catalogProductEntityInts;
		this.catalogProductEntityVarchars = catalogProductEntityVarchars;
		this.eavFormElements = eavFormElements;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "attribute_id", unique = true, nullable = false)
	public Short getAttributeId() {
		return this.attributeId;
	}

	public void setAttributeId(Short attributeId) {
		this.attributeId = attributeId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entity_type_id", nullable = false)
	public EavEntityType getEavEntityType() {
		return this.eavEntityType;
	}

	public void setEavEntityType(EavEntityType eavEntityType) {
		this.eavEntityType = eavEntityType;
	}

	@Column(name = "attribute_code")
	public String getAttributeCode() {
		return this.attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	@Column(name = "attribute_model")
	public String getAttributeModel() {
		return this.attributeModel;
	}

	public void setAttributeModel(String attributeModel) {
		this.attributeModel = attributeModel;
	}

	@Column(name = "backend_model")
	public String getBackendModel() {
		return this.backendModel;
	}

	public void setBackendModel(String backendModel) {
		this.backendModel = backendModel;
	}

	@Column(name = "backend_type", nullable = false, length = 8)
	public String getBackendType() {
		return this.backendType;
	}

	public void setBackendType(String backendType) {
		this.backendType = backendType;
	}

	@Column(name = "backend_table")
	public String getBackendTable() {
		return this.backendTable;
	}

	public void setBackendTable(String backendTable) {
		this.backendTable = backendTable;
	}

	@Column(name = "frontend_model")
	public String getFrontendModel() {
		return this.frontendModel;
	}

	public void setFrontendModel(String frontendModel) {
		this.frontendModel = frontendModel;
	}

	@Column(name = "frontend_input", length = 50)
	public String getFrontendInput() {
		return this.frontendInput;
	}

	public void setFrontendInput(String frontendInput) {
		this.frontendInput = frontendInput;
	}

	@Column(name = "frontend_label")
	public String getFrontendLabel() {
		return this.frontendLabel;
	}

	public void setFrontendLabel(String frontendLabel) {
		this.frontendLabel = frontendLabel;
	}

	@Column(name = "frontend_class")
	public String getFrontendClass() {
		return this.frontendClass;
	}

	public void setFrontendClass(String frontendClass) {
		this.frontendClass = frontendClass;
	}

	@Column(name = "source_model")
	public String getSourceModel() {
		return this.sourceModel;
	}

	public void setSourceModel(String sourceModel) {
		this.sourceModel = sourceModel;
	}

	@Column(name = "is_required", nullable = false)
	public short getIsRequired() {
		return this.isRequired;
	}

	public void setIsRequired(short isRequired) {
		this.isRequired = isRequired;
	}

	@Column(name = "is_user_defined", nullable = false)
	public short getIsUserDefined() {
		return this.isUserDefined;
	}

	public void setIsUserDefined(short isUserDefined) {
		this.isUserDefined = isUserDefined;
	}

	@Column(name = "default_value", length = 65535)
	public String getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Column(name = "is_unique", nullable = false)
	public short getIsUnique() {
		return this.isUnique;
	}

	public void setIsUnique(short isUnique) {
		this.isUnique = isUnique;
	}

	@Column(name = "note")
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CustomerEntityText> getCustomerEntityTexts() {
		return this.customerEntityTexts;
	}

	public void setCustomerEntityTexts(Set<CustomerEntityText> customerEntityTexts) {
		this.customerEntityTexts = customerEntityTexts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CustomerEntityDatetime> getCustomerEntityDatetimes() {
		return this.customerEntityDatetimes;
	}

	public void setCustomerEntityDatetimes(Set<CustomerEntityDatetime> customerEntityDatetimes) {
		this.customerEntityDatetimes = customerEntityDatetimes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<EavEntityAttribute> getEavEntityAttributes() {
		return this.eavEntityAttributes;
	}

	public void setEavEntityAttributes(Set<EavEntityAttribute> eavEntityAttributes) {
		this.eavEntityAttributes = eavEntityAttributes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CustomerAddressEntityInt> getCustomerAddressEntityInts() {
		return this.customerAddressEntityInts;
	}

	public void setCustomerAddressEntityInts(Set<CustomerAddressEntityInt> customerAddressEntityInts) {
		this.customerAddressEntityInts = customerAddressEntityInts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CatalogProductEntityDatetime> getCatalogProductEntityDatetimes() {
		return this.catalogProductEntityDatetimes;
	}

	public void setCatalogProductEntityDatetimes(Set<CatalogProductEntityDatetime> catalogProductEntityDatetimes) {
		this.catalogProductEntityDatetimes = catalogProductEntityDatetimes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CatalogProductEntityMediaGallery> getCatalogProductEntityMediaGalleries() {
		return this.catalogProductEntityMediaGalleries;
	}

	public void setCatalogProductEntityMediaGalleries(Set<CatalogProductEntityMediaGallery> catalogProductEntityMediaGalleries) {
		this.catalogProductEntityMediaGalleries = catalogProductEntityMediaGalleries;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CustomerEavAttributeWebsite> getCustomerEavAttributeWebsites() {
		return this.customerEavAttributeWebsites;
	}

	public void setCustomerEavAttributeWebsites(Set<CustomerEavAttributeWebsite> customerEavAttributeWebsites) {
		this.customerEavAttributeWebsites = customerEavAttributeWebsites;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CatalogCategoryEntityDecimal> getCatalogCategoryEntityDecimals() {
		return this.catalogCategoryEntityDecimals;
	}

	public void setCatalogCategoryEntityDecimals(Set<CatalogCategoryEntityDecimal> catalogCategoryEntityDecimals) {
		this.catalogCategoryEntityDecimals = catalogCategoryEntityDecimals;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CatalogProductIndexEav> getCatalogProductIndexEavs() {
		return this.catalogProductIndexEavs;
	}

	public void setCatalogProductIndexEavs(Set<CatalogProductIndexEav> catalogProductIndexEavs) {
		this.catalogProductIndexEavs = catalogProductIndexEavs;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CustomerAddressEntityVarchar> getCustomerAddressEntityVarchars() {
		return this.customerAddressEntityVarchars;
	}

	public void setCustomerAddressEntityVarchars(Set<CustomerAddressEntityVarchar> customerAddressEntityVarchars) {
		this.customerAddressEntityVarchars = customerAddressEntityVarchars;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<WeeeTax> getWeeeTaxes() {
		return this.weeeTaxes;
	}

	public void setWeeeTaxes(Set<WeeeTax> weeeTaxes) {
		this.weeeTaxes = weeeTaxes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CatalogCategoryEntityVarchar> getCatalogCategoryEntityVarchars() {
		return this.catalogCategoryEntityVarchars;
	}

	public void setCatalogCategoryEntityVarchars(Set<CatalogCategoryEntityVarchar> catalogCategoryEntityVarchars) {
		this.catalogCategoryEntityVarchars = catalogCategoryEntityVarchars;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CatalogProductEntityText> getCatalogProductEntityTexts() {
		return this.catalogProductEntityTexts;
	}

	public void setCatalogProductEntityTexts(Set<CatalogProductEntityText> catalogProductEntityTexts) {
		this.catalogProductEntityTexts = catalogProductEntityTexts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CustomerEntityDecimal> getCustomerEntityDecimals() {
		return this.customerEntityDecimals;
	}

	public void setCustomerEntityDecimals(Set<CustomerEntityDecimal> customerEntityDecimals) {
		this.customerEntityDecimals = customerEntityDecimals;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<EavAttributeLabel> getEavAttributeLabels() {
		return this.eavAttributeLabels;
	}

	public void setEavAttributeLabels(Set<EavAttributeLabel> eavAttributeLabels) {
		this.eavAttributeLabels = eavAttributeLabels;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CustomerAddressEntityDatetime> getCustomerAddressEntityDatetimes() {
		return this.customerAddressEntityDatetimes;
	}

	public void setCustomerAddressEntityDatetimes(Set<CustomerAddressEntityDatetime> customerAddressEntityDatetimes) {
		this.customerAddressEntityDatetimes = customerAddressEntityDatetimes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CustomerAddressEntityDecimal> getCustomerAddressEntityDecimals() {
		return this.customerAddressEntityDecimals;
	}

	public void setCustomerAddressEntityDecimals(Set<CustomerAddressEntityDecimal> customerAddressEntityDecimals) {
		this.customerAddressEntityDecimals = customerAddressEntityDecimals;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<EavAttributeOption> getEavAttributeOptions() {
		return this.eavAttributeOptions;
	}

	public void setEavAttributeOptions(Set<EavAttributeOption> eavAttributeOptions) {
		this.eavAttributeOptions = eavAttributeOptions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CatalogProductEntityDecimal> getCatalogProductEntityDecimals() {
		return this.catalogProductEntityDecimals;
	}

	public void setCatalogProductEntityDecimals(Set<CatalogProductEntityDecimal> catalogProductEntityDecimals) {
		this.catalogProductEntityDecimals = catalogProductEntityDecimals;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CatalogCategoryEntityInt> getCatalogCategoryEntityInts() {
		return this.catalogCategoryEntityInts;
	}

	public void setCatalogCategoryEntityInts(Set<CatalogCategoryEntityInt> catalogCategoryEntityInts) {
		this.catalogCategoryEntityInts = catalogCategoryEntityInts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CatalogCategoryEntityDatetime> getCatalogCategoryEntityDatetimes() {
		return this.catalogCategoryEntityDatetimes;
	}

	public void setCatalogCategoryEntityDatetimes(Set<CatalogCategoryEntityDatetime> catalogCategoryEntityDatetimes) {
		this.catalogCategoryEntityDatetimes = catalogCategoryEntityDatetimes;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public CatalogEavAttribute getCatalogEavAttribute() {
		return this.catalogEavAttribute;
	}

	public void setCatalogEavAttribute(CatalogEavAttribute catalogEavAttribute) {
		this.catalogEavAttribute = catalogEavAttribute;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CustomerEntityVarchar> getCustomerEntityVarchars() {
		return this.customerEntityVarchars;
	}

	public void setCustomerEntityVarchars(Set<CustomerEntityVarchar> customerEntityVarchars) {
		this.customerEntityVarchars = customerEntityVarchars;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CatalogCategoryEntityText> getCatalogCategoryEntityTexts() {
		return this.catalogCategoryEntityTexts;
	}

	public void setCatalogCategoryEntityTexts(Set<CatalogCategoryEntityText> catalogCategoryEntityTexts) {
		this.catalogCategoryEntityTexts = catalogCategoryEntityTexts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CatalogProductIndexEavDecimal> getCatalogProductIndexEavDecimals() {
		return this.catalogProductIndexEavDecimals;
	}

	public void setCatalogProductIndexEavDecimals(Set<CatalogProductIndexEavDecimal> catalogProductIndexEavDecimals) {
		this.catalogProductIndexEavDecimals = catalogProductIndexEavDecimals;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CustomerEntityInt> getCustomerEntityInts() {
		return this.customerEntityInts;
	}

	public void setCustomerEntityInts(Set<CustomerEntityInt> customerEntityInts) {
		this.customerEntityInts = customerEntityInts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CatalogProductEntityGallery> getCatalogProductEntityGalleries() {
		return this.catalogProductEntityGalleries;
	}

	public void setCatalogProductEntityGalleries(Set<CatalogProductEntityGallery> catalogProductEntityGalleries) {
		this.catalogProductEntityGalleries = catalogProductEntityGalleries;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CustomerFormAttribute> getCustomerFormAttributes() {
		return this.customerFormAttributes;
	}

	public void setCustomerFormAttributes(Set<CustomerFormAttribute> customerFormAttributes) {
		this.customerFormAttributes = customerFormAttributes;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public CustomerEavAttribute getCustomerEavAttribute() {
		return this.customerEavAttribute;
	}

	public void setCustomerEavAttribute(CustomerEavAttribute customerEavAttribute) {
		this.customerEavAttribute = customerEavAttribute;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CustomerAddressEntityText> getCustomerAddressEntityTexts() {
		return this.customerAddressEntityTexts;
	}

	public void setCustomerAddressEntityTexts(Set<CustomerAddressEntityText> customerAddressEntityTexts) {
		this.customerAddressEntityTexts = customerAddressEntityTexts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<SalesruleProductAttribute> getSalesruleProductAttributes() {
		return this.salesruleProductAttributes;
	}

	public void setSalesruleProductAttributes(Set<SalesruleProductAttribute> salesruleProductAttributes) {
		this.salesruleProductAttributes = salesruleProductAttributes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CatalogProductEntityInt> getCatalogProductEntityInts() {
		return this.catalogProductEntityInts;
	}

	public void setCatalogProductEntityInts(Set<CatalogProductEntityInt> catalogProductEntityInts) {
		this.catalogProductEntityInts = catalogProductEntityInts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<CatalogProductEntityVarchar> getCatalogProductEntityVarchars() {
		return this.catalogProductEntityVarchars;
	}

	public void setCatalogProductEntityVarchars(Set<CatalogProductEntityVarchar> catalogProductEntityVarchars) {
		this.catalogProductEntityVarchars = catalogProductEntityVarchars;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "eavAttribute")
	public Set<EavFormElement> getEavFormElements() {
		return this.eavFormElements;
	}

	public void setEavFormElements(Set<EavFormElement> eavFormElements) {
		this.eavFormElements = eavFormElements;
	}

}
