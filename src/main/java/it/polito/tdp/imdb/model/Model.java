package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private ImdbDAO dao;
	private SimpleWeightedGraph<Actor, DefaultWeightedEdge> grafo;
	private Map<Integer, Actor> idMap;
	private List<Actor> verticiActors;
	
	public Model() {
		dao = new ImdbDAO();
		idMap = new HashMap<Integer, Actor>();
		
		for(Actor actor : dao.listAllActors()) {
			idMap.put(actor.getId(), actor);
		}
	}
	
	public List<String> getGeneri() {
		return dao.getGeneri();
	}
	
	public List<Actor> getAttori() {
		Collections.sort(verticiActors);
		return verticiActors;
	}
	
	public void creaGrafo(String genere) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		verticiActors = new ArrayList<>(dao.getAttoriVertici(idMap, genere));
		
		Graphs.addAllVertices(grafo, verticiActors);
		for(Adiacenza adiacenza : dao.getArchi(idMap, genere)) {
			if(grafo.getEdge(adiacenza.getAttore1(), adiacenza.getAttore2()) == null) {
				Graphs.addEdgeWithVertices(grafo, adiacenza.getAttore1(), adiacenza.getAttore2(), adiacenza.getPeso());
			}
		}
	}
	
	public Integer getNVertici() {
		return grafo.vertexSet().size();
	}
	 
	public Integer getNArchi() {
		return grafo.edgeSet().size();
	}
	
	public List<Actor> getAttoriConnessi(Actor attoreP) {
		ConnectivityInspector<Actor, DefaultWeightedEdge> cInspector = new ConnectivityInspector<>(grafo);
		List<Actor> attoriRagg = new ArrayList<>(cInspector.connectedSetOf(attoreP));
		attoriRagg.remove(attoreP);
		Collections.sort(attoriRagg);
		return attoriRagg;
	}
	
	public String doSimulazione(int n) {
		String string = null;
		Simulatore simulatore = new Simulatore(grafo);
		simulatore.init(n);
		simulatore.run();
		
		string = simulatore.stampaOutput();
		return string;
	}
}
