package de.thws.adapter.out.persistance.repository;


import de.thws.adapter.out.persistance.entities.RatingJpaEntity;
import de.thws.adapter.out.persistance.mapper.RatingMapper;
import de.thws.domain.exception.DuplicateEntityException;
import de.thws.domain.model.Rating;
import de.thws.domain.model.RatingFilter;
import de.thws.domain.port.out.DeleteRatingPort;
import de.thws.domain.port.out.PersistRatingPort;
import de.thws.domain.port.out.ReadRatingPort;
import de.thws.domain.port.out.UpdateRatingPort;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class RatingPersistenceAdapter implements PanacheRepository<RatingJpaEntity>, DeleteRatingPort, UpdateRatingPort, PersistRatingPort, ReadRatingPort {

    @Inject
    RatingMapper ratingMapper;

    @Inject
    EntityManager entityManager;



    @Transactional
    @Override
    public void persistRating(Rating rating) {
        try {
            final var jpaRating = this.ratingMapper.toJpaEntity(rating);
            persist(jpaRating);
            flush();
            rating.setId(jpaRating.getId());
        } catch (ConstraintViolationException e) {
            throw new DuplicateEntityException("User has already rated this book.");
        }
    }

    @Override
    public Optional<Rating> readRatingById(Long id) {
        final var jpaRating = findById(id);
        return Optional.ofNullable(jpaRating).
                map(ratingMapper::toDomainModel);
    }

    @Override
    public List<Rating> readAllRatings(RatingFilter filter, int pageIndex, int pageSize) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();
        if (filter.getRating() > 0) {
            query.append(" AND rating = :rating");
            params.put("rating", filter.getRating());
        }
        if (filter.getBookId() != null) {
            query.append(" AND book.id = :bookId");
            params.put("bookId", filter.getBookId());
        }
        if (filter.getUserId() != null) {
            query.append(" AND user.id = :userId");
            params.put("userId", filter.getUserId());
        }
        if (filter.getCreatedAfter() != null) {
            query.append(" AND creationTime >= :createdAfter");
            params.put("createdAfter", filter.getCreatedAfter());
        }
        if (filter.getCreatedBefore() != null) {
            query.append(" AND creationTime <= :createdBefore");
            params.put("createdBefore", filter.getCreatedBefore());
        }
        int startIndex = (pageIndex - 1) * pageSize;
        int endIndex = startIndex + pageSize;
        final var jpaRatings = find(query.toString(), params).range(startIndex, endIndex).list();
        return ratingMapper.toDomainModels(jpaRatings);
    }

    @Transactional
    @Override
    public void updateRating(Rating rating) {
        final var jpaRating = ratingMapper.toJpaEntity(rating);
        entityManager.merge(jpaRating);
        entityManager.flush();
    }

    @Override
    @Transactional
    public boolean deleteRatingById(long id){
        RatingJpaEntity entity = findById(id);
        if (entity == null) {
            return false;
        }
        delete(entity);
        return true;
    }
    }


