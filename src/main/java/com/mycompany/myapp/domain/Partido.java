package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mycompany.myapp.domain.util.CustomDateTimeDeserializer;
import com.mycompany.myapp.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Partido.
 */
@Entity
@Table(name = "PARTIDO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Partido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    

    @NotNull        
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "hora_inicio", nullable = false)
    private DateTime hora_inicio;

    @NotNull        
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "hora_final", nullable = false)
    private DateTime hora_final;

    @NotNull        
    @Column(name = "equipo_local", nullable = false)
    private String equipo_local;

    @NotNull        
    @Column(name = "equipo_visitante", nullable = false)
    private String equipo_visitante;

    @NotNull        
    @Column(name = "arbitro", nullable = false)
    private String arbitro;

    @NotNull        
    @Column(name = "resultado", nullable = false)
    private Integer resultado;

    @OneToOne
    private Estadio estadio;

    @ManyToOne
    private Temporada temporada;

    @OneToMany(mappedBy = "partido")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Estadistica> estadisticas = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(DateTime hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public DateTime getHora_final() {
        return hora_final;
    }

    public void setHora_final(DateTime hora_final) {
        this.hora_final = hora_final;
    }

    public String getEquipo_local() {
        return equipo_local;
    }

    public void setEquipo_local(String equipo_local) {
        this.equipo_local = equipo_local;
    }

    public String getEquipo_visitante() {
        return equipo_visitante;
    }

    public void setEquipo_visitante(String equipo_visitante) {
        this.equipo_visitante = equipo_visitante;
    }

    public String getArbitro() {
        return arbitro;
    }

    public void setArbitro(String arbitro) {
        this.arbitro = arbitro;
    }

    public Integer getResultado() {
        return resultado;
    }

    public void setResultado(Integer resultado) {
        this.resultado = resultado;
    }

    public Estadio getEstadio() {
        return estadio;
    }

    public void setEstadio(Estadio estadio) {
        this.estadio = estadio;
    }

    public Temporada getTemporada() {
        return temporada;
    }

    public void setTemporada(Temporada temporada) {
        this.temporada = temporada;
    }

    public Set<Estadistica> getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(Set<Estadistica> estadisticas) {
        this.estadisticas = estadisticas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Partido partido = (Partido) o;

        if ( ! Objects.equals(id, partido.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Partido{" +
                "id=" + id +
                ", hora_inicio='" + hora_inicio + "'" +
                ", hora_final='" + hora_final + "'" +
                ", equipo_local='" + equipo_local + "'" +
                ", equipo_visitante='" + equipo_visitante + "'" +
                ", arbitro='" + arbitro + "'" +
                ", resultado='" + resultado + "'" +
                '}';
    }
}
