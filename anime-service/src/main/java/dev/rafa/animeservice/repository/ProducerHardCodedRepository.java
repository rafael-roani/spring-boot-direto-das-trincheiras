package dev.rafa.animeservice.repository;

import dev.rafa.animeservice.domain.Producer;
import external.dependency.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProducerHardCodedRepository {

    private final ProducerData producerData;

    @Qualifier("connectionMySql")
    private final Connection connection;

    public List<Producer> findAll() {
        log.debug("{}", connection);
        return producerData.getProducers();
    }

    public Optional<Producer> findById(Long id) {
        return producerData.getProducers().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    public List<Producer> findByName(String name) {
        return producerData.getProducers().stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .toList();
    }

    public void update(Producer producer) {
        delete(producer);
        save(producer);
    }

    public Producer save(Producer producer) {
        producerData.getProducers().add(producer);
        return producer;
    }

    public void delete(Producer producer) {
        producerData.getProducers().remove(producer);
    }

}
