package Hospital.demo.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "dean")
public class Dean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dean_id")
    private Integer deanId;

    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "dean")
    private List<Patient> patients;

    public Dean(Integer deanId, String name) {
        this.deanId = deanId;
        this.name = name;
    }

    public Dean() {
    }

    public Integer getDeanId() {
        return deanId;
    }

    public void setDeanId(Integer deanId) {
        this.deanId = deanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

