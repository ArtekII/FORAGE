package service;

import org.springframework.stereotype.Service;

import repository.StatutRepository;
import model.Statut;
import java.util.List;

@Service
public class StatutService {
    private final StatutRepository statutRepository;

    public StatutService(StatutRepository statutRepository) {
        this.statutRepository = statutRepository;
    }

    public List<Statut> getStatuts() {
        return statutRepository.findAll();
    }

    public Statut getStatutById(Long id) {
        return statutRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Statut introuvable: " + id));
    }
}
