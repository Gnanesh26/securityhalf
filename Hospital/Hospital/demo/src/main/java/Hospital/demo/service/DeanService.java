package Hospital.demo.service;

import Hospital.demo.Entity.Dean;
import Hospital.demo.Repository.DeanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeanService {

    @Autowired
    private DeanRepository deanRepository;


}

