import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {
    private final StringProperty title;
    private final StringProperty price;
    private final StringProperty rating;
    private final StringProperty reviewCount;
    private final StringProperty availability;

    public Product(String title, String price, String rating, String reviewCount, String availability) {
        this.title = new SimpleStringProperty(title);
        this.price = new SimpleStringProperty(price);
        this.rating = new SimpleStringProperty(rating);
        this.reviewCount = new SimpleStringProperty(reviewCount);
        this.availability = new SimpleStringProperty(availability);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getPrice() {
        return price.get();
    }

    public StringProperty priceProperty() {
        return price;
    }

    public String getRating() {
        return rating.get();
    }

    public StringProperty ratingProperty() {
        return rating;
    }

    public String getReviewCount() {
        return reviewCount.get();
    }

    public StringProperty reviewCountProperty() {
        return reviewCount;
    }

    public String getAvailability() {
        return availability.get();
    }

    public StringProperty availabilityProperty() {
        return availability;
    }
}
