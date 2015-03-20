package com.app.dbIO;

public class PathHandler {
	
	private String pathName;
	
	public final static PathHandler INSTANCE = new PathHandler();
	
	protected PathHandler() {
		
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}
	

	

}
