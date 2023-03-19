package com.ogz.user.service;

import com.ogz.user.dtoConvertor.AwaitUserCreateToAwaitUser;
import com.ogz.user.repository.AwaitUserRepository;
import org.ogz.dto.AwaitUserCreate;
import org.ogz.model.AwaitUser;
import org.springframework.stereotype.Service;

import java.util.List;

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
        System.out.println(userList);
        if (userList.size() == 0) return null;
        return repository.findAll().get(0);
    }

    public AwaitUser updateAwaitUser(AwaitUser user) {
        return repository.save(user);
    }

    public List<AwaitUser> getAll(){
        var all = repository.findAll();
        System.out.println(all);
        return all;
    }
}
