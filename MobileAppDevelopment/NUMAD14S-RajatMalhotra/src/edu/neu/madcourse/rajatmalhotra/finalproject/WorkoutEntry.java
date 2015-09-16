package edu.neu.madcourse.rajatmalhotra.finalproject;

public class WorkoutEntry {
	
//	int _id;
	String name;
	int reps;
	int weight;
	
	public WorkoutEntry()
	{
		
	}
	
	public WorkoutEntry(String name, int reps, int weight) {
		
		this.name = name;
		this.reps = reps;
		this.weight = weight;
	}
	
	public WorkoutEntry(int _id, String name, int reps, int weight) {
	
//		this._id = _id;
		this.name = name;
		this.reps = reps;
		this.weight = weight;
	}

//	public int get_id() {
//		return _id;
//	}
//
//	public void set_id(int _id) {
//		this._id = _id;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getReps() {
		return reps;
	}

	public void setReps(int reps) {
		this.reps = reps;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

}
