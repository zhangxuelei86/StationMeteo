/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stationmeteo.controller;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import stationmeteo.java.Capteur;
import stationmeteo.java.serialize.ICapteurSerialize;
import stationmeteo.java.Icapteur;
import stationmeteo.java.SuperCapteur;
import stationmeteo.java.serialize.SerializerCapteur;

/**
 * Simple Preloader Using the ProgressBar Control
 *
 * @author clguilbert
 */
public class MainController extends BorderPane implements Initializable {
    
    @FXML
    private Button addButton;
    @FXML
    private Button uptButton;
    @FXML
    private Button delButton;
    @FXML
    private Button groupButton;
    @FXML
    private Button digitalButton;
    @FXML
    private Button thermoButton;
    @FXML
    private Button iconButton;
    @FXML
    private ListView capteurList;
    
    private ObservableList<Icapteur> listeDeCapteur = FXCollections.observableList(new ArrayList());
    private Icapteur selectedCapteur;
    private CapteurController capteurcontrol;
    private SerializerCapteur XMLdatamanager=new SerializerCapteur();;
   // public void setApp(StationMeteo application){
   //     StationMeteo application1 = application;
   // }

    public ObservableList<Icapteur> getListeDeCapteur() {
        return listeDeCapteur;
    }

    public void setListeDeCapteur(ObservableList<Icapteur> listeDeCapteur) {
        this.listeDeCapteur = listeDeCapteur;
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        capteurList.setOnMouseClicked(me -> selectedCapteur=(Icapteur) capteurList.getSelectionModel().getSelectedItem());
        /**capteurList.setCellFactory(new Callback<ListView<Capteur>, ListCell<Capteur>>() {
            @Override
            public ListCell<Capteur> call(ListView<Capteur> param) {
                return new CellFactory();
            }
        });**/
        capteurList.setOnDragDetected(me -> {

            if(selectedCapteur!=null){capteurList.startDragAndDrop(TransferMode.ANY);
            System.out.println(selectedCapteur.getNom()+" est en train d'etre bougé");
                System.out.println("DRAGGING");
            }
            capteurList.setOnDragOver(event -> {
                System.out.println("DROPPED");
                Capteur souscap= (Capteur) selectedCapteur;
                capteurList.setOnMouseReleased(hover -> selectedCapteur=(Icapteur) capteurList.getSelectionModel().getSelectedItem());

                SuperCapteur sc=new SuperCapteur(selectedCapteur.getId(),selectedCapteur.getNom(),souscap);

                event.setDropCompleted(true);

                event.consume();
            });
        });


        addButton.setOnMousePressed(me -> ouvrirFenetreAjout());
        groupButton.setOnMousePressed(me -> ouvrirFenetreSuperCapteur());
        uptButton.setOnMousePressed(me -> {

            if(selectedCapteur!=null)ouvrirFenetreModif();
        });
        delButton.setOnMousePressed(me -> {
            listeDeCapteur.remove(selectedCapteur);
            if(selectedCapteur.getClass()==Capteur.class){
                
                ((Capteur) selectedCapteur).getLeThread().interrupt();
            }
            selectedCapteur=null;
        });
        digitalButton.setOnMousePressed(me -> {
            if(selectedCapteur!=null)affichageDigital();
        });
        thermoButton.setOnMousePressed((MouseEvent me) -> {
            if(selectedCapteur!=null)affichageThermo();
        });
        iconButton.setOnMousePressed((MouseEvent me) -> {
            if(selectedCapteur!=null)affichageIcone();
        });

        

        

        //listeDeCapteur.add(captdef);
       
        

        /**capteurList.setOnDragDetected(event -> {
            if (! listeDeCapteur.isEmpty()) {
                Dragboard db = capteurList.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent cc = new ClipboardContent();
                cc.putString(capteurList.getItems());
                db.setContent(cc);
                dragSource.set(capteurList);
            }
        });

        capteurList.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });

        capteurList.setOnDragDone(event -> listView.getItems().remove(capteurList.getItem()));

        capteurList.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString() && dragSource.get() != null) {
                // in this example you could just do
                // listView.getItems().add(db.getString());
                // but more generally:

                ListCell<String> dragSourceCell = dragSource.get();
                listView.getItems().add(dragSourceCell.getItem());
                event.setDropCompleted(true);
                dragSource.set(null);
            } else {
                event.setDropCompleted(false);
            }
        });**/
    capteurList.setItems(listeDeCapteur);
       
                
    }
    public void ChargerCapteur(){
       if(listeDeCapteur!=null){
            List<ICapteurSerialize> result=XMLdatamanager.chargeCapteur();
            if(result!=null){
            listeDeCapteur.clear();
            for(int i=0;i<XMLdatamanager.chargeCapteur().size();i++){
            listeDeCapteur.add((Capteur )XMLdatamanager.chargeCapteur().get(i));
            }
            }
       //XMLdatamanager.chargeCapteur();
       }
    }
    public void sauveCapteur(){
        
        if(this.XMLdatamanager!= null){
            ArrayList test =new ArrayList();
            test.addAll(capteurList.getItems());
            //ObservableList items = this.capteurList.getItems();
        XMLdatamanager.sauveCapteurs(test);
        }
        else System.out.println("prout");
}

    private void ouvrirFenetreAjout(){
        Stage modif=new Stage();
        URL url=getClass().getResource("/stationmeteo/ressources/fxml/fenetreAjout.fxml");
        FXMLLoader loader = new FXMLLoader(url);          
        BorderPane page;
        AjoutController ajoutcont=new AjoutController();
        loader.setController(ajoutcont);
        try{
            page = loader.load();
            Scene scene = new Scene(page);
            modif.setScene(scene);
        }
        catch (IOException e){
            e.printStackTrace();     
        }
        modif.setTitle("Nouveau Capteur");
        modif.showAndWait();
        if(ajoutcont.getCapteur()!=null) listeDeCapteur.add(ajoutcont.getCapteur());
    }

    private void ouvrirFenetreSuperCapteur(){
        Stage modif=new Stage();
        URL url=getClass().getResource("/stationmeteo/ressources/fxml/fenetreSuperCapteur.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        BorderPane page;
        SuperCapController supcont=new SuperCapController(listeDeCapteur);
        loader.setController(supcont);
        try{
            page = loader.load();
            Scene scene = new Scene(page);
            modif.setScene(scene);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        modif.setTitle("Creer Super Capteur");
        modif.showAndWait();
    }

    private void ouvrirFenetreModif(){
        Stage modif=new Stage();
        URL url=getClass().getResource("/stationmeteo/ressources/fxml/fenetreModif.fxml");
        FXMLLoader loader = new FXMLLoader(url);          
        BorderPane page;
        ModifController modifcont=new ModifController(selectedCapteur);

        loader.setController(modifcont);
        try{
            page = loader.load();
            Scene scene = new Scene(page);
            modif.setScene(scene);
        }
        catch (IOException e){
            e.printStackTrace();     
        }
        modif.setTitle("Modifier Capteur"); //OUI
        modif.showAndWait();
        if(modifcont.isModified()) {
            listeDeCapteur.remove(selectedCapteur);
            selectedCapteur=modifcont.getCapteur();
            listeDeCapteur.add(selectedCapteur);

        }
    }

    public ListView getCapteurList() {
        return capteurList;
    }

    public void setCapteurList(ListView capteurList) {
        this.capteurList = capteurList;
    }
    private void affichageDigital(){
        Stage digital=new Stage();
        URL url=getClass().getResource("/stationmeteo/ressources/fxml/fenetreDigitale.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        BorderPane page;
        capteurcontrol=new CapteurController(selectedCapteur);
        loader.setController(capteurcontrol);
        try{
            page = loader.load();
            Scene scene = new Scene(page);
            digital.setScene(scene);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        digital.setTitle("Affichage Numérique");
        digital.show();
    }

    private void affichageThermo(){
        Stage thermo=new Stage();
        URL url=getClass().getResource("/stationmeteo/ressources/fxml/fenetreThermo.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        BorderPane page;
        capteurcontrol=new CapteurController(selectedCapteur);
        loader.setController(capteurcontrol);
        try{
            page = loader.load();
            Scene scene = new Scene(page);
            thermo.setScene(scene);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        thermo.setTitle("Affichage Thermomètre");
        thermo.show();
    }

    private void affichageIcone(){
        Stage icone=new Stage();
        URL url=getClass().getResource("/stationmeteo/ressources/fxml/fenetreIcone.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        BorderPane page;
        capteurcontrol=new CapteurController(selectedCapteur);
        loader.setController(capteurcontrol);
        try{
            page = loader.load();
            Scene scene = new Scene(page);
            icone.setScene(scene);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        icone.setTitle("Affichage Pictogramme");
        icone.show();
    }
        
        public Icapteur getCapteur(){
            return selectedCapteur;
        }
}
