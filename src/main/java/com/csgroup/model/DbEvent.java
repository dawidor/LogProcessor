package com.csgroup.model;

import com.csgroup.enums.State;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "Events")
public class DbEvent {

    @Id
    private String id;
    @Column(name="alert")
    private boolean alert;
    @Column(name="type")
    private String type;
    @Column(name="duration")
    private Long duration;
    @Column(name="host")
    private String host;

    public DbEvent() {}
}
