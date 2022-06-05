package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.imdb.model.Event.EventType;

public class Simulatore {
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private int nGiorni;
	private PriorityQueue<Event> queue;
	
	// Dati in uscita dalla simulazione
	private int numeroGiorniPausa;
	private Set<Actor> intervistati;
	
	public Simulatore(Graph<Actor, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
		queue = new PriorityQueue<>();
	}
	
	public void init(int n) {
		nGiorni = n;
		numeroGiorniPausa = 0;
		intervistati = new HashSet<>();
		
		// Il primo giorno il produttore sceglie l'attore da intervistare in maniera casuale
		Actor intervistato = selezionaIntervistato(this.grafo.vertexSet()); 
		intervistati.add(intervistato);
		
		// 1 indica che siamo al primo giorno
		queue.add(new Event(1, EventType.DA_INTERVISTARE, intervistato));
	}
	
	public void run() {
		while (!queue.isEmpty()) {
			Event e = queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		// Prendo il giorno che sono arrivato a processare nell'evento e continuo a generare eventi finchè non arrivo a nGiorni stabiliti in input
		int giorno = e.getGiorno();
		if(giorno < nGiorni) {
			switch (e.getType()) {
			case DA_INTERVISTARE:
				double random = Math.random();
				Actor intervistato = null;
				if(random <= 0.6) {
					intervistato = selezionaIntervistato(this.grafo.vertexSet()); 
					intervistati.add(intervistato);				
				}
				else {
					intervistato = selezionaAdiacente(e.getAttore());
					if(intervistato == null) {
						intervistato = selezionaIntervistato(grafo.vertexSet());
					}
					
					intervistati.add(intervistato);
				}
				
				if(intervistato.getGender().compareTo(e.getAttore().getGender()) == 0) {
					// Se per due giorni consecutivi intervisto un attore dello stesso genere di film allora con il 90% di probabilità prendo ferie il giorno dopo
					if(Math.random() <= 0.9)
						queue.add(new Event(giorno + 1, EventType.FERIE, e.getAttore()));
					else {
						queue.add(new Event(giorno + 1, EventType.DA_INTERVISTARE, intervistato));
					}
				}				
				break;
				
			case FERIE:
				numeroGiorniPausa++;
				intervistato = selezionaIntervistato(this.grafo.vertexSet()); 
				intervistati.add(intervistato);		
				queue.add(new Event(giorno + 1, EventType.DA_INTERVISTARE, intervistato));
				break;
				
			default:
				break;
			}
		}
		
	}
	
	private Actor selezionaIntervistato(Collection<Actor> lista) { // Lista: sono i vertici del grafo all'inizio
		Set<Actor> candidati = new HashSet<Actor>(lista);
		candidati.removeAll(intervistati); // Dai possibili candidati tolgo coloro che sono già stati intervistati
		
		int scelto = (int)(Math.random()*candidati.size());
		return (new ArrayList<Actor>(candidati)).get(scelto);
	}
	
	private Actor selezionaAdiacente(Actor a) {
		// Mi prendo i vicini di un certo utente, tolgo quelli già intervistati e vedo se ne resta qualcuno
		List<Actor> vicini = Graphs.neighborListOf(grafo, a);
		vicini.removeAll(intervistati); // Dalla lista dei vicini tolgo quelli che sono già stati intervistati
		
		if(vicini.size() == 0)
			return null; // Capita quando il vertice è isolato oppure tutti gli adiacenti sono già stati intervistati
		
		// Calcolo il massimo ora
		double max = 0;
		for(Actor v : vicini) {
			double peso = grafo.getEdgeWeight(grafo.getEdge(a, v)); // Prendo il peso dell'arco che collega a e v
			if(peso > max) {
				max = peso;
			}
		}
		
		// Creo una nuova lista di migliori che hanno peso = max
		List<Actor> migliori = new ArrayList<>();
		for(Actor v : vicini) {
			double peso = grafo.getEdgeWeight(grafo.getEdge(a, v)); // Prendo il peso dell'arco che collega a e v
			if(peso == max) {
				migliori.add(v);
			}			
		}
		
		int scelto = (int)(Math.random()*migliori.size()); // Se size() = 1 la math.random() mi restituisce un numero tra 0 e 1, approssimato a (int) mi restituisce 0 che
														   // sarà l'unico elemento della lista
		return migliori.get(scelto);
	}
	
	public String stampaOutput() {
		String stampa = "Attori intervistati dal produttore: \n";
		
		for(Actor actor : intervistati) {
			stampa += actor.toString() + "\n";
		}
		
		stampa += "Numero di pause che il produttore si è preso in " + nGiorni + ": " + numeroGiorniPausa;
		return stampa;		
	}
}
