package com.web.rest_api_demo.controller;

import com.web.rest_api_demo.entity.Actor;
import com.web.rest_api_demo.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/actors")
public class ActorController {
    @Autowired
    ActorService actorService;

    @GetMapping
    public List<Actor> findAll() {
        return actorService.getAllActors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actor> findById(@PathVariable Integer id) {
        Optional<Actor> actor = actorService.getActorById(id);
        return actor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Actor> createActor(@RequestBody Actor actor) {
        Actor createdActor = actorService.createActor(actor);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdActor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Integer id) {
        actorService.deleteActorById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Actor> updateActor(@PathVariable Integer id, @RequestBody Actor actor) {
        Optional<Actor> updatedActor = actorService.updateActor(id, actor);

        return updatedActor
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
