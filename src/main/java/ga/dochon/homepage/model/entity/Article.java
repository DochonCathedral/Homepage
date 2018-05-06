package ga.dochon.homepage.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

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
    @Length(min = 1, max = 45)
    private String title;

    @Column
    private Integer idUser;

    @Column
    @NotNull
    private Integer idBoard;

    @CreatedDate
    @Column(name = "dateCreated", nullable = false)
    private LocalDateTime dateCreated;

    @Column
    private Integer countHit;

    @Column
    @NotNull
    @Length(max = 65535)
    private String contents;

    @Column
    @NotNull
    @Min(0)
    @Max(10)
    private Short status;

    @Column
    @NotNull
    @Min(0)
    @Max(10)
    private Short type;

    @Column
    @Length(max = 45)
    private String articlePassword;

    @Column
    @Length(max = 1000)
    private String thumbnail;
}
