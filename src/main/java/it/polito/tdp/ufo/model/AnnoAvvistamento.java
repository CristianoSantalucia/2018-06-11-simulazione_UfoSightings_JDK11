package it.polito.tdp.ufo.model;

public class AnnoAvvistamento
{
	private Integer anno; 
	private Integer avvistamenti;
	public AnnoAvvistamento(Integer anno, Integer avvistamenti)
	{
		this.anno = anno;
		this.avvistamenti = avvistamenti;
	}
	public Integer getAnno()
	{
		return anno;
	}
	public void setAnno(Integer anno)
	{
		this.anno = anno;
	}
	public Integer getAvvistamenti()
	{
		return avvistamenti;
	}
	public void setAvvistamenti(Integer avvistamenti)
	{
		this.avvistamenti = avvistamenti;
	}
	@Override public String toString()
	{
		return String.format("%d (%d)", this.anno, this.avvistamenti);
	} 
	
}
