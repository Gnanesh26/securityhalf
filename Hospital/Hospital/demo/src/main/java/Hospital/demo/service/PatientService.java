package Hospital.demo.service;

import Hospital.demo.Controller.DoctorController;
import Hospital.demo.Entity.Patient;
import Hospital.demo.Repository.DoctorRepository;
import Hospital.demo.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;
    DoctorRepository doctorRepository;

    DoctorController doctorController;

    public Patient getPatientById(Integer id) {
        return patientRepository.findById(id).orElse(null);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public void createPatient(Patient patient) {
    }
}

