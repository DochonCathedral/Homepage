package ga.dochon.homepage.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="Board")
@Data // Lombok - Getter, Setter 사용
@Accessors(chain = true) // Lombok - builder pattern 사용
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Hibernate를 쓰는 객체들에는 handler가 필드로 붙는다. 근데 이 객체에 Jackson이 직렬화 시 필드들에 접근을 해야 하는데, getter가 없어 접근을 하지 못한다. 그래서 Ignore 처리.
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
    private BoardType type;

    @Column
    @Length(max = 45)
    private String description;



    public enum BoardType {
        NORMAL ((short)1),
        GALLERY ((short)2),
        QNA ((short)3)
        ;

        private short type;
        private static Map map = new HashMap<>();

        BoardType(short type) {
            this.type = type;
        }

        public short getType() {
            return this.type;
        }

        static {
            for (BoardType boardType : BoardType.values()) {
                map.put(boardType.type, boardType);
            }
        }

        public static BoardType valueOf(short boardType) {
            return (BoardType) map.get(boardType);
        }
    }
}
