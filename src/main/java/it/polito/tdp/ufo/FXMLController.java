package it.polito.tdp.ufo;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.ufo.model.AnnoAvvistamento;
import it.polito.tdp.ufo.model.Model;
import it.polito.tdp.ufo.model.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController
{
	private Model model;

	@FXML private ResourceBundle resources;

	@FXML private URL location;

	@FXML private ComboBox<AnnoAvvistamento> boxAnno;

	@FXML private ComboBox<State> boxStato;

	@FXML private TextArea txtResult;

	private final String ERRORE = "\n\nERRORE! controllare che i dati inseriti siano corretti";
	
	@FXML void handleAvvistamenti(ActionEvent event)
	{
		AnnoAvvistamento year = this.boxAnno.getValue(); 
		if (year == null)
		{
			this.txtResult.appendText("\n\nErrore, scegliere elemento dalla lista");
			return;
		} 

		//resetto testo
		this.txtResult.clear();
    	this.txtResult.appendText("Crea grafo...\n");

    	//creo grafo
    	this.model.creaGrafo(year.getAnno());
    	txtResult.appendText(String.format("\nGRAFO CREATO CON:\n#Vertici: %d\n#Archi: %d",
				this.model.getNumVertici(),
				this.model.getNumArchi()));
 

		//aggiungo risultati alla combobox 
		this.boxStato.getItems().clear();
		this.boxStato.getItems().addAll(this.model.getVertici()); 
	}
	
	@FXML void handleAnalizza(ActionEvent event)
	{
		State state = this.boxStato.getValue(); 
		if (state == null)
		{
			this.txtResult.appendText("\n\nErrore, scegliere elemento dalla lista");
			return;
		} 
		
		this.txtResult.appendText("\n" + this.model.getInfo(state));
	}

	@FXML void handleSequenza(ActionEvent event)
	{

	}

	@FXML void initialize()
	{
		assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Ufo.fxml'.";
		assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Ufo.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Ufo.fxml'.";

	}

	public void setModel(Model model)
	{
		this.model = model;
		
		this.boxAnno.getItems().addAll(this.model.getAnniAvvistamenti()); 
	}
}
