package it.polito.tdp.imdb.model;

import it.polito.tdp.imdb.model.Event;

public class Event implements Comparable<Event> {
	public enum EventType {
		DA_INTERVISTARE,
		FERIE
	}
	
	private int giorno;
	private EventType type;
	private Actor attore;
	
	public Event(int giorno, EventType type, Actor attore) {
		super();
		this.giorno = giorno;
		this.type = type;
		this.attore = attore;
	}

	public int getGiorno() {
		return giorno;
	}


	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}


	public Actor getAttore() {
		return attore;
	}


	public void setAttore(Actor attore) {
		this.attore = attore;
	}


	public EventType getType() {
		return type;
	}

	@Override
	public int compareTo(Event o) {
		return this.giorno - o.giorno;
	}
}
