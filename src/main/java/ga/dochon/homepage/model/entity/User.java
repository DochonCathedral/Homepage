package ga.dochon.homepage.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="User")
@Data
@Accessors(chain = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "idUser")
    private Integer idUser;

    @Column(name = "idKakao")
    @NotNull
    private Integer idKakao;

    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    private String nickname;

    @Column
    @NotNull
    private Date dateJoined;
    
    @Column
    private String accessToken;
    
    @Column
    private String freshToken;
    
    @Column
    private String thumbnail;
}
