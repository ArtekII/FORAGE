package main;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import model.Demande;
import repository.DemandeRepository;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        //repository
        DemandeRepository demandeRepository = context.getBean(DemandeRepository.class);
        List<Demande> demandes = demandeRepository.findAll();
        demandes.forEach(demande -> System.out.println(demande.getId()));
        
    }
}
