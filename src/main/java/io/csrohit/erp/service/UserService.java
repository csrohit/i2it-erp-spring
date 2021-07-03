package io.csrohit.erp.service;

import io.csrohit.erp.model.MyUserDetails;
import io.csrohit.erp.model.User;
import io.csrohit.erp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Page<User> findAllUsers(int page, int size, String sort, String search) {
        Pageable pageable = null;
        String key = "username";
        Sort.Direction direction = Sort.Direction.ASC;

        if(sort != null && !sort.isEmpty()){
            String sortParams[] = sort.split(",");
            key = sortParams[0];
            if (sortParams.length == 2 && sortParams[1].equalsIgnoreCase("desc")){
                direction = Sort.Direction.DESC;
            }else{
                LOG.info("Invalid sort parameters");
            }
        }
        LOG.debug("Sorting by {} {}", key, direction);
        Sort.Order order = new Sort.Order(direction, key).ignoreCase();
        LOG.debug("Fetching page= {}, size= {}", page, size);
        pageable = PageRequest.of(page, size, Sort.by(order));
        LOG.debug(search);
        if(search != null && !search.isEmpty()){
            String searchParams[] = search.split("=");
            if (searchParams.length == 2){
                switch (searchParams[0]){
                    case "name":
                        LOG.debug("Search by name = {}", searchParams[1]);
                        return userRepository.findByNameContainingIgnoreCase(searchParams[1], pageable);
                }
            }

        }
        return userRepository.findAll(pageable);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
        return;
    }
}

