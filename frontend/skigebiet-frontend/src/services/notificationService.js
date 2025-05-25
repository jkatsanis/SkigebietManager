// Notification types
export const NOTIFICATION_TYPES = {
    SUCCESS: 'success',
    ERROR: 'error',
    INFO: 'info',
    WARNING: 'warning'
};

// Create and show a notification
const showNotification = (message, type = NOTIFICATION_TYPES.INFO) => {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <div class="notification-content">
            <span class="notification-message">${message}</span>
            <button class="notification-close">&times;</button>
        </div>
    `;

    // Add to document
    document.body.appendChild(notification);

    // Add show class after a small delay for animation
    setTimeout(() => {
        notification.classList.add('show');
    }, 10);

    // Auto remove after 5 seconds
    const timeout = setTimeout(() => {
        removeNotification(notification);
    }, 5000);

    // Add click handler for close button
    const closeButton = notification.querySelector('.notification-close');
    closeButton.addEventListener('click', () => {
        clearTimeout(timeout);
        removeNotification(notification);
    });
};

// Remove notification with animation
const removeNotification = (notification) => {
    notification.classList.remove('show');
    notification.addEventListener('transitionend', () => {
        notification.remove();
    });
};

// Export notification functions
export const notificationService = {
    success: (message) => showNotification(message, NOTIFICATION_TYPES.SUCCESS),
    error: (message) => showNotification(message, NOTIFICATION_TYPES.ERROR),
    info: (message) => showNotification(message, NOTIFICATION_TYPES.INFO),
    warning: (message) => showNotification(message, NOTIFICATION_TYPES.WARNING)
}; 