package com.ogz.message.service;

import com.ogz.message.dtoConvertor.AwaitUserCreateToAwaitUser;
import com.ogz.message.repository.AwaitUserRepository;
import org.ogz.dto.AwaitUserCreate;
import org.ogz.model.AwaitUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AwaitUserService {

    private final AwaitUserRepository repository;

    public AwaitUserService(AwaitUserRepository repository) {
        this.repository = repository;
    }

    public void createAwaitUser(AwaitUserCreate user){
        repository.save(AwaitUserCreateToAwaitUser.convert(user));
    }

    public AwaitUser getFirstAwaituser(){
        var userList = repository.findAll();
        if (userList.size() == 0) return null;
        return repository.findAll().get(0);
    }

    public AwaitUser updateAwaitUser(AwaitUser user) {
        return repository.save(user);
    }

    public AwaitUser deleteAwaitUser(String id){
        AwaitUser user = repository.findById(id).orElse(null);
        if (Objects.isNull(user)) return null;
        repository.deleteById(id);
        return user;
    }
    public List<AwaitUser> getAll(){
        var all = repository.findAll();
        System.out.println(all);
        return all;
    }
}
