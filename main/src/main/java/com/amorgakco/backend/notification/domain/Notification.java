package com.amorgakco.backend.notification.domain;

import com.amorgakco.backend.global.BaseTime;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.member.domain.Member;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SoftDelete;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete
public class Notification extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private SendingType sendingType;

    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_member_id" ,nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_member_id" ,nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id" ,nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Group group;

    @Builder
    public Notification(
        final String title, Member sender,
        final String content,
        final Member receiver,
        final SendingType sendingType, NotificationType notificationType, Group group) {
        this.title = title;
        this.sender = sender;
        this.content = content;
        this.receiver = receiver;
        this.sendingType = sendingType;
        this.notificationType = notificationType;
        this.group = group;
    }
}
