package pck.java.fe.manager.modal;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProdController implements Initializable {
    public TextField prodName;
    public TextField prodPrice;
    public ImageView imageView;
    public Button btnSelectImg;
    public Button btnAddProduct;
    public ComboBox prodUnit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void onBtnAddProductClicked(ActionEvent ae) {
        String name = prodName.getText(),
                unit = String.valueOf(prodUnit.getValue());
        Double price = Double.valueOf(prodPrice.getText());
    }

}
