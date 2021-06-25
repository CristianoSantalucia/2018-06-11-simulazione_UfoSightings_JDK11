package it.polito.tdp.ufo.model;

/*
 * classe Model preimpostata questo documento è soggetto ai relativi diritti di
 * ©Copyright giugno 2021
 */

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.*;

import it.polito.tdp.ufo.db.SightingsDAO;

public class Model
{
	private SightingsDAO dao;
	private Map<String, State> vertici;
	private Graph<State, DefaultEdge> grafo;

	public Model()
	{
		this.dao = new SightingsDAO();
	}

	public Collection<AnnoAvvistamento> getAnniAvvistamenti()
	{
		return dao.getAnniAvvistamenti();
	}

	public void creaGrafo(int year)
	{
		// ripulisco mappa e grafo
		this.vertici = new HashMap<>();
		this.grafo = new SimpleDirectedGraph<>(DefaultEdge.class); //

		/// vertici
		this.dao.getVertici(vertici, year); // riempio la mappa
		Graphs.addAllVertices(this.grafo, this.vertici.values());

		/// archi
		List<Adiacenza> adiacenze = new ArrayList<>(this.dao.getAdiacenze(vertici, year));
		for (Adiacenza a : adiacenze)
		{
			State s1 = a.getS1(); 
			State s2 = a.getS2();
			this.grafo.addEdge(s1, s2);
		}
	}

	public List<State> getSuccessivi(State s)
	{
		return Graphs.successorListOf(this.grafo, s); 
	}
	public List<State> getPrecedenti(State s)
	{
		return Graphs.predecessorListOf(this.grafo, s); 
	} 
	
	public String getInfo(State s)
	{
		String str = "\nSUCCESSIVI: "; 
		for(State state : this.getSuccessivi(s))
		{
			str += "\n" + state;
		}
		str += "\nPRECEDENTI: "; 
		for(State state : this.getPrecedenti(s))
		{
			str += "\n" + state;
		}
		str += "\nRAGGIUNGIBLI: "; 
		ConnectivityInspector<State, DefaultEdge> insp = new ConnectivityInspector<>(this.grafo);
		List<State> raggiungibili = new ArrayList<>(insp.connectedSetOf(s));
		for (State state : raggiungibili)
		{
			str += "\n" + state;
		}
		return str; 
	}
	
	public int getNumVertici()
	{
		return this.grafo.vertexSet().size();
	}

	public int getNumArchi()
	{
		return this.grafo.edgeSet().size();
	}

	public Collection<State> getVertici()
	{
		List<State> vertici = new ArrayList<>(this.grafo.vertexSet());
		vertici.sort((v1, v2) -> v1.getName().compareTo(v2.getName()));
		return vertici;
	}

	public Collection<DefaultEdge> getArchi()
	{
		return this.grafo.edgeSet();
	}
}