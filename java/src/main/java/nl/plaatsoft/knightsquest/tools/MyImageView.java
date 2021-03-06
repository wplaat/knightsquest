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


package nl.plaatsoft.knightsquest.tools;

import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nl.plaatsoft.knightsquest.ui.Navigator;

public class MyImageView extends ImageView {

	public MyImageView(double x, double y, Image image, double scale) {

		setImage(image);
		setLayoutX(x);
		setLayoutY(y);
		setScaleX(scale);
		setScaleY(scale);
	}
		
	public MyImageView(double x, double y, Image image, double scale, boolean invert) {

		ColorAdjust blackout = new ColorAdjust();
		blackout.setBrightness(1.0);

		setImage(image);
		setLayoutX(x);
		setLayoutY(y);		
		setScaleX(scale);
		setScaleY(scale);

		setEffect(blackout);
		setCache(true);
		setCacheHint(CacheHint.SPEED);
	}

	public MyImageView(double x, double y, String resource, double scale) {

		Image image = new Image(resource);
		setImage(image);
		
		setLayoutX(x);
		setLayoutY(y);
		setScaleX(scale);
		setScaleY(scale);
	}
	
	public MyImageView(double x, double y, double width, double heigth, String resource) {

		Image image = new Image(resource);	
		setImage(image);		
		setLayoutX(x);
		setLayoutY(y);
		setFitWidth(width);
		setFitHeight(heigth);
		setPreserveRatio(true);
	}
	
	public MyImageView(double x, double y, String resource, double scale, int page) {

		Image image = new Image(resource);	
		setImage(image);		
		setLayoutX(x);
		setLayoutY(y);
		setScaleX(scale);
		setScaleY(scale);
		
		setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
	         	Navigator.go(page);
	      }
	   });
	}
}
