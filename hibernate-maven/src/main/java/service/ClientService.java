package service;

import org.springframework.stereotype.Service;
import model.Client;
import repository.ClientRepository;

import java.util.List;

@Service
public class ClientService {
    
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getClients() {
        return clientRepository.findAll();
    }

}
