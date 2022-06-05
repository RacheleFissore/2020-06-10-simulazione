/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;
	private boolean entrato = false;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	txtResult.clear();
    	if(entrato && boxAttore.getValue() != null) {
    		Actor actor = boxAttore.getValue();
    		txtResult.appendText("ATTORI SIMILI A: " + actor.toString() + "\n");
    		for(Actor actor2 : model.getAttoriConnessi(actor)) {
    			txtResult.appendText(actor2.toString() + "\n");
    		}
    	}
    	else {
    		txtResult.appendText("Selezionare un attore e creare prima il grafo");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	if(boxGenere.getValue() != null) {
    		entrato = true;
       		model.creaGrafo(boxGenere.getValue());
       		boxAttore.getItems().addAll(model.getAttori());
    		txtResult.appendText("Grafo creato!\n");
    		txtResult.appendText("# Vertici: " + model.getNVertici());
    		txtResult.appendText("\n# Archi: " + model.getNArchi());
    	}
    	else {
    		txtResult.appendText("Selezionare un genere");
    	}
    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	txtResult.clear();
    	if(entrato) {
    		try {
				int n = Integer.parseInt(txtGiorni.getText());
				String string = model.doSimulazione(n);
				txtResult.appendText(string);
			} catch (Exception e) {
				throw e;
			}
    	}
    	else {
    		txtResult.appendText("Creare prima il grafo");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	boxGenere.getItems().addAll(model.getGeneri());
    }
}
