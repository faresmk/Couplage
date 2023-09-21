import java.util.*;
import java.io.*;


public class Graphe {
    Maillon [] sommets; //tableau de listes d'adjacence


    Graphe(InputStream in) throws Exception {
        lire(in);
    }

    void lire(InputStream in) throws Exception {
        Scanner s;
        int nombre_sommets;
        String specification_arc;
        String [] parties;
        int numero, source, destination, etiquette;

        s = new Scanner(in);
        nombre_sommets = s.nextInt();
        sommets = new Maillon[nombre_sommets];

        while (s.hasNext()) {
            specification_arc = s.next();
            if (!specification_arc.matches(
                    "[0-9]+/[0-9]+\\+[0-9]+/->-?[0-9]+"))
                throw new Exception("Arc mal formé : " + specification_arc);

            parties = specification_arc.split("/", 2);
            numero = Integer.valueOf(parties[0]);
            parties = parties[1].split("\\+", 2);
            source = Integer.valueOf(parties[0]) - 1;
            parties = parties[1].split("/->", 2);
            destination = Integer.valueOf(parties[0]) - 1;
            etiquette = Integer.valueOf(parties[1]);

            Maillon nouveau, courant;
            nouveau = new Maillon();
            nouveau.arc = new Arc(numero, source, destination,
                    new Etiquette(etiquette));
            nouveau.suivant = null;
            if (sommets[source] == null) {
                sommets[source] = nouveau;
            } else {
                courant = sommets[source];
                while (courant.suivant != null)
                    courant = courant.suivant;
                courant.suivant = nouveau;
            }
        }
    }

    public String toString() {
        String resultat;

        resultat = sommets.length + "\n";
        for (int i=0; i<sommets.length; i++) {
            Maillon courant;

            courant = sommets[i];
            while (courant != null) {
                resultat += courant.arc + "\n";
                courant = courant.suivant;
            }
        }

        return resultat;
    }

    //retourne le nombre de sommets du graphe
    public int getNbSommets() {
    	return this.sommets.length;
    }
    //returne le nombre d'arcs du graphe
    public int getNbArcs() {
    	int nbArcs = 0;
    	for (int i = 0; i < this.sommets.length; i++) {
    		Maillon courant = this.sommets[i];
    		while (courant != null) {
    			nbArcs++;
    			courant = courant.suivant;
    		}
    	}
    	return nbArcs;
    }

    //verfier l'existence d'un arc entre deux sommets
    public boolean existeArc(int sour, int dest) {
    	Maillon courant = this.sommets[sour-1];
    	while (courant != null) {
    		if (courant.arc.destination==dest-1 ) {
    			return true;
    		}
        		courant = courant.suivant;
    	}
    	return false;
    }
    //étiquette associée à un arc
    public int getEtiquette(int source, int destination) {
    	Maillon courant = this.sommets[source-1];
    	while (courant != null) {
    		if (courant.arc.destination == destination-1) {
    			return courant.arc.etiquette.valeur;
    		}
    		courant = courant.suivant;
    	}
    	return 0;
    }
    //ensemble des successeurs d'un sommet (retourné sous forme de tableau)
    public int[] getSuccesseurs(int sommet) {
    	int[] successeurs = new int[this.getNbSommets()];
    	int i = 0;
    	Maillon courant = this.sommets[sommet-1];
    	while (courant != null) {
    		successeurs[i] = courant.arc.destination+1;
    		i++;
    		courant = courant.suivant;
    	}
    	return successeurs;
    }
    //ensemble des prédécesseurs d'un sommet (retourné sous forme de tableau)
    public int[] getPredecesseurs(int sommet) {
    	int[] predecesseurs = new int[this.getNbSommets()];
    	int i = 0;
    	for (int j = 0; j < this.sommets.length; j++) {
    		Maillon courant = this.sommets[j];
    		while (courant != null) {
    			if (courant.arc.destination == sommet-1) {
    				predecesseurs[i] = j+1;
    				i++;
    			}
    			courant = courant.suivant;
    		}
    	}
    	return predecesseurs;
    }

    //retourne le degré d'un sommet
    public int getDegre(int sommet) {
        int[] successeurs = this.getSuccesseurs(sommet);
        int[] predecesseurs = this.getPredecesseurs(sommet);
        int degre = 0;
        for (int i = 0; i < successeurs.length; i++) {
            if (successeurs[i] != 0) {
                degre++;
            }
        }
        for (int i = 0; i < predecesseurs.length; i++) {
            if (predecesseurs[i] != 0) {
                degre++;
            }
        }
        return degre;
    }



    //ArcsIndépendants : retourne un tableau contenant les numéros des arcs qui sont indépendants
    public Arc[] getArcsIndependants() {
    	Arc[] arcsIndep = new Arc[this.getNbArcs()];
    	int i = 0;
    	for (int j = 0; j < this.sommets.length; j++) {
    		Maillon courant = this.sommets[j];
    		while (courant != null) {
    			if (this.getDegre(courant.arc.source+1) == 1 && this.getDegre(courant.arc.destination+1) == 1) {
    				arcsIndep[i] = courant.arc;
    				i++;
    			}
    			courant = courant.suivant;
    		}
    	}
    	return arcsIndep;
    }





    //EstBiparti : retourne vrai si le graphe est biparti, faux sinon en utlisan les methodes precidentes
    public boolean estBiparti() {
		//on crée un tableau de sommets
        	int[] sommets = new int[this.getNbSommets()];
        	for (int i = 0; i < sommets.length; i++) {
        		sommets[i] = 0;
        	}

        	for (int i = 0; i < this.sommets.length; i++) {
        		Maillon courant = this.sommets[i];
        		while (courant != null) {
        			if (sommets[i] == 0) {
        				sommets[i] = 1;
        				sommets[courant.arc.destination] = 2;
        			}
        			else if (sommets[i] == 1) {
        				sommets[courant.arc.destination] = 2;
        			}
        			else if (sommets[i] == 2) {
        				sommets[courant.arc.destination] = 1;
        			}
        			courant = courant.suivant;
        		}
        	}
        	for (int i = 0; i < this.sommets.length; i++) {
        		Maillon courant = this.sommets[i];
        		while (courant != null) {
        			if (sommets[i] == sommets[courant.arc.destination]) {
        				return false;
        			}
        			courant = courant.suivant;
        		}
        	}
        	return true;
    }

	//EstCouplage : retourne vrai si le graphe est un couplage, faux sinon en utilisant les methodes precidentes
	public boolean estCouplage() {
		int[] successeurs;
		int compteur = 0;
		for (int i = 0; i < this.getNbSommets(); i++) {
			successeurs = this.getSuccesseurs(i);
			compteur = 0;
			for (int j = 0; j < successeurs.length; j++) {
				if (successeurs[j] != 0) {
					compteur++;
				}
			}
			if (compteur > 1) {
				return false;
			}
		}
		return true;
	}

}
