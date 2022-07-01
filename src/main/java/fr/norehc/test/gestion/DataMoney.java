package fr.norehc.test.gestion;

import java.util.UUID;

public class DataMoney extends AbstractData {
	
	private long money;
	private long bankMoney;
	
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

	public void setBankMoney(long bankMoney) {
		if(bankMoney < 0) {
			bankMoney = 0;
		}
		this.bankMoney = bankMoney;
	}

	public long getBankMoney() {
		return bankMoney;
	}

	public void addBankMoney(long bankMoney) {
		this.bankMoney += bankMoney;
	}

	public void subBankMoney(long bankMoney) {
		if(bankMoney > this.bankMoney) {
			bankMoney = this.bankMoney;
		}
		this.bankMoney -= bankMoney;
	}

	public boolean hasBankMoney(long bankMoney) {
		if(money <= 0) return true;

		return this.bankMoney >= money;
	}
	
}
