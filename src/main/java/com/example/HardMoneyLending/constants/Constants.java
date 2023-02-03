package com.example.HardMoneyLending.constants;

public class Constants {

    public static final String DOWNLOAD_PROPERTY_FILE_URL = "api/v1/property/download?filename=";
    public static final String DOWNLOAD_PROFILE_PIC_URL = "api/v1/file/download?filename=";

    public static final String BORROWER_TYPE = "borrower";
    public static final String INVESTOR_TYPE = "investor";
    public static final String PROPERTY_TYPE_TYPE = "property_type";
    public static final String PROPERTY_IMAGE_TYPE = "property_image_type";
    public static final String PROPERTY_FILE_TYPE = "property_file_type";

    public static final String INVESTOR_PROFILE_PIC_DIRECTORY = "//investor-profile-pics";
    public static final String BORROWER_PROFILE_PIC_DIRECTORY = "//borrower-profile-pics";

    public static final String BORROWER_PROPERTY_IMAGE_DIRECTORY = "//borrower-property-images";
    public static final String BORROWER_PROPERTY_FILE_DIRECTORY = "//borrower-property-files";

    public static final String PROPERTY_TYPE_ICON_IMAGE = "PROPERTY_TYPE_ICON";
    public static final String INVESTOR_PROFILE_IMAGE = "INVESTOR_PROFILE_IMAGE";
    public static final String BORROWER_PROFILE_IMAGE = "BORROWER_PROFILE_IMAGE";

    public static final String PROPERTY_IMAGE = "PROPERTY_IMAGE";
    public static final String PROPERTY_FILE = "PROPERTY_FILE";

    public static String COGNITO_ID;

    public static String getUserCognitoId() {
        return COGNITO_ID;
    }

    public static void setUserCognitoId(String id) {
        COGNITO_ID = id;
    }

}
