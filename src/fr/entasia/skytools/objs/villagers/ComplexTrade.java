package fr.entasia.skytools.objs.villagers;

import fr.entasia.apis.other.Randomiser;
import fr.entasia.errors.EntasiaException;
import fr.entasia.skycore.apis.InternalAPI;

public class ComplexTrade {


	private Randomiser r = new Randomiser(0, false);
	private Trade[] trades;

	public ComplexTrade(Trade... trades){
		this.trades = trades;
		for(Trade t : trades){
			r.max+=t.percent;
		}
	}

	public Trade getTrade(){
		r.regen();
		for(Trade t : trades){
			if(r.isInNext(t.percent)){
				return t;
			}
		}
		InternalAPI.warn("Invalid trade requested (out of range)", false);
		throw new EntasiaException();
	}

}
