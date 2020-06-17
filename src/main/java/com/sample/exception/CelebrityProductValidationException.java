package com.sample.exception;

public class CelebrityProductValidationException extends RuntimeException {

	public CelebrityProductValidationException(String celebrityProductSearchValidationException) {
		super(celebrityProductSearchValidationException);
	}

	public CelebrityProductValidationException(String message, Exception e) {
		super(message, e);
	}

    private static final long serialVersionUID = 580467609419437897L;

    public static final String CELEBRITY_PRODUCT_SEARCH_VALIDATION_EXCEPTION = "validation failed during celebrity product search";

    public static final String CELEBRITY_PRODUCT_UPDATE_VALIDATION_EXCEPTION = "validation failed during celebrity product update";

    public static final String CELEBRITY_PRODUCT_UPDATE_PRODUCT_ID_VALIDATION_EXCEPTION = "validation failed as can't have multiple product Ids for sort order update";
    //	public static final String CELEBRITY_PRODUCT_UPDATE_STATUS_ONLINE_EXCEPTION = "Product cannot be marked exclusive as it is online with multiple celebrities";
    public static final String CELEBRITY_PRODUCT_UPDATE_STATUS_ONLINE_EXCEPTION = "Product cannot be marked 'Exclusive' as it is 'Online' with other celebrities. In order to make this product Exclusive with this Celebrity, first mark this product id as 'Offline' And 'Not Exclusive' from other Celebrities";
    public static final String CELEBRITY_PRODUCT_UPDATE_EXCLUSIVE_WITH_OTHERS_EXCEPTION = "Product cannot be marked Exclusive OR Online as it is Exclusive with Celebrity ";
    public static final String CELEBRITY_PRODUCT_BUNDLE_UPDATE_EXCLUSIVE_WITH_OTHERS_EXCEPTION = "Product cannot be marked exclusive or online as some of the child products are exclusive with other Celebrities";
    public static final String CELEBRITY_PRODUCT_PARENT_BUNDLE_UPDATE_EXCLUSIVE_WITH_OTHERS_EXCEPTION = "Product cannot be marked exclusive or online as it is part of bundle product which is exclusive with other Celebrity";

    public static final String CELEBRITY_PRODUCT_CANNOT_MARK_EXCLUSIVE_DYNAMIC = "Product (sku : %s) cannot be marked 'Exclusive' as it is 'Online' with Celebrity with ID: %s";
    public static final String CELEBRITY_PRODUCT_CANNOT_MARK_EXCLUSIVE_OR_ONLINE_DYNAMIC = "Product (sku : %s) cannot be marked Exclusive OR Online as it is Exclusive with Celebrity with ID : %s ";
    public static final String CELEBRITY_PRODUCT_BUNDLE_CHILD_EXCLUSIVE_WITH_OTHERS_EXCEPTION_DYNAMIC = "Product (sku : %s) cannot be marked exclusive and online as some of the child products are exclusive with other Celebrity with ID: %s";
    public static final String CELEBRITY_PRODUCT_BUNDLE_PARENT_EXCLUSIVE_WITH_OTHERS_EXCEPTION_DYNAMIC = "Product (sku : %s) cannot be marked exclusive and online as it is part of bundle product which is exclusive with other Celebrity with ID: %s";
    public static final String CELEBRITY_PRODUCT_UPDATE_LABEL_USED_EXCEPTION = "The label number (%s) is already used with another product (id: %s) under this same celebrity.";

	public class CELEBRITY_PRODUCT_MAP_PARENT_CHILD {
		public static final String EMPTY_REQUEST_EXCEPTION = "No product IDs to map";

	}
}
