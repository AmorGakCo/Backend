package com.amorgakco.backend.notification.repository;

import com.amorgakco.backend.notification.domain.Notification;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class NotificationBulkInsertRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<Notification> notifications){
        String sql =""" 
                insert into notification (
                deleted,
                created_at,
                receiver_id,
                sender_id,
                content,
                title,
                notification_type,
                sending_type)
            VALUES (?,?,?,?,?,?,?,?)
            """;

        jdbcTemplate.batchUpdate(sql,
            notifications,
            notifications.size(),
            (PreparedStatement ps, Notification notification) -> {
                ps.setInt(1, 0);
                ps.setTimestamp(2, Timestamp.valueOf(notification.getCreatedAt()));
                ps.setLong(3, notification.getReceiverId());
                ps.setLong(4, notification.getSenderId());
                ps.setString(5, notification.getContent());
                ps.setString(6, notification.getTitle());
                ps.setString(7, String.valueOf(notification.getNotificationType()));
                ps.setString(8,String.valueOf(notification.getSendingType()));
            });
    }

}
