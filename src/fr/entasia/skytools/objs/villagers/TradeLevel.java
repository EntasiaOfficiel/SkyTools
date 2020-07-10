package fr.entasia.skytools.objs.villagers;

public class TradeLevel {

	public final Trade[] trades;
	public int min = 0, random = 0;

	public TradeLevel(Trade... trades){
		this.trades = trades;
	}

	public TradeLevel min(int min){
		this.min = min;
		return this;
	}

	public TradeLevel random(int random){
		this.random = random;
		return this;
	}

}
