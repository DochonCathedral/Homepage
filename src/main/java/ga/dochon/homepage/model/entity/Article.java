package ga.dochon.homepage.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="Article")
@Data
@Accessors(chain = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Article implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "idArticle")
    private Integer idArticle;

    @Column
    @NotNull
    @Length(max = 45)
    private String title;

    @Column
    @Positive
    private Integer idUser;

    @Column
    @NotNull
    @Positive
    private Integer idBoard;

    //@CreatedDate
    @Column(name = "dateCreated", nullable = false, columnDefinition="dateCreated default CURRENT_TIMESTAMP")
    private LocalDateTime dateCreated;

    @Column(name = "countHit", nullable = false, columnDefinition="countHit default 0")
    @Positive
    private Integer countHit;

    @Column
    @NotNull
    @Length(max = 65535)
    private String contents;

    @Column
    @NotNull
    private ArticleStatus status;

    @Column
    @NotNull
    private ArticleType type;

    @Column
    @Length(min = 4, max = 45)
    private String articlePassword;

    @Column
    @Length(max = 1000)
    private String thumbnail;



    public enum ArticleStatus {
        CREATED ((short)0), // 생성됨
        EDITED ((short)1),  // 수정됨
        DELETED ((short)2), // 삭제됨
        ;

        private short status;
        private static Map map = new HashMap<>();

        ArticleStatus(short status) {
            this.status = status;
        }

        public short getStatus() {
            return this.status;
        }

        static {
            for (ArticleStatus articleStatus : ArticleStatus.values()) {
                map.put(articleStatus.status, articleStatus);
            }
        }

        public static ArticleStatus valueOf(short articleStatus) {
            return (ArticleStatus) map.get(articleStatus);
        }
    }


    public enum ArticleType {
        NORMAL ((short)0), // 일반글
        NOTICE ((short)1), // 공지글
        SECRET ((short)2), // 비밀글
        ;

        private short type;
        private static Map map = new HashMap<>();

        ArticleType(short type) {
            this.type = type;
        }

        public short getType() {
            return this.type;
        }

        static {
            for (ArticleType articleType : ArticleType.values()) {
                map.put(articleType.type, articleType);
            }
        }

        public static ArticleType valueOf(short articleType) {
            return (ArticleType) map.get(articleType);
        }
    }

    public Article incrementHit() {
        this.countHit++;
        return this;
    }
}
