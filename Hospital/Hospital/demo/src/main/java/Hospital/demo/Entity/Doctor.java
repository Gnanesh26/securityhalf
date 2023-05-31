package Hospital.demo.Entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Integer doctorId;

    @Column(name = "name")
    private String name;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "disease")
    private String disease;

    @Column(name = "severity")
    private Integer severity;


    @OneToMany(mappedBy = "assignedDoctor")
    private List<Patient> patients;
    public Doctor(Integer doctorId, String name, LocalDate joinDate, String disease, Integer severity) {
        this.doctorId = doctorId;
        this.name = name;
        this.joinDate = joinDate;
        this.disease = disease;
        this.severity = severity;
    }
    public Doctor(String name) {
        this.name = name;
    }

    public Doctor() {
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }
}
