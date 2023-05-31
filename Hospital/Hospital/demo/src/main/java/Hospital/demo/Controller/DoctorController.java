package Hospital.demo.Controller;

import Dto.RequestAuth;
import Hospital.demo.Entity.Doctor;
import Hospital.demo.Entity.Patient;
import Hospital.demo.Entity.UserInfo;
import Hospital.demo.Repository.DoctorRepository;
import Hospital.demo.Repository.PatientRepository;
import Hospital.demo.service.DoctorService;
import Hospital.demo.service.JwtService;
import Hospital.demo.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController

public class DoctorController {

    @Autowired
    DoctorService doctorService;
    @Autowired
    private JwtService jwtService;
    DoctorRepository doctorRepository;

    PatientRepository patientRepository;

    PatientService patientService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public DoctorController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PreAuthorize("hasAuthority('ROLE_DEAN')")
    @PostMapping("doctors")
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        doctor.setPatients(null);
        Doctor createdDoctor = doctorService.createDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDoctor);
    }


    @PreAuthorize("hasAuthority('ROLE_DEAN')")
    @GetMapping("/doctors and their patients by id")
    public ResponseEntity<Doctor> getDoctorDetails(@RequestParam("id") int id) {
        Doctor doctor = doctorService.getDoctorById(id);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(doctor);
    }

    @PreAuthorize("hasAuthority('ROLE_DEAN')")
    @GetMapping("/all doctors-with-patients")
    public ResponseEntity<List<Doctor>> getAllDoctorsWithPatients() {
        List<Doctor> doctors = doctorService.getAllDoctors();

        for (Doctor doctor : doctors) {
            List<Patient> patients = doctor.getPatients();
            for (Patient patient : patients) {
            }
        }
        return ResponseEntity.ok(doctors);
    }


    @PreAuthorize("hasAuthority('ROLE_DEAN') or hasAuthority('ROLE_DOCTOR')")
    @GetMapping("/patients-and-doctorsbysorting1")
    public ResponseEntity<List<Doctor>> getAllDoctorsWithPatients(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        List<Doctor> doctors = doctorService.getAllDoctors();

        // Sort doctors based on the given sortBy parameter and sortOrder
        doctors.sort((doctor1, doctor2) -> {
            int result;
            if (sortBy.equals("id")) {
                result = Integer.compare(doctor1.getDoctorId(), doctor2.getDoctorId());
            } else if (sortBy.equals("date")) {
                result = doctor1.getJoinDate().compareTo(doctor2.getJoinDate());
            } else if (sortBy.equals("severity")) {
                int maxSeverity1 = getMaxSeverity(doctor1);
                int maxSeverity2 = getMaxSeverity(doctor2);
                result = Integer.compare(maxSeverity1, maxSeverity2);
            } else {
                result = Integer.compare(doctor1.getDoctorId(), doctor2.getDoctorId());
            }

            if (sortOrder.equalsIgnoreCase("desc")) {
                result = -result;
            }

            return result;
        });

        return ResponseEntity.ok(doctors);
    }

    private int getMaxSeverity(Doctor doctor) {
        return doctor.getPatients().stream()
                .mapToInt(Patient::getSeverity)
                .max()
                .orElse(0);
    }

    @PreAuthorize("hasAuthority('ROLE_DEAN')")
    @GetMapping("/patientsbysorting")
    public ResponseEntity<List<Patient>> getAllPatientsWithSorting(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        List<Patient> patients = patientService.getAllPatients();
        patients.sort((patient1, patient2) -> {
            int result;
            switch (sortBy) {
                case "id":
                    result = Integer.compare(patient1.getPatientId(), patient2.getPatientId());
                    break;
                case "date":
                    result = patient1.getJoinDate().compareTo(patient2.getJoinDate());
                    break;
                case "severity":
                    result = Integer.compare(patient1.getSeverity(), patient2.getSeverity());
                    break;
                default:
                    result = Integer.compare(patient1.getPatientId(), patient2.getPatientId());
            }

            if (sortOrder.equalsIgnoreCase("desc")) {
                result = -result;
            }

            return result;
        });

        return ResponseEntity.ok(patients);
    }

    @PreAuthorize("hasAuthority('ROLE_DEAN') or hasAuthority('ROLE_DOCTOR')")
    @GetMapping("/all-patients-details-except-discharge")
    public ResponseEntity<List<Patient>> getAllPatientsDetails() {
        List<Patient> patients = patientService.getAllPatients();
        List<Patient> filteredPatients = new ArrayList<>();

        for (Patient patient : patients) {
            if (!patient.getStatus().equalsIgnoreCase("discharge")) {
                Patient p = new Patient();
                p.setPatientId(patient.getPatientId());
                p.setName(patient.getName());
                p.setDisease(patient.getDisease());
                p.setSeverity(patient.getSeverity());
                p.setJoinDate(patient.getJoinDate());
                p.setStatus(patient.getStatus());

                filteredPatients.add(p);
            }
        }
        return ResponseEntity.ok(filteredPatients);
    }


    @PreAuthorize("hasAuthority('ROLE_DEAN') or hasAuthority('ROLE_DOCTOR')")
    @GetMapping("/all-patientsbyStatus")
    public ResponseEntity<List<Patient>> getAllPatients(@RequestParam String status) {
        List<Patient> patients = patientService.getAllPatients();
        List<Patient> filteredPatients = new ArrayList<>();

        for (Patient patient : patients) {
            if (status == null || patient.getStatus().equalsIgnoreCase(status)) {
                filteredPatients.add(patient);

            }
        }

        return ResponseEntity.ok(filteredPatients);
    }

    @PostMapping("/new")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return doctorService.addUser(userInfo);

    }

    @PostMapping("/auth")
    public String authenticateAndGetToken(@RequestBody RequestAuth requestAuth) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestAuth.getUsername(), requestAuth.getPassword()));
        if (authenticate.isAuthenticated()) {
            return jwtService.generateToken(requestAuth.getUsername());

        } else {
            throw new UsernameNotFoundException("invalid user request");

        }
    }
}



