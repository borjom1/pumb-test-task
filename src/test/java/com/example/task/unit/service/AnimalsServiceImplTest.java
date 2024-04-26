package com.example.task.unit.service;

import com.example.task.dto.*;
import com.example.task.entity.AnimalEntity;
import com.example.task.repository.AnimalsRepository;
import com.example.task.service.impl.AnimalsServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.example.task.dto.AnimalType.cat;
import static com.example.task.dto.AnimalType.dog;
import static com.example.task.dto.Sex.female;
import static com.example.task.dto.Sex.male;
import static com.example.task.dto.AnimalOption.*;
import static com.example.task.entity.AnimalPriceCategory.fourth;
import static com.example.task.entity.AnimalPriceCategory.second;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnimalsServiceImplTest {

    private static final List<AnimalEntity> ANIMAL_ENTITIES = List.of(
            AnimalEntity.builder()
                    .name("Mia").cost(24.0).sex(female)
                    .type(cat).weight(31.0).category(second)
                    .build(),
            AnimalEntity.builder()
                    .name("Nala").cost(68.0).sex(female)
                    .type(dog).weight(6.0).category(fourth)
                    .build()
    );

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<AnimalEntity> criteriaQuery;

    @Mock
    private TypedQuery<AnimalEntity> typedQuery;

    @Mock
    private Root<AnimalEntity> root;

    @Mock
    private AnimalsRepository animalsRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private AnimalsServiceImpl animalsService;

    @BeforeEach
    void setUp() {
        // construct service
        animalsService = new AnimalsServiceImpl(
                entityManager,
                animalsRepository,
                validator,
                new ModelMapper()
        );
    }

    @Test
    @DisplayName("Should persist valid animals")
    void Should_PersistValidAnimals() {

        final var invalidAnimal = Animal.builder()
                .name("Simba")
                .type(dog)
                .weight(6.0)
                .build();

        final List<Animal> animals = List.of(
                Animal.builder()
                        .name("Mia").cost(24.0).sex(female)
                        .type(cat).weight(31.0).build(),
                Animal.builder()
                        .name("Nala").cost(68.0).sex(female)
                        .type(dog).weight(6.0).build(),
                invalidAnimal
        );

        when(validator.validate(any(Animal.class))).thenReturn(Collections.emptySet());
        //noinspection unchecked
        when(validator.validate(invalidAnimal)).thenReturn(Set.of(mock(ConstraintViolation.class)));

        animalsService.persistAnimals(animals);

        verify(animalsRepository, only()).saveAll(ANIMAL_ENTITIES);
    }

    @Nested
    @DisplayName("Test getAnimalsBy()")
    class TestGetAnimalsBy {

        @BeforeEach
        void setUp() {
            // mock criteria api query
            when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
            when(criteriaBuilder.createQuery(AnimalEntity.class)).thenReturn(criteriaQuery);

            when(criteriaQuery.from(AnimalEntity.class)).thenReturn(root);
            when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
            when(criteriaQuery.where(any(Predicate[].class))).thenReturn(criteriaQuery);

            when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        }

        @ParameterizedTest
        @MethodSource("filters")
        @DisplayName("Should collect predicates when filter criteria specified")
        void Should_CollectPredicates_When_FilterCriteriaSpecified(@NonNull Filter filter) {

            final List<Predicate> predicates = new ArrayList<>();
            if (filter.type != null) {
                final var predicate = mock(Predicate.class);
                predicates.add(predicate);
                when(criteriaBuilder.equal(root.get(type.name()), filter.type)).thenReturn(predicate);
            }

            if (filter.category != null) {
                final var predicate = mock(Predicate.class);
                predicates.add(predicate);
                when(criteriaBuilder.equal(root.get(category.name()), filter.category - 1)).thenReturn(predicate);
            }

            if (filter.sex != null) {
                final var predicate = mock(Predicate.class);
                predicates.add(predicate);
                when(criteriaBuilder.equal(root.get(sex.name()), filter.sex)).thenReturn(predicate);
            }

            when(typedQuery.getResultList()).thenReturn(emptyList());

            // service call
            animalsService.getAnimalsBy(filter.type, filter.category, filter.sex, null);

            verify(criteriaQuery, times(1)).where(predicates.toArray(Predicate[]::new));
            verify(criteriaQuery, never()).orderBy(any(Order[].class));
        }

        @NonNull
        private static Stream<Filter> filters() {
            return Stream.of(
                    new Filter(dog, 1, null),
                    new Filter(dog, null, null),
                    new Filter(dog, null, male),
                    new Filter(null, null, female),
                    new Filter(null, 1, female),
                    new Filter(null, 3, null),
                    new Filter(null, null, null)
            );
        }

        @ParameterizedTest
        @MethodSource("sorts")
        @DisplayName("Should order animals when sort option specified")
        void Should_OrderAnimals_When_SortOptionSpecified(List<AnimalOption> options) {

            final List<Order> orders = options != null ?
                    options.stream()
                            .map(option -> {
                                final var path = mock(Path.class);
                                //noinspection unchecked
                                when(root.get(option.name())).thenReturn(path);

                                final var order = mock(Order.class);
                                when(criteriaBuilder.asc(path)).thenReturn(order);
                                return order;
                            })
                            .toList()
                    : null;

            when(typedQuery.getResultList()).thenReturn(emptyList());

            // service call
            animalsService.getAnimalsBy(null, null, null, options);

            if (orders != null && !orders.isEmpty()) {
                verify(criteriaQuery, times(1)).orderBy(orders);
            } else {
                verify(criteriaQuery, never()).orderBy(any(Order[].class));
            }
        }

        @NonNull
        private static Stream<List<AnimalOption>> sorts() {
            return Stream.of(
                    List.of(cost),
                    List.of(name),
                    List.of(name, cost),
                    List.of(name, cost, type),
                    List.of(name, cost, type, weight, sex, category),
                    null,
                    emptyList()
            );
        }

        @Test
        @DisplayName("Should map fetched animals when exist")
        void Should_MapFetchedAnimals_When_Exist() {

            when(typedQuery.getResultList()).thenReturn(ANIMAL_ENTITIES);

            final List<AnimalDto> expectedAnimals = List.of(
                    AnimalDto.builder()
                            .name("Mia").cost(24.0).sex(female)
                            .type(cat).weight(31.0).category(2)
                            .build(),
                    AnimalDto.builder()
                            .name("Nala").cost(68.0).sex(female)
                            .type(dog).weight(6.0).category(4)
                            .build()
            );

            final List<AnimalDto> actualAnimals = animalsService.getAnimalsBy(null, null, null, null);

            assertThat(actualAnimals).isNotNull();
            assertThat(actualAnimals).isNotEmpty();
            assertThat(actualAnimals).hasSize(expectedAnimals.size());
            assertThat(actualAnimals).isEqualTo(expectedAnimals);

            verify(typedQuery, only()).getResultList();
        }

    }

    record Filter(
            AnimalType type,
            Integer category,
            Sex sex
    ) {
    }

}
