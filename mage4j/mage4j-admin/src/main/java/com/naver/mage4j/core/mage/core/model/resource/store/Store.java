package com.naver.mage4j.core.mage.core.model.resource.store;

// Generated 2014. 7. 17 ���� 12:15:45 by Hibernate Tools 4.0.0

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.naver.mage4j.core.mage.core.model.resource.design.DesignChange;
import com.naver.mage4j.core.mage.core.model.resource.layout.CoreLayoutLink;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogCategoryEntityDatetime;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogCategoryEntityDecimal;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogCategoryEntityInt;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogCategoryEntityText;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogCategoryEntityVarchar;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogCategoryProductIndex;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogCompareItem;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogProductEnabledIndex;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogProductEntityDatetime;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogProductEntityDecimal;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogProductEntityGallery;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogProductEntityInt;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogProductEntityMediaGalleryValue;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogProductEntityText;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogProductEntityVarchar;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogProductIndexEav;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogProductIndexEavDecimal;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogProductOptionPrice;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogProductOptionTitle;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogProductOptionTypePrice;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogProductOptionTypeTitle;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogProductSuperAttributeLabel;
import com.naver.mage4j.core.mage.core.model.resource.model.CatalogsearchQuery;
import com.naver.mage4j.core.mage.core.model.resource.model.CheckoutAgreement;
import com.naver.mage4j.core.mage.core.model.resource.model.CmsBlock;
import com.naver.mage4j.core.mage.core.model.resource.model.CmsPage;
import com.naver.mage4j.core.mage.core.model.resource.model.CoreVariableValue;
import com.naver.mage4j.core.mage.core.model.resource.model.CouponAggregated;
import com.naver.mage4j.core.mage.core.model.resource.model.CouponAggregatedOrder;
import com.naver.mage4j.core.mage.core.model.resource.model.CouponAggregatedUpdated;
import com.naver.mage4j.core.mage.core.model.resource.model.CustomerEntity;
import com.naver.mage4j.core.mage.core.model.resource.model.DataflowBatch;
import com.naver.mage4j.core.mage.core.model.resource.model.DownloadableLinkTitle;
import com.naver.mage4j.core.mage.core.model.resource.model.DownloadableSampleTitle;
import com.naver.mage4j.core.mage.core.model.resource.model.EavAttributeLabel;
import com.naver.mage4j.core.mage.core.model.resource.model.EavAttributeOptionValue;
import com.naver.mage4j.core.mage.core.model.resource.model.EavEntity;
import com.naver.mage4j.core.mage.core.model.resource.model.EavEntityDatetime;
import com.naver.mage4j.core.mage.core.model.resource.model.EavEntityDecimal;
import com.naver.mage4j.core.mage.core.model.resource.model.EavEntityInt;
import com.naver.mage4j.core.mage.core.model.resource.model.EavEntityStore;
import com.naver.mage4j.core.mage.core.model.resource.model.EavEntityText;
import com.naver.mage4j.core.mage.core.model.resource.model.EavEntityVarchar;
import com.naver.mage4j.core.mage.core.model.resource.model.EavFormFieldsetLabel;
import com.naver.mage4j.core.mage.core.model.resource.model.EavFormType;
import com.naver.mage4j.core.mage.core.model.resource.model.NewsletterQueue;
import com.naver.mage4j.core.mage.core.model.resource.model.NewsletterSubscriber;
import com.naver.mage4j.core.mage.core.model.resource.model.Poll;
import com.naver.mage4j.core.mage.core.model.resource.model.Rating;
import com.naver.mage4j.core.mage.core.model.resource.model.RatingOptionVoteAggregated;
import com.naver.mage4j.core.mage.core.model.resource.model.RatingTitle;
import com.naver.mage4j.core.mage.core.model.resource.model.ReportComparedProductIndex;
import com.naver.mage4j.core.mage.core.model.resource.model.ReportEvent;
import com.naver.mage4j.core.mage.core.model.resource.model.ReportViewedProductAggregatedDaily;
import com.naver.mage4j.core.mage.core.model.resource.model.ReportViewedProductAggregatedMonthly;
import com.naver.mage4j.core.mage.core.model.resource.model.ReportViewedProductAggregatedYearly;
import com.naver.mage4j.core.mage.core.model.resource.model.ReportViewedProductIndex;
import com.naver.mage4j.core.mage.core.model.resource.model.Review;
import com.naver.mage4j.core.mage.core.model.resource.model.ReviewDetail;
import com.naver.mage4j.core.mage.core.model.resource.model.ReviewEntitySummary;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesBestsellersAggregatedDaily;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesBestsellersAggregatedMonthly;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesBestsellersAggregatedYearly;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesBillingAgreement;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesFlatCreditmemo;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesFlatCreditmemoGrid;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesFlatInvoice;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesFlatInvoiceGrid;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesFlatOrder;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesFlatOrderGrid;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesFlatOrderItem;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesFlatQuote;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesFlatQuoteItem;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesFlatShipment;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesFlatShipmentGrid;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesInvoicedAggregated;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesInvoicedAggregatedOrder;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesOrderAggregatedCreated;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesOrderAggregatedUpdated;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesOrderStatusLabel;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesRecurringProfile;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesRefundedAggregated;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesRefundedAggregatedOrder;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesShippingAggregated;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesShippingAggregatedOrder;
import com.naver.mage4j.core.mage.core.model.resource.model.SalesruleLabel;
import com.naver.mage4j.core.mage.core.model.resource.model.Sitemap;
import com.naver.mage4j.core.mage.core.model.resource.model.Tag;
import com.naver.mage4j.core.mage.core.model.resource.model.TagProperties;
import com.naver.mage4j.core.mage.core.model.resource.model.TagRelation;
import com.naver.mage4j.core.mage.core.model.resource.model.TagSummary;
import com.naver.mage4j.core.mage.core.model.resource.model.TaxCalculationRateTitle;
import com.naver.mage4j.core.mage.core.model.resource.model.TaxOrderAggregatedCreated;
import com.naver.mage4j.core.mage.core.model.resource.model.TaxOrderAggregatedUpdated;
import com.naver.mage4j.core.mage.core.model.resource.model.WishlistItem;
import com.naver.mage4j.core.mage.core.model.resource.model.XmlconnectApplication;
import com.naver.mage4j.core.mage.core.model.resource.store.group.StoreGroup;
import com.naver.mage4j.core.mage.core.model.resource.translate.CoreTranslate;
import com.naver.mage4j.core.mage.core.model.resource.url.UrlRewrite;
import com.naver.mage4j.core.mage.core.model.resource.website.Website;

/**
 * CoreStore generated by hbm2java
 */
@Entity
@Table(name = "core_store"
	, catalog = "magento"
	, uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class Store implements java.io.Serializable {
	private Short storeId;
	private Website website;
	private StoreGroup coreStoreGroup;
	private String code;
	private String name;
	private short sortOrder;
	private Boolean isActive;
	private Set<CatalogProductSuperAttributeLabel> catalogProductSuperAttributeLabels = new HashSet<CatalogProductSuperAttributeLabel>(0);
	private Set<ReportViewedProductAggregatedMonthly> reportViewedProductAggregatedMonthlies = new HashSet<ReportViewedProductAggregatedMonthly>(0);
	private Set<TaxOrderAggregatedUpdated> taxOrderAggregatedUpdateds = new HashSet<TaxOrderAggregatedUpdated>(0);
	private Set<EavEntityDecimal> eavEntityDecimals = new HashSet<EavEntityDecimal>(0);
	private Set<CheckoutAgreement> checkoutAgreements = new HashSet<CheckoutAgreement>(0);
	private Set<SalesRecurringProfile> salesRecurringProfiles = new HashSet<SalesRecurringProfile>(0);
	private Set<SalesBillingAgreement> salesBillingAgreements = new HashSet<SalesBillingAgreement>(0);
	private Set<SalesRefundedAggregatedOrder> salesRefundedAggregatedOrders = new HashSet<SalesRefundedAggregatedOrder>(0);
	private Set<CatalogProductIndexEav> catalogProductIndexEavs = new HashSet<CatalogProductIndexEav>(0);
	private Set<SalesOrderStatusLabel> salesOrderStatusLabels = new HashSet<SalesOrderStatusLabel>(0);
	private Set<NewsletterSubscriber> newsletterSubscribers = new HashSet<NewsletterSubscriber>(0);
	private Set<EavFormType> eavFormTypes = new HashSet<EavFormType>(0);
	private Set<CatalogProductEntityMediaGalleryValue> catalogProductEntityMediaGalleryValues = new HashSet<CatalogProductEntityMediaGalleryValue>(0);
	private Set<TagRelation> tagRelations = new HashSet<TagRelation>(0);
	private Set<CatalogProductEntityDatetime> catalogProductEntityDatetimes = new HashSet<CatalogProductEntityDatetime>(0);
	private Set<RatingTitle> ratingTitles = new HashSet<RatingTitle>(0);
	private Set<CatalogProductOptionTypeTitle> catalogProductOptionTypeTitles = new HashSet<CatalogProductOptionTypeTitle>(0);
	private Set<CatalogProductEntityVarchar> catalogProductEntityVarchars = new HashSet<CatalogProductEntityVarchar>(0);
	private Set<UrlRewrite> urlRewrites = new HashSet<UrlRewrite>(0);
	private Set<SalesFlatOrderItem> salesFlatOrderItems = new HashSet<SalesFlatOrderItem>(0);
	private Set<XmlconnectApplication> xmlconnectApplications = new HashSet<XmlconnectApplication>(0);
	private Set<EavAttributeOptionValue> eavAttributeOptionValues = new HashSet<EavAttributeOptionValue>(0);
	private Set<SalesFlatInvoice> salesFlatInvoices = new HashSet<SalesFlatInvoice>(0);
	private Set<CatalogProductEntityText> catalogProductEntityTexts = new HashSet<CatalogProductEntityText>(0);
	private Set<TaxCalculationRateTitle> taxCalculationRateTitles = new HashSet<TaxCalculationRateTitle>(0);
	private Set<SalesBestsellersAggregatedMonthly> salesBestsellersAggregatedMonthlies = new HashSet<SalesBestsellersAggregatedMonthly>(0);
	private Set<CatalogProductIndexEavDecimal> catalogProductIndexEavDecimals = new HashSet<CatalogProductIndexEavDecimal>(0);
	private Set<SalesOrderAggregatedUpdated> salesOrderAggregatedUpdateds = new HashSet<SalesOrderAggregatedUpdated>(0);
	private Set<WishlistItem> wishlistItems = new HashSet<WishlistItem>(0);
	private Set<CatalogProductOptionPrice> catalogProductOptionPrices = new HashSet<CatalogProductOptionPrice>(0);
	private Set<EavAttributeLabel> eavAttributeLabels = new HashSet<EavAttributeLabel>(0);
	private Set<SalesOrderAggregatedCreated> salesOrderAggregatedCreateds = new HashSet<SalesOrderAggregatedCreated>(0);
	private Set<CoreVariableValue> coreVariableValues = new HashSet<CoreVariableValue>(0);
	private Set<Sitemap> sitemaps = new HashSet<Sitemap>(0);
	private Set<CatalogCategoryEntityInt> catalogCategoryEntityInts = new HashSet<CatalogCategoryEntityInt>(0);
	private Set<SalesFlatShipment> salesFlatShipments = new HashSet<SalesFlatShipment>(0);
	private Set<TagProperties> tagPropertieses = new HashSet<TagProperties>(0);
	private Set<ReportComparedProductIndex> reportComparedProductIndexes = new HashSet<ReportComparedProductIndex>(0);
	private Set<Poll> polls = new HashSet<Poll>(0);
	private Set<ReviewDetail> reviewDetails = new HashSet<ReviewDetail>(0);
	private Set<SalesFlatShipmentGrid> salesFlatShipmentGrids = new HashSet<SalesFlatShipmentGrid>(0);
	private Set<CatalogProductOptionTitle> catalogProductOptionTitles = new HashSet<CatalogProductOptionTitle>(0);
	private Set<EavEntityStore> eavEntityStores = new HashSet<EavEntityStore>(0);
	private Set<SalesFlatQuoteItem> salesFlatQuoteItems = new HashSet<SalesFlatQuoteItem>(0);
	private Set<CatalogCategoryEntityText> catalogCategoryEntityTexts = new HashSet<CatalogCategoryEntityText>(0);
	private Set<CatalogProductOptionTypePrice> catalogProductOptionTypePrices = new HashSet<CatalogProductOptionTypePrice>(0);
	private Set<CouponAggregated> couponAggregateds = new HashSet<CouponAggregated>(0);
	private Set<EavEntity> eavEntities = new HashSet<EavEntity>(0);
	private Set<SalesFlatQuote> salesFlatQuotes = new HashSet<SalesFlatQuote>(0);
	private Set<DataflowBatch> dataflowBatches = new HashSet<DataflowBatch>(0);
	private Set<SalesShippingAggregated> salesShippingAggregateds = new HashSet<SalesShippingAggregated>(0);
	private Set<SalesInvoicedAggregatedOrder> salesInvoicedAggregatedOrders = new HashSet<SalesInvoicedAggregatedOrder>(0);
	private Set<DownloadableSampleTitle> downloadableSampleTitles = new HashSet<DownloadableSampleTitle>(0);
	private Set<ReportViewedProductAggregatedYearly> reportViewedProductAggregatedYearlies = new HashSet<ReportViewedProductAggregatedYearly>(0);
	private Set<EavEntityDatetime> eavEntityDatetimes = new HashSet<EavEntityDatetime>(0);
	private Set<CatalogsearchQuery> catalogsearchQueries = new HashSet<CatalogsearchQuery>(0);
	private Set<CatalogCategoryEntityDecimal> catalogCategoryEntityDecimals = new HashSet<CatalogCategoryEntityDecimal>(0);
	private Set<CatalogProductEntityInt> catalogProductEntityInts = new HashSet<CatalogProductEntityInt>(0);
	private Set<CatalogCategoryEntityVarchar> catalogCategoryEntityVarchars = new HashSet<CatalogCategoryEntityVarchar>(0);
	private Set<DownloadableLinkTitle> downloadableLinkTitles = new HashSet<DownloadableLinkTitle>(0);
	private Set<RatingOptionVoteAggregated> ratingOptionVoteAggregateds = new HashSet<RatingOptionVoteAggregated>(0);
	private Set<EavEntityVarchar> eavEntityVarchars = new HashSet<EavEntityVarchar>(0);
	private Set<SalesInvoicedAggregated> salesInvoicedAggregateds = new HashSet<SalesInvoicedAggregated>(0);
	private Set<EavEntityInt> eavEntityInts = new HashSet<EavEntityInt>(0);
	private Set<ReportViewedProductAggregatedDaily> reportViewedProductAggregatedDailies = new HashSet<ReportViewedProductAggregatedDaily>(0);
	private Set<CoreLayoutLink> coreLayoutLinks = new HashSet<CoreLayoutLink>(0);
	private Set<TaxOrderAggregatedCreated> taxOrderAggregatedCreateds = new HashSet<TaxOrderAggregatedCreated>(0);
	private Set<CustomerEntity> customerEntities = new HashSet<CustomerEntity>(0);
	private Set<SalesShippingAggregatedOrder> salesShippingAggregatedOrders = new HashSet<SalesShippingAggregatedOrder>(0);
	private Set<CoreTranslate> coreTranslates = new HashSet<CoreTranslate>(0);
	private Set<Tag> tags = new HashSet<Tag>(0);
	private Set<SalesRefundedAggregated> salesRefundedAggregateds = new HashSet<SalesRefundedAggregated>(0);
	private Set<ReviewEntitySummary> reviewEntitySummaries = new HashSet<ReviewEntitySummary>(0);
	private Set<Review> reviews = new HashSet<Review>(0);
	private Set<SalesFlatInvoiceGrid> salesFlatInvoiceGrids = new HashSet<SalesFlatInvoiceGrid>(0);
	private Set<SalesBestsellersAggregatedYearly> salesBestsellersAggregatedYearlies = new HashSet<SalesBestsellersAggregatedYearly>(0);
	private Set<ReportEvent> reportEvents = new HashSet<ReportEvent>(0);
	private Set<CatalogProductEntityGallery> catalogProductEntityGalleries = new HashSet<CatalogProductEntityGallery>(0);
	private Set<SalesBestsellersAggregatedDaily> salesBestsellersAggregatedDailies = new HashSet<SalesBestsellersAggregatedDaily>(0);
	private Set<Poll> polls_1 = new HashSet<Poll>(0);
	private Set<CouponAggregatedUpdated> couponAggregatedUpdateds = new HashSet<CouponAggregatedUpdated>(0);
	private Set<SalesFlatCreditmemo> salesFlatCreditmemos = new HashSet<SalesFlatCreditmemo>(0);
	private Set<CmsPage> cmsPages = new HashSet<CmsPage>(0);
	private Set<SalesFlatOrderGrid> salesFlatOrderGrids = new HashSet<SalesFlatOrderGrid>(0);
	private Set<NewsletterQueue> newsletterQueues = new HashSet<NewsletterQueue>(0);
	private Set<EavEntityText> eavEntityTexts = new HashSet<EavEntityText>(0);
	private Set<ReportViewedProductIndex> reportViewedProductIndexes = new HashSet<ReportViewedProductIndex>(0);
	private Set<CatalogCategoryEntityDatetime> catalogCategoryEntityDatetimes = new HashSet<CatalogCategoryEntityDatetime>(0);
	private Set<CatalogCategoryProductIndex> catalogCategoryProductIndexes = new HashSet<CatalogCategoryProductIndex>(0);
	private Set<CmsBlock> cmsBlocks = new HashSet<CmsBlock>(0);
	private Set<CatalogProductEntityDecimal> catalogProductEntityDecimals = new HashSet<CatalogProductEntityDecimal>(0);
	private Set<CouponAggregatedOrder> couponAggregatedOrders = new HashSet<CouponAggregatedOrder>(0);
	private Set<CatalogCompareItem> catalogCompareItems = new HashSet<CatalogCompareItem>(0);
	private Set<TagSummary> tagSummaries = new HashSet<TagSummary>(0);
	private Set<DesignChange> designChanges = new HashSet<DesignChange>(0);
	private Set<SalesFlatCreditmemoGrid> salesFlatCreditmemoGrids = new HashSet<SalesFlatCreditmemoGrid>(0);
	private Set<Rating> ratings = new HashSet<Rating>(0);
	private Set<SalesFlatOrder> salesFlatOrders = new HashSet<SalesFlatOrder>(0);
	private Set<SalesruleLabel> salesruleLabels = new HashSet<SalesruleLabel>(0);
	private Set<CatalogProductEnabledIndex> catalogProductEnabledIndexes = new HashSet<CatalogProductEnabledIndex>(0);
	private Set<EavFormFieldsetLabel> eavFormFieldsetLabels = new HashSet<EavFormFieldsetLabel>(0);

	private transient final StoreHelper helper;

	public Store() {
		helper = new StoreHelper(this);
	}

	public Store(Website coreWebsite, StoreGroup coreStoreGroup, String name, short sortOrder, Boolean isActive) {
		this();
		this.website = coreWebsite;
		this.coreStoreGroup = coreStoreGroup;
		this.name = name;
		this.sortOrder = sortOrder;
		this.isActive = isActive;
	}

	public Store(Store other) {
		this();
		this.website = other.website;
		this.coreStoreGroup = other.coreStoreGroup;
		this.code = other.code;
		this.name = other.name;
		this.sortOrder = other.sortOrder;
		this.isActive = other.isActive;
		this.catalogProductSuperAttributeLabels = other.catalogProductSuperAttributeLabels;
		this.reportViewedProductAggregatedMonthlies = other.reportViewedProductAggregatedMonthlies;
		this.taxOrderAggregatedUpdateds = other.taxOrderAggregatedUpdateds;
		this.eavEntityDecimals = other.eavEntityDecimals;
		this.checkoutAgreements = other.checkoutAgreements;
		this.salesRecurringProfiles = other.salesRecurringProfiles;
		this.salesBillingAgreements = other.salesBillingAgreements;
		this.salesRefundedAggregatedOrders = other.salesRefundedAggregatedOrders;
		this.catalogProductIndexEavs = other.catalogProductIndexEavs;
		this.salesOrderStatusLabels = other.salesOrderStatusLabels;
		this.newsletterSubscribers = other.newsletterSubscribers;
		this.eavFormTypes = other.eavFormTypes;
		this.catalogProductEntityMediaGalleryValues = other.catalogProductEntityMediaGalleryValues;
		this.tagRelations = other.tagRelations;
		this.catalogProductEntityDatetimes = other.catalogProductEntityDatetimes;
		this.ratingTitles = other.ratingTitles;
		this.catalogProductOptionTypeTitles = other.catalogProductOptionTypeTitles;
		this.catalogProductEntityVarchars = other.catalogProductEntityVarchars;
		this.urlRewrites = other.urlRewrites;
		this.salesFlatOrderItems = other.salesFlatOrderItems;
		this.xmlconnectApplications = other.xmlconnectApplications;
		this.eavAttributeOptionValues = other.eavAttributeOptionValues;
		this.salesFlatInvoices = other.salesFlatInvoices;
		this.catalogProductEntityTexts = other.catalogProductEntityTexts;
		this.taxCalculationRateTitles = other.taxCalculationRateTitles;
		this.salesBestsellersAggregatedMonthlies = other.salesBestsellersAggregatedMonthlies;
		this.catalogProductIndexEavDecimals = other.catalogProductIndexEavDecimals;
		this.salesOrderAggregatedUpdateds = other.salesOrderAggregatedUpdateds;
		this.wishlistItems = other.wishlistItems;
		this.catalogProductOptionPrices = other.catalogProductOptionPrices;
		this.eavAttributeLabels = other.eavAttributeLabels;
		this.salesOrderAggregatedCreateds = other.salesOrderAggregatedCreateds;
		this.coreVariableValues = other.coreVariableValues;
		this.sitemaps = other.sitemaps;
		this.catalogCategoryEntityInts = other.catalogCategoryEntityInts;
		this.salesFlatShipments = other.salesFlatShipments;
		this.tagPropertieses = other.tagPropertieses;
		this.reportComparedProductIndexes = other.reportComparedProductIndexes;
		this.polls = other.polls;
		this.reviewDetails = other.reviewDetails;
		this.salesFlatShipmentGrids = other.salesFlatShipmentGrids;
		this.catalogProductOptionTitles = other.catalogProductOptionTitles;
		this.eavEntityStores = other.eavEntityStores;
		this.salesFlatQuoteItems = other.salesFlatQuoteItems;
		this.catalogCategoryEntityTexts = other.catalogCategoryEntityTexts;
		this.catalogProductOptionTypePrices = other.catalogProductOptionTypePrices;
		this.couponAggregateds = other.couponAggregateds;
		this.eavEntities = other.eavEntities;
		this.salesFlatQuotes = other.salesFlatQuotes;
		this.dataflowBatches = other.dataflowBatches;
		this.salesShippingAggregateds = other.salesShippingAggregateds;
		this.salesInvoicedAggregatedOrders = other.salesInvoicedAggregatedOrders;
		this.downloadableSampleTitles = other.downloadableSampleTitles;
		this.reportViewedProductAggregatedYearlies = other.reportViewedProductAggregatedYearlies;
		this.eavEntityDatetimes = other.eavEntityDatetimes;
		this.catalogsearchQueries = other.catalogsearchQueries;
		this.catalogCategoryEntityDecimals = other.catalogCategoryEntityDecimals;
		this.catalogProductEntityInts = other.catalogProductEntityInts;
		this.catalogCategoryEntityVarchars = other.catalogCategoryEntityVarchars;
		this.downloadableLinkTitles = other.downloadableLinkTitles;
		this.ratingOptionVoteAggregateds = other.ratingOptionVoteAggregateds;
		this.eavEntityVarchars = other.eavEntityVarchars;
		this.salesInvoicedAggregateds = other.salesInvoicedAggregateds;
		this.eavEntityInts = other.eavEntityInts;
		this.reportViewedProductAggregatedDailies = other.reportViewedProductAggregatedDailies;
		this.coreLayoutLinks = other.coreLayoutLinks;
		this.taxOrderAggregatedCreateds = other.taxOrderAggregatedCreateds;
		this.customerEntities = other.customerEntities;
		this.salesShippingAggregatedOrders = other.salesShippingAggregatedOrders;
		this.coreTranslates = other.coreTranslates;
		this.tags = other.tags;
		this.salesRefundedAggregateds = other.salesRefundedAggregateds;
		this.reviewEntitySummaries = other.reviewEntitySummaries;
		this.reviews = other.reviews;
		this.salesFlatInvoiceGrids = other.salesFlatInvoiceGrids;
		this.salesBestsellersAggregatedYearlies = other.salesBestsellersAggregatedYearlies;
		this.reportEvents = other.reportEvents;
		this.catalogProductEntityGalleries = other.catalogProductEntityGalleries;
		this.salesBestsellersAggregatedDailies = other.salesBestsellersAggregatedDailies;
		this.polls_1 = other.polls_1;
		this.couponAggregatedUpdateds = other.couponAggregatedUpdateds;
		this.salesFlatCreditmemos = other.salesFlatCreditmemos;
		this.cmsPages = other.cmsPages;
		this.salesFlatOrderGrids = other.salesFlatOrderGrids;
		this.newsletterQueues = other.newsletterQueues;
		this.eavEntityTexts = other.eavEntityTexts;
		this.reportViewedProductIndexes = other.reportViewedProductIndexes;
		this.catalogCategoryEntityDatetimes = other.catalogCategoryEntityDatetimes;
		this.catalogCategoryProductIndexes = other.catalogCategoryProductIndexes;
		this.cmsBlocks = other.cmsBlocks;
		this.catalogProductEntityDecimals = other.catalogProductEntityDecimals;
		this.couponAggregatedOrders = other.couponAggregatedOrders;
		this.catalogCompareItems = other.catalogCompareItems;
		this.tagSummaries = other.tagSummaries;
		this.designChanges = other.designChanges;
		this.salesFlatCreditmemoGrids = other.salesFlatCreditmemoGrids;
		this.ratings = other.ratings;
		this.salesFlatOrders = other.salesFlatOrders;
		this.salesruleLabels = other.salesruleLabels;
		this.catalogProductEnabledIndexes = other.catalogProductEnabledIndexes;
		this.eavFormFieldsetLabels = other.eavFormFieldsetLabels;
	}

	@Transient
	public StoreHelper getHelper() {
		return helper;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "store_id", unique = true, nullable = false)
	public Short getStoreId() {
		return this.storeId;
	}

	@Transient
	public Short getId() {
		return storeId;
	}

	public void setStoreId(Short storeId) {
		this.storeId = storeId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "website_id", nullable = false)
	public Website getWebsite() {
		return this.website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id", nullable = false)
	public StoreGroup getCoreStoreGroup() {
		return this.coreStoreGroup;
	}

	public void setCoreStoreGroup(StoreGroup coreStoreGroup) {
		this.coreStoreGroup = coreStoreGroup;
	}

	@Column(name = "code", unique = true, length = 32)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "sort_order", nullable = false)
	public short getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(short sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Column(name = "is_active", nullable = false)
	public boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogProductSuperAttributeLabel> getCatalogProductSuperAttributeLabels() {
		return this.catalogProductSuperAttributeLabels;
	}

	public void setCatalogProductSuperAttributeLabels(Set<CatalogProductSuperAttributeLabel> catalogProductSuperAttributeLabels) {
		this.catalogProductSuperAttributeLabels = catalogProductSuperAttributeLabels;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<ReportViewedProductAggregatedMonthly> getReportViewedProductAggregatedMonthlies() {
		return this.reportViewedProductAggregatedMonthlies;
	}

	public void setReportViewedProductAggregatedMonthlies(Set<ReportViewedProductAggregatedMonthly> reportViewedProductAggregatedMonthlies) {
		this.reportViewedProductAggregatedMonthlies = reportViewedProductAggregatedMonthlies;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<TaxOrderAggregatedUpdated> getTaxOrderAggregatedUpdateds() {
		return this.taxOrderAggregatedUpdateds;
	}

	public void setTaxOrderAggregatedUpdateds(Set<TaxOrderAggregatedUpdated> taxOrderAggregatedUpdateds) {
		this.taxOrderAggregatedUpdateds = taxOrderAggregatedUpdateds;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<EavEntityDecimal> getEavEntityDecimals() {
		return this.eavEntityDecimals;
	}

	public void setEavEntityDecimals(Set<EavEntityDecimal> eavEntityDecimals) {
		this.eavEntityDecimals = eavEntityDecimals;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "coreStores")
	public Set<CheckoutAgreement> getCheckoutAgreements() {
		return this.checkoutAgreements;
	}

	public void setCheckoutAgreements(Set<CheckoutAgreement> checkoutAgreements) {
		this.checkoutAgreements = checkoutAgreements;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesRecurringProfile> getSalesRecurringProfiles() {
		return this.salesRecurringProfiles;
	}

	public void setSalesRecurringProfiles(Set<SalesRecurringProfile> salesRecurringProfiles) {
		this.salesRecurringProfiles = salesRecurringProfiles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesBillingAgreement> getSalesBillingAgreements() {
		return this.salesBillingAgreements;
	}

	public void setSalesBillingAgreements(Set<SalesBillingAgreement> salesBillingAgreements) {
		this.salesBillingAgreements = salesBillingAgreements;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesRefundedAggregatedOrder> getSalesRefundedAggregatedOrders() {
		return this.salesRefundedAggregatedOrders;
	}

	public void setSalesRefundedAggregatedOrders(Set<SalesRefundedAggregatedOrder> salesRefundedAggregatedOrders) {
		this.salesRefundedAggregatedOrders = salesRefundedAggregatedOrders;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogProductIndexEav> getCatalogProductIndexEavs() {
		return this.catalogProductIndexEavs;
	}

	public void setCatalogProductIndexEavs(Set<CatalogProductIndexEav> catalogProductIndexEavs) {
		this.catalogProductIndexEavs = catalogProductIndexEavs;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesOrderStatusLabel> getSalesOrderStatusLabels() {
		return this.salesOrderStatusLabels;
	}

	public void setSalesOrderStatusLabels(Set<SalesOrderStatusLabel> salesOrderStatusLabels) {
		this.salesOrderStatusLabels = salesOrderStatusLabels;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<NewsletterSubscriber> getNewsletterSubscribers() {
		return this.newsletterSubscribers;
	}

	public void setNewsletterSubscribers(Set<NewsletterSubscriber> newsletterSubscribers) {
		this.newsletterSubscribers = newsletterSubscribers;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<EavFormType> getEavFormTypes() {
		return this.eavFormTypes;
	}

	public void setEavFormTypes(Set<EavFormType> eavFormTypes) {
		this.eavFormTypes = eavFormTypes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogProductEntityMediaGalleryValue> getCatalogProductEntityMediaGalleryValues() {
		return this.catalogProductEntityMediaGalleryValues;
	}

	public void setCatalogProductEntityMediaGalleryValues(Set<CatalogProductEntityMediaGalleryValue> catalogProductEntityMediaGalleryValues) {
		this.catalogProductEntityMediaGalleryValues = catalogProductEntityMediaGalleryValues;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<TagRelation> getTagRelations() {
		return this.tagRelations;
	}

	public void setTagRelations(Set<TagRelation> tagRelations) {
		this.tagRelations = tagRelations;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogProductEntityDatetime> getCatalogProductEntityDatetimes() {
		return this.catalogProductEntityDatetimes;
	}

	public void setCatalogProductEntityDatetimes(Set<CatalogProductEntityDatetime> catalogProductEntityDatetimes) {
		this.catalogProductEntityDatetimes = catalogProductEntityDatetimes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<RatingTitle> getRatingTitles() {
		return this.ratingTitles;
	}

	public void setRatingTitles(Set<RatingTitle> ratingTitles) {
		this.ratingTitles = ratingTitles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogProductOptionTypeTitle> getCatalogProductOptionTypeTitles() {
		return this.catalogProductOptionTypeTitles;
	}

	public void setCatalogProductOptionTypeTitles(Set<CatalogProductOptionTypeTitle> catalogProductOptionTypeTitles) {
		this.catalogProductOptionTypeTitles = catalogProductOptionTypeTitles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogProductEntityVarchar> getCatalogProductEntityVarchars() {
		return this.catalogProductEntityVarchars;
	}

	public void setCatalogProductEntityVarchars(Set<CatalogProductEntityVarchar> catalogProductEntityVarchars) {
		this.catalogProductEntityVarchars = catalogProductEntityVarchars;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "store")
	public Set<UrlRewrite> getUrlRewrites() {
		return this.urlRewrites;
	}

	public void setUrlRewrites(Set<UrlRewrite> urlRewrites) {
		this.urlRewrites = urlRewrites;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesFlatOrderItem> getSalesFlatOrderItems() {
		return this.salesFlatOrderItems;
	}

	public void setSalesFlatOrderItems(Set<SalesFlatOrderItem> salesFlatOrderItems) {
		this.salesFlatOrderItems = salesFlatOrderItems;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<XmlconnectApplication> getXmlconnectApplications() {
		return this.xmlconnectApplications;
	}

	public void setXmlconnectApplications(Set<XmlconnectApplication> xmlconnectApplications) {
		this.xmlconnectApplications = xmlconnectApplications;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<EavAttributeOptionValue> getEavAttributeOptionValues() {
		return this.eavAttributeOptionValues;
	}

	public void setEavAttributeOptionValues(Set<EavAttributeOptionValue> eavAttributeOptionValues) {
		this.eavAttributeOptionValues = eavAttributeOptionValues;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesFlatInvoice> getSalesFlatInvoices() {
		return this.salesFlatInvoices;
	}

	public void setSalesFlatInvoices(Set<SalesFlatInvoice> salesFlatInvoices) {
		this.salesFlatInvoices = salesFlatInvoices;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogProductEntityText> getCatalogProductEntityTexts() {
		return this.catalogProductEntityTexts;
	}

	public void setCatalogProductEntityTexts(Set<CatalogProductEntityText> catalogProductEntityTexts) {
		this.catalogProductEntityTexts = catalogProductEntityTexts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<TaxCalculationRateTitle> getTaxCalculationRateTitles() {
		return this.taxCalculationRateTitles;
	}

	public void setTaxCalculationRateTitles(Set<TaxCalculationRateTitle> taxCalculationRateTitles) {
		this.taxCalculationRateTitles = taxCalculationRateTitles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesBestsellersAggregatedMonthly> getSalesBestsellersAggregatedMonthlies() {
		return this.salesBestsellersAggregatedMonthlies;
	}

	public void setSalesBestsellersAggregatedMonthlies(Set<SalesBestsellersAggregatedMonthly> salesBestsellersAggregatedMonthlies) {
		this.salesBestsellersAggregatedMonthlies = salesBestsellersAggregatedMonthlies;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogProductIndexEavDecimal> getCatalogProductIndexEavDecimals() {
		return this.catalogProductIndexEavDecimals;
	}

	public void setCatalogProductIndexEavDecimals(Set<CatalogProductIndexEavDecimal> catalogProductIndexEavDecimals) {
		this.catalogProductIndexEavDecimals = catalogProductIndexEavDecimals;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesOrderAggregatedUpdated> getSalesOrderAggregatedUpdateds() {
		return this.salesOrderAggregatedUpdateds;
	}

	public void setSalesOrderAggregatedUpdateds(Set<SalesOrderAggregatedUpdated> salesOrderAggregatedUpdateds) {
		this.salesOrderAggregatedUpdateds = salesOrderAggregatedUpdateds;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<WishlistItem> getWishlistItems() {
		return this.wishlistItems;
	}

	public void setWishlistItems(Set<WishlistItem> wishlistItems) {
		this.wishlistItems = wishlistItems;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogProductOptionPrice> getCatalogProductOptionPrices() {
		return this.catalogProductOptionPrices;
	}

	public void setCatalogProductOptionPrices(Set<CatalogProductOptionPrice> catalogProductOptionPrices) {
		this.catalogProductOptionPrices = catalogProductOptionPrices;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<EavAttributeLabel> getEavAttributeLabels() {
		return this.eavAttributeLabels;
	}

	public void setEavAttributeLabels(Set<EavAttributeLabel> eavAttributeLabels) {
		this.eavAttributeLabels = eavAttributeLabels;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesOrderAggregatedCreated> getSalesOrderAggregatedCreateds() {
		return this.salesOrderAggregatedCreateds;
	}

	public void setSalesOrderAggregatedCreateds(Set<SalesOrderAggregatedCreated> salesOrderAggregatedCreateds) {
		this.salesOrderAggregatedCreateds = salesOrderAggregatedCreateds;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CoreVariableValue> getCoreVariableValues() {
		return this.coreVariableValues;
	}

	public void setCoreVariableValues(Set<CoreVariableValue> coreVariableValues) {
		this.coreVariableValues = coreVariableValues;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<Sitemap> getSitemaps() {
		return this.sitemaps;
	}

	public void setSitemaps(Set<Sitemap> sitemaps) {
		this.sitemaps = sitemaps;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogCategoryEntityInt> getCatalogCategoryEntityInts() {
		return this.catalogCategoryEntityInts;
	}

	public void setCatalogCategoryEntityInts(Set<CatalogCategoryEntityInt> catalogCategoryEntityInts) {
		this.catalogCategoryEntityInts = catalogCategoryEntityInts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesFlatShipment> getSalesFlatShipments() {
		return this.salesFlatShipments;
	}

	public void setSalesFlatShipments(Set<SalesFlatShipment> salesFlatShipments) {
		this.salesFlatShipments = salesFlatShipments;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<TagProperties> getTagPropertieses() {
		return this.tagPropertieses;
	}

	public void setTagPropertieses(Set<TagProperties> tagPropertieses) {
		this.tagPropertieses = tagPropertieses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<ReportComparedProductIndex> getReportComparedProductIndexes() {
		return this.reportComparedProductIndexes;
	}

	public void setReportComparedProductIndexes(Set<ReportComparedProductIndex> reportComparedProductIndexes) {
		this.reportComparedProductIndexes = reportComparedProductIndexes;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "coreStores")
	public Set<Poll> getPolls() {
		return this.polls;
	}

	public void setPolls(Set<Poll> polls) {
		this.polls = polls;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<ReviewDetail> getReviewDetails() {
		return this.reviewDetails;
	}

	public void setReviewDetails(Set<ReviewDetail> reviewDetails) {
		this.reviewDetails = reviewDetails;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesFlatShipmentGrid> getSalesFlatShipmentGrids() {
		return this.salesFlatShipmentGrids;
	}

	public void setSalesFlatShipmentGrids(Set<SalesFlatShipmentGrid> salesFlatShipmentGrids) {
		this.salesFlatShipmentGrids = salesFlatShipmentGrids;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogProductOptionTitle> getCatalogProductOptionTitles() {
		return this.catalogProductOptionTitles;
	}

	public void setCatalogProductOptionTitles(Set<CatalogProductOptionTitle> catalogProductOptionTitles) {
		this.catalogProductOptionTitles = catalogProductOptionTitles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<EavEntityStore> getEavEntityStores() {
		return this.eavEntityStores;
	}

	public void setEavEntityStores(Set<EavEntityStore> eavEntityStores) {
		this.eavEntityStores = eavEntityStores;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesFlatQuoteItem> getSalesFlatQuoteItems() {
		return this.salesFlatQuoteItems;
	}

	public void setSalesFlatQuoteItems(Set<SalesFlatQuoteItem> salesFlatQuoteItems) {
		this.salesFlatQuoteItems = salesFlatQuoteItems;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogCategoryEntityText> getCatalogCategoryEntityTexts() {
		return this.catalogCategoryEntityTexts;
	}

	public void setCatalogCategoryEntityTexts(Set<CatalogCategoryEntityText> catalogCategoryEntityTexts) {
		this.catalogCategoryEntityTexts = catalogCategoryEntityTexts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogProductOptionTypePrice> getCatalogProductOptionTypePrices() {
		return this.catalogProductOptionTypePrices;
	}

	public void setCatalogProductOptionTypePrices(Set<CatalogProductOptionTypePrice> catalogProductOptionTypePrices) {
		this.catalogProductOptionTypePrices = catalogProductOptionTypePrices;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CouponAggregated> getCouponAggregateds() {
		return this.couponAggregateds;
	}

	public void setCouponAggregateds(Set<CouponAggregated> couponAggregateds) {
		this.couponAggregateds = couponAggregateds;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<EavEntity> getEavEntities() {
		return this.eavEntities;
	}

	public void setEavEntities(Set<EavEntity> eavEntities) {
		this.eavEntities = eavEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesFlatQuote> getSalesFlatQuotes() {
		return this.salesFlatQuotes;
	}

	public void setSalesFlatQuotes(Set<SalesFlatQuote> salesFlatQuotes) {
		this.salesFlatQuotes = salesFlatQuotes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<DataflowBatch> getDataflowBatches() {
		return this.dataflowBatches;
	}

	public void setDataflowBatches(Set<DataflowBatch> dataflowBatches) {
		this.dataflowBatches = dataflowBatches;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesShippingAggregated> getSalesShippingAggregateds() {
		return this.salesShippingAggregateds;
	}

	public void setSalesShippingAggregateds(Set<SalesShippingAggregated> salesShippingAggregateds) {
		this.salesShippingAggregateds = salesShippingAggregateds;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesInvoicedAggregatedOrder> getSalesInvoicedAggregatedOrders() {
		return this.salesInvoicedAggregatedOrders;
	}

	public void setSalesInvoicedAggregatedOrders(Set<SalesInvoicedAggregatedOrder> salesInvoicedAggregatedOrders) {
		this.salesInvoicedAggregatedOrders = salesInvoicedAggregatedOrders;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<DownloadableSampleTitle> getDownloadableSampleTitles() {
		return this.downloadableSampleTitles;
	}

	public void setDownloadableSampleTitles(Set<DownloadableSampleTitle> downloadableSampleTitles) {
		this.downloadableSampleTitles = downloadableSampleTitles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<ReportViewedProductAggregatedYearly> getReportViewedProductAggregatedYearlies() {
		return this.reportViewedProductAggregatedYearlies;
	}

	public void setReportViewedProductAggregatedYearlies(Set<ReportViewedProductAggregatedYearly> reportViewedProductAggregatedYearlies) {
		this.reportViewedProductAggregatedYearlies = reportViewedProductAggregatedYearlies;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<EavEntityDatetime> getEavEntityDatetimes() {
		return this.eavEntityDatetimes;
	}

	public void setEavEntityDatetimes(Set<EavEntityDatetime> eavEntityDatetimes) {
		this.eavEntityDatetimes = eavEntityDatetimes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogsearchQuery> getCatalogsearchQueries() {
		return this.catalogsearchQueries;
	}

	public void setCatalogsearchQueries(Set<CatalogsearchQuery> catalogsearchQueries) {
		this.catalogsearchQueries = catalogsearchQueries;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogCategoryEntityDecimal> getCatalogCategoryEntityDecimals() {
		return this.catalogCategoryEntityDecimals;
	}

	public void setCatalogCategoryEntityDecimals(Set<CatalogCategoryEntityDecimal> catalogCategoryEntityDecimals) {
		this.catalogCategoryEntityDecimals = catalogCategoryEntityDecimals;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogProductEntityInt> getCatalogProductEntityInts() {
		return this.catalogProductEntityInts;
	}

	public void setCatalogProductEntityInts(Set<CatalogProductEntityInt> catalogProductEntityInts) {
		this.catalogProductEntityInts = catalogProductEntityInts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogCategoryEntityVarchar> getCatalogCategoryEntityVarchars() {
		return this.catalogCategoryEntityVarchars;
	}

	public void setCatalogCategoryEntityVarchars(Set<CatalogCategoryEntityVarchar> catalogCategoryEntityVarchars) {
		this.catalogCategoryEntityVarchars = catalogCategoryEntityVarchars;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<DownloadableLinkTitle> getDownloadableLinkTitles() {
		return this.downloadableLinkTitles;
	}

	public void setDownloadableLinkTitles(Set<DownloadableLinkTitle> downloadableLinkTitles) {
		this.downloadableLinkTitles = downloadableLinkTitles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<RatingOptionVoteAggregated> getRatingOptionVoteAggregateds() {
		return this.ratingOptionVoteAggregateds;
	}

	public void setRatingOptionVoteAggregateds(Set<RatingOptionVoteAggregated> ratingOptionVoteAggregateds) {
		this.ratingOptionVoteAggregateds = ratingOptionVoteAggregateds;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<EavEntityVarchar> getEavEntityVarchars() {
		return this.eavEntityVarchars;
	}

	public void setEavEntityVarchars(Set<EavEntityVarchar> eavEntityVarchars) {
		this.eavEntityVarchars = eavEntityVarchars;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesInvoicedAggregated> getSalesInvoicedAggregateds() {
		return this.salesInvoicedAggregateds;
	}

	public void setSalesInvoicedAggregateds(Set<SalesInvoicedAggregated> salesInvoicedAggregateds) {
		this.salesInvoicedAggregateds = salesInvoicedAggregateds;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<EavEntityInt> getEavEntityInts() {
		return this.eavEntityInts;
	}

	public void setEavEntityInts(Set<EavEntityInt> eavEntityInts) {
		this.eavEntityInts = eavEntityInts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<ReportViewedProductAggregatedDaily> getReportViewedProductAggregatedDailies() {
		return this.reportViewedProductAggregatedDailies;
	}

	public void setReportViewedProductAggregatedDailies(Set<ReportViewedProductAggregatedDaily> reportViewedProductAggregatedDailies) {
		this.reportViewedProductAggregatedDailies = reportViewedProductAggregatedDailies;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CoreLayoutLink> getCoreLayoutLinks() {
		return this.coreLayoutLinks;
	}

	public void setCoreLayoutLinks(Set<CoreLayoutLink> coreLayoutLinks) {
		this.coreLayoutLinks = coreLayoutLinks;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<TaxOrderAggregatedCreated> getTaxOrderAggregatedCreateds() {
		return this.taxOrderAggregatedCreateds;
	}

	public void setTaxOrderAggregatedCreateds(Set<TaxOrderAggregatedCreated> taxOrderAggregatedCreateds) {
		this.taxOrderAggregatedCreateds = taxOrderAggregatedCreateds;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CustomerEntity> getCustomerEntities() {
		return this.customerEntities;
	}

	public void setCustomerEntities(Set<CustomerEntity> customerEntities) {
		this.customerEntities = customerEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesShippingAggregatedOrder> getSalesShippingAggregatedOrders() {
		return this.salesShippingAggregatedOrders;
	}

	public void setSalesShippingAggregatedOrders(Set<SalesShippingAggregatedOrder> salesShippingAggregatedOrders) {
		this.salesShippingAggregatedOrders = salesShippingAggregatedOrders;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CoreTranslate> getCoreTranslates() {
		return this.coreTranslates;
	}

	public void setCoreTranslates(Set<CoreTranslate> coreTranslates) {
		this.coreTranslates = coreTranslates;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<Tag> getTags() {
		return this.tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesRefundedAggregated> getSalesRefundedAggregateds() {
		return this.salesRefundedAggregateds;
	}

	public void setSalesRefundedAggregateds(Set<SalesRefundedAggregated> salesRefundedAggregateds) {
		this.salesRefundedAggregateds = salesRefundedAggregateds;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<ReviewEntitySummary> getReviewEntitySummaries() {
		return this.reviewEntitySummaries;
	}

	public void setReviewEntitySummaries(Set<ReviewEntitySummary> reviewEntitySummaries) {
		this.reviewEntitySummaries = reviewEntitySummaries;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "coreStores")
	public Set<Review> getReviews() {
		return this.reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesFlatInvoiceGrid> getSalesFlatInvoiceGrids() {
		return this.salesFlatInvoiceGrids;
	}

	public void setSalesFlatInvoiceGrids(Set<SalesFlatInvoiceGrid> salesFlatInvoiceGrids) {
		this.salesFlatInvoiceGrids = salesFlatInvoiceGrids;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesBestsellersAggregatedYearly> getSalesBestsellersAggregatedYearlies() {
		return this.salesBestsellersAggregatedYearlies;
	}

	public void setSalesBestsellersAggregatedYearlies(Set<SalesBestsellersAggregatedYearly> salesBestsellersAggregatedYearlies) {
		this.salesBestsellersAggregatedYearlies = salesBestsellersAggregatedYearlies;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<ReportEvent> getReportEvents() {
		return this.reportEvents;
	}

	public void setReportEvents(Set<ReportEvent> reportEvents) {
		this.reportEvents = reportEvents;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogProductEntityGallery> getCatalogProductEntityGalleries() {
		return this.catalogProductEntityGalleries;
	}

	public void setCatalogProductEntityGalleries(Set<CatalogProductEntityGallery> catalogProductEntityGalleries) {
		this.catalogProductEntityGalleries = catalogProductEntityGalleries;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesBestsellersAggregatedDaily> getSalesBestsellersAggregatedDailies() {
		return this.salesBestsellersAggregatedDailies;
	}

	public void setSalesBestsellersAggregatedDailies(Set<SalesBestsellersAggregatedDaily> salesBestsellersAggregatedDailies) {
		this.salesBestsellersAggregatedDailies = salesBestsellersAggregatedDailies;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<Poll> getPolls_1() {
		return this.polls_1;
	}

	public void setPolls_1(Set<Poll> polls_1) {
		this.polls_1 = polls_1;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CouponAggregatedUpdated> getCouponAggregatedUpdateds() {
		return this.couponAggregatedUpdateds;
	}

	public void setCouponAggregatedUpdateds(Set<CouponAggregatedUpdated> couponAggregatedUpdateds) {
		this.couponAggregatedUpdateds = couponAggregatedUpdateds;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesFlatCreditmemo> getSalesFlatCreditmemos() {
		return this.salesFlatCreditmemos;
	}

	public void setSalesFlatCreditmemos(Set<SalesFlatCreditmemo> salesFlatCreditmemos) {
		this.salesFlatCreditmemos = salesFlatCreditmemos;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "coreStores")
	public Set<CmsPage> getCmsPages() {
		return this.cmsPages;
	}

	public void setCmsPages(Set<CmsPage> cmsPages) {
		this.cmsPages = cmsPages;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesFlatOrderGrid> getSalesFlatOrderGrids() {
		return this.salesFlatOrderGrids;
	}

	public void setSalesFlatOrderGrids(Set<SalesFlatOrderGrid> salesFlatOrderGrids) {
		this.salesFlatOrderGrids = salesFlatOrderGrids;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "coreStores")
	public Set<NewsletterQueue> getNewsletterQueues() {
		return this.newsletterQueues;
	}

	public void setNewsletterQueues(Set<NewsletterQueue> newsletterQueues) {
		this.newsletterQueues = newsletterQueues;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<EavEntityText> getEavEntityTexts() {
		return this.eavEntityTexts;
	}

	public void setEavEntityTexts(Set<EavEntityText> eavEntityTexts) {
		this.eavEntityTexts = eavEntityTexts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<ReportViewedProductIndex> getReportViewedProductIndexes() {
		return this.reportViewedProductIndexes;
	}

	public void setReportViewedProductIndexes(Set<ReportViewedProductIndex> reportViewedProductIndexes) {
		this.reportViewedProductIndexes = reportViewedProductIndexes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogCategoryEntityDatetime> getCatalogCategoryEntityDatetimes() {
		return this.catalogCategoryEntityDatetimes;
	}

	public void setCatalogCategoryEntityDatetimes(Set<CatalogCategoryEntityDatetime> catalogCategoryEntityDatetimes) {
		this.catalogCategoryEntityDatetimes = catalogCategoryEntityDatetimes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogCategoryProductIndex> getCatalogCategoryProductIndexes() {
		return this.catalogCategoryProductIndexes;
	}

	public void setCatalogCategoryProductIndexes(Set<CatalogCategoryProductIndex> catalogCategoryProductIndexes) {
		this.catalogCategoryProductIndexes = catalogCategoryProductIndexes;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "coreStores")
	public Set<CmsBlock> getCmsBlocks() {
		return this.cmsBlocks;
	}

	public void setCmsBlocks(Set<CmsBlock> cmsBlocks) {
		this.cmsBlocks = cmsBlocks;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogProductEntityDecimal> getCatalogProductEntityDecimals() {
		return this.catalogProductEntityDecimals;
	}

	public void setCatalogProductEntityDecimals(Set<CatalogProductEntityDecimal> catalogProductEntityDecimals) {
		this.catalogProductEntityDecimals = catalogProductEntityDecimals;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CouponAggregatedOrder> getCouponAggregatedOrders() {
		return this.couponAggregatedOrders;
	}

	public void setCouponAggregatedOrders(Set<CouponAggregatedOrder> couponAggregatedOrders) {
		this.couponAggregatedOrders = couponAggregatedOrders;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogCompareItem> getCatalogCompareItems() {
		return this.catalogCompareItems;
	}

	public void setCatalogCompareItems(Set<CatalogCompareItem> catalogCompareItems) {
		this.catalogCompareItems = catalogCompareItems;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<TagSummary> getTagSummaries() {
		return this.tagSummaries;
	}

	public void setTagSummaries(Set<TagSummary> tagSummaries) {
		this.tagSummaries = tagSummaries;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "store")
	public Set<DesignChange> getDesignChanges() {
		return this.designChanges;
	}

	public void setDesignChanges(Set<DesignChange> designChanges) {
		this.designChanges = designChanges;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesFlatCreditmemoGrid> getSalesFlatCreditmemoGrids() {
		return this.salesFlatCreditmemoGrids;
	}

	public void setSalesFlatCreditmemoGrids(Set<SalesFlatCreditmemoGrid> salesFlatCreditmemoGrids) {
		this.salesFlatCreditmemoGrids = salesFlatCreditmemoGrids;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "coreStores")
	public Set<Rating> getRatings() {
		return this.ratings;
	}

	public void setRatings(Set<Rating> ratings) {
		this.ratings = ratings;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesFlatOrder> getSalesFlatOrders() {
		return this.salesFlatOrders;
	}

	public void setSalesFlatOrders(Set<SalesFlatOrder> salesFlatOrders) {
		this.salesFlatOrders = salesFlatOrders;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<SalesruleLabel> getSalesruleLabels() {
		return this.salesruleLabels;
	}

	public void setSalesruleLabels(Set<SalesruleLabel> salesruleLabels) {
		this.salesruleLabels = salesruleLabels;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<CatalogProductEnabledIndex> getCatalogProductEnabledIndexes() {
		return this.catalogProductEnabledIndexes;
	}

	public void setCatalogProductEnabledIndexes(Set<CatalogProductEnabledIndex> catalogProductEnabledIndexes) {
		this.catalogProductEnabledIndexes = catalogProductEnabledIndexes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coreStore")
	public Set<EavFormFieldsetLabel> getEavFormFieldsetLabels() {
		return this.eavFormFieldsetLabels;
	}

	public void setEavFormFieldsetLabels(Set<EavFormFieldsetLabel> eavFormFieldsetLabels) {
		this.eavFormFieldsetLabels = eavFormFieldsetLabels;
	}
}
