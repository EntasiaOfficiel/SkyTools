package fr.entasia.skytools.objs.villagers;

public class TradeLevel {

	public final Trade[] trades;
	public int min = 1, random = 2;

	public TradeLevel(Trade... trades){
		this.trades = trades;
	}

	public TradeLevel min(int min){
		this.min = min;
		return this;
	}

	public TradeLevel max(int max){
		this.random = max;
		return this;
	}

}
