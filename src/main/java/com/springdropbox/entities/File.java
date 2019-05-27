package com.springdropbox.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@Data
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "path")
    private String path;

    @Column(name = "size")
    private Long size;

    @Column(name = "create_at")
    @CreationTimestamp
    private LocalDateTime create_at;

    private File() {
    }

    public File(User user, String title, String path, Long size) {
        this.user = user;
        this.title = title;
        this.path = path;
        this.size = size;
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", create_at=" + create_at +
                '}';
    }
}
