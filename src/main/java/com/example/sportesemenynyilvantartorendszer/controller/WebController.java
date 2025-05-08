package com.example.sportesemenynyilvantartorendszer.controller;

import com.example.sportesemenynyilvantartorendszer.model.Event;
import com.example.sportesemenynyilvantartorendszer.model.Participant;
import com.example.sportesemenynyilvantartorendszer.service.EventService;
import com.example.sportesemenynyilvantartorendszer.service.ParticipantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final EventService eventService;
    private final ParticipantService participantService;
    private static final int MAX_AGE = 99;

    @ModelAttribute("favoriteCount")
    public long favoriteCount() {
        return eventService.findAll().stream()
                .filter(Event::isFavorite)
                .count();
    }

    @ModelAttribute("categories")
    public List<String> categories() {
        return List.of(
                "Futás", "Kosárlabda", "Tenisz", "Úszás", "Labdarúgás",
                "Röplabda", "Kézilabda", "Jégkorong", "Kerékpározás",
                "Vívás", "Asztalitenisz"
        );
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("events", eventService.findAll().stream()
                .sorted(Comparator.comparing(Event::getDate))
                .toList());
        model.addAttribute("title", "Összes sportesemény");
        return "index";
    }

    @GetMapping("/football")
    public String footballPage(Model model) {
        return listEventsByCategory(model,
                "Labdarúgás", "Labdarúgás események");
    }

    @GetMapping("/basketball")
    public String basketballPage(Model model) {
        return listEventsByCategory(model,
                "Kosárlabda", "Kosárlabda események");
    }

    @GetMapping("/tennis")
    public String tennisPage(Model model) {
        return listEventsByCategory(model, "Tenisz", "Tenisz események");
    }

    @GetMapping("/swimming")
    public String swimmingPage(Model model) {
        return listEventsByCategory(model, "Úszás", "Úszás események");
    }

    @GetMapping("/running")
    public String runningPage(Model model) {
        return listEventsByCategory(model, "Futás", "Futás események");
    }

    @GetMapping("/volleyball")
    public String volleyballPage(Model model) {
        return listEventsByCategory(model, "Röplabda", "Röplabda események");
    }

    @GetMapping("/handball")
    public String handballPage(Model model) {
        return listEventsByCategory(model, "Kézilabda", "Kézilabda események");
    }

    @GetMapping("/icehockey")
    public String icehockeyPage(Model model) {
        return listEventsByCategory(model, "Jégkorong", "Jégkorong események");
    }

    @GetMapping("/cycling")
    public String cyclingPage(Model model) {
        return listEventsByCategory(model,
                "Kerékpározás", "Kerékpározás események");
    }

    @GetMapping("/fencing")
    public String fencingPage(Model model) {
        return listEventsByCategory(model, "Vívás", "Vívás események");
    }

    @GetMapping("/tabletennis")
    public String tabletennisPage(Model model) {
        return listEventsByCategory(model,
                "Asztalitenisz", "Asztalitenisz események");
    }

    @GetMapping("/favorites")
    public String favoritesPage(Model model) {
        model.addAttribute("events", eventService.findAll().stream()
                .filter(Event::isFavorite)
                .sorted(Comparator.comparing(Event::getDate))
                .toList());
        model.addAttribute("title", "Kedvenc események");
        return "event-list";
    }

    @GetMapping("/upcoming-events")
    public String upcomingEvents(Model model) {
        model.addAttribute("events", eventService.findAll().stream()
                .filter(event -> event.getDate() != null
                        && event.getDate().isAfter(LocalDate.now()))
                .sorted(Comparator.comparing(Event::getDate))
                .toList());
        model.addAttribute("title", "Közelgő események");
        return "event-list";
    }

    @GetMapping("/completed-events")
    public String completedEvents(Model model) {
        model.addAttribute("events", eventService.findAll().stream()
                .filter(event -> event.getDate() != null
                        && !event.getDate().isAfter(LocalDate.now()))
                .sorted(Comparator.comparing(Event::getDate))
                .toList());
        model.addAttribute("title", "Teljesített események");
        return "event-list";
    }

    @GetMapping("/add")
    public String showAddPage(Model model) {
        if (!model.containsAttribute("participant")) {
            model.addAttribute("participant", new Participant());
        }
        model.addAttribute("events", eventService.findAll().stream()
                .sorted(Comparator.comparing(Event::getDate))
                .toList());
        return "add";
    }



    @PostMapping("/add-participant")
    public String addParticipant(
            @Valid @ModelAttribute Participant participant,
            BindingResult bindingResult,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String newEventName,
            @RequestParam(required = false) String newEventLocation,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.participant",
                    bindingResult);
            redirectAttributes.addFlashAttribute("participant", participant);
            return "redirect:/add";
        }

        if (participant.getAge() < 0 || participant.getAge() > MAX_AGE) {
            redirectAttributes.addAttribute("error", "invalidAge");
            return "redirect:/add";
        }

        boolean existingSelected = participant.getEvent() != null
                && participant.getEvent().getId() != null;

        boolean newEventProvided = newEventName != null
                && !newEventName.isBlank()
                && newEventLocation != null
                && !newEventLocation.isBlank();

        if (!existingSelected && !newEventProvided) {
            redirectAttributes.addAttribute("error", "noEvent");
            return "redirect:/add";
        }

        if (newEventProvided && !existingSelected) {
            Event newEvent = Event.builder()
                    .name(newEventName)
                    .location(newEventLocation)
                    .category(category)
                    .date(participant.getActivityDate())
                    .favorite(false)
                    .build();
            eventService.save(newEvent);
            participant.setEvent(newEvent);
        } else if (existingSelected) {
            Event existingEvent = eventService
                    .findById(participant.getEvent().getId())
                    .orElse(null);
            if (existingEvent != null) {
                participant.setActivityDate(existingEvent.getDate());
                participant.setEvent(existingEvent);
            }
        }

        participantService.save(participant);
        return "redirect:/";
    }
    @GetMapping("/editParticipant/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Participant participant = participantService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Résztvevő nem található ID: " + id));
        List<Event> events = eventService.findAll().stream()
                .sorted(Comparator.comparing(Event::getDate))
                .toList();
        model.addAttribute("participant", participant);
        model.addAttribute("events", events);
        return "edit";
    }

    @PostMapping("/updateParticipant/{id}")
    public String updateParticipant(@PathVariable Long id,
 @Valid @ModelAttribute Participant participant,
 BindingResult bindingResult,
 @RequestParam(required = false) String category,
 @RequestParam(required = false) String newEventName,
 @RequestParam(required = false) String newEventLocation,
 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
"org.springframework.validation." + "BindingResult.participant",
                    bindingResult);
            redirectAttributes.addFlashAttribute("participant", participant);
            return "redirect:/editParticipant/" + id;
        }

        if (participant.getAge() < 0 || participant.getAge() > MAX_AGE) {
            redirectAttributes.addFlashAttribute("error", "invalidAge");
            return "redirect:/editParticipant/" + id;
        }

        boolean existingSelected = participant.getEvent()
                != null && participant.getEvent().getId() != null;
        boolean newEventProvided = newEventName
                != null && !newEventName.isBlank()
                && newEventLocation != null && !newEventLocation.isBlank();

        if (!existingSelected && !newEventProvided) {
            redirectAttributes.addFlashAttribute("error", "noEvent");
            return "redirect:/editParticipant/" + id;
        }

        if (newEventProvided && !existingSelected) {
            Event newEvent = Event.builder()
                    .name(newEventName)
                    .location(newEventLocation)
                    .category(category)
                    .date(participant.getActivityDate())
                    .favorite(false)
                    .build();
            eventService.save(newEvent);
            participant.setEvent(newEvent);
        } else if (existingSelected) {
            Event existingEvent = eventService
                    .findById(participant.getEvent().getId())
                    .orElse(null);
            if (existingEvent != null) {
                participant.setActivityDate(existingEvent.getDate());
                participant.setEvent(existingEvent);
            }
        }

        participantService.updateParticipant(id, participant);
        return "redirect:/";
    }



    @PostMapping("/toggle-favorite/{id}")
    public String toggleFavorite(@PathVariable Long id,
                                 HttpServletRequest request) {
        eventService.findById(id).ifPresent(event -> {
            event.setFavorite(!event.isFavorite());
            eventService.save(event);
        });
        return "redirect:" + getReferer(request);
    }

    @PostMapping("/unfollow-favorite/{id}")
    public String unfollowFavorite(@PathVariable Long id,
                                   HttpServletRequest request) {
        eventService.findById(id).ifPresent(event -> {
            if (event.isFavorite()) {
                event.setFavorite(false);
                eventService.save(event);
            }
        });
        return "redirect:" + getReferer(request);
    }

    @PostMapping("/delete-participant/{id}")
    public String deleteParticipant(@PathVariable Long id,
                                    HttpServletRequest request) {
        participantService.deleteById(id);
        return "redirect:" + getReferer(request);
    }

    private String listEventsByCategory(Model model,
                                        String category,
                                        String title) {
        model.addAttribute("events", eventService.findByCategory(category)
                .stream()
                .sorted(Comparator.comparing(Event::getDate))
                .toList());
        model.addAttribute("title", title);
        return "event-list";
    }

    private String getReferer(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return referer != null ? referer : "/";
    }
    @PostMapping("/delete-event/{id}")
    public String deleteEvent(@PathVariable Long id,
                              HttpServletRequest request) {
        eventService.findById(id).ifPresent(event -> {

            List<Participant> participants =
                    participantService.findAll().stream()
                            .filter(p -> p.getEvent().getId().equals(id))
                            .toList();
            participants.forEach(p -> participantService.deleteById(p.getId()));


            eventService.deleteById(id);
        });
        return "redirect:" + getReferer(request);
    }

}
