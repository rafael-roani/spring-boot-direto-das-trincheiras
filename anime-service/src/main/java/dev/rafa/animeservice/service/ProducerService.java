package dev.rafa.animeservice.service;

import dev.rafa.animeservice.domain.Producer;
import dev.rafa.animeservice.repository.ProducerRepository;
import dev.rafa.commonscore.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final ProducerRepository repository;

    public List<Producer> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public void delete(Long id) {
        Producer producerToDelete = findByIdOrThrowNotFound(id);
        repository.delete(producerToDelete);
    }

    public void update(Producer producerToUpdate) {
        assertProducerExists(producerToUpdate.getId());
        repository.save(producerToUpdate);
    }

    public void assertProducerExists(Long id) {
        findByIdOrThrowNotFound(id);
    }

    public Producer findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producer not found"));
    }

    public Producer save(Producer producer) {
        return repository.save(producer);
    }

}
