package dev.rafa.animeservice.service;

import dev.rafa.animeservice.domain.Producer;
import dev.rafa.animeservice.exception.NotFoundException;
import dev.rafa.animeservice.repository.ProducerHardCodedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final ProducerHardCodedRepository repository;

    public List<Producer> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public void delete(Long id) {
        Producer producerToDelete = findByIdOrThrowNotFound(id);
        repository.delete(producerToDelete);
    }

    public void update(Producer producerToUpdate) {
        Producer producer = findByIdOrThrowNotFound(producerToUpdate.getId());
        producerToUpdate.setCreatedAt(producer.getCreatedAt());
        repository.update(producerToUpdate);
    }

    public Producer findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producer not found"));
    }

    public Producer save(Producer producer) {
        return repository.save(producer);
    }

}
