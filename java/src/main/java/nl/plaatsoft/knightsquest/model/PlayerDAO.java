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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import nl.plaatsoft.knightsquest.tools.MyData;
import nl.plaatsoft.knightsquest.tools.MyFactory;

public class PlayerDAO {

	private List <Player> players = new ArrayList<Player>();
	
	public void getTexture(GraphicsContext gc, int player) { 
		
		switch(player) {
		
			case 1: // Player 1
					gc.setFill(Color.YELLOW);
					break;
				
			case 2: // Player 2
					gc.setFill(Color.RED);
					break;
			
			case 3: // Player 3
					gc.setFill(Color.CYAN);
					break;
		
			case 4: // Player 4
					gc.setFill(Color.MAGENTA);
					break;
					
			case 5: // Player 5
					gc.setFill(Color.BROWN);
					break;
					
			case 6: // Player 6
					gc.setFill(Color.LIGHTBLUE);
					break;
		}		
	}	
	
	public String getColor(int player) { 
		
		switch(player) {
		
			case 1: // Player 1
					return "yellow";

			case 2: // Player 2
					return "red";
			
			case 3: // Player 3
					return "cyan";
		
			case 4: // Player 4
					return "magenta";
					
			case 5: // Player 5
					return "brown";
					
			case 6: // Player 6
					return "lightblue";
		}		
		return "";
	}	
	
	public Player createPlayer(GraphicsContext gc, int id, Pane pane, PlayerEnum type) {
			
		Player player = new Player(id, type);
		MyFactory.getPlayerDAO().getPlayers().add(player);
		
		for (int i=0; i<MyData.getTowers(); i++) {
						
			MyFactory.getRegionDAO().createStartRegion(i+1, player, pane);		
		}				
		return player;
	}
	
	public Player getHumanPlayer() {
		
		Iterator<Player> iter = players.iterator();  
		while (iter.hasNext()) {
			Player player = (Player) iter.next();
			if (player.getType()==PlayerEnum.HUMAN_LOCAL) {
				return player;
			}
		}
		return null;
	}
		
	public boolean hasPlayerNoMoves(Player player) {
	
		/* Check if human player has moves lefts in this turn */
		Iterator<Region> iter2 = player.getRegion().iterator();  
		while (iter2.hasNext()) {
			Region region = (Region) iter2.next();
				
			Iterator<Land> iter3 = region.getLands().iterator();  
			while (iter3.hasNext()) {
				Land land = (Land) iter3.next();
					
				if ((land.getSoldier()!=null) && land.getSoldier().isEnabled()) {
					return false;
				}
			}
		}
		
		return true;
	}
		
	public List <Player> getPlayers() {
		return players;
	}

	public void setPlayers(List <Player> players) {
		this.players = players;
	}
}
