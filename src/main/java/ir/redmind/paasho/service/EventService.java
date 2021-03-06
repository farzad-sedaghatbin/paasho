package ir.redmind.paasho.service;

import ir.redmind.paasho.domain.Event;
import ir.redmind.paasho.service.dto.EventDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Event.
 */
public interface EventService {

    /**
     * Save a event.
     *
     * @param eventDTO the entity to save
     * @return the persisted entity
     */
    EventDTO save(EventDTO eventDTO);

    /**
     * Get all the events.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<EventDTO> findAll(Pageable pageable);

    /**
     * Get all the Event with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<EventDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" event.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Event> findOne(Long id);

    /**
     * Delete the "id" event.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    void approve(Long id);

    /**
     * Search for the event corresponding to the query.
     *
     * @return the list of entities
     */
    List<Event> searchByTitleOrDescription(String key);

    Page<EventDTO> search(String query, Pageable pageable);

    Page<EventDTO> searchByBuilder(String query, Pageable pageable);

    Event findByCode(String code);

    Event addUrl(String url, String code);
}
