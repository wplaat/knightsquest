/**
 *  @file
 *  @brief 
 *  @author wplaat
 *
 *  Copyright (C) 2008-2016 PlaatSoft
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, version 3.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package nl.plaatsoft.knightsquest.ui;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import nl.plaatsoft.knightsquest.tools.MyFactory;
import nl.plaatsoft.knightsquest.tools.MyMusic;

public class Main extends Application {

	final static Logger log = Logger.getLogger( Main.class);
		
	@Override
	public void start(Stage primaryStage) {

		MyFactory.getSettingDAO().load();  
		
		setUserAgentStylesheet(STYLESHEET_MODENA);

		Navigator.setStage(primaryStage);
		Navigator.go(Navigator.INTRO1);

		primaryStage.setTitle(Constants.APP_NAME + " v" + Constants.APP_VERSION);
		primaryStage.setScene(Navigator.getScene());
		primaryStage.setWidth(MyFactory.getSettingDAO().getSettings().getWidth());
		primaryStage.setHeight(MyFactory.getSettingDAO().getSettings().getHeight()+20);       	
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("images/logo3.png"));
						
        if (MyFactory.getSettingDAO().getSettings().getX()!=0) {
               primaryStage.setX(MyFactory.getSettingDAO().getSettings().getX());
               primaryStage.setY(MyFactory.getSettingDAO().getSettings().getY());
        }
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
               MyFactory.getSettingDAO().getSettings().setX(primaryStage.getX());
               MyFactory.getSettingDAO().getSettings().setY(primaryStage.getY());          
               MyFactory.getSettingDAO().save();   
               
               Platform.exit();
               System.exit(0);               
            }
        });
        
        primaryStage.show();
        
		MyMusic.play();        
	}

	public static void main(String[] args) {
		
		log.info(Constants.APP_NAME + " v" + Constants.APP_VERSION+" start");
		
		String version = System.getProperty("java.version");
		String[] parts = version.split("_");
		if (((parts[0].equals("1.7.0") && Integer.parseInt(parts[1]) < 70))
				|| ((parts[0].equals("1.8.0") && Integer.parseInt(parts[1]) < 100))) {
			JOptionPane.showMessageDialog(null, "Java v" + version + " is to old. Please upgrade!");
			System.exit(1);
		}
		launch(args);
		
		log.info(Constants.APP_NAME + " v" + Constants.APP_VERSION+" end");
	}

}
