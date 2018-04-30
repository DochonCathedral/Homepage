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

    @CreatedDate
    @Column(name = "dateCreated", nullable = false)
    private LocalDateTime dateCreated;

    @Column
    @NotNull
    @Length(max = 200)
    private String contents;

    @Column
    @NotNull
    @Min(0)
    @Max(10)
    private Short status;

    @Column
    @Length(max = 45)
    private String replyPassword;
}
