package it.polito.tdp.nobel.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> partenza;
	private List<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;
	
	
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.partenza=dao.getTuttiEsami();
	}


	public List<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {

		List<Esame> parziale = new ArrayList<Esame>();
		this.soluzioneMigliore = new ArrayList<Esame>();
		this.mediaSoluzioneMigliore = 0;
		
		cercaBrutto(parziale, 0, numeroCrediti);
		//cercaBello(parziale, 0, numeroCrediti);
		
		return soluzioneMigliore;	
	}

	//complessita: N!
	private void cercaBrutto(List<Esame> parziale, int L, int m) {

		//casi terminali
		//controllo i crediti
		int crediti = this.sommaCrediti(parziale);
		if(crediti>m) {
			return;
		}
		
		if(crediti==m) {
			double media = this.calcolaMedia(parziale);
			if(media>this.mediaSoluzioneMigliore) {
				this.soluzioneMigliore= new ArrayList<>(parziale); //la sovrascrivo
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
		/*for(Esame e : partenza) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cercaBrutto(parziale, L+1, m);
				parziale.remove(e);  //backtracking
				
			}
		}*/
		
		//N.B.: Non è ancora "perfetto": il controllo i>=L non è sufficiente ad evitare tutti i casi duplicati
			/*for(int i = 0; i < partenza.size(); i ++) {
			
				if(!parziale.contains(partenza.get(i)) && i >= L) {
					parziale.add(partenza.get(i));
					cercaBrutto(parziale, L+1, m);
					parziale.remove(partenza.get(i));
				}
					
			}
		*/
		
		//metodo definitivo
		
		int lastIndex=0;
		if(parziale.size()>0) {
			lastIndex =partenza.indexOf(parziale.get(parziale.size()-1));
		}
		
		for(int i=lastIndex; i<partenza.size();i++) {
			if(!parziale.contains(partenza.get(i))) {
				parziale.add(partenza.get(i));
				cercaBrutto(parziale, L+1, m);
				parziale.remove(partenza.get(i));
			}
		}
		
	}
	
	//complessità: 2^N (quindi anche lui esponenziale ma migliore di N!, comunque anhe lui con numeri alti è lento)
	private void cercaBello(List<Esame> parziale, int L, int m) {
		//casi terminali
			//controllo i crediti
			int crediti = this.sommaCrediti(parziale);
			if(crediti>m) {
				return;
			}
				
			if(crediti==m) {
				double media = this.calcolaMedia(parziale);
				if(media>this.mediaSoluzioneMigliore) {
					this.soluzioneMigliore= new ArrayList<>(parziale); //la sovrascrivo
					this.mediaSoluzioneMigliore=media;
				}
					
				return;
			}
			//se arrivo qua sicuramente: crediti < m e quindi posso andare avanti nella ricorsione (a meno che non siano finiti gli esami) 
			// L = N -> non ci sono più esami da aggiungere
			if(L==partenza.size()) {
				return;
			}
			
			//generazione sottoproblemi
			//partenza[L] è da aggiungere oppure no? provo entrambe le cose
			parziale.add(partenza.get(L));
			cercaBello(parziale, L+1, m);
			parziale.remove(partenza.get(L));
			cercaBello(parziale, L+1, m);
	}


	public double calcolaMedia(List<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(List<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
