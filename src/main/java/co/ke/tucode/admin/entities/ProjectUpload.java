package co.ke.tucode.admin.entities;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ProjectDataTableUpdload")
public class ProjectUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "projectUploadID", updatable = false, nullable = false)
    private Integer id;
    
    @Lob
    @Column(nullable = true)
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "info_id", nullable = false)
    private ProjectInfo info;
}
