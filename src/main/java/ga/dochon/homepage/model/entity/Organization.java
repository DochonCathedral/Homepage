package ga.dochon.homepage.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Organization")
@Data
@Accessors(chain = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Organization implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "idOrganization") // Column의 name을 정해주지 않으면 id_organization 처럼 만들어서 select하려고 함...;;
    private Integer idOrganization;

    @Column(name = "idParent")
    private Integer idParent; // null 일수 있으니 Integer

    @Column
    private String name;

    @Column
    private String description;
}
