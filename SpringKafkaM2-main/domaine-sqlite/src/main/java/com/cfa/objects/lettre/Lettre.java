package com.cfa.objects.lettre;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Date;

@Entity
@Table(name = "lettre")
@Getter
@Setter
@ToString
@NoArgsConstructor

public class Lettre implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "message")
    private String message;

    @Column(name = "creationDate")
    private String creationDate;

    @Column(name = "treatmentDate")
    private String treatmentDate;
}
