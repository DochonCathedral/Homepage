package ga.dochon.homepage.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="Board")
@Data // Lombok - Getter, Setter 사용
@Accessors(chain = true) // Lombok - builder pattern 사용
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Hibernate의 Json 수정 관련 에러로 인해 추가
public class Board implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "idBoard")
    private Integer idBoard;

    @Column
    @NotNull
    @Length(min = 1, max = 45)
    private String name;

    /*
    // 이렇게 하면 get 할땐 좋은데, create 할때 에러 발생함.. idOrganization으로 일단 ㄱㄱ.
    // 추후 필요성 있을 때 수정하는 걸로.
    // 필요할 땐 BoardReq를 따로 만들자.
    @ManyToOne(targetEntity=Organization.class, fetch=FetchType.LAZY)
    @JoinColumn(name="idOrganization", nullable=false)
    private Organization organization;
    */
    @Column
    @NotNull
    private Integer idOrganization;

    @Column
    @NotNull
    @Min(0)
    @Max(10)
    private Short type;

    @Column
    @Length(max = 45)
    private String description;
}
