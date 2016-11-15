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

package nl.plaatsoft.knightsquest.utils;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import nl.plaatsoft.knightsquest.model.Region;
import nl.plaatsoft.knightsquest.model.Land;
import nl.plaatsoft.knightsquest.model.Player;
import nl.plaatsoft.knightsquest.model.Soldier;
import nl.plaatsoft.knightsquest.model.SoldierEnum;
import nl.plaatsoft.knightsquest.tools.MyRandom;

public class SoldierUtils {

	final private static Logger log = Logger.getLogger( SoldierUtils.class);		
	
	private static Image tower = new Image("images/tower.png", Constants.SEGMENT_SIZE+4, Constants.SEGMENT_SIZE+4, false, false);
	private static Image pawn = new Image("images/pawn.png", Constants.SEGMENT_SIZE+4, Constants.SEGMENT_SIZE+4, false, false);
	private static Image horse = new Image("images/horse.png", Constants.SEGMENT_SIZE+4, Constants.SEGMENT_SIZE+4, false, false);
	private static Image bishop = new Image("images/bishop.png", Constants.SEGMENT_SIZE+4, Constants.SEGMENT_SIZE+4, false, false);
	private static Image queen = new Image("images/queen.png", Constants.SEGMENT_SIZE+4, Constants.SEGMENT_SIZE+4, false, false);
	private static Image king = new Image("images/king.png", Constants.SEGMENT_SIZE+4, Constants.SEGMENT_SIZE+4, false, false);
	private static Image cross = new Image("images/cross.png", Constants.SEGMENT_SIZE+4, Constants.SEGMENT_SIZE+4, false, false);
			
	public static void createSoldier(Region region, Pane pane) {
	
		//log.info("soldier create start");
					
		/* Create new Soldier if there is enough food */  
		if (region.foodAvailable()>=getFoodNeeds(SoldierEnum.PAWN)) {
						
			Land land1 = RegionUtils.getTowerPosition(region);
			if (land1!=null) {
				/* Create new Soldier if there is room around the castle */  
				List <Land> list2 = LandUtils.getOwnLand(land1.getX(), land1.getY(), region.getPlayer());			
				Iterator<Land> iter2 = list2.iterator();  						
				if (iter2.hasNext()) {				
					Land land2 = (Land) iter2.next();
					
					Soldier soldier = new Soldier(SoldierEnum.PAWN, region.getPlayer());
					soldier.getImageView().setPosition(land2.getX(), land2.getY());
					pane.getChildren().add(soldier.getImageView());
					land2.setSoldier(soldier);
					
					log.info("New "+soldier.getType()+" [x="+land2.getX()+"|y="+land2.getY()+"|regionId="+region.getId()+"] created!");
				}
			}							
		}
		
		//log.info("soldier create end");
	}
	
	public static int activateMoveSoldier(Player player) {
		
		int count = 0;
		Iterator<Region> iter2 = player.getRegion().iterator();  
		while (iter2.hasNext()) {
			Region region = (Region) iter2.next();
			
			Iterator<Land> iter3 = region.getLands().iterator();  
			while (iter3.hasNext()) {
				Land land = (Land) iter3.next();
				
				if ((land.getSoldier()!=null) && (land.getSoldier().getType()!=SoldierEnum.TOWER)) {
					land.getSoldier().setMoved(false);
					 count++;
				}
			}
		}
		return count;
	}
		
	public static void moveSoldier(Region region) {
		
		//log.info("soldier move start");
		
		Iterator<Land> iter1 = region.getLands().iterator();  
		while (iter1.hasNext()) {
			Land land1 = (Land) iter1.next();
			
			if ((land1.getSoldier()!=null) && 
				!land1.getSoldier().isMoved() && 
				(land1.getSoldier().getType()!=SoldierEnum.CROSS) && 
				(land1.getSoldier().getType()!=SoldierEnum.TOWER)) {
					
				//log.info(land1.getSoldier().getType()+" found [x="+land1.getX()+"|y="+land1.getY()+"]");	
				
				/* --------------------------- */
				/* Upgrade soldier if possible */
				/* --------------------------- */
				
				if (land1.getSoldier().getType()!=SoldierEnum.KING) {
					List <Land> list2 = LandUtils.getUpgradeSoldiers(land1.getX(), land1.getY(), region.getPlayer());
					Iterator<Land> iter2 = list2.iterator();
					while (iter2.hasNext()) {
						Land land2 = (Land) iter2.next();

						// Only upgrade if there is enough food
						if (region.foodAvailable()>SoldierUtils.getFoodNeeds(upgrade(land1.getSoldier().getType()))) {
						
							land1.getSoldier().setMoved(true);
							
							@SuppressWarnings("unused")
							SoldierEnum currentType = land1.getSoldier().getType();
							SoldierEnum nextType = upgrade(land1.getSoldier().getType());
														
							land2.setSoldier(land1.getSoldier());
							land2.getSoldier().setType(nextType);
							//log.info("Move ["+land1.getX()+","+land1.getY()+"]->["+land2.getX()+","+land2.getY()+"] Upgrade soldier ["+currentType+"->"+nextType+"]");
						
							land1.setSoldier(null);						
							return;
						}
					}
				}
				
				/* ------------------------------------- */
				/* Conquer enemy land or defend own land */
				/* ------------------------------------- */
				
				List <Land> list2 = LandUtils.getEnemyLand(land1.getX(), land1.getY(), region.getPlayer());	
				Land land2 = MyRandom.nextLand(list2);
				if (land2!=null) {
									
					if (land2.getSoldier()!=null) {
						
						/* Enemy land is protected with soldier */	
						int attackStrength = land1.getSoldier().getType().getValue();
						int defendStrength = land2.getSoldier().getType().getValue();
						
						if (attackStrength>defendStrength) {
								
							land1.getSoldier().setMoved(true);
							
							// log.info(land1.getSoldier().getType()+" ["+land1.getX()+","+land1.getY()+"] attack and kills "+land2.getSoldier().getType()+" ["+land2.getX()+","+land2.getY()+"]");
							
							land2.setSoldier(land1.getSoldier());
							land2.setPlayer(region.getPlayer());
							land1.setSoldier(null);
															
							// Remove land from current owner, if any
							Region region2 = PlayerUtils.getPlayer(land2);
							if (region2!=null) {
								region2.getLands().remove(land2);
							}	
							
							// Add land to player castle								
							region.getLands().add(land2);
							return;
						} 
							
					} else {
						/* Enemy land is unprotected */
					
						land1.getSoldier().setMoved(true);
									
						// log.info("land1.getSoldier().getType()+" ["+land1.getX()+","+land1.getY()+"]->["+land2.getX()+","+land2.getY()+"]");
					
						land2.setSoldier(land1.getSoldier());
						land2.setPlayer(region.getPlayer());
						land1.setSoldier(null);
															
						//	Remove land from current owner, if any
						Region region2 = PlayerUtils.getPlayer(land2);
						if (region!=null) {
							region2.getLands().remove(land2);
						}	
							
						// 	Add land to player castle								
						region.getLands().add(land2);		
						return;				
					}
				}
								
				/* ------------------------ */
				/* Move soldier to new land */		
				/* ------------------------ */
				
				List <Land> list4 = LandUtils.getNewLand(land1.getX(), land1.getY());					
				Land land4 = MyRandom.nextLand(list4);
				if (land4!=null) {
											
					land1.getSoldier().setMoved(true);
							
					land4.setSoldier(land1.getSoldier());
					land4.setPlayer(region.getPlayer());
					land1.setSoldier(null);
							
					// Add land to player castle								
					region.getLands().add(land4);
							
					// log.info("land1.getSoldier().getType()+" ["+land1.getX()+","+land1.getY()+"]->["+land4.getX()+","+land4.getY()+"]");
					return;
				}
						
				/* ------------------------ */
				// Move soldier on own land
				/* ------------------------ */
				
				List <Land> list5 = LandUtils.getOwnLand(land1.getX(), land1.getY(), region.getPlayer());	
				Land land5 = MyRandom.nextLand(list5);
				if (land5!=null) {

					land1.getSoldier().setMoved(true);
													
					land5.setSoldier(land1.getSoldier());
					land5.setPlayer(region.getPlayer());
					land1.setSoldier(null);

					//log.info("land1.getSoldier().getType()+" ["+land1.getX()+","+land1.getY()+"]->["+land5.getX()+","+land5.getY()+"]");
					return;
				}		
			}
		}	
	}

	public static Image get(SoldierEnum army) {
	
		switch(army) {
	
			case TOWER: 
				return tower;
		
			case PAWN:
				return pawn;
								
			case BISHOP:
				return bishop;
				
			case HORSE:
				return horse;
				
			case QUEEN:
				return queen;
				
			case KING:
				return king;
				
			case CROSS:
				return cross;
			
			default:
				log.error("Unknown soldier found!");
				break;
		}
		return null;
	}		
	
	public static SoldierEnum upgrade(SoldierEnum type) {
		
		SoldierEnum value;
		
		switch(type) {
		
			case PAWN:
				value = SoldierEnum.BISHOP;
				break;
			
			case BISHOP:
				value = SoldierEnum.HORSE;
				break;
				
			case HORSE:
				value = SoldierEnum.QUEEN;
				break;
				
			case QUEEN:
				value = SoldierEnum.KING;
				break;
				
			default:
				value=null;
				break;
		}
			
		return value;
	}
	
	public static int getFoodNeeds(SoldierEnum type) {
			
		int value=0;
				
		switch(type) {
	
			case PAWN:
				value = 2;
				break;
				
			case BISHOP:
				value = 5;
				break;
				
			case HORSE:
				value = 10;
				break;
				
			case QUEEN:
				value = 20;
				break;
				
			case KING:
				value = 40;
				break;
				
			default:
				value = 0;					
				break;
						
		}		
		return value;
	}		
}
