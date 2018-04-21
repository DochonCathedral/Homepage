package ga.dochon.homepage.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Organization")
@Data
public class Organization implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "idOrganization") // Column의 name을 정해주지 않으면 id_organization 처럼 만들어서 select하려고 함...;;
    private int idOrganization;

    @Column(name = "idParent")
    private Integer idParent; // null 일수 있으니 Integer

    @Column
    private String name;

    @Column
    private String description;

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}
