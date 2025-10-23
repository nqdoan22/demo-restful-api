package com.web.rest_api_demo.service;

import com.web.rest_api_demo.entity.Actor;
import com.web.rest_api_demo.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActorService {
    @Autowired
    private ActorRepository actorRepository;

    public List<Actor> getAllActors(){
        return actorRepository.findAll();
    }

    public Optional<Actor> getActorById(Integer id){
        return actorRepository.findById(id);
    }

    public Actor createActor(Actor actor){
        return actorRepository.save(actor);
    }

    public void deleteActorById(int id){
        actorRepository.deleteById(id);
    }

    public Optional<Actor> updateActor(Integer id,  Actor actor){
        return actorRepository.findById(id).map(existingActor -> {
            existingActor.setFirstName(actor.getFirstName());
            existingActor.setLastName(actor.getLastName());
            return actorRepository.save(existingActor);
        });
    }

}
