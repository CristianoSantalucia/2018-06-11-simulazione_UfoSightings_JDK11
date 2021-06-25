package it.polito.tdp.ufo.model;

public class State
{
	private String name;

	public State(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	@Override public String toString()
	{
		return name.toUpperCase();
	} 
	
}	
