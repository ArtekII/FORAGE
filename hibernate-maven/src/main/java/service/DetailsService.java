package service;

import org.springframework.stereotype.Service;
import model.Details;
import repository.DetailsRepository;

import java.util.List;

@Service
public class DetailsService {
    private final DetailsRepository detailsRepository;

    public DetailsService(DetailsRepository detailsRepository) {
        this.detailsRepository = detailsRepository;
    }

    public List<Details> getDetails() {
        return detailsRepository.findAll();
    }

    public List<Details> getDetailsByDevis_Id(Long id) {
        return detailsRepository.findByDevis_Id(id);
    }

    public Details getDetailsById(Long id) {
        return detailsRepository.findById(id).get();
    }

    public Details createDetails(Details details) {
        Details savedDetails = detailsRepository.save(details);
        return savedDetails;
    }
}
