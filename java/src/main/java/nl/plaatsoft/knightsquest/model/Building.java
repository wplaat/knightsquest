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

package nl.plaatsoft.knightsquest.model;

import nl.plaatsoft.knightsquest.tools.MyFactory;

public class Building {

	private Land land;	
	private BuildingEnum type;

	public Building(BuildingEnum type, Land land) {
		
		this.type = type;
		this.setLand(land);
	}
	
	void draw() {
		
		int offset = 0;
		if ((land.getY() % 2)==1) {
			offset = land.getSize()*2;
		} 
	             	
		double posX = land.getSize()+(land.getX()*(land.getSize()*4))+offset-2;
		double posY = (land.getY()*land.getSize())+(land.getSize()/2)-2;
		
		land.getGc().setGlobalAlpha(1.0);			
		
		land.getGc().drawImage(MyFactory.getBuildingDAO().get(type), posX, posY);
		
		//log.info("draw ["+land.getX()+","+land.getY()+"]");
	}

	public BuildingEnum getType() {
		return type;
	}

	public void setType(BuildingEnum type) {
		this.type = type;
	}
	
	public Land getLand() {
		return land;
	}

	public void setLand(Land land) {
		this.land = land;
	}
}
