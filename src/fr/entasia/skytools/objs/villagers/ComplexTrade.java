package fr.entasia.skytools.objs.villagers;

import fr.entasia.apis.other.Randomiser;
import fr.entasia.errors.EntasiaException;
import fr.entasia.skycore.apis.InternalAPI;

import java.util.ArrayList;

public class ComplexTrade {


	private Randomiser r = new Randomiser(0, false);
	private Trade[] trades;

	public ComplexTrade(Trade... trades){
		this.trades = trades;
		System.out.println("constructor");
		for(Trade t : trades){
			System.out.println("add "+t.percent);
			r.max+=t.percent;
		}
	}

	public Trade getTrade(){
		r.regen();
		System.out.println(r.number);
		for(Trade t : trades){
		System.out.println(t.percent);
			if(r.isInNext(t.percent)){
				return t;
			}
		}
		InternalAPI.warn("Invalid trade requested (out of range)", false);
		throw new EntasiaException();
	}

}
