import com.opencsv.CSVWriter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;

public class App extends Application {

    private TableView<Product> tableView;
    private TextField urlFileField;
    private Button browseButton, scrapeButton;
    private File urlFile;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Amazon Scraper");

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        // Top panel
        ToolBar toolBar = new ToolBar();
        urlFileField = new TextField();
        urlFileField.setPromptText("Select URL File");
        browseButton = new Button("Browse");
        scrapeButton = new Button("Scrape");

        toolBar.getItems().addAll(urlFileField, browseButton, scrapeButton);
        root.setTop(toolBar);

        // TableView
        tableView = new TableView<>();
        TableColumn<Product, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());

        TableColumn<Product, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());

        TableColumn<Product, String> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().ratingProperty());

        TableColumn<Product, String> reviewCountColumn = new TableColumn<>("Total Reviews");
        reviewCountColumn.setCellValueFactory(cellData -> cellData.getValue().reviewCountProperty());

        TableColumn<Product, String> availabilityColumn = new TableColumn<>("Availability");
        availabilityColumn.setCellValueFactory(cellData -> cellData.getValue().availabilityProperty());

        tableView.getColumns().addAll(titleColumn, priceColumn, ratingColumn, reviewCountColumn, availabilityColumn);
        root.setCenter(tableView);

        // Button Actions
        browseButton.setOnAction(e -> handleBrowse());
        scrapeButton.setOnAction(e -> handleScrape());

        primaryStage.show();
    }

    private void handleBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        urlFile = fileChooser.showOpenDialog(null);
        if (urlFile != null) {
            urlFileField.setText(urlFile.getAbsolutePath());
        }
    }

    private void handleScrape() {
        if (urlFile == null) {
            showAlert(Alert.AlertType.ERROR, "No File Selected", "Please select a URL file.");
            return;
        }

        ObservableList<Product> products = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(urlFile))) {
            String url;
            while ((url = br.readLine()) != null) {
                Product product = scrapeProduct(url);
                if (product != null) {
                    products.add(product);
                }
            }

            // Update TableView
            tableView.setItems(products);

            // Write to CSV
            writeToCSV(products);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while processing the file.");
        }
    }

    private Product scrapeProduct(String URL) {
        final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36";
        final String ACCEPT_LANGUAGE = "en-US,en;q=0.5";

        try {
            Document doc = Jsoup.connect(URL)
                    .userAgent(USER_AGENT)
                    .header("Accept-Language", ACCEPT_LANGUAGE)
                    .get();

            String title = extractText(doc, "span#productTitle", "NA").replace(",", "");
            String price = extractText(doc, "span#priceblock_ourprice", "NA").replace(",", "");
            String rating = extractText(doc, "i.a-icon.a-icon-star.a-star-4-5 > span.a-icon-alt", "NA").replace(",", "");
            if (rating.equals("NA")) {
                rating = extractText(doc, "span.a-icon-alt", "NA").replace(",", "");
            }
            String reviewCount = extractText(doc, "span#acrCustomerReviewText", "NA").replace(",", "");
            String availability = extractText(doc, "div#availability > span", "NA").replace(",", "");

            return new Product(title, price, rating, reviewCount, availability);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String extractText(Document doc, String cssQuery, String defaultValue) {
        Element element = doc.selectFirst(cssQuery);
        return element != null ? element.text().trim() : defaultValue;
    }

    private void writeToCSV(ObservableList<Product> products) {
        try (CSVWriter writer = new CSVWriter(new FileWriter("out.csv", true))) {
            String[] header = {"Product Title", "Price", "Rating", "Total Reviews", "Availability"};
            writer.writeNext(header);
            for (Product product : products) {
                String[] data = {
                        product.getTitle(),
                        product.getPrice(),
                        product.getRating(),
                        product.getReviewCount(),
                        product.getAvailability()
                };
                writer.writeNext(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
