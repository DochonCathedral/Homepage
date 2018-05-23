package ga.dochon.homepage.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="Reply")
@Data
@Accessors(chain = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Reply implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "idReply")
    private Integer idReply;

    @Column
    @NotNull
    private Integer idArticle;

    @Column
    private Integer idParent;

    @Column
    @Min(0)
    private Integer idUser;

    @CreatedDate
    @Column(name = "dateCreated", nullable = false)
    private LocalDateTime dateCreated;

    @Column
    @NotNull
    @Length(max = 200)
    private String contents;

    @Column
    @NotNull
    private ReplyStatus status;

    @Column
    @Length(max = 45)
    private String replyPassword;

    public enum ReplyStatus {
        CREATED ((short)0), // 생성됨
        EDITED ((short)1),  // 수정됨
        DELETED ((short)2), // 삭제됨
        ;

        private short status;
        private static Map map = new HashMap<>();

        ReplyStatus(short status) {
            this.status = status;
        }

        public short getStatus() {
            return this.status;
        }

        static {
            for (ReplyStatus replyStatus : ReplyStatus.values()) {
                map.put(replyStatus.status, replyStatus);
            }
        }

        public static ReplyStatus valueOf(short replyStatus) {
            return (ReplyStatus) map.get(replyStatus);
        }
    }
}
