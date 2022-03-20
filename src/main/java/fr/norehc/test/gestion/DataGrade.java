package fr.norehc.test.gestion;

import java.util.UUID;

import fr.norehc.test.gestion.unit.GradeUnit;

public class DataGrade extends AbstractData {
	private GradeUnit grade;
	private long end;
	
	public DataGrade(UUID uuid) {
		this.uuid = uuid;
	}
	
	public void setGrade(GradeUnit grade) {
		this.grade = grade;
		end = -1;
	}
	
	public void setGrade(GradeUnit grade, long seconds ) {
		if(seconds <= 0) {
			setGrade(grade);
		}else {
			this.grade = grade;
			end = seconds * + System.currentTimeMillis();
		}
	}
	
	public GradeUnit getGrade() {
		return grade;
	}
	
	public long getEnd() {
		return end;
	}
	
	public boolean isTemporary() {
		return end != -1;
	}
	
	public boolean isValid() {
		return end != -1 && end < System.currentTimeMillis();
	}
}
