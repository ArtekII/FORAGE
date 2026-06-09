package service;

import org.springframework.stereotype.Service;
import model.Commune;
import repository.CommuneRepository;

import java.util.List;

@Service
public class CommuneService {
    
    private final CommuneRepository communeRepository;

    public CommuneService(CommuneRepository communeRepository) {
        this.communeRepository = communeRepository;
    }
    public List<Commune> getAllCommunes() {
        return communeRepository.findAll();
    }
    public List<Commune> getCommunesByDistrictId(Long districtId) {
        return communeRepository.findByDistrict_Id(districtId);
    }

    public Commune getCommuneByLibelleAndDistrictId(String libelle, Long districtId) {
        return communeRepository.findByLibelleAndDistrict_Id(libelle, districtId)
                .orElseThrow(() -> new IllegalStateException(
                        "Commune introuvable avec libellé: " + libelle + " et district ID: " + districtId));
    }

    public Commune getCommuneById(Long id) {
        return communeRepository.findById(id).get();
    }

}
