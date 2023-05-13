package com.sp.repository;

import com.sp.model.OwnerUUID;
import org.springframework.data.repository.CrudRepository;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public abstract class SimpleUUIDAbstractRepository<T extends OwnerUUID> implements CrudRepository<T, UUID> {

    HashMap<UUID, T> owners = new HashMap<UUID, T>();

    @Override
    public <S extends T> S save(S s) {
        return null;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<T> findById(UUID uuid) {
        T owner = owners.getOrDefault(uuid, this.getNullOwner());
        return Optional.ofNullable(owner);
    }

    public abstract T getNullOwner();

    @Override
    public boolean existsById(UUID uuid) {
        return owners.containsKey(uuid);
    }

    @Override
    public Iterable<T> findAll() {
        return owners.values();
    }

    @Override
    public Iterable<T> findAllById(Iterable<UUID> iterable) {
        return null;
    }

    @Override
    public long count() {
        return owners.size();
    }

    @Override
    public void deleteById(UUID uuid) {
        owners.remove(uuid);
    }

    @Override
    public void delete(T owner) {
        this.deleteById(owner.getUUID());
    }

    @Override
    public void deleteAll(Iterable<? extends T> iterable) {
        iterable.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        owners.clear();
    }
}
