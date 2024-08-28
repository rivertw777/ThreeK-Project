package com.ThreeK_Project.api_server.global.audit;

import com.ThreeK_Project.api_server.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class UserAuditEntity {

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @LastModifiedDate
    @Column(updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by")
    private User deletedBy;

    public void initUserAuditData(User user) {
        this.createdAt = LocalDateTime.now();
        this.createdBy = user;
        this.updatedBy = user;
    }

    public void updateUserAuditData(User user) {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = user;
    }

    public void deleteUserAuditData(User user) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = user;
    }

    public void setOriginalCreatedAt(LocalDateTime originalCreatedAt) {
        this.createdAt = originalCreatedAt;
    }

}
