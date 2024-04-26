package com.example.task.service.impl;

import com.example.task.dto.*;
import com.example.task.entity.AnimalEntity;
import com.example.task.entity.AnimalPriceCategory;
import com.example.task.repository.AnimalsRepository;
import com.example.task.service.AnimalsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimalsServiceImpl implements AnimalsService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final AnimalsRepository animalsRepository;

    private final Validator validator;
    private final ModelMapper modelMapper;

    @Transactional
    public void persistAnimals(@NonNull List<Animal> animals) {
        final List<AnimalEntity> animalEntities = animals.stream()
                .filter(animal -> validator.validate(animal).isEmpty())
                .map(animal -> modelMapper.map(animal, AnimalEntity.class))
                .peek(this::findPriceCategory)
                .toList();
        animalsRepository.saveAll(animalEntities);
    }

    @Override
    public List<AnimalDto> getAnimalsBy(AnimalType type, Integer category, Sex sex, List<AnimalOption> sortBy) {

        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<AnimalEntity> animalsQuery = cb.createQuery(AnimalEntity.class);
        final Root<AnimalEntity> root = animalsQuery.from(AnimalEntity.class);

        // collect predicates
        final List<Predicate> filterPredicates = new LinkedList<>();
        if (type != null) {
            filterPredicates.add(cb.equal(root.get(AnimalOption.type.name()), type));
        }
        if (category != null) {
            // since enum consts starts with 0, therefore perform -1
            filterPredicates.add(cb.equal(root.get(AnimalOption.category.name()), category - 1));
        }
        if (sex != null) {
            filterPredicates.add(cb.equal(root.get(AnimalOption.sex.name()), sex));
        }

        animalsQuery.select(root).where(filterPredicates.toArray(Predicate[]::new));

        // sort by specified fields
        if (sortBy != null && !sortBy.isEmpty()) {
            animalsQuery.orderBy(
                    sortBy.stream()
                            .distinct()
                            .map(field -> cb.asc(root.get(field.name())))
                            .toList()
            );
        }

        return entityManager.createQuery(animalsQuery)
                .getResultList()
                .stream()
                .map(animal -> AnimalDto.builder()
                        .name(animal.getName())
                        .cost(animal.getCost())
                        .category(animal.getCategory().ordinal() + 1)
                        .weight(animal.getWeight())
                        .type(animal.getType())
                        .sex(animal.getSex())
                        .build())
                .toList();
    }

    private void findPriceCategory(AnimalEntity animal) {
        for (var category : AnimalPriceCategory.values()) {
            if (category.isMatched(animal.getCost())) {
                animal.setCategory(category);
                break;
            }
        }
    }

}
