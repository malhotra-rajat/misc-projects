package edu.neu.madcourse.rajatmalhotra.trickiestpart;

public class Exercise {
	
	//private variables
    int _id;
	String name;

	
	// Empty constructor
    public Exercise(){
         
    }
    
    // constructor
    public Exercise(String name){
        this.name = name;
    }
    
    // constructor
    public Exercise(int _id, String name){
        this._id = _id;
    	this.name = name;
    }
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int _id) {
		this._id = _id;
	}

}
