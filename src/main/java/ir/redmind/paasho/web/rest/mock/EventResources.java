package ir.redmind.paasho.web.rest.mock;


import io.micrometer.core.annotation.Timed;
import ir.redmind.paasho.domain.Event;
import ir.redmind.paasho.domain.User;
import ir.redmind.paasho.domain.enumeration.ContactType;
import ir.redmind.paasho.domain.enumeration.PriceType;
import ir.redmind.paasho.security.SecurityUtils;
import ir.redmind.paasho.service.CategoryService;
import ir.redmind.paasho.service.EventService;
import ir.redmind.paasho.service.UserService;
import ir.redmind.paasho.service.dto.mock.*;
import ir.redmind.paasho.service.mapper.CategoryMapper;
import ir.redmind.paasho.service.mapper.EventMapper;
import ir.redmind.paasho.web.rest.errors.BadRequestAlertException;
import ir.redmind.paasho.web.rest.util.HeaderUtil;
import ir.redmind.paasho.web.rest.util.PaginationUtil;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@RestController
@RequestMapping("/api/v1/events/")
public class EventResources {

    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventMapper eventMapper;
    private final CategoryMapper categoryMapper;

    public EventResources(EventService eventService, UserService userService, CategoryService categoryService, EventMapper eventMapper, CategoryMapper categoryMapper) {
        this.eventService = eventService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.eventMapper = eventMapper;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping(value = "{code}/detail")
    @Timed
    @CrossOrigin(origins = "*")
    public ResponseEntity<DetailEventDTO> myEvents(@PathVariable("code") String code) {

        Event event = eventService.findByCode(code);
        DetailEventDTO eventDTO = getDetailEventDTO(code, event);
        return ResponseEntity.ok(eventDTO);

    }

    private DetailEventDTO getDetailEventDTO(@PathVariable("code") String code, Event event) {
        DetailEventDTO eventDTO = new DetailEventDTO();
        eventDTO.setCode(code);
//        eventDTO.setPic("");
        eventDTO.setTitle(event.getTitle());
        eventDTO.setPricing(event.getPriceType());
        eventDTO.setScore(event.getCreator().getScore().floatValue());
//        eventDTO.setTime("07:00");
//        eventDTO.setDate("1397/11/28");
        eventDTO.setAddress(event.getAddress());
        eventDTO.setCategory(event.getCategories().iterator().next().getName());
        eventDTO.setDescription(event.getDescription());
        eventDTO.setInstagram(event.getInstagram());
        eventDTO.setTel(event.getTel());
        eventDTO.setTelegram(event.getTelegram());
        eventDTO.setParticipantNumber(event.getParticipants().size());
        eventDTO.setView(event.getVisitCount());
        eventDTO.setLatitude(event.getLatitude());
        eventDTO.setLongitude(event.getLongitude());

        eventDTO.setAgeLimitFrom(10);
        eventDTO.setAgeLimitTo(40);
        eventDTO.setJoinStatus(JoinStatus.values()[new Random().nextInt(3)]);
        eventDTO.setLatitude(35.714558);
        eventDTO.setLongitude(51.414440);
        eventDTO.setCreator("فرزاد صداقت بین");
        return eventDTO;
    }

    @GetMapping(value = "search")
    @Timed
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<EventDTO>> events(@RequestParam("key") String key, Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(multiMatchQuery("key").field("title").field("description").type(MultiMatchQueryBuilder.Type.BEST_FIELDS)).build();
        Page<ir.redmind.paasho.service.dto.EventDTO> events = eventService.searchByBuilder(searchQuery.getQuery(), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        List<EventDTO> eventDTOS = new ArrayList<>();

        events.getContent().forEach(e -> {
            EventDTO event = getEventDTO(e);
            eventDTOS.add(event);

        });
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(events, String.format("/api/v1/search"));
        return ResponseEntity.ok().headers(headers).body(eventDTOS);

    }

    private EventDTO getEventDTO(ir.redmind.paasho.service.dto.EventDTO e) {
        EventDTO event = new EventDTO();
        event.setCode(e.getCode());
//            event.setPic(e.);
        event.setTitle(e.getTitle());
        event.setPricing(PriceType.FREE);
//            event.setTime("07:00");
//            event.setDate("1397/11/28");
        event.setCategoryId(Math.toIntExact(e.getCategories().iterator().next().getId()));
        User creator = userService.getUserWithAuthorities(e.getCreatorId()).get();
        event.setScore(creator.getScore().floatValue());
        event.setCreator(creator.getFirstName() + " " + creator.getLastName());
        event.setEditable(creator.getLogin().equalsIgnoreCase(SecurityUtils.getCurrentUserLogin().get()));
        return event;
    }

    @GetMapping(value = "{category}/titles")
    @Timed
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<String>> getTitles(@PathVariable("category") String category) {
        List<String> titles = new ArrayList<>();
        titles.add("پایه ای بریم فوتبال");
        titles.add("پایه ای بریم دورهمی");
        titles.add("پایه ای بریم سینما");
        titles.add("پایه ای بریم کوه");
        return ResponseEntity.ok(titles);
//TODO : create opbjects in jhipster
    }

    @GetMapping(value = "{code}/participants")
    @Timed
    @CrossOrigin(origins = "*")
    public ResponseEntity<Page<ProfileDTO>> participants(@PathVariable("code") String code, Pageable pageable) {

        Event event = eventService.findByCode(code);
        List<ProfileDTO> profileDTOS = new ArrayList<>();
//        if(event.getParticipants()!=null)
        event.getParticipants().forEach(p -> {
            ProfileDTO profileDTO = getProfileDTO(p);
            profileDTOS.add(profileDTO);
        });
        Page<ProfileDTO> page = new PageImpl(profileDTOS, new PageRequest(pageable.getPageNumber() + 1, profileDTOS.size()), 20);
        return ResponseEntity.ok(page);

    }

    private ProfileDTO getProfileDTO(User p) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setAvatar(p.getAvatar());
        profileDTO.setBirthYear(Integer.parseInt(p.getBirthDate()));
        profileDTO.setEmail(p.getEmail());
        profileDTO.setFirstName(p.getFirstName());
        profileDTO.setGender(p.getGender());
        profileDTO.setId(p.getId());
        profileDTO.setTelegram(p.getContacts().stream().filter(c -> c.getType().equals(ContactType.TELEGRAM)).findFirst().get().getValue());
        profileDTO.setInstagram(p.getContacts().stream().filter(c -> c.getType().equals(ContactType.INSTAGRAM)).findFirst().get().getValue());
        profileDTO.setLastName(p.getLastName());
        return profileDTO;
    }

    @GetMapping(value = "{code}/share")
    @Timed
    @CrossOrigin(origins = "*")
    public ResponseEntity<ShareDTO> share(@PathVariable("code") String code) {
        Event event = eventService.findByCode(code);

        ShareDTO shareDto = new ShareDTO();
        shareDto.setUser(event.getCreator().getFirstName() + " " + event.getCreator().getLastName());
        shareDto.setContent("میخواد با شما رویداد" + event.getTitle() + " را به اشتراک بگذارد دریافت پاشو از ");
        shareDto.setAndroidMarketURL("https://cafebazaar.ir");
        shareDto.setIosMarketURL("https://sibapp.com");
        return ResponseEntity.ok(shareDto);

    }


    @PostMapping(value = "{code}/rating")
    @Timed
    @CrossOrigin(origins = "*")
    public ResponseEntity<HttpStatus> rating(@PathVariable("code") String code, @RequestParam("rating") Double rating) {
//todo do rating
        return ResponseEntity.ok(HttpStatus.OK);

    }


    @PostMapping(value = "{code}/join")
    @Timed
    @CrossOrigin(origins = "*")
    public ResponseEntity<HttpStatus> join(@PathVariable("code") String code) {
//todo create notification
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PostMapping(value = "{code}/refuse")
    @Timed
    @CrossOrigin(origins = "*")
    public ResponseEntity<HttpStatus> refuse(@PathVariable("code") String code) {
//todo delete notification
        return ResponseEntity.ok(HttpStatus.OK);

    }


    @PostMapping(value = "")
    @Timed
    @CrossOrigin(origins = "*")
    public ResponseEntity<ir.redmind.paasho.service.dto.EventDTO> createEvent(@RequestBody CreateEventDTO createEventDTO) {

        Event event = new Event();
        event.setAddress(createEventDTO.getAddress());
        event.setCode(UUID.randomUUID().toString());
        event.setDescription(createEventDTO.getDescription());
        event.setTitle(createEventDTO.getTitle());
        event.setTel(createEventDTO.getTel());
        event.setLatitude(createEventDTO.getLatitude());
        event.setLongitude(createEventDTO.getLongitude());
        event.setMinAge(createEventDTO.getAgeLimitFrom());
        event.setMaxAge(createEventDTO.getAgeLimitTo());
        event.addCategories(categoryMapper.toEntity(categoryService.findOne((long) createEventDTO.getCategoryId()).get()));
//        event.setTelegram(createEventDTO.gett);
        event.setCreator(userService.getUserWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin().get()).get());
        eventService.save(eventMapper.toDto(event));
        return ResponseEntity.ok(eventMapper.toDto(event));
    }

    @PostMapping(value = "/{code}/upload")
    @Timed
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile[] multipartFile) {
//todo to ftp end save address to event
//        createEventDTO.setId(10l);
//        return ResponseEntity.ok(createEventDTO);
        return ResponseEntity.ok(multipartFile[0].getName());
    }

    @PutMapping("")
    public ResponseEntity<ir.redmind.paasho.service.dto.EventDTO> updateEvent(@RequestBody ir.redmind.paasho.service.dto.EventDTO eventDTO) throws URISyntaxException {
        if (eventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", "EVENT", "idnull");
        }
        ir.redmind.paasho.service.dto.EventDTO result = eventService.save(eventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("EVENT", eventDTO.getId().toString()))
            .body(result);
    }

    @GetMapping(value = "map")
    @Timed
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<MapEventDTO>> events(@RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude) {


        List<MapEventDTO> eventDTOS = new ArrayList<>();

        MapEventDTO event1 = new MapEventDTO();
        event1.setCode("123");
        event1.setPic("https://media.glassdoor.com/l/00/05/01/26/mhw-mt-shasta-climbing-event.jpg");
        event1.setTitle("کوه نوردی");
        event1.setPricing(PriceType.FREE);
        event1.setScore(4.5f);
        event1.setTime("07:00");
        event1.setTime("07:00");
        event1.setCategoryId(1);
        event1.setDate("1397/11/28");
        event1.setLatitude(35.713107);
        event1.setLongitude(51.412740);

        MapEventDTO event2 = new MapEventDTO();
        MapEventDTO event3 = new MapEventDTO();
        MapEventDTO event4 = new MapEventDTO();

        event2.setCode("567");
        event2.setPic("https://www.parship.ie/pics/pictures/en_IE/single-life_dating-for-true-love282x172.jpg");
        event2.setTitle("قرار دو نفره");
        event2.setPricing(PriceType.DUTCH);
        event2.setScore(5f);
        event2.setTime("20:00");
        event2.setDate("1397/11/28");
        event2.setCategoryId(2);
        event2.setLatitude(35.714558);
        event2.setLongitude(51.414440);

        event3.setCode("234");
        event3.setPic("https://d1zpvjny0s6omk.cloudfront.net/media/fileupload/2015/10/12/lombardi_stanzione-3521.jpg");
        event3.setTitle("کلاس نقاشی");
        event3.setPricing(PriceType.MONETARY);
        event3.setScore(3f);
        event3.setTime("12:30");
        event3.setDate("1397/11/28");
        event3.setLatitude(35.716020);
        event3.setLongitude(51.425097);
        event3.setCategoryId(3);


        event4.setCode("456");
        event4.setPic("https://www.bransoncc.com/wp-content/uploads/2016/04/Soccer2.jpg");
        event4.setTitle("فوتبال سالنی");
        event4.setPricing(PriceType.DUTCH);
        event4.setScore(4.2f);
        event4.setTime("21:00");
        event4.setDate("1397/11/28");
        event4.setLatitude(35.720862);
        event4.setLongitude(51.426506);
        event4.setCategoryId(4);


        eventDTOS.add(event1);
        eventDTOS.add(event2);
        eventDTOS.add(event3);
        eventDTOS.add(event4);

        return ResponseEntity.ok(eventDTOS);

    }


    @PutMapping(value = "{code}/view")
    @Timed
    @CrossOrigin(origins = "*")
    public ResponseEntity<HttpStatus> view(@PathVariable("code") String code) {

        return ResponseEntity.ok(HttpStatus.OK);

    }

}
