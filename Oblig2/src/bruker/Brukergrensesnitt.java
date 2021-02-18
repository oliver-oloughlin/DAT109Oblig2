package bruker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import aktorer.Bil;
import aktorer.Kunde;
import aktorer.Leiekontor;
import aktorer.Utleieselskap;
import utils.AktivUtleieselskap;
import utils.Utleiegruppe;

public class Brukergrensesnitt {
	
	private final Scanner scanner = new Scanner(System.in);
	private final Utleieselskap selskap = AktivUtleieselskap.selskap;
	private Kunde kunde = null;
	
	public void start() {
		
		loggInn();
		
		reserverBil();
		
		scanner.close();
		
	}
	
	private void loggInn() {
		
		String fornavn = null;
		String etternavn = null;
		System.out.println("Logg inn som kunde, skriv f�rst fornavn: ");
		
		while(kunde == null) {
			
			while(fornavn == null) {
				
				fornavn = scanner.next();
				
				if(fornavn == null) {
					System.out.println("Pr�v igjen, skriv fornavn: ");
				}
				
			}
			
			System.out.println("Skriv etternavn: ");
			
			while(etternavn == null) {
				
				etternavn = scanner.next();
				
				if(etternavn == null) {
					System.out.println("Pr�v igjen, skriv etternavn: ");
				}
				
			}
			
			kunde = selskap.loggInn(fornavn, etternavn);
			
			if(kunde == null) {
				System.out.println("Kunde med dette navnet ble ikke funnet. Program avbrytes.");
				System.exit(1);
			}
			
		}
		
		System.out.println("-- Du har logget inn som '" + kunde.getFornavn() + " " + kunde.getEtternavn() + "' --\n");
		 
	}
	
	private void reserverBil() {
		
		List<Leiekontor> kontorer = selskap.getLeiekontorer();
		
		System.out.println("Skriv tallet p� �nsket leiekontor for henting: ");
		Leiekontor utleiekontor = null;
		int valg = -1;
		while(utleiekontor == null) {
			
			for(int i = 0; i < kontorer.size(); i++) {
				System.out.println((i + 1) + ": " + kontorer.get(i).getAddresse().getPoststed());
			}
			
			try {
				valg = Integer.parseInt(scanner.next()) - 1;
			}
			catch(NumberFormatException e) {}
			
			if(valg < 0 || valg >= kontorer.size()) {
				System.out.println("Ugyldig valg, pr�v igjen: ");
			}
			else {
				utleiekontor = kontorer.get(valg);
			}
			
		}
		
		System.out.println("Skriv tallet p� �nsket leiekontor for levering: ");
		Leiekontor leveringskontor = null;
		valg = -1;
		while(leveringskontor == null) {
			
			for(int i = 0; i < kontorer.size(); i++) {
				System.out.println((i + 1) + ": " + kontorer.get(i).getAddresse().getPoststed());
			}
			
			try {
				valg = Integer.parseInt(scanner.next()) - 1;
			}
			catch(NumberFormatException e) {}
			
			if(valg < 0 || valg >= kontorer.size()) {
				System.out.println("Ugyldig valg, pr�v igjen: ");
			}
			else {
				leveringskontor = kontorer.get(valg);
			}
			
		}
		
		System.out.println("Skriv dato for utleie");
		LocalDate utleiedato = null;
		while(utleiedato == null) {
			
			try {
				System.out.println("�r: ");
				int aar = Integer.parseInt(scanner.next());
				System.out.println("M�nde: ");
				int maande = Integer.parseInt(scanner.next());
				System.out.println("Dag: ");
				int dag = Integer.parseInt(scanner.next());
				
				utleiedato = LocalDate.of(aar, maande, dag);
			}
			catch(Throwable e) {}
			
		}
		
		System.out.println("Velg klokkeslett for henting");
		LocalTime tidspunkt = null;
		while(tidspunkt == null) {
			
			try {
				System.out.println("Time: ");
				int time = Integer.parseInt(scanner.next());
				System.out.println("Minutt: ");
				int minutt = Integer.parseInt(scanner.next());
				
				tidspunkt = LocalTime.of(time, minutt);
			}
			catch(Throwable e) {}
			
		}
		
		System.out.println("Skriv antall dager for leie: ");
		int antallDager = 0;
		while(antallDager < 1) {
			
			try {
				antallDager = Integer.parseInt(scanner.next());
			}
			catch(NumberFormatException e) {}
			
			if(antallDager < 1) {
				System.out.println("Ugyldig valg av antall dager, pr�v igjen: ");
			}
			
		}
		
		List<Bil> biler = selskap.sokBil(utleiekontor, leveringskontor, utleiedato, tidspunkt, antallDager);
		Bil bil = velgBil(biler);
		selskap.reserverBil(kunde, bil, utleiekontor, leveringskontor, utleiedato, tidspunkt, antallDager);
		
	}
	
	private Bil velgBil(List<Bil> biler) {
		
		List<Utleiegruppe> ledigeGrupper = biler.stream().map(b -> b.getUtleiegruppe()).distinct().collect(Collectors.toList());
		
		System.out.println("Skriv tall p� �nsket bilgruppe: ");
		for(int i = 0; i < ledigeGrupper.size(); i++) {
			System.out.println((i + 1) + ": " + ledigeGrupper.get(i));
		}
		
		Utleiegruppe valgtGruppe = null;
		while(valgtGruppe == null) {
			
			try {
				int valg = Integer.parseInt(scanner.next()) - 1;
				valgtGruppe = ledigeGrupper.get(valg);
			}
			catch(Throwable e) {}
			
		}
		
		Bil bil = null;
		for(int i = 0; i < biler.size(); i++) {
			
			if(biler.get(i).getUtleiegruppe() == valgtGruppe) {
				bil = biler.get(i);
				break;
			}
			
		}
		
		return bil;
		
	}

}









