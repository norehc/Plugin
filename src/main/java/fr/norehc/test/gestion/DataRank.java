package fr.norehc.test.gestion;

import java.util.UUID;

import fr.norehc.test.gestion.unit.RankUnit;

public class DataRank extends AbstractData {
	private RankUnit rank;
	
	public DataRank(UUID uuid) {
		this.uuid = uuid;
	}
	
	public void setRank(RankUnit rank) {
		this.rank = rank;
	}
	
	public RankUnit getRank() {
		return rank;
	}
}
