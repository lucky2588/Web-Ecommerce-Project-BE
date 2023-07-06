package com.total.webecommerce.entity.projection;

import com.total.webecommerce.entity.Notification;
import com.total.webecommerce.entity.support.NotificationStatus;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

/**
 * Projection for {@link Notification}
 */
public interface NotificationInfo {
    Integer getId();

    String getUsername();

    Integer getTypeOf();

    String getTitle();

    String getContent();
    String getAvatar();

    NotificationStatus getNotificationStatus();

    LocalDate getCreateAt();
    @RequiredArgsConstructor
    class NotificationInfoImpl implements NotificationInfo{
        private final Notification notification;

        @Override
        public Integer getId() {
            return notification.getId();
        }

        @Override
        public String getUsername() {
            return notification.getUsername();
        }
        @Override
        public String getAvatar() {
            return notification.getAvatar();
        }

        @Override
        public Integer getTypeOf() {
            return notification.getTypeOf();
        }

        @Override
        public String getTitle() {
            return notification.getTitle();
        }

        @Override
        public String getContent() {
            return notification.getContent();
        }

        @Override
        public NotificationStatus getNotificationStatus() {
            return notification.getNotificationStatus();
        }

        @Override
        public LocalDate getCreateAt() {
            return notification.getCreateAt();
        }
    }
}