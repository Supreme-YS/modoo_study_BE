package com.modoostudy.user.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class chatDetailsTB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne
    @JoinColumn(name="speaker")
    private userTB userTB;

    @Column
    private String chatContents;

    @Column
    private LocalDateTime chatTime;

    @ManyToOne
    @JoinColumn
    private chatTB chatTB;

}
