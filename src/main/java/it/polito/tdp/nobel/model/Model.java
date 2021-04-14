package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> partenza;
	private Set<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;
	
	
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.partenza=dao.getTuttiEsami();
	}


	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {

		Set<Esame> parziale = new HashSet<Esame>();
		this.soluzioneMigliore = new HashSet<Esame>();
		this.mediaSoluzioneMigliore = 0;
		
		cerca(parziale, 0, numeroCrediti);
		
		return soluzioneMigliore;	
	}

	//complessita: N!
	private void cerca(Set<Esame> parziale, int L, int m) {

		//casi terminali
		//controllo i crediti
		int crediti = this.sommaCrediti(parziale);
		if(crediti>m) {
			return;
		}
		
		if(crediti==m) {
			double media = this.calcolaMedia(parziale);
			if(media>this.mediaSoluzioneMigliore) {
				this.soluzioneMigliore= new HashSet<>(parziale); //la sovrascrivo
				this.mediaSoluzioneMigliore=media;
			}
			
			return;
		}
		//se arrivo qua sicuramente: crediti < m e quindi posso andare avanti nella ricorsione (a meno che non siano finiti gli esami) 
		// L = N -> non ci sono più esami da aggiungere
		if(L==partenza.size()) {
			return;
		}
		
		//generare i sotto-problemi
		for(Esame e : partenza) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca(parziale, L+1, m);
				parziale.remove(e);  //backtracking
				
			}
		}
	}


	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
