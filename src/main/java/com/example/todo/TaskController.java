package com.example.todo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TaskController {

    private final TaskRepository repo;
    private final TrelloService trelloService;

    public TaskController(TaskRepository repo, TrelloService trelloService) {
        this.repo = repo;
        this.trelloService = trelloService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tasks", repo.findAll());
        model.addAttribute("task", new Task());
        return "index";
    }

    @PostMapping("/tasks")
    public String create(Task task, RedirectAttributes ra) {
        repo.save(task);

        try {
            TrelloService.TrelloCard card = trelloService.createCardForTask(task);
            task.setTrelloCardId(card.id);
            task.setTrelloShortUrl(card.shortUrl != null ? card.shortUrl : card.url);
            repo.save(task);
            ra.addFlashAttribute("msg", "Task skapad och skickad till Trello!");
        } catch (Exception e) {
            ra.addFlashAttribute("msg", "Task skapad lokalt men kunde inte skapa i Trello: " + e.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/toggle")
    public String toggle(@PathVariable Long id) {
        repo.findById(id).ifPresent(t -> {
            t.setDone(!t.isDone());
            repo.save(t);
        });
        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/delete")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/";
    }
}
