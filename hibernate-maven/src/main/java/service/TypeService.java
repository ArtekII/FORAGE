package service;

import java.util.List;

import org.springframework.stereotype.Service;

import model.Type;
import repository.TypeRepository;

@Service
public class TypeService {
    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public List<Type> getTypes() {
        return typeRepository.findAll();
    }

    public Type getTypeById(Long id) {
        return typeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Type introuvable: " + id));
    }
}
