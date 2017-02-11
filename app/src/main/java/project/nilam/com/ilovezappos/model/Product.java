package project.nilam.com.ilovezappos.model;


public class Product {

    private String brandName;
    private String thumbnailImageUrl;
    private long productId;

    private String originalPrice;
    private long styleId;
    private long colorId;

    private String price;

    private String percentOff;
    private String productUrl;

    private String productName;

    public Product() {
    }


    public String getProductId() {
        return String.valueOf(productId);
    }

    public String getProductName() {
        return productName;
    }

    public String getPrice() {
        return price;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

}