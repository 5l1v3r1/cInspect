package cinspect.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Collections;

import com.jenkov.crawler.mt.io.CrawlerMT;
import com.jenkov.crawler.util.SameWebsiteOnlyFilter;

import cinspect.main.Main;
import cinspect.web.ResourceRequestType;
import cinspect.web.WebDatabase;
import cinspect.web.WebResource;

import com.aquafx_project.AquaFx;

public class GUI extends Application {
	Pane textDisplayPane, textInputPane, loadingPane;
	VBox optionsVBox, checkboxVBox, delayBox;
	HBox bottomBox;
	TextField inputTextField;
	ScrollPane scroll;
	CheckBox sqlCheck, rceCheck, lfiCheck, xssCheck, 
			 rfiCheck, tsqlCheck, udrjsCheck, appdosCheck,
			 phpinfoCheck, ccssnCheck;
	Button runButton, stopButton, closeButton, minimizeButton, maximizeButton;
	Slider delaySlider;
	Label delayLabel;
	public static ProgressBar loadingBar;
	static TextFlow text;
	double xOffset, yOffset, textOffset = 5;
	boolean maxToggle = false;
	double oldX, oldY, oldW, oldH;
	String url = "http://192.168.1.29/";
	
	static int requestDelay = 0;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		//Initial setup
		AquaFx.style();
		
		BorderPane mainPane = new BorderPane();
		Scene mainScene = new Scene(mainPane, 500, 500);
		mainScene.setRoot(mainPane);
		mainScene.getStylesheets().add("cInspect/GUI/ButtonStyle.css");
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("cInspect - A Web Vulnerability Scanner");
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		
		//Display instantiation
		textDisplayPane = new Pane();
		bottomBox = new HBox();
		textInputPane = new Pane();
		textInputPane.setPrefHeight(50);
		checkboxVBox = new VBox();
		checkboxVBox.setStyle("-fx-padding: 10; -fx-background-color: rgb(230.0, 230.0, 230.0);");
		checkboxVBox.setSpacing(8);
		checkboxVBox.setPrefSize(80, 200);
		optionsVBox = new VBox();
		optionsVBox.setStyle("-fx-padding: 5");
		optionsVBox.setSpacing(10);
		optionsVBox.setPrefWidth(125);
		delayBox = new VBox();
		loadingPane = new Pane();
		loadingPane.setStyle("-fx-padding: 10;");
		
		//Save default window size for resizing purposes
		oldW = mainScene.getWidth();
		oldH = mainScene.getHeight();
		
		//Interactive objects instantiation------------------------------
		
		//Text Field
		inputTextField = new TextField();
		inputTextField.setPrefWidth(375-textOffset);
		inputTextField.setLayoutX(textOffset);
		inputTextField.setOnAction(new TextFieldHandler());
		
		//Checkboxes
		sqlCheck = new CheckBox("SQL");
		rceCheck = new CheckBox("RCE");
		lfiCheck = new CheckBox("LFI");
		xssCheck = new CheckBox("XSS");
		rfiCheck = new CheckBox("RFI");
		tsqlCheck = new CheckBox("Timed SQL");
		udrjsCheck = new CheckBox("UDRJS");
		appdosCheck = new CheckBox("AppDoS");
		phpinfoCheck = new CheckBox("Phpinfo");
		ccssnCheck = new CheckBox("CC/SSN");
		
		//Buttons
		runButton = new Button("Run Program");
		runButton.setOnAction(new RunButtonHandler());
		stopButton = new Button("STOP");
		stopButton.setOnAction(new StopButtonHandler());
		
		//Delay Slider
		delayLabel = new Label("Delay: 0 ms");
		delaySlider = new Slider();
		delaySlider.setMin(0);
		delaySlider.setMax(1000);
		delaySlider.setValue(0);
		delaySlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
            	delayLabel.setText("Delay: " + String.format("%.0f", new_val) + " ms");
            	requestDelay = new_val.intValue();
            	//loadingBar.setProgress(new_val.doubleValue()/1000);
            }
        });
		
		//Toolbar
		closeButton = new Button("  ");
		closeButton.getStyleClass().add("close");
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
			}
		});
		minimizeButton = new Button("  ");
		minimizeButton.getStyleClass().add("minimize");
		minimizeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primaryStage.toBack();
			}
		});
		maximizeButton = new Button("  ");
		maximizeButton.getStyleClass().add("maximize");
		maximizeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Screen screen = Screen.getPrimary();
				
				if(maxToggle == false){
					Rectangle2D bounds = screen.getVisualBounds();
					oldX = primaryStage.getX();
					oldY = primaryStage.getY();
					primaryStage.setX(bounds.getMinX());
					primaryStage.setY(bounds.getMinY());
					primaryStage.setWidth(bounds.getWidth());
					primaryStage.setHeight(bounds.getHeight());
					scroll.setPrefSize(bounds.getWidth()-125-textOffset, bounds.getHeight()-100);
					inputTextField.setPrefWidth(bounds.getWidth()-125-textOffset);
					maxToggle = true;
				}
				else if(maxToggle == true){
					primaryStage.setX(oldX);
					primaryStage.setY(oldY);
					primaryStage.setWidth(oldW);
					primaryStage.setHeight(oldH);
					scroll.setPrefSize(375-textOffset, 400);
					inputTextField.setPrefWidth(375-textOffset);
					maxToggle = false;
				}
			}
		});
		
		ToolBar toolbar = new ToolBar();
		toolbar.getItems().addAll(closeButton, new Label(" "), 
				minimizeButton, new Label(" "), maximizeButton);
		toolbar.setOnMousePressed(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	            xOffset = event.getSceneX();
	            yOffset = event.getSceneY();
	        }
        });
        toolbar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });
		
        //Loading Bar
        loadingBar = new ProgressBar();
        loadingBar.setProgress(0);
        loadingBar.setLayoutX(10);
        
        //Textflow
  		text = new TextFlow();
  		text.setPrefSize(375-textOffset, 400);
  		text.setLayoutX(textOffset);
  		text.setLayoutY(textOffset);
  		
  		//ScrollPane
  		scroll = new ScrollPane();
  		scroll.setContent(text);
  		scroll.setPrefSize(375-textOffset, 400);
  		scroll.setStyle("-fx-background-color: WHITE;");
  		scroll.setLayoutX(textOffset);
  		scroll.setLayoutY(textOffset);
  		scroll.vvalueProperty().bind(text.heightProperty());
  		//scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		
		//Add items to VBox
		optionsVBox.getChildren().addAll(checkboxVBox, delayBox, runButton, stopButton);
		checkboxVBox.getChildren().addAll(sqlCheck, rceCheck, lfiCheck, xssCheck, 
				 rfiCheck, tsqlCheck, udrjsCheck, appdosCheck, phpinfoCheck, ccssnCheck);
		delayBox.getChildren().addAll(delayLabel, delaySlider);
		
		//Add items to Bottom Pane
		bottomBox.getChildren().addAll(textInputPane, loadingPane);
		textInputPane.getChildren().addAll(inputTextField);
		loadingPane.getChildren().addAll(loadingBar);
		
		//Add items to Center Pane
		textDisplayPane.getChildren().add(scroll);
		
		//BorderPane layout
		mainPane.setCenter(textDisplayPane);
		mainPane.setBottom(bottomBox);
		mainPane.setRight(optionsVBox);
		mainPane.setTop(toolbar);
		
		//Display App
		primaryStage.show();
		
	}
	
	public static void print(String output){
		System.out.println(output);
		String temp = output;
		Text t = new Text();
		t.setText(temp);
		Text tnewline = new Text();
		tnewline.setText("\n");
		if(temp.indexOf("vulnerable") != -1)
			t.setFill(Color.RED);
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	text.getChildren().addAll(t, tnewline);
           }
         });

		//text.appendText(output+"\n");
	}
	
	public class TextFieldHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event) {
			String input = inputTextField.getText();
			inputTextField.setText("");
			url = input;
			print("URL: " + input);
		}
	}
	
	public class StopButtonHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event){
			print("---------- TERMINATING ----------");
			
			for(Thread t : Main.threads) {
				t.interrupt();
			}
			
			Main.threads.clear();
		}
	}
	
	public class RunButtonHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event) {
			Thread crawlerManagerThread = new Thread() {
				public void run() {
					//all this needs to be in a thread
					CrawlerMT crawler  = new CrawlerMT(new SameWebsiteOnlyFilter(url));
	        		crawler.addUrl(url);
	        		crawler.crawl();
	        
	        		print("Done crawling. Crawled " + com.jenkov.crawler.mt.io.CrawlerMT.crawledUrls.size() + " URLS.");
	        		WebDatabase.printDatabase();
	        		
	        		//reset the progress bar:
	        		loadingBar.setProgress(0);
			
					List<WebResource> resources = WebDatabase.getDatabase(); //this needs to be updated.
					Collections.reverse(resources);
			
					print("\n");
			
			
					Main.spawnThreads(3, sqlCheck.isSelected(), rceCheck.isSelected(), lfiCheck.isSelected(), xssCheck.isSelected(), rfiCheck.isSelected(), tsqlCheck.isSelected(), udrjsCheck.isSelected(), appdosCheck.isSelected(), phpinfoCheck.isSelected(), ccssnCheck.isSelected());
				}
			};
			
			crawlerManagerThread.start();
			print("Starting to crawl...");
		}
	}
	
	public synchronized static int getRequestDelay() {
		return requestDelay;
	}
}