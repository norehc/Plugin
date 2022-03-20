package fr.norehc.test.gestion;

import java.util.UUID;

public class DataMoney extends AbstractData {
	
	private long money;
	
	public DataMoney(UUID uuid) {
		this.uuid = uuid;
	}
	
	public void setMoney(long money) {
		if(money < 0) {
			money = 0;
		}
		this.money = money;
	}
	
	public long getMoney() {
		return money;
	}
	
	public void addMoney(long money) {
		this.money += money;
	}
	
	public void subMoney(long money) {
		if(money > this.money) {
			money = this.money;
		}
		this.money -= money;
	}
	
	public boolean hasMoney(long money) {
		if(money <= 0) return true;
		
		return this.money >= money;
	}
	
}
