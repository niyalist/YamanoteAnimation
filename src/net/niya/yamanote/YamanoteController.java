package net.niya.yamanote;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;


public class YamanoteController implements Initializable{
	@FXML BorderPane basePane;
	@FXML AnchorPane matrixPane;
	@FXML Canvas canvas;

	// Starting date of JR East Data.
	int date = 18;
	int hour = 6;
	int min = 0;

	private YamanoteLineDB lineDB = YamanoteLineDB.getInstance();
	
	// Before executing this program, I downloaded every data about Yamanote Line from the server.　
	private String dir = "/Users/niya/Documents/sezaki/2017/dbhackathon/";
	private List<TrainStatus> trainList = new ArrayList<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		viewUpdate();
		
		AnimationSerivce service = new AnimationSerivce();
		service.start();
	}

	private void viewUpdate(){
		int centerX = 470;
		int centerY = 470;
		int innerRadius = 370;
		int outerRadius = 410;
		int radius = (innerRadius + outerRadius )/2;
		int stationWidth = 20;
		int stationHeight = 60;
		
		int trainWidth = 55;
		int carWidth = 5;		
		double trainHeight = 8.0;
		double baseHeight = 12.0;		
		
		GraphicsContext gc = canvas.getGraphicsContext2D();		
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		Affine originalAffine = gc.getTransform();		

		String dateString = "April " + String.format("%02d", date) +", 2017";
		String timeString = String.format("%02d", hour) + ":" + String.format("%02d", min);

		Font f = gc.getFont();
		gc.setFont(Font.font(f.getFamily(), f.getSize()*3));
		gc.fillText(dateString, 360, 430);
		gc.setFont(Font.font(f.getFamily(), f.getSize()*4));		
		gc.fillText(timeString, 410, 530);		
		gc.setFont(f);
		
		//Draw railway lines
		gc.setLineWidth(2.0);
		gc.setStroke(Color.GRAY);
		gc.strokeOval(centerX - innerRadius, centerY - innerRadius, innerRadius * 2, innerRadius * 2);		
		gc.strokeOval(centerX - outerRadius, centerY - outerRadius, outerRadius * 2, outerRadius * 2);

		gc.setLineDashes(null);
		gc.setLineWidth(1.0);

		//Draw Stations
		Affine affine = gc.getTransform();
		affine.appendTranslation(centerX, centerY);
		Affine zeroAffine = affine.clone();

		for(YamanoteStationInfo info: lineDB.lineArray){
			affine.appendRotation(info.station_angle);
			gc.setTransform(affine);
			gc.setFill(Color.WHITE);
			gc.fillRect(radius - stationHeight/2, -stationWidth/2, stationHeight, stationWidth);
			gc.setFill(Color.BLACK);
			gc.strokeRect(radius - stationHeight/2, -stationWidth/2, stationHeight, stationWidth);
//			gc.fillText(info.station_name, radius - info.station_name.length() * 12 - 70 , 14-stationWidth/2); //Japanese
			gc.fillText(info.station_name_e, radius - info.station_name_e.length() * 6 - 55 , 14-stationWidth/2); //English
			affine = zeroAffine.clone();
			
		}
		
		// 
		for(TrainStatus status: trainList){
			double trainRadius = status.direction? outerRadius: innerRadius;
			affine.appendRotation(status.train_angle);
			gc.setTransform(affine);
			gc.setFill(Color.GREEN);
			gc.fillRect(trainRadius - trainHeight/2, -trainWidth/2, trainHeight, trainWidth);
//			gc.strokeRect(trainRadius - trainHeight/2, -trainWidth/2, trainHeight, trainWidth);
			
			gc.setFill(Color.BLUE);
			gc.setStroke(Color.GRAY);
			for(int i = 0; i < status.rate.length; i++){
				double rate = status.rate[i];
				gc.fillRect(trainRadius + trainHeight/2, -trainWidth/2 + carWidth*(10-i), baseHeight * rate, carWidth);
				gc.strokeRect(trainRadius + trainHeight/2, -trainWidth/2 + carWidth*(10-i), baseHeight*rate, carWidth);
			}
			
			
			gc.setFill(Color.BLACK);			
//			gc.fillText(status.trainID, trainRadius, 14);
			affine = zeroAffine.clone();
		}
		gc.setStroke(Color.BLACK);
		gc.setTransform(originalAffine);
	}
	
	class AnimationSerivce extends ScheduledService<Boolean> {

	    protected Task createTask() {
	        return new Task<Boolean>() {
	            protected Boolean call() throws Exception{
					String fileName = "04_" + String.format("%02d", date) + "_" + String.format("%02d", hour) + String.format("%02d", min) + ".csv";
					System.out.println(fileName);
					List<TrainStatus> tList = loadTrainInfo(fileName);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							trainList = tList;
							viewUpdate();
						}
					});
					
					next();
					Thread.sleep(500);
	                return true;
	            }
	        };
	    }
		private void next(){
			min += 1;
			if(min >= 60){
				min = 0;
				hour += 1;
				if(hour >= 1 && hour <= 5) hour = 6; //Don't show from 1 am to 5 am.
				if(hour >= 24){
					hour = 0;
					date += 1;
					if(date >= 24){
						date = 18;
					}
				}
			}
		}
		private List<TrainStatus> loadTrainInfo(String file){
			List<TrainStatus> trainList = new ArrayList<>();
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dir + file), "UTF-8"));
				reader.readLine(); //Skip header

				while(reader.ready()){
					String line = reader.readLine();
					TrainStatus data = new TrainStatus(line);
					trainList.add(data);
				}
				System.out.println("Read: " + trainList.size());
				reader.close();
			}catch(IOException e){
				e.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}
			return trainList;
		}
		
   	}
}

